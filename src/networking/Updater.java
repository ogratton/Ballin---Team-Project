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

import com.esotericsoftware.kryonet.Client;

import resources.Resources;

/**
 * This class observes the Character objects on the Client and sends the changes in the controls
 * to the Server to update the physics. It only sends an update when the controls have changed.
 * @author axn598
 *
 */
public class Updater extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private Client client;
	private Resources resources;
	private boolean oldUp, oldRight, oldLeft, oldDown, oldDashing, oldBlocking = false;
	
	/**
	 * Constructs the updater.
	 * @param cModel The ConnectionDataModel
	 * @param client The Kryonet Client object
	 * @param resources Resources object
	 */
	public Updater(ConnectionDataModel cModel, Client client, Resources resources) {
		super();
		this.cModel = cModel;
		this.client = client;
		this.resources = resources;
	}


	/**
	 * Updates when the character it is observing controls change. It sends the changes in the controls to
	 * the server.
	 */
	@Override
	public void update(Observable o, Object arg) {
		List<resources.Character> characters = resources.getPlayerList();
		for(int i=0; i<characters.size(); i++) {
			if(characters.get(i).getId().equals(cModel.getMyId()) && hasControlsChanged(characters.get(i))) {		
				CharacterInfo info = new CharacterInfo(cModel.getMyId(), characters.get(i).isUp(), characters.get(i).isRight(), characters.get(i).isLeft(), characters.get(i).isDown(), characters.get(i).isDashing(), characters.get(i).isBlocking());
				
				// Send a message to update the server.
				GameData gameData = new GameData(info);
				Message message = new Message(Command.GAME, Note.UPDATE, cModel.getMyId(), null, cModel.getSessionId(), null, gameData);
				try {
					client.sendUDP(message);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		repaint();
	}
	
	/**
	 * Check if the controls pressed have changed since the last update.
	 * @param c The character
	 * @return true if the controls pressed have changed, false otherwise.
	 */
	private boolean hasControlsChanged(resources.Character c) {
		if(c.isUp() != oldUp || c.isDown() != oldDown || c.isRight() != oldRight || c.isLeft() != oldLeft || c.isBlocking() != oldBlocking || c.isDashing() != oldDashing) {
			oldUp = c.isUp();
			oldDown = c.isDown();
			oldRight = c.isRight();
			oldLeft = c.isLeft();
			oldDashing = c.isDashing();
			oldBlocking = c.isBlocking();
			return true;
		}
		else {
			oldUp = c.isUp();
			oldDown = c.isDown();
			oldRight = c.isRight();
			oldLeft = c.isLeft();
			oldDashing = c.isDashing();
			oldBlocking = c.isBlocking();
			return false;
		}
	}

}




