package networking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import resources.Character;

public class ConnectionData {

	private int sessionId;
	private ConcurrentMap<Integer, Session> sessions = new ConcurrentHashMap<Integer, Session>();
	private ClientInformation myClient;
	private Display display;
	private int highlightedSessionId = 0;
	private int highlightedClientId = 0;
	private String receivedMessage;
	private int targetId;
	private boolean gameInProgress;
	private Map<Integer, Character> characters = new ConcurrentHashMap<Integer, Character>();
	private List<CharacterInfo> charactersList = new ArrayList<CharacterInfo>();
	
	public List<CharacterInfo> getCharactersList() {
		return charactersList;
	}

	public void setCharactersList(List<CharacterInfo> charactersList) {
		this.charactersList = charactersList;
	}

	public Map<Integer, Character> getCharacters() {
		return characters;
	}

	public void setCharacters(Map<Integer, Character> characters) {
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
	
	public ConcurrentMap<Integer, Session> getSessionsTable() {
		return sessions;
	}
	
	public void setSessionsTable(ConcurrentMap<Integer, Session> sessions) {
		this.sessions = sessions;
	}
	
	public int getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
	public ClientInformation getClient(int sessionId, int clientId) {
		return sessions.get(sessionId).getClient(clientId);
	}
	
	public void addClient(int sessionId, int clientId, ClientInformation client) {
		sessions.get(sessionId).addClient(clientId, client);
	}
	
	public void removeClient(int sessionId, int clientId) {
		sessions.get(sessionId).removeClient(clientId);
	}
	
	public ConcurrentMap<Integer, Session>getSessions() {
		return sessions;
	}
	
	public List<ClientInformation> getClientsForSession(int sessionId) {
		return sessions.get(sessionId).getAllClients();
	}
	
	public void addSession(int sessionId) {
		sessions.put(sessionId, new Session(sessionId, new ConcurrentHashMap<Integer, ClientInformation>()));
	}
	
	public void removeSession(int sessionId) {
		sessions.remove(sessionId);
	}
	
	public Session getSession(int sessionId) {
		return sessions.get(sessionId);
	}
	
	public List<Session> getAllSessions() {
		ArrayList<Session> sessionList = new ArrayList<Session>();
	    for(Session s : sessions.values()) {
	    	sessionList.add(s);
	    }
	    return sessionList;
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public void setDisplay(Display display) {
		this.display = display;
	}
	
	public int getHighlightedSessionId() {
		return highlightedSessionId;
	}
	
	public void setHiglightedSessionId(int id) {
		highlightedSessionId = id;
	}
	
	public int getHighlightedClientId() {
		return highlightedClientId;
	}
	
	public void setHighlightedClientId(int clientId) {
		highlightedClientId = clientId;
	}
	
	public String getReceivedMessage() {
		return receivedMessage;
	}
	
	public void setReceivedMessage(String message) {
		receivedMessage = message;
	}
	
	public int getTargetId() {
		return targetId;
	}
	
	public void setTargetId(int id) {
		targetId = id;
	}
	
	public boolean isGameInProgress() {
		return gameInProgress;
	}
	
	public void setGameInProgress(boolean bool) {
		this.gameInProgress = bool;
	}
}
