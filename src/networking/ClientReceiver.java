package networking;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import resources.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ai.pathfinding.MapCosts;
import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.MapReader;
import resources.NetworkMove;
import resources.Resources;

// Gets messages from other clients via the server (by the
// ServerSender thread).

public class ClientReceiver extends Thread {

  private ObjectInputStream server;
  private int i = -1;
  private int j = -1;
  private GameSharing gs;
  private ConnectionDataModel cModel;
  private ObjectOutputStream toServer;

  /**
   * Creates a Client Receiver Thread which receives messages from the server.
   * These messages are interpreted and then the Client Receiver changes the Client Data Model.
   * When a game is started, the information about the game is shared to the Client Sender by the Game Sharing class.
   * @param server The server from which the Client Receiver reads the messages.
   * @param gs The GameSharing object which contains the information about the game.
   * @param cModel The Client Data Model which contains the information about the client.
   * @param toServer The output stream to the server receiver to which messages are printed to.
   */
  ClientReceiver(ObjectInputStream server, GameSharing gs, ConnectionDataModel cModel, ObjectOutputStream toServer) {
    this.server = server;
    this.gs = gs;
    this.cModel = cModel;
    this.toServer = toServer;
  }

  /**
   * Runs the thread.
   */
  @SuppressWarnings("unchecked")
public void run() {
    Message message = new Message();
    ConcurrentMap<UUID, Session> sessions;
    //ConcurrentMap<UUID, SerializableSession> serializedSessions;
    
    try {
    	while(message.getCommand() != Command.QUIT) {
    		message = (Message)server.readUnshared();
//    		if(message.getObject().getClass().getName() == "networking.GameData" && ((GameData)message.getObject()).getMoves() != null) {
//    			System.out.println("Client: " + ((GameData)message.getObject()).getMoves().size());
//    		}
    		
    		switch(message.getCommand()) {
    		case SESSION:
    			switch(message.getNote()) {
    			case COMPLETED:
    				System.out.println("Received sessions from Server");
//    				serializedSessions = (ConcurrentMap<UUID, SerializableSession>)message.getObject();
//    				sessions = deSerialize(serializedSessions);
    				sessions = (ConcurrentMap<UUID, Session>)message.getObject();
    				cModel.setSessionsTable(sessions);
    				break;
    			case CREATED:
    				System.out.println("Session created");
//    				serializedSessions = (ConcurrentMap<UUID, SerializableSession>)message.getObject();
//    				sessions = deSerialize(serializedSessions);
    				sessions = (ConcurrentMap<UUID, Session>)message.getObject();
    				cModel.setSessionsTable(sessions);
    				cModel.setSessionId(message.getCurrentSessionId());
    				break;
    			case JOINED:
    				System.out.println("Session Joined");
//    				serializedSessions = (ConcurrentMap<UUID, SerializableSession>)message.getObject();
//    				sessions = deSerialize(serializedSessions);
    				sessions = (ConcurrentMap<UUID, Session>)message.getObject();
    				cModel.setSessionsTable(sessions);
    				cModel.setSessionId(message.getCurrentSessionId());
    				break;
    			case LEFT:
    				System.out.println("Session Left");
//    				serializedSessions = (ConcurrentMap<UUID, SerializableSession>)message.getObject();
//    				sessions = deSerialize(serializedSessions);
    				sessions = (ConcurrentMap<UUID, Session>)message.getObject();
    				cModel.setSessionsTable(sessions);
    				cModel.setSessionId(null);
    				break;
    			}
    			break;
    		case SEND_ID:
    			cModel.setClientInformation(new ClientInformation(message.getSenderId(), message.getMessage()));
    			// Create a thread for the GUI:
    		    ClientGUI gui = new ClientGUI(cModel, message.getMessage(), toServer);
    		    gui.start();
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
    				UUID id;
    				CharacterInfo c;
    				List<CharacterInfo> info = gameData.getCharactersList();
    				Updater updater = new Updater(cModel, toServer, resources);
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
    				
    				//make the map the default just in case the following fails
			        resources.Map.Tile[][] tiles = null;	
					MapReader mr = new MapReader();	
					try
					{
						tiles = mr.readMap("./resources/maps/map1.csv");
						System.out.println("I guess it worked then");
					}
					catch (IOException e)
					{
						System.out.println("File not found");
						e.printStackTrace();
					}
					
					resources.setMap(new Map(1200, 650, tiles, Map.World.CAVE, "Test Map"));
					new MapCosts(resources);
    				cModel.setResources(resources);
    				
    				Physics p = new Physics(resources, true);
    				p.start();
    				
    				// create ui thread
    				Graphics g = new Graphics(resources, updater, false);
    				g.start();
    				
    				break;
    			case UPDATE:
    				//System.out.println("Got Here");
    				if(cModel.getResources() != null) {
    					gameData = (GameData)message.getObject();
        				//List<CharacterInfo> charactersList = gameData.getCharactersList();
    					Queue<NetworkMove> moves = gameData.getMoves();
        				resources = cModel.getResources();
        				List<resources.Character> players = resources.getPlayerList();
        				
//        				int index = 0;
//        				for(int i=0; i<players.size(); i++) {
//        					for(int j=0; j<charactersList.size(); j++) {
//        						if(charactersList.get(j).getId().equals(cModel.getMyId())) {
//        							index = j;
//        						}
//        					}
//        				}
        				
//        				System.out.println("Client: " + cModel.getMyId());
//        				System.out.println("Request ID Sent: " + resources.getRequestId());
//        				System.out.println("Request ID Received: " + charactersList.get(index).getRequestId());
//        				System.out.println("Index: " + index);
        				//if(charactersList.get(index).getRequestId() >= resources.getRequestId()) {
//        					for(int i=0; i<players.size(); i++) {
//            					for(int j=0; j<charactersList.size(); j++) {
//            						if (charactersList.get(j).getId().equals(players.get(i).getId())) {
//            							//System.out.println("X: " + charactersList.get(j).getX());
//            							players.get(i).setX(charactersList.get(j).getX());
//            							players.get(i).setY(charactersList.get(j).getY());
//            							players.get(i).setBlocking(charactersList.get(j).isBlocking());
//            							players.get(i).setFalling(charactersList.get(j).isFalling());
//            							
//            							players.get(i).setDead(charactersList.get(j).isDead());
//            							players.get(i).setDashing(charactersList.get(j).isDashing());
//            						}
//            					}
//            				}
        				//}
        				Character character;
        				NetworkMove move;
        				Queue<NetworkMove> sentClientMoves = resources.getSentClientMoves();
        				//System.out.println("Got Here");
        				while(!moves.isEmpty() && moves != null) {
        					System.out.println("Move size: " + moves.size());
        					for(int i=0; i<players.size(); i++) {
        						character = players.get(i);
        						if(moves.peek().id != null && moves.peek().id.equals(character.getId())) {
        							move = moves.remove();
        							character.setX(move.x);
        							character.setY(move.y);
        							character.setFalling(move.isFalling);
        							character.setBlocking(move.isBlocking);
        							character.setDashing(move.isDashing);
        							character.setDead(move.isDead);
        							if(moves.peek().t.equals(sentClientMoves.peek().t)) {
        								sentClientMoves.remove();
        							}
        						}
        					}
        				}
        				LinkedList<NetworkMove> temp = new LinkedList<NetworkMove>();
        				character = resources.getMyCharacter();
        				while(!sentClientMoves.isEmpty()) {
        					move = sentClientMoves.remove();
        					character.setX(move.x);
							character.setY(move.y);
							character.setFalling(move.isFalling);
							character.setBlocking(move.isBlocking);
							character.setDashing(move.isDashing);
							character.setDead(move.isDead);
							temp.offer(move);
        				}
        				resources.setSentClientMoves(temp);
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
    catch (Exception e) {
      e.printStackTrace();
      System.out.println("Server seems to have died " + e.getMessage());
      System.exit(1); // Give up.
    }
  }

  private ConcurrentMap<UUID, Session> deSerialize(ConcurrentMap<UUID, SerializableSession> serializedSessions) {
	  
	  ConcurrentMap<UUID, Session> sessions = new ConcurrentHashMap<UUID, Session>();
	  
	  Iterator<Entry<UUID, SerializableSession>> it =serializedSessions.entrySet().iterator();
	  while (it.hasNext()) {
	      java.util.Map.Entry<UUID, SerializableSession> pair = (java.util.Map.Entry<UUID, SerializableSession>)it.next();
	      sessions.put(pair.getKey(), deSerialize(pair.getValue()));
	      it.remove();
	  }
	  return sessions;
  }
  
  private Session deSerialize(SerializableSession s) {
		return new Session(s.getId(), s.getClients(), s.isGameInProgress());
  }
}
