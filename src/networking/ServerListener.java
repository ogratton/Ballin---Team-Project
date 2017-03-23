package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import gamemodes.GameModeFFA;
import resources.Resources;

/**
 * Listens to the server and fires methods when it receives messages
 * @author aaquibnaved
 *
 */
public class ServerListener extends Listener {
	
	private ConcurrentMap<String, Session> sessions;
	private ConcurrentMap<String, ClientInformation> clients;
	private ConcurrentMap<String, Resources> resourcesMap;
	private ConcurrentMap<String, Connection> connections;
	
	/**
	 * Constructs the listener
	 * @param sessions The HashMap of sessions
	 * @param clients The HashMap of clients
	 * @param resourcesMap The HashMap of Resources objects
	 * @param connections The HashMap of connections
	 */
	public ServerListener(ConcurrentMap<String, Session> sessions, ConcurrentMap<String, ClientInformation> clients, ConcurrentMap<String, Resources> resourcesMap, ConcurrentMap<String, Connection> connections) {
		this.sessions = sessions;
		this.clients = clients;
		this.resourcesMap = resourcesMap;
		this.connections = connections;
	}
	
	/**
	 * Fires when a client connects to the server.
	 */
	public void connected(Connection connection) {
		System.out.println("Connection received from: " + connection.getRemoteAddressTCP().getHostName());
	}
	
