package networking;

import java.util.List;
import resources.Character;
import resources.Resources;

import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;

public class ConnectionDataModel extends Observable {

	private ConnectionData data;
	
	public ConnectionDataModel(ConnectionData data) {
		this.data = data;
	}
	
	public String getMyId() {
		return data.getClientInformation().getId();
	}
	
	public void setMyId(String id) {
		data.getClientInformation().setId(id);
		setChanged();
		notifyObservers();
	}
	
	public ClientInformation getClientInformation() {
		return data.getClientInformation();
	}
	
	public synchronized void setClientInformation(ClientInformation client) {
		data.setClientInformatin(client);
		setChanged();
		notifyObservers();
	}
	
	public ConcurrentMap<String, Session> getSessionsTable() {
		return data.getSessionsTable();
	}
	
	public void setSessionsTable(ConcurrentMap<String, Session> sessions) {
		data.setSessionsTable(sessions);
		setChanged();
		notifyObservers();
	}
	
	public String getSessionId() {
		return this.data.getSessionId();
	}
	
	public synchronized void setSessionId(String id) {
		this.data.setSessionId(id);
		setChanged();
		notifyObservers();
	}
	
	public ConnectionData getConnectionData() {
		return this.data;
	}
	
	public synchronized void addClient(String sessionId, String clientId, ClientInformation client) {
		this.data.addClient(sessionId, clientId, client);
		setChanged();
		notifyObservers();
	}
	
	public synchronized void removeClient(String sessionId, String clientId) {
		this.data.removeClient(sessionId, clientId);
		setChanged();
		notifyObservers();
	}
	
	public synchronized Session getSession(String sessionId) {
		return data.getSession(sessionId);
	}
	
	public synchronized ClientInformation getClient(String sessionId, String clientId) {
		return this.data.getClient(sessionId, clientId);
	}
	
	public synchronized String getTargetId() {
		return data.getTargetId();
	}
	
	public synchronized void setTargetId(String id) {
		data.setTargetId(id);
		setChanged();
		notifyObservers();
	}
	
	public synchronized List<Session> getAllSessions() {
		return data.getAllSessions();
	}
	
	public synchronized boolean isGameInProgress() {
		return data.isGameInProgress();
	}
	
	public synchronized void setGameInProgress(boolean bool) {
		data.setGameInProgress(bool);
	}
	
	public Map<String, Character> getCharacters() {
		return data.getCharacters();
	}

	public void setCharacters(Map<String, Character> characters) {
		data.setCharacters(characters);
		setChanged();
		notifyObservers();
	}
	
	public Character getCharacter(Integer id) {
		return data.getCharacter(id);
	}

	public Character getMyCharacter() {
		return data.getMyCharacter();
	}

	public synchronized void setMyCharacter(Character myCharacter) {
		data.setMyCharacter(myCharacter);
		setChanged();
		notifyObservers();
	}
	
	public List<CharacterInfo> getCharactersList() {
		return data.getCharactersList();
	}
	
	public synchronized void setCharactersList(List<CharacterInfo> list) {
		data.setCharactersList(list);
		setChanged();
		notifyObservers();
	}
	
	public Resources getResources() {
		return data.getResources();
	}
	
	public synchronized void setResources(Resources res) {
		data.setResources(res);
		setChanged();
		notifyObservers();
	}
	
	public boolean isReady() {
		return data.isReady();
	}
	
	public synchronized void setReady(boolean b) {
		data.setReady(b);
		setChanged();
		notifyObservers();
	}
	
	public Connection getConnection() {
		return data.getConnection();
	}
	
	public synchronized void setConnection(Connection connection) {
		data.setConnection(connection);
	}
}
