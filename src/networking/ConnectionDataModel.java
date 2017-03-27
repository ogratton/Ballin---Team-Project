package networking;

import java.util.List;
import resources.Character;
import resources.Resources;

import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;

/**
 * A model for the Connection Data class
 * @author axn598
 *
 */
public class ConnectionDataModel extends Observable {

	private ConnectionData data;
	
	/**
	 * Constructor for this class.
	 * @param data The Connection Data object.
	 */
	public ConnectionDataModel(ConnectionData data) {
		this.data = data;
	}
	
	/**
	 * Get the ID of the client
	 * @return The ID of the client
	 */
	public String getMyId() {
		return data.getClientInformation().getId();
	}
	
	/**
	 * Set the ID of the client
	 * @param id The ID
	 */
	public void setMyId(String id) {
		data.getClientInformation().setId(id);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the ClientInformation object of the client
	 * @return The ClientInformation object.
	 */
	public ClientInformation getClientInformation() {
		return data.getClientInformation();
	}
	
	/**
	 * Set the information of the client stored on the client
	 * @param client
	 */
	public synchronized void setClientInformation(ClientInformation client) {
		data.setClientInformatin(client);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the HashMap of the sessions
	 * @return
	 */
	public ConcurrentMap<String, Session> getSessionsTable() {
		return data.getSessionsTable();
	}
	
	/**
	 * Set the HashMap of the sessions.
	 * @return
	 */
	public void setSessionsTable(ConcurrentMap<String, Session> sessions) {
		data.setSessionsTable(sessions);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the ID of the session the client is currently in. Null if the client is not in a session.
	 * @return The ID of the session
	 */
	public String getSessionId() {
		return this.data.getSessionId();
	}
	
	/**
	 * Store the session ID of the session the Client is currently in.
	 * @param sessionId The ID of the session
	 */
	public synchronized void setSessionId(String id) {
		this.data.setSessionId(id);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the whole Connection Data object.
	 * @return
	 */
	public ConnectionData getConnectionData() {
		return this.data;
	}
	
	/**
	 * Add a client to a session
	 * @param sessionId The ID of the session
	 * @param clientId The ID of the client
	 * @param client The Client being stored.
	 */
	public synchronized void addClient(String sessionId, String clientId, ClientInformation client) {
		this.data.addClient(sessionId, clientId, client);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Remove a client from a session
	 * @param sessionId The ID of the session
	 * @param clientId The ID of the client
	 */
	public synchronized void removeClient(String sessionId, String clientId) {
		this.data.removeClient(sessionId, clientId);
		setChanged();
		notifyObservers();
	}
	/**
	 * Remove a client from a session
	 * @param sessionId The ID of the session
	 * @param clientId The ID of the client
	 */
	public synchronized Session getSession(String sessionId) {
		return data.getSession(sessionId);
	}
	
	/**
	 * Get a session
	 * @param sessionId The Id of the session
	 * @return The Session
	 */
	public synchronized ClientInformation getClient(String sessionId, String clientId) {
		return this.data.getClient(sessionId, clientId);
	}
	
	/**
	 * Get the Id of the client this client is sending a message to.
	 * @return The ID
	 */
	public synchronized String getTargetId() {
		return data.getTargetId();
	}
	
	/**
	 * Store Id of the client this client is sending a message to.
	 * @param id The ID
	 */
	public synchronized void setTargetId(String id) {
		data.setTargetId(id);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get all the sessions stored on the client.
	 * @return A list of all the sessions
	 */
	public synchronized List<Session> getAllSessions() {
		return data.getAllSessions();
	}
	
	/**
	 * Is the game in progress?
	 * @return true if the game is in progress, false otherwise.
	 */
	public synchronized boolean isGameInProgress() {
		return data.isGameInProgress();
	}
	
	/**
	 * Set if the game is in progress
	 * @param bool If the game is in progress.
	 */
	public synchronized void setGameInProgress(boolean bool) {
		data.setGameInProgress(bool);
	}
	
	/**
	 * Gets the list of Characters on the client
	 * @return
	 */
	public Map<String, Character> getCharacters() {
		return data.getCharacters();
	}

	/**
	 * Sets the list of characters on the client.
	 * @param characters
	 */
	public void setCharacters(Map<String, Character> characters) {
		data.setCharacters(characters);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get a particular character using a specified index of the Character List
	 * @param id The index of the character in the lsit
	 * @return The character
	 */
	public Character getCharacter(Integer id) {
		return data.getCharacter(id);
	}

	/**
	 * Get the character of the client.
	 * @return
	 */
	public Character getMyCharacter() {
		return data.getMyCharacter();
	}

	/**
	 * Set the character of the client
	 * @param myCharacter
	 */
	public synchronized void setMyCharacter(Character myCharacter) {
		data.setMyCharacter(myCharacter);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Gets a list of CharacterInfo objects. This list is used when receiving the updated list of positions
	 * of characters during a game.
	 * @return
	 */
	public List<CharacterInfo> getCharactersList() {
		return data.getCharactersList();
	}
	
	/**
	 * Sets the list of CharacterInfo objects. This list is used when receiving the updated list of positions
	 * of characters during a game.
	 * @param charactersList The list of CharcacterInfo objects
	 */
	public synchronized void setCharactersList(List<CharacterInfo> list) {
		data.setCharactersList(list);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Gets the version of the resources stored on the client
	 * @return
	 */
	public Resources getResources() {
		return data.getResources();
	}
	
	/**
	 * Sets the version of the resources stored on the client
	 * @param resources
	 */
	public synchronized void setResources(Resources res) {
		data.setResources(res);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Is the client ready?
	 * @return true if the client is ready for the game to start, false otherwise.
	 */
	public boolean isReady() {
		return data.isReady();
	}
	
	/**
	 * Set if the client is ready.
	 * @param b If the client is ready.
	 */
	public synchronized void setReady(boolean b) {
		data.setReady(b);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Gets the Kryonet connection object to the server
	 * @return
	 */
	public Connection getConnection() {
		return data.getConnection();
	}
	
	/**
	 * Sets the Kryonet connection object to the server for this client
	 * @param connection
	 */
	public synchronized void setConnection(Connection connection) {
		data.setConnection(connection);
	}
}
