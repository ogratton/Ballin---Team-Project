package networking;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ai.pathfinding.MapCosts;
import gamemodes.Deathmatch;
import gamemodes.GameModeFFA;
import gamemodes.HotPotato;
import gamemodes.LastManStanding;
import graphics.Graphics;
import resources.Character;
import resources.Map;
import resources.MapReader;
import resources.Resources;
import resources.Resources.Mode;
import ui.SessionListMenu;
import ui.UIRes;

public class ClientListener extends Listener {
	
	public ConnectionDataModel cModel;
	public Client client;
	
	public ClientListener(ConnectionDataModel cModel, Client client) {
		this.cModel = cModel;
		this.client = client;
	}
	
	@SuppressWarnings("unchecked")
	public void received(Connection connection, Object object) {
		
		Message message;
    	ConcurrentMap<String, Session> sessions;
       if (object instanceof Message) {
    	   message = (Message)object;
   		switch(message.getCommand()) {
   		case SESSION:
   			switch(message.getNote()) {
   			case COMPLETED:
   				System.out.println("Received sessions from Server");
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				break;
   			case CREATED:
   				System.out.println("Session created");
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				cModel.setSessionId(message.getCurrentSessionId());
   				break;
   			case JOINED:
   				System.out.println("Session Joined");
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				cModel.setSessionId(message.getCurrentSessionId());
   				break;
   			case LEFT:
   				System.out.println("Session Left");
   				sessions = (ConcurrentMap<String, Session>)message.getObject();
   				cModel.setSessionsTable(sessions);
   				cModel.setSessionId(null);
   				break;
   			}
   			break;
   		case SEND_ID:
   			cModel.setClientInformation(new ClientInformation(message.getSenderId(), message.getMessage()));
   			cModel.setConnection(connection);
   			cModel.setMyId(message.getSenderId());
   			SessionListMenu sessionList = new SessionListMenu();
			JPanel panel = sessionList.getLobbyListPanel(cModel, client);
			UIRes.switchPanel(panel);
   			// Create a thread for the GUI:
//   		    ClientGUI gui = new ClientGUI(cModel, message.getMessage(), client);
//   		    gui.start();
   		    
   			break;
   		case GAME:
   			GameData gameData;
   			Resources resources;
   			switch(message.getNote()) {
   			case START:
   				System.out.println("Game Started");
   				gameData = (GameData)message.getObject();
   				cModel.setGameInProgress(true);
   				
   				resources = new Resources();
   				
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
   					
   					player = new Character(Character.Class.ARCHER, 1);
   					player.setId(id);
   					player.setXWithoutNotifying(x);
   					player.setYWithoutNotifying(y);
   					player.setPlayerNumber(c.getPlayerNumber());
   					resources.addPlayerToList(player);
   					cModel.getCharacters().put(id, player);
   					
   					if(id.equals(cModel.getMyId())) {
   						resources.setId(id);
   						player.addObserver(updater);
   					}
   				}
   				
   				Session session = cModel.getSession(cModel.getSessionId());
   				String mapName = session.getSessionName();
   				Map.World tileset = session.getTileset();
   				Mode modeName = session.getGameMode();
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
   				
				resources.setMap(new Map(1200, 675, tileset, mapName));
				new MapCosts(resources);
   				cModel.setResources(resources);
   				
//   				Physics p = new Physics(resources);
//   				p.start();
   				
   				// create ui thread
   				Graphics g = new Graphics(resources, updater, false);
   				g.start();
   				
   				break;
   			case UPDATE:
   				if(cModel.getResources() != null) {
   					//System.out.println("Got to Update on Client.");
   					gameData = (GameData)message.getObject();
       				List<CharacterInfo> charactersList = gameData.getCharactersList();
       				resources = cModel.getResources();
       				List<resources.Character> players = resources.getPlayerList();
       				//System.out.println("Player Size: " + players.size());
       				//System.out.println("Characters Size: " + charactersList.size());
       					for(int i=0; i<players.size(); i++) {
           					for(int j=0; j<charactersList.size(); j++) {
           						if (charactersList.get(j).getId().equals(players.get(i).getId())) {
           							//System.out.println("Dashing: " + charactersList.get(j).isDashing());
           							players.get(i).setX(charactersList.get(j).getX());
           							players.get(i).setY(charactersList.get(j).getY());
           							players.get(i).setBlocking(charactersList.get(j).isBlocking());
           							players.get(i).setFalling(charactersList.get(j).isFalling());
           							players.get(i).setDead(charactersList.get(j).isDead());
           							players.get(i).setDashing(charactersList.get(j).isDashing());
           							
           							//System.out.println(charactersList.get(j).isDashing());
           						}
           					}
           				}
   				}
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
}
