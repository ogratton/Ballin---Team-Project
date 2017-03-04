package networking;

import java.io.Serializable;
import java.util.UUID;

public class ClientInformation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3087945278365659260L;
	private UUID id;
	private final String name;
	private MessageQueue queue;
	private Session session;
	private boolean ready;
	
	public ClientInformation(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.queue = new MessageQueue();
		this.ready = false;
	}
	
	public ClientInformation(UUID id, String name) {
		this.id = id;
		this.name = name;
		this.queue = new MessageQueue();
		this.ready = false;
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
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
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean b) {
		this.ready = b;
	}
}
