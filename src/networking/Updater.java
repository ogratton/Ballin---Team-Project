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
			if(characters.get(i).getId() == cModel.getMyId()) {
				CharacterInfo info = new CharacterInfo(characters.get(i).getId(), characters.get(i).getX(), characters.get(i).getY());
				List<CharacterInfo> charactersList = new ArrayList<CharacterInfo>();
				charactersList.add(info);
				GameData gameData = new GameData(charactersList);
				Message message = new Message(Command.GAME, Note.UPDATE, cModel.getMyId(), -1, cModel.getSessionId(), -1, gameData);
				try {
					toServer.writeUnshared(message);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		repaint();
	}

}




