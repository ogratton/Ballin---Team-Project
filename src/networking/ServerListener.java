package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

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
	
	public void connected(Connection connection) {
		System.out.println("Connection received from: " + connection.getRemoteAddressTCP().getHostName());
	}
	
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
                break;
            // Fires when the message is something to do with a session
    		case SESSION:
    			switch(message.getNote()) {
    				// Fires when refresh is pressed by a client
    				case INDEX:
    					// Sends back all the sessions to all the clients
					  	senderClient = clients.get(message.getSenderId());
					  	//ConcurrentMap<String, SerializableSession> serialized = serialize(this.sessions);
					  	response = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
					  	//System.out.println("Sessions Inside While Loop: " + serialize(this.sessions).size());
					  	//System.out.println("Number of Sessions: " + sessions.size());
					  	
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
				  		
				  		// Sends the response to everyone connected to the server.
				  		for(Connection c : connections.values()) {
				  			c.sendTCP(response);
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
				  		}
				  		
				  		senderClient.setSessionId("");
				  		
				  		// If the host leaves, set a new host for the session.
				  		if(session.getId().equals(senderClient.getSessionId()) && session.getAllClients().size() <= 0) {
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
				  		
				  	// Sends the response to everyone connected to the server.
				  		
				  		Message response1 = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
				  		for(Connection c : connections.values()) {
				  			c.sendTCP(response1);
				  		}
				  		
				  		break;
				  	default:
				  		break;
				  	}
    			// Fires when the message is something to do with the game state
			  	case GAME:
			  		GameData data;
			  		switch(message.getNote()) {
			  		// Fires when the client presses "Not Ready"
			  		case STOP:
			  			client = clients.get(message.getSenderId());
			  			client.setReady(false);
			  			session = sessions.get(message.getCurrentSessionId());
			  			client = session.getClient(message.getSenderId());
			  			client.setReady(false);
			  			break;
			  		// Fires when the client presses "Ready"
			  		case START:
			  			session = sessions.get(message.getCurrentSessionId());
			  			client = session.getClient(message.getSenderId());
			  			client.setReady(true);
			  			client = clients.get(message.getSenderId());
			  			client.setReady(true);
			  			
			  			//System.out.println("Current Session ID: " + message.getCurrentSessionId());
			  			//System.out.println("Current Session: " + session);
			  			// If all the clients are ready, then start the game.
			  			if(session.allClientsReady()) {

			  				NetworkingDemo.startServerGame(session, resourcesMap, sessions, connections);
			  				
			  				List<resources.Character> characters = resourcesMap.get(session.getId()).getPlayerList();
			  				
			  				// Update player colour and class type of the client on the server.
				  			for(int i=0; i<characters.size(); i++) {
				  				if(characters.get(i).equals(client.getId())) {
				  					characters.get(i).setPlayerNumber(client.getPlayerNumber());
				  					characters.get(i).setClassType(client.getCharacterClass());
				  				}
				  			}
				  			
			  				session.setGameInProgress(true);
			  				//List<resources.Character> characters = resourcesMap.get(session.getId()).getPlayerList();
			  				List<CharacterInfo> characterInfo = new ArrayList<CharacterInfo>();
			  				for(int i=0; i<characters.size(); i++) {
			  					resources.Character character = characters.get(i);
			  					characterInfo.add(new CharacterInfo(character.getId(), character.getX(), character.getY(), character.getPlayerNumber()));
			  				}
						  
			  				data = new GameData(characterInfo);
			  				// Creates a message to send to all clients connected to the session
			  				Message startGame = new Message(Command.GAME, Note.START, message.getSenderId(), message.getReceiverId(), message.getCurrentSessionId(), message.getTargetSessionId(), data);
			  				
			  				List<ClientInformation> tempClients = session.getAllClients();
			  				Connection c;
			  				// Sends the response to everyone in the session.
			  				// This will start the game for everyone
			  				for(int i=0; i<tempClients.size(); i++) {
			  					c = connections.get(tempClients.get(i).getId());
			  					c.sendTCP(startGame);
			  				}
			  			}
					  
			  			break;
			  		// This fires when the game state is updated by any of the clients
			  		case UPDATE:
			  			session = sessions.get(message.getCurrentSessionId());
					  
			  			// Reads the updated data.
			  			data = (GameData)message.getObject();
			  			Resources res = resourcesMap.get(session.getId());
			  			CharacterInfo info = data.getInfo();
			  			for(int i=0; i<res.getPlayerList().size(); i++) {
			  				resources.Character c = res.getPlayerList().get(i);
			  				
			  				// Only update the player that was changed.
			  				if(info.getId().equals(c.getId())) {
			  					//c.setControls(info.isUp(), info.isDown(), info.isLeft(), info.isRight(), info.isDashing(), info.isBlocking());
			  					c.setUp(info.isUp());
			  					c.setDown(info.isDown());
			  					c.setRight(info.isRight());
			  					c.setLeft(info.isLeft());
			  					if(info.isDashing()) {
			  						c.setDashing(info.isDashing());
			  					}
//			  					if(info.sendDashing) {
//			  						c.setDashing(info.isDashing());
//			  					}
//			  					if(info.sendBlocking) {
//			  						c.setBlocking(info.isBlocking());
//			  					}
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
	
	public void disconnected(Connection connection) {
		System.out.println("Disconnection");
		
		// Removing client from the server.
		for (Map.Entry<String, Connection> entry : connections.entrySet()) {
		    String key = entry.getKey();
		    Connection value = entry.getValue();
		    if(value.equals(connection)) {
		    	ClientInformation client = clients.get(key);
		    	String sessionId = client.getSessionId();
		    	sessions.get(sessionId).removeClient(key);
		    	clients.remove(key);
		    	connections.remove(key);
		    	
		    	Resources resources = resourcesMap.get(sessionId);
		    	ArrayList<resources.Character> characters = resources.getPlayerList();
		    	for(int i=0; i<characters.size(); i++) {
		    		if(characters.get(i).getId().equals(key)) {
		    			characters.get(i).setLives(0);
		    			characters.get(i).setDead(true);
		    		}
		    	}
		    }
		    break;
		}
	}
}

