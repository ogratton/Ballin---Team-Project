package networking;

public class Message {
	
	private Command command;
	private String message;
	private int myId;
	private int targetId;
	private Object object;
	
	public Message() {
		command = Command.NULL;
		message = "";
		myId = -1;
		targetId = -1;
		object = new Empty();
	}
	
	public Message(Command command, String message, int myId, int targetId) {
		this.command = command;
		this.message = message;
		this.myId = myId;
		this.targetId = targetId;
		this.object = new Empty();		
	}
	
	public Message(Command command, String message, int myId, int targetId, Object object) {
		this.command = command;
		this.message = message;
		this.myId = myId;
		this.targetId = targetId;
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

	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
