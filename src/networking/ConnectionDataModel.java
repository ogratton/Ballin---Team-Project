package networking;

import java.util.List;
import resources.Character;
import resources.Resources;

import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentMap;

public class ConnectionDataModel extends Observable {

	private ConnectionData data;
	
	public ConnectionDataModel(ConnectionData data) {
		this.data = data;
	}
	
	public int getMyId() {
		return data.getClientInformation().getId();
	}
	
	public void setMyId(int id) {
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
	
	public ConcurrentMap<Integer, Session> getSessionsTable() {
		return data.getSessionsTable();
	}
	
	public void setSessionsTable(ConcurrentMap<Integer, Session> sessions) {
		data.setSessionsTable(sessions);
		setChanged();
		notifyObservers();
	}
	
	public int getSessionId() {
		return this.data.getSessionId();
	}
	
	public synchronized void setSessionId(int id) {
		this.data.setSessionId(id);
		setChanged();
		notifyObservers();
	}
	
	public ConnectionData getConnectionData() {
		return this.data;
	}
	
	public synchronized void addClient(int sessionId, int clientId, ClientInformation client) {
		this.data.addClient(sessionId, clientId, client);
		setChanged();
		notifyObservers();
	}
	
	public synchronized void removeClient(int sessionId, int clientId) {
		this.data.removeClient(sessionId, clientId);
		setChanged();
		notifyObservers();
	}
	
	public synchronized Session getSession(int sessionId) {
		return data.getSession(sessionId);
	}
	
	public synchronized ClientInformation getClient(int sessionId, int clientId) {
		return this.data.getClient(sessionId, clientId);
	}
	
	public synchronized int getHighlightedSessionId() {
		return this.data.getHighlightedSessionId();
	}
	
	public synchronized void setHighlightedSessionId(int id) {
		this.data.setHiglightedSessionId(id);
		setChanged();
		notifyObservers();
	}
	
	public synchronized int getHighlightedClientId() {
		return this.data.getHighlightedClientId();
	}
	
	public synchronized void setHighlightedClientId(int id) {
		this.data.setHighlightedClientId(id);
		setChanged();
		notifyObservers();
	}
	
	public synchronized String getReceivedMessage() {
		return data.getReceivedMessage();
	}
	
	public synchronized void setReceivedMessage(String message) {
		data.setReceivedMessage(message);
		setChanged();
		notifyObservers();
	}
	
	public synchronized int getTargetId() {
		return data.getTargetId();
	}
	
	public synchronized void setTargetId(int id) {
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
	
	public Map<Integer, Character> getCharacters() {
		return data.getCharacters();
	}

	public void setCharacters(Map<Integer, Character> characters) {
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

	public void setMyCharacter(Character myCharacter) {
		data.setMyCharacter(myCharacter);
		setChanged();
		notifyObservers();
	}
	
	public List<CharacterInfo> getCharactersList() {
		return data.getCharactersList();
	}
	
	public void setCharactersList(List<CharacterInfo> list) {
		data.setCharactersList(list);
	}
	
	public Resources getResources() {
		return data.getResources();
	}
	
	public synchronized void setResources(Resources res) {
		data.setResources(res);
	}
}
