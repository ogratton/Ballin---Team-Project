package networking;

import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

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
    ConcurrentMap<Integer, Session> sessions;
    
    try {
    	while(message.getCommand() != Command.QUIT) {
    		message = (Message)server.readUnshared();
    		switch(message.getCommand()) {
    		case SESSION:
    			switch(message.getNote()) {
    			case COMPLETED:
    				System.out.println("Received sessions from Server");
    				sessions = (ConcurrentMap<Integer, Session>)message.getObject();
    				cModel.setSessionsTable(sessions);
    				break;
    			case CREATED:
    				System.out.println("Session created");
    				sessions = (ConcurrentMap<Integer, Session>)message.getObject();
    				cModel.setSessionsTable(sessions);
    				cModel.setSessionId(message.getCurrentSessionId());
    				break;
    			case JOINED:
    				System.out.println("Session Joined");
    				sessions = (ConcurrentMap<Integer, Session>)message.getObject();
    				cModel.setSessionsTable(sessions);
    				cModel.setSessionId(message.getCurrentSessionId());
    				break;
    			case LEFT:
    				System.out.println("Session Left");
    				sessions = (ConcurrentMap<Integer, Session>)message.getObject();
    				cModel.setSessionsTable(sessions);
    				cModel.setSessionId(0);
    				break;
    			}
    			break;
    		case SEND_ID:
    			cModel.setClientInformation(new ClientInformation(message.getSenderId(), message.getMessage()));
    			break;
    		case GAME:
    			GameData gameData;
    			switch(message.getNote()) {
    			case START:
    				System.out.println("Game Started");
    				gameData = (GameData)message.getObject();
    				if(!cModel.isGameInProgress()) {
    					NetworkingDemo.setGame(cModel, gameData, toServer);
    				}
    				cModel.setGameInProgress(true);
    				break;
    			case UPDATE:
    				if(cModel.getResources() != null) {
    					gameData = (GameData)message.getObject();
        				List<CharacterInfo> charactersList = gameData.getCharactersList();
        				Resources resources = cModel.getResources();
        				List<resources.Character> players = resources.getPlayerList();
        				for(int i=0; i<players.size(); i++) {
        					for(int j=0; j<charactersList.size(); j++) {
        						if (charactersList.get(j).getId() == players.get(i).getId()) {
        							players.get(i).setXWithoutNotifying(charactersList.get(j).getX());
        							players.get(i).setYWithoutNotifying(charactersList.get(j).getY());
        							if(charactersList.get(j).getId() == 1000000) {
        								//System.out.println("Updated: " + charactersList.get(j).getId());
        							}
        	    					
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
    catch (Exception e) {
      e.printStackTrace();
      System.out.println("Server seems to have died " + e.getMessage());
      System.exit(1); // Give up.
    }
  }
}