	/**
	 * Fires when the server receives a message.
	 */
	public void received(Connection connection, Object object) {
		
		Message response;
  	  	String sessionId;
  	  	Session session;
  	  	List<ClientInformation> clientList;
  	  	ClientInformation senderClient;
  	  	ClientInformation client;
    	
  	  	// Checks if it has a received a message
    	if(object instanceof Message) {
    		
    		Message message = (Message)object;
    		
    		switch(message.getCommand()) {
    		// This fires when it is the first time the client connects to the server and stores the new client
    		// It also sends back a response with the generated ID for the newly connected client.
    		case MESSAGE:
    			// For debugging:
                System.out.println(message.getMessage() + " connected");

                // Create the client and store them in the hash map.
                client = new ClientInformation(message.getMessage());
                clients.put(client.getId(), client);
                connections.put(client.getId(), connection);
                
                // Send back the ID as a message.
                Message idMessage = new Message();
                idMessage.setCommand(Command.SEND_ID);
                idMessage.setSenderId(client.getId());
                idMessage.setMessage(message.getMessage());
                connection.sendTCP(idMessage);
                
                // Send the Client all the sessions.
                response = new Message(Command.SESSION, Note.COMPLETED, client.getId(), null, null, null, sessions);
                connection.sendTCP(response);
                break;
            // Fires when the message is something to do with a session
    		case SESSION:
    			switch(message.getNote()) {
    				// Fires when refresh is pressed by a client
    				case INDEX:
    					// Sends back all the sessions to all the clients
					  	senderClient = clients.get(message.getSenderId());
					  	response = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
					  	
					  	// Sends the response to everyone connected to the server.
					  	for(Connection c : connections.values()) {
				  			c.sendTCP(response);
				  		}
					  	break;
					// Fires when a session is created
				  	case CREATE:
				  		System.out.println("Creating Session.");
				  		senderClient = clients.get(message.getSenderId());
				  		if(senderClient.getSessionId() != null && (!senderClient.getSessionId().equals(""))) {
				  			sessions.get(senderClient.getSessionId()).removeClient(senderClient.getId());
				  		}
				  		session = (Session)message.getObject();
				  		sessions.put(session.getId(), session);
				  		resourcesMap.put(session.getId(), new Resources());
				  		senderClient.setSessionId(session.getId());
				  		response = new Message(Command.SESSION, Note.CREATED, senderClient.getId(), null, session.getId(), null, sessions);
				  		
				  		System.out.println("Client ID: " + senderClient.getId());
				  		
				  		// Sends the response to everyone connected to the server.
				  		for(Connection c : connections.values()) {
				  			c.sendTCP(response);
				  		}
			  		
				  		System.out.println("Session created.");
				  		break;
				  	// Fires when a client joins a session
				  	case JOIN:
				  		senderClient = clients.get(message.getSenderId());
				  		sessionId = message.getTargetSessionId();
					  
				  		if(senderClient.getSessionId() != null && (!senderClient.getSessionId().equals(""))) {
				  			sessions.get(senderClient.getSessionId()).removeClient(senderClient.getId());
				  		}
				  		session = sessions.get(sessionId);
				  		session.addClient(senderClient.getId(), senderClient);
				  		senderClient.setSessionId(session.getId());
				  		response = new Message(Command.SESSION, Note.JOINED, senderClient.getId(), null, session.getId(), null, sessions);
				  		connections.get(senderClient.getId()).sendTCP(response);
				  		
				  		Message reset_ready = new Message(Command.SESSION, Note.RESET_READY, senderClient.getId(), null, null, null, sessions);
				  		for(int i=0; i<session.getAllClients().size(); i++) {
				  			connections.get(session.getAllClients().get(i).getId()).sendTCP(reset_ready);
				  			session.getAllClients().get(i).setReady(false);
				  			clients.get(session.getAllClients().get(i).getId()).setReady(false);
				  		}
				  		
				  		Message refresh = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
				  		for(Connection c : connections.values()) {
				  			c.sendTCP(refresh);
				  		}
			  		
				  		break;
				  	// Fires when a client leaves a session.
				  	case LEAVE:
				  		senderClient = clients.get(message.getSenderId());
				  		senderClient.setReady(false);
				  		sessionId = message.getCurrentSessionId();
				  		session = sessions.get(sessionId);
				  		System.out.println("Session ID: " + sessionId);
				  		System.out.println("Number of clients in the sessions: " + session.getAllClients().size());
				  		System.out.println("Sender Client ID: " + senderClient.getId());
				  		session.removeClient(senderClient.getId());
				  		
				  		for(int i=0; i<session.getAllClients().size(); i++) {
				  			System.out.println("Client ID: " + session.getAllClients().get(i).getId());
				  			session.getAllClients().get(i).setReady(false);
				  			clients.get(session.getAllClients().get(i).getId()).setReady(false);
				  		}
				  		
				  		senderClient.setSessionId("");
				  		
				  		// If the host leaves, set a new host for the session.
				  		if(session.getHostName().equals(senderClient.getName()) && session.getAllClients().size() >= 1) {
				  			ClientInformation newHost = session.getAllClients().get(0);
				  			session.setHostName(newHost.getName());
				  		}
				  		
				  		System.out.println("Number of clients in the sessions: " + session.getAllClients().size());
				  		// If there are no clients in the session, delete the session.
				  		if(session.getAllClients().size() <= 0) {
				  			sessions.remove(session.getId());
				  		}
				  		
				  		response = new Message(Command.SESSION, Note.LEFT, senderClient.getId(), null, null, null, sessions);
				  		//Message response2 = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
				  		System.out.println("Number of Clients: " + session.getAllClients().size());
				  		Connection conn = connections.get(senderClient.getId());
				  		conn.sendTCP(response);
				  		
				  		session.setGameInProgress(false);
				  		// Sends the response to everyone connected to the server.
				  		
				  		Message response1 = new Message(Command.SESSION, Note.RESET_READY, senderClient.getId(), null, null, null, sessions);
				  		for(int i=0; i<session.getAllClients().size(); i++) {
				  			connections.get(session.getAllClients().get(i).getId()).sendTCP(response1);
				  		}
				  		
				  		Message response2 = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
				  		for(Connection c : connections.values()) {
				  			c.sendTCP(response2);
				  		}
				  		
				  		break;
				  	default:
				  		break;
				  	}
    			// Fires when the message is something to do with the game state
			  	case GAME:
			  		GameData data;
			  		List<ClientInformation> tempClients;
			  		Connection c;
			  		switch(message.getNote()) {
			  		// Fires when the client presses "Not Ready"
			  		case STOP:
			  			client = clients.get(message.getSenderId());
			  			client.setReady(false);
			  			session = sessions.get(message.getCurrentSessionId());
			  			client = session.getClient(message.getSenderId());
			  			client.setReady(false);
			  			
			  			Message response1 = new Message(Command.SESSION, Note.COMPLETED, message.getSenderId(), null, null, null, sessions);
			  			tempClients = session.getAllClients();
		  				// Sends the response to everyone in the session.
		  				// This update who is ready for each client in the session
		  				for(int i=0; i<tempClients.size(); i++) {
		  					c = connections.get(tempClients.get(i).getId());
		  					c.sendTCP(response1);
		  				}
			  			
			  			break;
			  		// Fires when the client presses "Ready"
			  		case START:
			  			session = sessions.get(message.getCurrentSessionId());
			  			
			  			ClientInformation sentClient = (ClientInformation)message.getObject();
			  			
			  			client = clients.get(message.getSenderId());
			  			client.setReady(true);
			  			
			  			client = session.getClient(message.getSenderId());
			  			client.setReady(true);
			  			client.setCharacterClass(sentClient.getCharacterClass());
			  			client.setPlayerNumber(sentClient.getPlayerNumber());
			  			
			  			Message response2 = new Message(Command.SESSION, Note.COMPLETED, message.getSenderId(), null, null, null, sessions);
			  			tempClients = session.getAllClients();
		  				// Sends the response to everyone in the session.
		  				// This update who is ready for each client in the session
		  				for(int i=0; i<tempClients.size(); i++) {
		  					c = connections.get(tempClients.get(i).getId());
		  					c.sendTCP(response2);
		  				}
			  			
			  			//System.out.println("Current Session ID: " + message.getCurrentSessionId());
			  			//System.out.println("Current Session: " + session);
			  			// If all the clients are ready, then start the game.
			  			if(session.allClientsReady()) {
			  				session.setGameInProgress(true);

			  				NetworkingDemo.startServerGame(session, resourcesMap, sessions, connections);
			  				
			  				List<resources.Character> characters = resourcesMap.get(session.getId()).getPlayerList();
			  				
				  			
			  				session.setGameInProgress(true);
			  				//List<resources.Character> characters = resourcesMap.get(session.getId()).getPlayerList();
			  				List<CharacterInfo> characterInfo = new ArrayList<CharacterInfo>();
			  				for(int i=0; i<characters.size(); i++) {
			  					resources.Character character = characters.get(i);
			  					characterInfo.add(new CharacterInfo(character.getId(), character.getX(), character.getY(), character.getPlayerNumber(), character.getClassType(), character.getName()));
			  				}
						  
			  				data = new GameData(characterInfo);
			  				// Creates a message to send to all clients connected to the session
			  				Message startGame = new Message(Command.GAME, Note.START, message.getSenderId(), message.getReceiverId(), message.getCurrentSessionId(), message.getTargetSessionId(), data);
			  				tempClients = session.getAllClients();
			  				// Sends the response to everyone in the session.
			  				// This will start the game for everyone
			  				for(int i=0; i<tempClients.size(); i++) {
			  					c = connections.get(tempClients.get(i).getId());
			  					c.sendTCP(startGame);
			  				}
			  				
			  				//System.out.println("X: " + resourcesMap.get(session.getId()).getPlayerList().get(0).getX());
			  				//// Countdown
			  				try {
								Thread.sleep(1000);
								Message countdown = new Message(Command.GAME, Note.COUNTDOWN, message.getSenderId(), message.getReceiverId(), message.getCurrentSessionId(), message.getTargetSessionId());
								for(int i=0; i<tempClients.size(); i++) {
				  					c = connections.get(tempClients.get(i).getId());
				  					c.sendTCP(countdown);
				  				}
			  					Thread.sleep(1000);
								for(int i=0; i<tempClients.size(); i++) {
				  					c = connections.get(tempClients.get(i).getId());
				  					c.sendTCP(countdown);
				  					
				  				}
								Thread.sleep(1000);
								for(int i=0; i<tempClients.size(); i++) {
				  					c = connections.get(tempClients.get(i).getId());
				  					c.sendTCP(countdown);
				  				}
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			  				
			  				Resources res = resourcesMap.get(session.getId());
			  				GameModeFFA mode = res.gamemode;
			  				
			  				GameEndChecker checker = new GameEndChecker(session.getId(), resourcesMap, sessions, connections, resourcesMap.get(session.getId()));
			  				checker.start();
			  				
			  				((Thread) mode).start();
			  				
			  			}
					  
			  			break;
			  		// This fires when the game state is updated by any of the clients
			  		case UPDATE:
			  			session = sessions.get(message.getCurrentSessionId());
			  			//System.out.println("X: " + resourcesMap.get(session.getId()).getPlayerList().get(0).getX());
					  
			  			if(session.isGameInProgress()) {
			  			// Reads the updated data.
				  			data = (GameData)message.getObject();
				  			Resources res = resourcesMap.get(session.getId());
				  			CharacterInfo info = data.getInfo();
				  			for(int i=0; i<res.getPlayerList().size(); i++) {
				  				resources.Character ch = res.getPlayerList().get(i);
				  				
				  				// Only update the player that was changed.
				  				if(info.getId().equals(ch.getId())) {
				  					//c.setControls(info.isUp(), info.isDown(), info.isLeft(), info.isRight(), info.isDashing(), info.isBlocking());
				  					ch.setUp(info.isUp());
				  					ch.setDown(info.isDown());
				  					ch.setRight(info.isRight());
				  					ch.setLeft(info.isLeft());
				  					if(info.isDashing()) {
				  						ch.setDashing(info.isDashing());
				  					}
//				  					if(info.sendDashing) {
//				  						c.setDashing(info.isDashing());
//				  					}
//				  					if(info.sendBlocking) {
//				  						c.setBlocking(info.isBlocking());
//				  					}
				  				}
				  			}
			  			}
			  			
					  
			  			break;
			  		default:
			  			break;
			  		}
			  	default:
			  		break;
			  	} 
    	}
	}
	
	/**
	 * Fires when a client disconnects.
	 */
	public void disconnected(Connection connection) {
		System.out.println("Disconnection");
		
		// Removing client from the server.
		for (Map.Entry<String, Connection> entry : connections.entrySet()) {
		    String key = entry.getKey();
		    Connection value = entry.getValue();
		    
		    // Remove the connection from the connections table
		    if(value.equals(connection)) {
		    	ClientInformation client = clients.get(key);
		    	clients.remove(key);
		    	connections.remove(key);
		    	
		    	// Remove the player from the resources for that session
		    	String sessionId = client.getSessionId();
		    	if(sessionId != null && sessions.get(sessionId) != null) {
		    		Session session = sessions.get(sessionId);
		    		session.removeClient(key);
			    	
		    		if(sessions.get(sessionId).isGameInProgress()) {
		    			Resources resources = resourcesMap.get(sessionId);
				    	ArrayList<resources.Character> characters = resources.getPlayerList();
				    	for(int i=0; i<characters.size(); i++) {
				    		if(characters.get(i).getId().equals(key)) {
				    			characters.get(i).setLives(0);
				    			characters.get(i).setDead(true);
				    			characters.get(i).setVisible(false);
				    			//characters.remove(i);
				    			Message removePlayer = new Message(Command.GAME, Note.REMOVE_PLAYER, null, key, sessionId, sessionId);
				    			
				    			List<ClientInformation> temp1Clients = session.getAllClients();
				    			Connection c;
				  				// Sends the response to everyone in the session.
				  				// This will start the game for everyone
				  				for(int j=0; j<temp1Clients.size(); j++) {
				  					c = connections.get(temp1Clients.get(j).getId());
				  					c.sendTCP(removePlayer);
				  				}
				  				break;
				    		}
				    	}
		    		}
		    		break;
		    	}
		    }
		}
		
		for (Map.Entry<String, Session> entry : sessions.entrySet()) {
		    String key = entry.getKey();
		    Session value = entry.getValue();
		    
		    if(value.getAllClients().size() <= 0) {
		    	sessions.remove(key);
		    }
		}

    	Message refresh = new Message(Command.SESSION, Note.COMPLETED, null, null, null, null, sessions);
    	for(Connection c: connections.values()) {
    		c.sendTCP(refresh);
    	}
	}
}

