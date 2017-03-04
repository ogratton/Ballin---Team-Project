package networking;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SessionView extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private JTextField text;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Reciever.
 */
	
	public SessionView(ConnectionDataModel cModel) {
		super();
		
		this.cModel = cModel;
		UUID id = cModel.getSessionId();
		if(id != null) {
			text = new JTextField(cModel.getSessionId().toString());
		}
		else {
			text = new JTextField("");
		}
		add(text);
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
		text.setText("Session ID: " + (cModel.getSessionId()));
		repaint();
	}

}


