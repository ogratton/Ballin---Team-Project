package networking;

public class Message {
	
	private Command command;
	private String message;
	private int myId;
	private int targetId;
	
	public Message(Command command, String message, int myId, int targetId) {
		this.command = command;
		this.message = message;
		this.myId = myId;
		this.targetId = targetId;
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
}
