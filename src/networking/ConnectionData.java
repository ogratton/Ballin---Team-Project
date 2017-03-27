package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;

import resources.Character;
import resources.Resources;

/**
 * This class stores the connection data between a client and server and is kept on the client.
 * This mirrors the information stored on the server.
 * @author axn598
 *
 */
public class ConnectionData {

	private String sessionId;
	private ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	private ClientInformation myClient;
	private String targetId;
	private boolean gameInProgress;
	private Map<String, Character> characters = new ConcurrentHashMap<String, Character>();
	private List<CharacterInfo> charactersList = new ArrayList<CharacterInfo>();
	private Resources resources;
	private boolean ready;
	private Connection connection;
	
	/**
	 * Gets the Kryonet connection object to the server
	 * @return
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Sets the Kryonet connection object to the server for this client
	 * @param connection
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Gets the version of the resources stored on the client
	 * @return
	 */
	public Resources getResources() {
		return resources;
	}

	/**
	 * Sets the version of the resources stored on the client
	 * @param resources
	 */
	public void setResources(Resources resources) {
		this.resources = resources;
	}

	/**
	 * Gets a list of CharacterInfo objects. This list is used when receiving the updated list of positions
	 * of characters during a game.
	 * @return
	 */
	public List<CharacterInfo> getCharactersList() {
		return charactersList;
	}

	/**
	 * Sets the list of CharacterInfo objects. This list is used when receiving the updated list of positions
	 * of characters during a game.
	 * @param charactersList The list of CharcacterInfo objects
	 */
	public void setCharactersList(List<CharacterInfo> charactersList) {
		this.charactersList = charactersList;
	}

	/**
	 * Gets the list of Characters on the client
	 * @return
	 */
	public Map<String, Character> getCharacters() {
		return characters;
	}

	/**
	 * Sets the list of characters on the client.
	 * @param characters
	 */
	public void setCharacters(Map<String, Character> characters) {
		this.characters = characters;
	}
	
	/**
	 * Get a particular character using a specified index of the Character List
	 * @param id The index of the character in the lsit
	 * @return The character
	 */
	public Character getCharacter(Integer id) {
		return characters.get(id);
	}

	/**
	 * Get the character of the client.
	 * @return
	 */
	public Character getMyCharacter() {
		return characters.get(myClient.getId());
	}

	/**
	 * Set the character of the client
	 * @param myCharacter
	 */
	public void setMyCharacter(Character myCharacter) {
		characters.put(myClient.getId(), myCharacter);
	}

	/**
	 * Get the information of the client stored on the client
	 * @return
	 */
	public ClientInformation getClientInformation() {
		return myClient;
	}
	
	/**
	 * Set the information of the client stored on the client
	 * @param client
	 */
	public void setClientInformatin(ClientInformation client) {
		myClient = client;
	}
	
	/**
	 * Get the HashMap of the sessions
	 * @return
	 */
	public ConcurrentMap<String, Session> getSessionsTable() {
		return sessions;
	}
	
	/**
	 * Set the HashMap of the sessions.
	 * @return
	 */
	public void setSessionsTable(ConcurrentMap<String, Session> sessions) {
		this.sessions = sessions;
	}
	
	/**
	 * Get the ID of the session the client is currently in. Null if the client is not in a session.
	 * @return The ID of the session
	 */
	public String getSessionId() {
		return sessionId;
	}
	
	/**
	 * Store the session ID of the session the Client is currently in.
	 * @param sessionId The ID of the session
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * Get a particular client
	 * @param sessionId the ID of the session the client is currently in.
	 * @param clientId The ID of the client
	 * @return The client
	 */
	public ClientInformation getClient(String sessionId, String clientId) {
		return sessions.get(sessionId).getClient(clientId);
	}
	
	/**
	 * Add a client to a session
	 * @param sessionId The ID of the session
	 * @param clientId The ID of the client
	 * @param client The Client being stored.
	 */
	public void addClient(String sessionId, String clientId, ClientInformation client) {
		sessions.get(sessionId).addClient(clientId, client);
	}
	
	/**
	 * Remove a client from a session
	 * @param sessionId The ID of the session
	 * @param clientId The ID of the client
	 */
	public void removeClient(String sessionId, String clientId) {
		sessions.get(sessionId).removeClient(clientId);
	}
	
	/**
	 * Get the HashMap of all the sessions.
	 * @return The sessions
	 */
	public ConcurrentMap<String, Session>getSessions() {
		return sessions;
	}
	
	/**
	 * Get a list of clients for a particular session
	 * @param sessionId The ID of the session
	 * @return The list of clients
	 */
	public List<ClientInformation> getClientsForSession(String sessionId) {
		return sessions.get(sessionId).getAllClients();
	}
	
	/**
	 * Add an empty session to the HashMap of sessions.
	 */
	public void addSession() {
		Session session = new Session(new ConcurrentHashMap<String, ClientInformation>());
		sessions.put(session.getId(), session);
	}
	
	/**
	 * Remove a session
	 * @param sessionId The ID of the session
	 */
	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}
	
	/**
	 * Get a session
	 * @param sessionId The Id of the session
	 * @return The Session
	 */
	public Session getSession(String sessionId) {
		return sessions.get(sessionId);
	}
	
	/**
	 * Get all the sessions stored on the client.
	 * @return A list of all the sessions
	 */
	public List<Session> getAllSessions() {
		ArrayList<Session> sessionList = new ArrayList<Session>();
		if(sessions != null) {
			for(Session s : sessions.values()) {
		    	sessionList.add(s);
		    }
		}
	    
	    return sessionList;
	}
	
	/**
	 * Get the Id of the client this client is sending a message to.
	 * @return The ID
	 */
	public String getTargetId() {
		return targetId;
	}
	
	/**
	 * Store Id of the client this client is sending a message to.
	 * @param id The ID
	 */
	public void setTargetId(String id) {
		targetId = id;
	}
	
	/**
	 * Is the game in progress?
	 * @return true if the game is in progress, false otherwise.
	 */
	public boolean isGameInProgress() {
		return gameInProgress;
	}
	
	/**
	 * Set if the game is in progress
	 * @param bool If the game is in progress.
	 */
	public void setGameInProgress(boolean bool) {
		this.gameInProgress = bool;
	}
	
	/**
	 * Is the client ready?
	 * @return true if the client is ready for the game to start, false otherwise.
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Set if the client is ready.
	 * @param b If the client is ready.
	 */
	public void setReady(boolean b) {
		ready = b;
	}
}
