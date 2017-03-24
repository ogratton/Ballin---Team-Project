package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.esotericsoftware.kryonet.Connection;

import resources.Powerup;
import resources.Resources;

/**
 * ClientUpdater observes a character on the server and sends messages to a client updating the position
 * of all the player in a game as well as other data such as the power ups and the timer.
 * @author axn598
 *
 */
public class ClientUpdater extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private ConcurrentMap<String, Resources> resourcesMap;
	private ConcurrentMap<String, Session> sessions;
	private ConcurrentMap<String, Connection> connections;
	private int serverTick = 20;
	private long lastPressProcessed = 0;
	

	/**
	 * Constructs the client updater.
	 * @param sessionId The ID of the session which this character is in
	 * @param resourcesMap The Hash map of the resources object for each running session
	 * @param sessions The Hash map of all the sessions and their IDs
	 * @param connections The Hash map of all the connections and the IDs of the clients which the connections send to.
	 */
	public ClientUpdater(String sessionId, ConcurrentMap<String, Resources> resourcesMap, ConcurrentMap<String, Session> sessions, ConcurrentMap<String, Connection> connections) {
		super();
		this.sessionId = sessionId;
		this.resourcesMap = resourcesMap;
		this.sessions = sessions;
		this.connections = connections;
	}


	/**
	 * Updates when the character changes. Sends the update message to all users in the same game.
	 * Sends the details of all characters in the game to update each client.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// Limit the update rate to once every 20 milliseconds
		if(System.currentTimeMillis() - lastPressProcessed > serverTick) {
			//System.out.println("Updated");
			// Check if resources exist for this character
			if(resourcesMap.get(sessionId) != null && resourcesMap.get(sessionId).gamemode != null) {
				
				// Check if the game is over. If it is, end the game, if it isn't, update the client.
				if(resourcesMap.get(sessionId).isGameOver()) {
					System.out.println("Ending the game");
					
					// Send a final update to the client with all the updated positions
					List<resources.Character> characters = resourcesMap.get(sessionId).getPlayerList();
					List<CharacterInfo> charactersList = new ArrayList<CharacterInfo>();
					resources.Character c;
					for(int i=0; i<characters.size(); i++) {
						c = characters.get(i);
						//System.out.println("X: " + c.getX());
						CharacterInfo info = new CharacterInfo(c.getId(), c.getX(), c.getY(), c.getPlayerNumber(), c.isFalling(), c.isDead(), c.isDashing(), c.isBlocking(), c.getStamina(), c.hasPowerup(), c.getLastPowerup(), c.getKills(), c.getDeaths(), c.getSuicides(), c.getLives(), c.getScore(), c.hasBomb(), c.getDyingStep(), c.isVisible(), c.isExploding(), c.getTimeOfDeath());
						charactersList.add(info);
					}
					
					GameData data = new GameData(charactersList);
					ArrayList<Powerup> powerUps = resourcesMap.get(sessionId).getPowerupList();
					
					// Set the timer to 0 so that death match ends
					data.setPowerUps(serializePowerUps(powerUps));
					data.setTimer(0);
					Message message = new Message(Command.GAME, Note.UPDATE, "", "", sessionId, sessionId, data);
					List<ClientInformation> clients = sessions.get(sessionId).getAllClients();
					ClientInformation client;
					for(int i = 0; i<clients.size(); i++) {
						client = clients.get(i);
						connections.get(client.getId()).sendTCP(message);
					}
					
					// If the game mode thread has finished, send the relevant messages to
					// the clients.
					if(resourcesMap.get(sessionId).gamemode.isGameOver()) {
						Message message1 = new Message(Command.GAME, Note.FINISHED, null, null, null, null);
						Message message2 = new Message(Command.SESSION, Note.COMPLETED, null, null, null, null, sessions);
						
						for(int i = 0; i<clients.size(); i++) {
							client = clients.get(i);
							client.setReady(false);
							connections.get(client.getId()).sendTCP(message);
							connections.get(client.getId()).sendTCP(message1);
							connections.get(client.getId()).sendTCP(message2);
						}
						// Remove the session ID and set the game in progress to false.
						resourcesMap.remove(sessionId);
						sessions.get(sessionId).setGameInProgress(false);
					}
				}
				else {
					List<resources.Character> characters = resourcesMap.get(sessionId).getPlayerList();
					List<CharacterInfo> charactersList = new ArrayList<CharacterInfo>();
					resources.Character c;
					for(int i=0; i<characters.size(); i++) {
						c = characters.get(i);
						//System.out.println("X: " + c.getX());
						CharacterInfo info = new CharacterInfo(c.getId(), c.getX(), c.getY(), c.getPlayerNumber(), c.isFalling(), c.isDead(), c.isDashing(), c.isBlocking(), c.getStamina(), c.hasPowerup(), c.getLastPowerup(), c.getKills(), c.getDeaths(), c.getSuicides(), c.getLives(), c.getScore(), c.hasBomb(), c.getDyingStep(), c.isVisible(), c.isExploding(), c.getTimeOfDeath());
						charactersList.add(info);
					}
					
					GameData data = new GameData(charactersList);
					ArrayList<Powerup> powerUps = resourcesMap.get(sessionId).getPowerupList();
					data.setPowerUps(serializePowerUps(powerUps));
					data.setTimer(resourcesMap.get(sessionId).getTimer());
					Message message = new Message(Command.GAME, Note.UPDATE, "", "", sessionId, sessionId, data);
					List<ClientInformation> clients = sessions.get(sessionId).getAllClients();
					ClientInformation client;
					for(int i = 0; i<clients.size(); i++) {
						client = clients.get(i);
						connections.get(client.getId()).sendUDP(message);
					}
				}
			}
			
			// Update the server tick
			lastPressProcessed = System.currentTimeMillis();
		}
	}
	
	/**
	 * Get the session ID of the session this character is in.
	 * @return The session ID
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Set the session ID of this character
	 * @param sessionId The session ID
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * Change an ArrayList of Power ups from an unserializable version into a serializable version.
	 * @param powerUps The ArrayList of Powerups.
	 * @return The ArrayList of serialized Powerups.
	 */
	public static ArrayList<SerializablePowerUp> serializePowerUps(ArrayList<Powerup> powerUps) {
		ArrayList<SerializablePowerUp> serialized = new ArrayList<SerializablePowerUp>();
		for(int i=0; i<powerUps.size(); i++) {
			serialized.add(new SerializablePowerUp(powerUps.get(i).getPower(), powerUps.get(i).getX(), powerUps.get(i).getY(), powerUps.get(i).isActive()));
		}
		
		return serialized;
	}
}




