package networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import gamemodes.Deathmatch;
import gamemodes.GameModeFFA;
import gamemodes.HotPotato;
import gamemodes.LastManStanding;
import graphics.Graphics;
import resources.Character;
import resources.Map;
import resources.MapCosts;
import resources.MapReader;
import resources.Powerup;
import resources.Resources;
import resources.Resources.Mode;
import ui.SessionListMenu;
import ui.UIRes;

/**
 * Listens to the Client for messages and connections
 * @author aaquibnaved
 *
 */
public class ClientListener extends Listener {
	
	public ConnectionDataModel cModel;
	public Client client;
	
	/**
	 * Initialises the listener using the model and the client.
	 * @param cModel The model of the connection data
	 * @param client The actual Kryonet client object which you can send messages through.
	 */
	public ClientListener(ConnectionDataModel cModel, Client client) {
		this.cModel = cModel;
		this.client = client;
	}
	
	/**
	 * Fires when the Client receives a message
	 */
	@SuppressWarnings("unchecked")
	public void received(Connection connection, Object object) {
		
		Message message;
    	ConcurrentMap<String, Session> sessions;
       if (object instanceof Message) {
    	   message = (Message)object;
   		switch(message.getCommand()) {
   		// Fires when the message is something to do with the sessions.
   		case SESSION:
   			switch(message.getNote()) {
   			// Fires when the refresh message is received by the client
   			case COMPLETED:
   				System.out.println("Received sessions from Server");
   				System.out.println("Current Session ID: " + cModel.getSessionId());
   				//cModel.setSessionId(message.getCurrentSessionId());
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				System.out.println("Number of Sessions: " + sessions.values().size());
   				break;
   			// Fires when the created message is received by the client
   			case CREATED:
   				System.out.println("Session created");
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				cModel.setSessionId(message.getCurrentSessionId());
   				break;
   			// Fires when the joined message is received by the client
   			case JOINED:
   				System.out.println("Session Joined");
   				cModel.setSessionId(message.getCurrentSessionId());
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				break;
   			// Fires when the left message is received by the client
   			case LEFT:
   				System.out.println("Session Left");
   				cModel.setSessionId(null);
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				break;
   			case RESET_READY:
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				cModel.setReady(false);
   				cModel.setGameInProgress(false);
   				break;
   			}
   			break;
   		// Fires at the start of the connection process to get the ID generated by the server
   		// for the client.
   		case SEND_ID:
   			cModel.setClientInformation(new ClientInformation(message.getSenderId(), message.getMessage()));
   			cModel.setConnection(connection);
   			
   			SessionListMenu sessionList = new SessionListMenu(client, this.cModel);
			UIRes.switchPanel(sessionList);
			System.out.println("Client ID: " + cModel.getMyId());
   			// Create a thread for the GUI:
//   		    ClientGUI gui = new ClientGUI(cModel, message.getMessage(), client);
//   		    gui.start();
   		    
   			break;
   		// Fires when the message is something to do with the actual game
   		case GAME:
   			GameData gameData;
   			Resources resources;
   			switch(message.getNote()) {
   			// Fires when the game start message is received
   			case START:
   				System.out.println("Game Started");
   				// Gets all the positions of the characters initialised by the Server
   				gameData = (GameData)message.getObject();
   				cModel.setGameInProgress(true);
   				
   				resources = new Resources();
   				cModel.setResources(resources);
   				resources.setUp(UIRes.resources.getUp());
   				resources.setDown(UIRes.resources.getDown());
   				resources.setRight(UIRes.resources.getRight());
   				resources.setLeft(UIRes.resources.getLeft());
   				resources.setDash(UIRes.resources.getDash());
   				resources.setSFXGain(UIRes.resources.getSFXGain());
   				
   				// Get the variables chosen for the session
   				Session session = cModel.getSession(cModel.getSessionId());
   				String mapName = session.getMapName();
   				Map.World style = session.getTileset();
   				Map.World tileset = session.getTileset();
   				Mode modeName = session.getGameMode();
   				Map map = new Map(1200, 650, style, mapName);
   				resources.setMap(map);
   				new MapCosts(resources);
   				
   				//  Set the game mode
   				GameModeFFA mode;
   				switch(modeName) {
   				case Deathmatch:
   					mode = new Deathmatch(resources);
   					break;
   				case LastManStanding:
   					mode = new LastManStanding(resources, 5);
   					break;
   				case HotPotato:
   					mode = new HotPotato(resources);
   					break;
   				default:
   					mode = new Deathmatch(resources);
   					break;
   				}
   				resources.gamemode = mode;
   				
				resources.setMap(map);
   				
   				// Creates new characters for each characrer info sent by the server
   				Character player;
   				double x;
   				double y;
   				String id;
   				CharacterInfo c;
   				List<CharacterInfo> info = gameData.getCharactersList();
   				Updater updater = new Updater(cModel, client, resources);
   				for(int i=0; i<info.size(); i++) {
   					c = info.get(i);
   					id = c.getId();
   					x = c.getX();
   					y = c.getY();
   					
   					player = new Character(c.getType(), c.getPlayerNumber(), c.getName());
   					player.setId(id);
   					player.setXWithoutNotifying(x);
   					player.setYWithoutNotifying(y);
   					player.setPlayerNumber(c.getPlayerNumber());
   					resources.addPlayerToList(player);
   					cModel.getCharacters().put(id, player);
   					
   					// If the character is the character controlled by
   					// this client, set the id in resources and observe the character.
   					if(id.equals(cModel.getMyId())) {
   						resources.setId(id);
   						player.setName(cModel.getClientInformation().getName());
   						player.addObserver(updater);
   					}
   				}
   				
   					
				//new MapCosts(resources);
   				cModel.setResources(resources);

   				//cModel.getResources().setCountdown(0);
   				
   				// Start the graphics
   				Graphics g = new Graphics(resources, null, false);
   				g.start();
   				
   				break;
   			// Fires when an update message is sent by the server
   			case UPDATE:
   				if(cModel.getResources() != null) {
   					
   					
   					// Update all the variables for every player sent by the server.
   					gameData = (GameData)message.getObject();
       				List<CharacterInfo> charactersList = gameData.getCharactersList();
       				resources = cModel.getResources();
       				
       				resources.setPowerUpList(deserialize(gameData.getPowerUps()));
       				resources.setTimer(gameData.getTimer());
       				List<resources.Character> players = resources.getPlayerList();
       					for(int i=0; i<players.size(); i++) {
           					for(int j=0; j<charactersList.size(); j++) {
           						if (charactersList.get(j).getId().equals(players.get(i).getId())) {
           							
           							//System.out.println("X Pos: " + charactersList.get(j).getX());
           							players.get(i).setX(charactersList.get(j).getX());
           							players.get(i).setY(charactersList.get(j).getY());
           							players.get(i).setBlocking(charactersList.get(j).isBlocking());
           							players.get(i).setFalling(charactersList.get(j).isFalling());
           							players.get(i).setDead(charactersList.get(j).isDead());
           							players.get(i).setDashing(charactersList.get(j).isDashing());
           							players.get(i).setStamina(charactersList.get(j).getStamina());
           							players.get(i).hasPowerup(charactersList.get(j).isHasPowerUp());
           							players.get(i).setLastPowerup(charactersList.get(j).getLastPowerUp());
           							players.get(i).setKills(charactersList.get(j).getKills());
           							players.get(i).setDeaths(charactersList.get(j).getDeaths());
           							players.get(i).setSuicides(charactersList.get(j).getSuicides());
           							players.get(i).setLives(charactersList.get(j).getLives());
           							players.get(i).setScore(charactersList.get(j).getScore());
           							players.get(i).hasBomb(charactersList.get(j).isHasBomb());
           							players.get(i).setDyingStep(charactersList.get(j).getDyingStep());
           							players.get(i).setVisible(charactersList.get(j).isVisible());
           							players.get(i).setExploding(charactersList.get(j).isExploding());
           							players.get(i).setTimeOfDeath(charactersList.get(j).getTimeOfDeath());
           						}
           					}
           				}
   				}
   				break;
   			case COUNTDOWN:
   				if(cModel.getResources() != null) {
   					cModel.getResources().decCountdown();
   				}
   				break;
   			case FINISHED:
   				cModel.setResources(new Resources());
   				cModel.setReady(false);
   				cModel.setGameInProgress(false);
   				//cModel.setCharacters(new ConcurrentHashMap<String, Character>());
   			case REMOVE_PLAYER:
//   				String removeClientId = message.getReceiverId();
//   				ArrayList<Character> characters = cModel.getResources().getPlayerList();
//   				for(int i=0; i<characters.size(); i++) {
//   					if(characters.get(i).equals(removeClientId)) {
//   		   				System.out.println("Player Deleted");
//   						characters.get(i).setDead(true);
//   						break;
//   					}
//   				}
   				break;
   			default:
   				break;
   			}
   			break;
			default:
				break;
   		}
       }

	}
	
	public static ArrayList<Powerup> deserialize(ArrayList<SerializablePowerUp> serialized) {
		ArrayList<Powerup> deserialized = new ArrayList<Powerup>();
		for(int i=0; i<serialized.size(); i++) {
			deserialized.add(new Powerup(serialized.get(i).getP(), serialized.get(i).getX(), serialized.get(i).getY(), serialized.get(i).isActive()));
		}
		return deserialized;
	}
}
