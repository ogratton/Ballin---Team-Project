package networking;

import java.util.List;
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
	}
	
	public ClientInformation getClientInformation() {
		return data.getClientInformation();
	}
	
	public synchronized void setClientInformation(ClientInformation client) {
		data.setClientInformatin(client);
	}
	
	public ConcurrentMap<Integer, Session> getSessionsTable() {
		return data.getSessionsTable();
	}
	
	public void setSessionsTable(ConcurrentMap<Integer, Session> sessions) {
		data.setSessionsTable(sessions);
	}
	
	public int getSessionId() {
		return this.data.getSessionId();
	}
	
	public synchronized void setSessionId(int id) {
		this.data.setSessionId(id);
	}
	
	public ConnectionData getConnectionData() {
		return this.data;
	}
	
	public synchronized void addClient(int sessionId, int clientId, ClientInformation client) {
		this.data.addClient(sessionId, clientId, client);;
	}
	
	public synchronized void removeClient(int sessionId, int clientId) {
		this.data.removeClient(sessionId, clientId);
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
	}
	
	public synchronized int getHighlightedClientId() {
		return this.data.getHighlightedClientId();
	}
	
	public synchronized void setHighlightedClientId(int id) {
		this.data.setHighlightedClientId(id);
	}
	
	public synchronized String getReceivedMessage() {
		return data.getReceivedMessage();
	}
	
	public synchronized void setReceivedMessage(String message) {
		data.setReceivedMessage(message);
	}
	
	public synchronized int getTargetId() {
		return data.getTargetId();
	}
	
	public synchronized void setTargetId(int id) {
		data.setTargetId(id);
	}
	
	public synchronized List<Session> getAllSessions() {
		return data.getAllSessions();
	}
}
