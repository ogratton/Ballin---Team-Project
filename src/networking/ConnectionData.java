package networking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import resources.Character;
import resources.Resources;

public class ConnectionData {

	private UUID sessionId;
	private ConcurrentMap<UUID, Session> sessions = new ConcurrentHashMap<UUID, Session>();
	private ClientInformation myClient;
	private Display display;
	private UUID highlightedSessionId = null;
	private UUID highlightedClientId = null;
	private String receivedMessage;
	private UUID targetId;
	private boolean gameInProgress;
	private Map<UUID, Character> characters = new ConcurrentHashMap<UUID, Character>();
	private List<CharacterInfo> charactersList = new ArrayList<CharacterInfo>();
	private Resources resources;
	private List<GameData> requests = new LinkedList<GameData>();
	
	public List<GameData> getRequests() {
		return requests;
	}
	
	public void setRequests(List<GameData> requests) {
		this.requests = requests;
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

	public Map<UUID, Character> getCharacters() {
		return characters;
	}

	public void setCharacters(Map<UUID, Character> characters) {
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
	
	public ConcurrentMap<UUID, Session> getSessionsTable() {
		return sessions;
	}
	
	public void setSessionsTable(ConcurrentMap<UUID, Session> sessions) {
		this.sessions = sessions;
	}
	
	public UUID getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(UUID sessionId) {
		this.sessionId = sessionId;
	}
	
	public ClientInformation getClient(UUID sessionId, UUID clientId) {
		return sessions.get(sessionId).getClient(clientId);
	}
	
	public void addClient(UUID sessionId, UUID clientId, ClientInformation client) {
		sessions.get(sessionId).addClient(clientId, client);
	}
	
	public void removeClient(UUID sessionId, UUID clientId) {
		sessions.get(sessionId).removeClient(clientId);
	}
	
	public ConcurrentMap<UUID, Session>getSessions() {
		return sessions;
	}
	
	public List<ClientInformation> getClientsForSession(UUID sessionId) {
		return sessions.get(sessionId).getAllClients();
	}
	
	public void addSession() {
		Session session = new Session(new ConcurrentHashMap<UUID, ClientInformation>());
		sessions.put(session.getId(), session);
	}
	
	public void removeSession(UUID sessionId) {
		sessions.remove(sessionId);
	}
	
	public Session getSession(UUID sessionId) {
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
	
	public UUID getHighlightedSessionId() {
		return highlightedSessionId;
	}
	
	public void setHiglightedSessionId(UUID id) {
		highlightedSessionId = id;
	}
	
	public UUID getHighlightedClientId() {
		return highlightedClientId;
	}
	
	public void setHighlightedClientId(UUID clientId) {
		highlightedClientId = clientId;
	}
	
	public String getReceivedMessage() {
		return receivedMessage;
	}
	
	public void setReceivedMessage(String message) {
		receivedMessage = message;
	}
	
	public UUID getTargetId() {
		return targetId;
	}
	
	public void setTargetId(UUID id) {
		targetId = id;
	}
	
	public boolean isGameInProgress() {
		return gameInProgress;
	}
	
	public void setGameInProgress(boolean bool) {
		this.gameInProgress = bool;
	}
}
