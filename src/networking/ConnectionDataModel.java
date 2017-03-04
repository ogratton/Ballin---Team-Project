package networking;

import java.util.List;
import resources.Character;
import resources.Resources;

import java.util.Map;
import java.util.Observable;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class ConnectionDataModel extends Observable {

	private ConnectionData data;
	
	public ConnectionDataModel(ConnectionData data) {
		this.data = data;
	}
	
	public UUID getMyId() {
		return data.getClientInformation().getId();
	}
	
	public void setMyId(UUID id) {
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
	
	public ConcurrentMap<UUID, Session> getSessionsTable() {
		return data.getSessionsTable();
	}
	
	public void setSessionsTable(ConcurrentMap<UUID, Session> sessions) {
		data.setSessionsTable(sessions);
		setChanged();
		notifyObservers();
	}
	
	public UUID getSessionId() {
		return this.data.getSessionId();
	}
	
	public synchronized void setSessionId(UUID id) {
		this.data.setSessionId(id);
		setChanged();
		notifyObservers();
	}
	
	public ConnectionData getConnectionData() {
		return this.data;
	}
	
	public synchronized void addClient(UUID sessionId, UUID clientId, ClientInformation client) {
		this.data.addClient(sessionId, clientId, client);
		setChanged();
		notifyObservers();
	}
	
	public synchronized void removeClient(UUID sessionId, UUID clientId) {
		this.data.removeClient(sessionId, clientId);
		setChanged();
		notifyObservers();
	}
	
	public synchronized Session getSession(UUID sessionId) {
		return data.getSession(sessionId);
	}
	
	public synchronized ClientInformation getClient(UUID sessionId, UUID clientId) {
		return this.data.getClient(sessionId, clientId);
	}
	
	public synchronized UUID getHighlightedSessionId() {
		return this.data.getHighlightedSessionId();
	}
	
	public synchronized void setHighlightedSessionId(UUID id) {
		this.data.setHiglightedSessionId(id);
		setChanged();
		notifyObservers();
	}
	
	public synchronized UUID getHighlightedClientId() {
		return this.data.getHighlightedClientId();
	}
	
	public synchronized void setHighlightedClientId(UUID id) {
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
	
	public synchronized UUID getTargetId() {
		return data.getTargetId();
	}
	
	public synchronized void setTargetId(UUID id) {
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
	
	public Map<UUID, Character> getCharacters() {
		return data.getCharacters();
	}

	public void setCharacters(Map<UUID, Character> characters) {
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
	}
	
	public boolean isReady() {
		return data.isReady();
	}
	
	public synchronized void setReady(boolean b) {
		data.setReady(b);
		setChanged();
		notifyObservers();
	}
}
