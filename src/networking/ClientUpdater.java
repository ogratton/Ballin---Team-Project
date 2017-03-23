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

public class ClientUpdater extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private ConcurrentMap<String, Resources> resourcesMap;
	private ConcurrentMap<String, Session> sessions;
	private ConcurrentMap<String, Connection> connections;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Receiver.
 */
	
	public ClientUpdater(String sessionId, ConcurrentMap<String, Resources> resourcesMap, ConcurrentMap<String, Session> sessions, ConcurrentMap<String, Connection> connections) {
		super();
		this.sessionId = sessionId;
		this.resourcesMap = resourcesMap;
		this.sessions = sessions;
		this.connections = connections;
	}

/**
 * Updates the button panel when changes are made to the Client Data Model
 * If the client is connected to another client (i.e: playing a game) the buttons in this panel are disabled.
 * This means that the client is forced to finish or quit the game before trying to press any other commands.
 * @param o
 * @param arg
 */
	@Override
	public void update(Observable o, Object arg) {
		//System.out.println("Updated");
		if(resourcesMap.get(sessionId) != null && resourcesMap.get(sessionId).gamemode != null) {
			if(resourcesMap.get(sessionId).isGameOver()) {
				System.out.println("Ending the game");
				
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
				data.setTimer(0);
				Message message = new Message(Command.GAME, Note.UPDATE, "", "", sessionId, sessionId, data);
				List<ClientInformation> clients = sessions.get(sessionId).getAllClients();
				ClientInformation client;
				for(int i = 0; i<clients.size(); i++) {
					client = clients.get(i);
					connections.get(client.getId()).sendTCP(message);
				}
				
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
		
		
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public static ArrayList<SerializablePowerUp> serializePowerUps(ArrayList<Powerup> powerUps) {
		ArrayList<SerializablePowerUp> serialized = new ArrayList<SerializablePowerUp>();
		for(int i=0; i<powerUps.size(); i++) {
			serialized.add(new SerializablePowerUp(powerUps.get(i).getPower(), powerUps.get(i).getX(), powerUps.get(i).getY(), powerUps.get(i).isActive()));
		}
		
		return serialized;
	}
}




