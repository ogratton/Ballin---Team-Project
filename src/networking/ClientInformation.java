package networking;

import java.util.UUID;

public class ClientInformation {
	
	private String id;
	private String name;
	private MessageQueue queue;
	private String sessionId;
	private boolean ready;
	
	public ClientInformation() {
		
	}
	
	public ClientInformation(String name) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.queue = new MessageQueue();
		this.ready = false;
	}
	
	public ClientInformation(String id, String name) {
		this.id = id;
		this.name = name;
		this.queue = new MessageQueue();
		this.ready = false;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	* Gets the blocking queue of messages which this client has.
	* @return the blocking queue of messages
	*/
	public MessageQueue getQueue() {
		return queue;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean b) {
		this.ready = b;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
