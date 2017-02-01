package networking;

public class ClientInformation {
	
	private final int id;
	private final String name;
	private MessageQueue queue;
	
	public ClientInformation(int id, String name) {
		this.id = id;
		this.name = name;
		this.queue = new MessageQueue();
	}
	
	public int getId() {
		return id;
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
}
