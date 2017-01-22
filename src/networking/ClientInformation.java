package networking;

public class ClientInformation {
	
	private final String name;
	private MessageQueue queue;
	
	public ClientInformation(String name) {
		this.name = name;
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
