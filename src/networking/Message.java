package networking;

public class Message {
	
	private Command command;
	private Note note;
	private String message;
	private String senderId;
	private String receiverId;
	private String currentSessionId;
	private String targetSessionId;
	private Object object;
	
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCurrentSessionId() {
		return currentSessionId;
	}

	public void setCurrentSessionId(String currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

	public String getTargetSessionId() {
		return targetSessionId;
	}

	public void setTargetSessionId(String targetSessionId) {
		this.targetSessionId = targetSessionId;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId != null ? senderId.toString() : "";
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId != null ? receiverId.toString() : "";
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
