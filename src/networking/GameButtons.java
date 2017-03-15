package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.esotericsoftware.kryonet.Client;

import resources.Resources;

public class GameButtons extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private Client client;
	private JButton ready;
	private JButton notReady;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Receiver.
 */
	
	public GameButtons(ConnectionDataModel cModel, Client client) {
		super();
		
		this.cModel = cModel;
		ready = new JButton("Ready");
		ready.addActionListener(e -> {
			if(cModel.getSession(cModel.getSessionId()).getAllClients().size() > 0) {
				if(!cModel.isGameInProgress()) {
					Message message = new Message(Command.GAME, Note.START, cModel.getMyId(), null, cModel.getSessionId(), null);
					try {
						client.sendTCP(message);
						cModel.setReady(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		notReady = new JButton("Not Ready");
		notReady.addActionListener(e -> {
			if(cModel.getSession(cModel.getSessionId()).getAllClients().size() > 1) {
				if(!cModel.isGameInProgress()) {
					Message message = new Message(Command.GAME, Note.STOP, cModel.getMyId(), null, cModel.getSessionId(), null);
					try {
						client.sendTCP(message);
						cModel.setReady(false);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		add(ready);
		add(notReady);
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
		repaint();
	}

}



