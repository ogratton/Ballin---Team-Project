package networking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;

import resources.Character;
import resources.Resources;

public class ConnectionData {

	private String sessionId;
	private ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	private ClientInformation myClient;
	private String highlightedSessionId = null;
	private String highlightedClientId = null;
	private String receivedMessage;
	private String targetId;
	private boolean gameInProgress;
	private Map<String, Character> characters = new ConcurrentHashMap<String, Character>();
	private List<CharacterInfo> charactersList = new ArrayList<CharacterInfo>();
	private Resources resources;
	private boolean ready;
	private Connection connection;
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Resources getResources() {
		return resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public List<CharacterInfo> getCharactersList() {
		return charactersList;
	}

	public void setCharactersList(List<CharacterInfo> charactersList) {
		this.charactersList = charactersList;
	}

	public Map<String, Character> getCharacters() {
		return characters;
	}

	public void setCharacters(Map<String, Character> characters) {
		this.characters = characters;
	}
	
	public Character getCharacter(Integer id) {
		return characters.get(id);
	}

	public Character getMyCharacter() {
		return characters.get(myClient.getId());
	}

	public void setMyCharacter(Character myCharacter) {
		characters.put(myClient.getId(), myCharacter);
	}

	public ClientInformation getClientInformation() {
		return myClient;
	}
	
	public void setClientInformatin(ClientInformation client) {
		myClient = client;
	}
	
	public ConcurrentMap<String, Session> getSessionsTable() {
		return sessions;
	}
	
	public void setSessionsTable(ConcurrentMap<String, Session> sessions) {
		this.sessions = sessions;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public ClientInformation getClient(String sessionId, String clientId) {
		return sessions.get(sessionId).getClient(clientId);
	}
	
	public void addClient(String sessionId, String clientId, ClientInformation client) {
		sessions.get(sessionId).addClient(clientId, client);
	}
	
	public void removeClient(String sessionId, String clientId) {
		sessions.get(sessionId).removeClient(clientId);
	}
	
	public ConcurrentMap<String, Session>getSessions() {
		return sessions;
	}
	
	public List<ClientInformation> getClientsForSession(String sessionId) {
		return sessions.get(sessionId).getAllClients();
	}
	
	public void addSession() {
		Session session = new Session(new ConcurrentHashMap<String, ClientInformation>());
		sessions.put(session.getId(), session);
	}
	
	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}
	
	public Session getSession(String sessionId) {
		return sessions.get(sessionId);
	}
	
	public List<Session> getAllSessions() {
		ArrayList<Session> sessionList = new ArrayList<Session>();
		if(sessions != null) {
			for(Session s : sessions.values()) {
		    	sessionList.add(s);
		    }
		}
	    
	    return sessionList;
	}
	
	public String getHighlightedSessionId() {
		return highlightedSessionId;
	}
	
	public void setHiglightedSessionId(String id) {
		highlightedSessionId = id;
	}
	
	public String getHighlightedClientId() {
		return highlightedClientId;
	}
	
	public void setHighlightedClientId(String clientId) {
		highlightedClientId = clientId;
	}
	
	public String getReceivedMessage() {
		return receivedMessage;
	}
	
	public void setReceivedMessage(String message) {
		receivedMessage = message;
	}
	
	public String getTargetId() {
		return targetId;
	}
	
	public void setTargetId(String id) {
		targetId = id;
	}
	
	public boolean isGameInProgress() {
		return gameInProgress;
	}
	
	public void setGameInProgress(boolean bool) {
		this.gameInProgress = bool;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean b) {
		ready = b;
	}
}
