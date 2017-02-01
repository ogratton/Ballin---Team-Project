package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ReceivedMessageBox extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private JTextArea receivedMessageBox;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Reciever.
 */
	
	public ReceivedMessageBox(ConnectionDataModel cModel) {
		super();
		
		this.cModel = cModel;
		
		receivedMessageBox = new JTextArea(5, 20);
		JScrollPane scrollPane = new JScrollPane(receivedMessageBox); 
		receivedMessageBox.setEditable(false);
		
		add(scrollPane);
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
		String message = cModel.getReceivedMessage();
		receivedMessageBox.setText(message);
		repaint();
	}

}
