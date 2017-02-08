package networking;

import java.io.Serializable;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5302365975590223550L;
	private Command command;
	private String message;
	private int senderId;
	private int receiverId;
	private Object object;
	
	public Message() {
		command = Command.NULL;
		message = "";
		senderId = -1;
		receiverId = -1;
		object = new Empty();
	}
	
	public Message(Command command, String message, int senderId, int receiverId) {
		this.command = command;
		this.message = message;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.object = new Empty();		
	}
	
	public Message(Command command, String message, int senderId, int receiverId, Object object) {
		this.command = command;
		this.message = message;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.object = object;		
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
