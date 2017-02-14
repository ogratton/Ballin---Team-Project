package networking;

import java.io.Serializable;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5302365975590223550L;
	private Command command;
	private Note note;
	private String message;
	private int senderId;
	private int receiverId;
	private int currentSessionId;
	private int targetSessionId;
	private Object object;
	
	public Message() {
		command = Command.NULL;
		note = Note.EMPTY;
		message = "";
		senderId = -1;
		receiverId = -1;
		object = new Empty();
		currentSessionId = -1;
		targetSessionId = -1;
	}
	
	public Message(Command command, Note note, int senderId, int receiverId, int currentSessionId, int targetSessionId) {
		this.command = command;
		this.note = note;
		this.message = "";
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.currentSessionId = currentSessionId;
		this.targetSessionId = targetSessionId;
		this.object = new Empty();
	}
	
	public Message(Command command, Note note, int senderId, int receiverId, int currentSessionId, int targetSessionId, Object object) {
		this.command = command;
		this.note = note;
		this.message = "";
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.object = object;
		this.currentSessionId = currentSessionId;
		this.targetSessionId = targetSessionId;
	}
	
	public Message(Command command, Note note, int senderId, int receiverId, int currentSessionId, int targetSessionId, Object object, String message) {
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

	public int getCurrentSessionId() {
		return currentSessionId;
	}

	public void setCurrentSessionId(int currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

	public int getTargetSessionId() {
		return targetSessionId;
	}

	public void setTargetSessionId(int targetSessionId) {
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

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
