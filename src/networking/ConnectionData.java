package networking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectionData {

	private int sessionId;
	private ConcurrentMap<Integer, Session> sessions = new ConcurrentHashMap<Integer, Session>();
	private ClientInformation myClient;
	private Display display;
	private int highlightedSessionId;
	private int highlightedClientId;
	private String receivedMessage;
	private int targetId;
	
	public ClientInformation getClientInformation() {
		return myClient;
	}
	
	public void setClientInformatin(ClientInformation client) {
		myClient = client;
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
		Iterator<Entry<Integer, Session>> it = sessions.entrySet().iterator();
		ArrayList<Session> sessionList = new ArrayList<Session>();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        sessionList.add((Session) pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
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
}
