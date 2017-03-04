package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import resources.Resources;

public class Updater extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private ObjectOutputStream toServer;
	private Resources resources;
	private boolean oldUp, oldRight, oldLeft, oldDown, oldJump, oldPunch, oldBlock = false;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Receiver.
 */
	
	public Updater(ConnectionDataModel cModel, ObjectOutputStream toServer, Resources resources) {
		super();
		this.cModel = cModel;
		this.toServer = toServer;
		this.resources = resources;
	}

/**
 * Updates the button panel when changes are made to the Client Data Model
 * If the client is connected to another client (i.e: playing a game) the buttons in this panel are disabled.
 * This means that the client is forced to finish or quit the game before trying to press any other commands.
 * @param o
 * @param arg
 */
	@Override
	public void update(Observable o, Object arg) {
		List<resources.Character> characters = resources.getPlayerList();
		for(int i=0; i<characters.size(); i++) {
			if(characters.get(i).getId().equals(cModel.getMyId()) && hasControlsChanged(characters.get(i))) {
				CharacterInfo info = new CharacterInfo(cModel.getMyId(), characters.get(i).isUp(), characters.get(i).isRight(), characters.get(i).isLeft(), characters.get(i).isDown(), characters.get(i).isJump(), characters.get(i).isPunch(), characters.get(i).isBlock());
				GameData gameData = new GameData(info);
				Message message = new Message(Command.GAME, Note.UPDATE, cModel.getMyId(), null, cModel.getSessionId(), null, gameData);
				try {
					toServer.writeUnshared(message);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		repaint();
	}
	
	private boolean any(resources.Character c) {
		if(c.isUp() || c.isDown() || c.isLeft() || c.isRight() || c.isBlock() || c.isJump() || c.isPunch()) {
			return true;
		}
		return false;
	}
	
	private boolean hasControlsChanged(resources.Character c) {
		if(c.isUp() != oldUp || c.isDown() != oldDown || c.isRight() != oldRight || c.isLeft() != oldLeft || c.isBlock() != oldBlock || c.isJump() != oldJump || c.isPunch() != oldPunch) {
			oldUp = c.isUp();
			oldDown = c.isDown();
			oldRight = c.isRight();
			oldLeft = c.isLeft();
			oldJump = c.isJump();
			oldPunch = c.isPunch();
			oldBlock = c.isBlock();
			return true;
		}
		else {
			oldUp = c.isUp();
			oldDown = c.isDown();
			oldRight = c.isRight();
			oldLeft = c.isLeft();
			oldJump = c.isJump();
			oldPunch = c.isPunch();
			oldBlock = c.isBlock();
			return false;
		}
	}

}




