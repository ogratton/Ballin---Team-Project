package networking;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5302365975590223550L;
	private Command command;
	private Note note;
	private String message;
	private UUID senderId;
	private UUID receiverId;
	private UUID currentSessionId;
	private UUID targetSessionId;
	private Object object;
	
	public Message() {
		command = Command.NULL;
		note = Note.EMPTY;
		message = "";
		senderId = null;
		receiverId = null;
		object = new Empty();
		currentSessionId = null;
		targetSessionId = null;
	}
	
	public Message(Command command, Note note, UUID senderId, UUID receiverId, UUID currentSessionId, UUID targetSessionId) {
		this.command = command;
		this.note = note;
		this.message = "";
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.currentSessionId = currentSessionId;
		this.targetSessionId = targetSessionId;
		this.object = new Empty();
	}
	
	public Message(Command command, Note note, UUID senderId, UUID receiverId, UUID currentSessionId, UUID targetSessionId, Object object) {
		this.command = command;
		this.note = note;
		this.message = "";
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.object = object;
		this.currentSessionId = currentSessionId;
		this.targetSessionId = targetSessionId;
	}
	
	public Message(Command command, Note note, UUID senderId, UUID receiverId, UUID currentSessionId, UUID targetSessionId, Object object, String message) {
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

	public UUID getCurrentSessionId() {
		return currentSessionId;
	}

	public void setCurrentSessionId(UUID currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

	public UUID getTargetSessionId() {
		return targetSessionId;
	}

	public void setTargetSessionId(UUID targetSessionId) {
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

	public UUID getSenderId() {
		return senderId;
	}

	public void setSenderId(UUID senderId) {
		this.senderId = senderId;
	}

	public UUID getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(UUID receiverId) {
		this.receiverId = receiverId;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
