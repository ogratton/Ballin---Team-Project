package networking;

import java.io.Serializable;

public class ClientInformation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3087945278365659260L;
	private int id;
	private final String name;
	private MessageQueue queue;
	private Session session;
	
	public ClientInformation(int id, String name) {
		this.id = id;
		this.name = name;
		this.queue = new MessageQueue();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
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
}
