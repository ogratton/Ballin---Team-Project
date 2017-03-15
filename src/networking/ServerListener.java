package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import resources.Resources;

public class ServerListener extends Listener {
	
	private ConcurrentMap<String, Session> sessions;
	private ConcurrentMap<String, ClientInformation> clients;
	private ConcurrentMap<String, Resources> resourcesMap;
	private ConcurrentMap<String, Connection> connections;
	
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
    	
    	if(object instanceof Message) {
    		
    		Message message = (Message)object;
    		
    		switch(message.getCommand()) {
    		case MESSAGE:
    			// For debugging:
                System.out.println(message.getMessage() + " connected");

                client = new ClientInformation(message.getMessage());
                clients.put(client.getId(), client);
                connections.put(client.getId(), connection);
                
                Message idMessage = new Message();
                idMessage.setCommand(Command.SEND_ID);
                idMessage.setSenderId(client.getId());
                idMessage.setMessage(message.getMessage());
                connection.sendTCP(idMessage);
                break;
    		case SESSION:
    			switch(message.getNote()) {
    			case INDEX:
    					System.out.println("Got here");
					  	senderClient = clients.get(message.getSenderId());
					  	//ConcurrentMap<String, SerializableSession> serialized = serialize(this.sessions);
					  	response = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
					  	//System.out.println("Sessions Inside While Loop: " + serialize(this.sessions).size());
					  	System.out.println("Number of Sessions: " + sessions.size());
					  	for(Connection c : connections.values()) {
				  			c.sendTCP(response);
				  		}
					  	break;
				  	case CREATE:
				  		System.out.println("Creating Session.");
				  		senderClient = clients.get(message.getSenderId());
				  		if(senderClient.getSessionId() != null) {
				  			sessions.get(senderClient.getSessionId()).removeClient(senderClient.getId());
				  		}
				  		ConcurrentMap<String, ClientInformation> sessionClients = new ConcurrentHashMap<String, ClientInformation>();
				  		sessionClients.put(senderClient.getId(), senderClient);
				  		session = new Session(sessionClients);
				  		sessions.put(session.getId(), session);
				  		resourcesMap.put(session.getId(), new Resources());
				  		senderClient.setSessionId(session.getId());
				  		response = new Message(Command.SESSION, Note.CREATED, senderClient.getId(), null, session.getId(), null, sessions);
				  		
				  		for(Connection c : connections.values()) {
				  			c.sendTCP(response);
				  		}
			  		
				  		System.out.println("Session created.");
				  		break;
				  	case JOIN:
				  		senderClient = clients.get(message.getSenderId());
				  		sessionId = message.getTargetSessionId();
					  
				  		if(senderClient.getSessionId() != null) {
				  			sessions.get(senderClient.getSessionId()).removeClient(senderClient.getId());
				  		}
				  		session = sessions.get(sessionId);
				  		session.addClient(senderClient.getId(), senderClient);
				  		senderClient.setSessionId(session.getId());
				  		response = new Message(Command.SESSION, Note.JOINED, senderClient.getId(), null, session.getId(), null, sessions);
				  		
				  		for(Connection c : connections.values()) {
				  			c.sendTCP(response);
				  		}
			  		
				  		break;
				  	case LEAVE:
				  		senderClient = clients.get(message.getSenderId());
				  		senderClient.setReady(false);
				  		sessionId = message.getTargetSessionId();
				  		session = sessions.get(sessionId);
				  		sessions.get(sessionId).removeClient(senderClient.getId());
				  		senderClient.setSessionId("");
				  		response = new Message(Command.SESSION, Note.LEFT, senderClient.getId(), null, null, null, sessions);
				  		senderClient.getQueue().offer(response);
				  		response = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
				  		
				  		for(Connection c : connections.values()) {
				  			c.sendTCP(response);
				  		}
				  		
				  		break;
				  	default:
				  		break;
				  	}
			  	case GAME:
			  		GameData data;
			  		switch(message.getNote()) {
			  		case STOP:
			  			//session = sessions.get(message.getCurrentSessionId());
			  			client = clients.get(message.getSenderId());
			  			client.setReady(false);
			  			break;
			  		case START:
			  			session = sessions.get(message.getCurrentSessionId());
			  			client = clients.get(message.getSenderId());
			  			client.setReady(true);
					  
			  			if(session.allClientsReady()) {
			  				NetworkingDemo.startServerGame(session, resourcesMap, sessions, connections);
			  				session.setGameInProgress(true);
			  				List<resources.Character> characters = resourcesMap.get(session.getId()).getPlayerList();
			  				List<CharacterInfo> characterInfo = new ArrayList<CharacterInfo>();
			  				for(int i=0; i<characters.size(); i++) {
			  					resources.Character character = characters.get(i);
			  					characterInfo.add(new CharacterInfo(character.getId(), character.getX(), character.getY(), character.getPlayerNumber()));
			  				}
						  
			  				data = new GameData(characterInfo);
			  				Message startGame = new Message(Command.GAME, Note.START, message.getSenderId(), message.getReceiverId(), message.getCurrentSessionId(), message.getTargetSessionId(), data);
						  
			  				for(Connection c : connections.values()) {
					  			c.sendTCP(startGame);
					  		}
			  			}
					  
					  
			  			break;
			  		case UPDATE:
			  			session = sessions.get(message.getCurrentSessionId());
					  
			  			data = (GameData)message.getObject();
			  			Resources res = resourcesMap.get(session.getId());
			  			CharacterInfo info = ((GameData)message.getObject()).getInfo();
			  			for(int i=0; i<res.getPlayerList().size(); i++) {
			  				resources.Character c = res.getPlayerList().get(i);
			  				if(info.getId().equals(c.getId())) {
			  					//c.setControls(info.isUp(), info.isDown(), info.isLeft(), info.isRight(), info.isDashing(), info.isBlocking());
			  					c.setUp(info.isUp());
			  					c.setDown(info.isDown());
			  					c.setRight(info.isRight());
			  					c.setLeft(info.isLeft());
			  					if(info.sendDashing) {
			  						c.setDashing(info.isDashing());
			  					}
			  					if(info.sendBlocking) {
			  						c.setBlocking(info.isBlocking());
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
	
	public void disconnected(Connection connection) {
		System.out.println("Disconnection");
	}
}

