
public class GameSharing {
	private NoughtsCrossesModel model;
	private boolean connected;
	private boolean sendMove;
	
	/**
	 * Creates a GameSharing object. This object will contains the model of the Noughts And Crosses game which will..
	 * ..be initialized when the client plays with another client.
	 * The model is set to null at the start as a game has not been started.
	 */
	public GameSharing() {
		this.model = null;
		this.connected = false;
		this.sendMove = false;
	}
	
	/**
	 * Gets the model which is stored in this object
	 * @return The Noughts And Crosses Model
	 */
	public NoughtsCrossesModel getModel() {
		return this.model;
	}
	
	/**
	 * Stores the Noughts And Crosses Model
	 * @param model The game model which is going to be stored
	 */
	public synchronized void setModel(NoughtsCrossesModel model) {
		this.model = model;
	}
	
	/**
	 * Checks if it is the turn of the client to make a move on the game board of the client.
	 * @return true if it is the turn of the client, false otherwise.
	 */
	public boolean isSendMove() {
		return sendMove;
	}
	
	/**
	 * Changes the value of sendMove when the turn has changed.
	 * @param bool The boolean value which sendMove is being set to.
	 */
	public void setSendMove(boolean bool) {
		this.sendMove = bool;
	}
	
	/**
	 * Checks if the client is connected during the game.
	 * @return true if the client is connected, false otherwise.
	 */
	public boolean getConnected() {
		return this.connected;
	}
	
	/**
	 * Changes if the client is connected or not
	 * @param bool The boolean value which setConnected is changed to.
	 */
	public synchronized void setConnected(boolean bool) {
		this.connected = bool;
	}
	
	/**
	 * Prints the object in a certain format.
	 */
	public String toString() {
		String text = "";
		for (int i = 0; i<3; i++) {
			for (int j = 0; j<3; j++) {
				text += " " + model.get(i, j);
			}
		}
		return text;
	}
}
