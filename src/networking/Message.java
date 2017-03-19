package networking;

/**
 * This is the class which is passed across then network containing the information.
 * @author aaquibnaved
 *
 */
public class Message {
	
	private Command command;
	private Note note;
	private String message;
	private String senderId;
	private String receiverId;
	private String currentSessionId;
	private String targetSessionId;
	private Object object;
	
	/**
	 * Empty constructor
	 */
	public Message() {
		command = Command.NULL;
		note = Note.EMPTY;
		message = "";
		senderId = "";
		receiverId = "";
		object = new Empty();
		currentSessionId = "";
		targetSessionId = "";
	}
	
	/**
	 * Creates a message with an arbitrary object
	 * @param command The command
	 * @param note The type of command
	 * @param senderId The ID of the client who sent the message
	 * @param receiverId The ID of the client who the sender wanted to send the message to.
	 * @param currentSessionId The ID of session which the client is currently in.
	 * @param targetSessionId The ID of the session which the client wants to send a message to.
	 */
	public Message(Command command, Note note, String senderId, String receiverId, String currentSessionId, String targetSessionId) {
		this.command = command;
		this.note = note;
		this.message = "";
		this.senderId = senderId != null ? senderId.toString() : "";
		this.receiverId = receiverId != null ? receiverId.toString() : "";
		this.currentSessionId = currentSessionId != null ? currentSessionId.toString() : "";
		this.targetSessionId = targetSessionId != null ? targetSessionId.toString() : "";
		this.object = new Empty();
	}
	
	/**
	 * Creates a message with an arbitrary object
	 * @param command The command
	 * @param note The type of command
	 * @param senderId The ID of the client who sent the message
	 * @param receiverId The ID of the client who the sender wanted to send the message to.
	 * @param currentSessionId The ID of session which the client is currently in.
	 * @param targetSessionId The ID of the session which the client wants to send a message to.
	 * @param object The arbitrary object which can be attached to the message.
	 */
	public Message(Command command, Note note, String senderId, String receiverId, String currentSessionId, String targetSessionId, Object object) {
		this.command = command;
		this.note = note;
		this.message = "";
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.object = object;
		this.currentSessionId = currentSessionId;
		this.targetSessionId = targetSessionId;
	}
	
	/**
	 * Creates a message with an arbitrary object and an arbitrary message as a string
	 * @param command The command
	 * @param note The type of command
	 * @param senderId The ID of the client who sent the message
	 * @param receiverId The ID of the client who the sender wanted to send the message to.
	 * @param currentSessionId The ID of session which the client is currently in.
	 * @param targetSessionId The ID of the session which the client wants to send a message to.
	 * @param object The arbitrary object which can be attached to the message.
	 * @param message The arbitrary string which can be attached to the message.
	 */
	public Message(Command command, Note note, String senderId, String receiverId, String currentSessionId, String targetSessionId, Object object, String message) {
		this.command = command;
		this.note = note;
		this.message = message;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.object = object;
		this.currentSessionId = currentSessionId;
		this.targetSessionId = targetSessionId;
	}

	/**
	 * Get the arbitrary string message
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the arbitrary string message
	 * @param message The message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get the ID of the session which the client was in when it was sent
	 * @return The ID of the session.
	 */
	public String getCurrentSessionId() {
		return currentSessionId;
	}

	/**
	 * Set the Current Session ID
	 * @param currentSessionId The session ID
	 */
	public void setCurrentSessionId(String currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

	/**
	 * Get the ID of the session which the message was targeted at.
	 * @return
	 */
	public String getTargetSessionId() {
		return targetSessionId;
	}

	/**
	 * Set the target session ID
	 * @param targetSessionId The target Session ID
	 */
	public void setTargetSessionId(String targetSessionId) {
		this.targetSessionId = targetSessionId;
	}
	
	/**
	 * Get the Command
	 * @return The command
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * Set the command
	 * @param command The command
	 */
	public void setCommand(Command command) {
		this.command = command;
	}

	/**
	 * Get the type of message
	 * @return The type of the message
	 */
	public Note getNote() {
		return note;
	}

	/**
	 * Set the type of the message
	 * @param note The type of the message
	 */
	public void setNote(Note note) {
		this.note = note;
	}

	/**
	 * Get the ID of the client who sent the message
	 * @return The ID of the client
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * Store the ID of the client who sent the message.
	 * @param senderId
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId != null ? senderId.toString() : "";
	}

	/**
	 * Get the ID of the client who this message is targeted at.
	 * @return The ID of the sender client
	 */
	public String getReceiverId() {
		return receiverId;
	}

	/**
	 * Store the ID of the client who this message is targeted at.
	 * @param receiverId The ID of the receiver client
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId != null ? receiverId.toString() : "";
	}

	/**
	 * Get the arbitrary object which can be stored in this message.
	 * @return The object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * Store the arbitrary object
	 * @param object The object
	 */
	public void setObject(Object object) {
		this.object = object;
	}
}
