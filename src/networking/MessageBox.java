package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessageBox extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private JTextArea messageBox;
	private JButton sendMessage;
	private JComboBox dropDown;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Reciever.
 */
	
	public MessageBox(ConnectionDataModel cModel, ObjectOutputStream toServer) {
		super();
		
		this.cModel = cModel;
		
		messageBox = new JTextArea(5, 20);
		JScrollPane scrollPane = new JScrollPane(messageBox); 
		messageBox.setEditable(true);
		
		sendMessage.addActionListener(e -> {
			String messageString = messageBox.getText();
			Message message = new Message(Command.MESSAGE, messageString, cModel.getMyId(), cModel.getTargetId());
			try {
				toServer.writeObject(message);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		List<ClientInformation> clients = cModel.getSession(cModel.getSessionId()).getAllClients();
		String[] clientNames = new String[clients.size()];
		for(int i=0; i<clients.size(); i++) {
			ClientInformation client = clients.get(i);
			clientNames[i] = "ID: " + client.getId() + ", Name: " + client.getName();
		}
		
		dropDown = new JComboBox<String>(clientNames);
		dropDown.addActionListener(e -> {
			String selection = (String) dropDown.getSelectedItem();
			int id = getIdFromSelection(selection);
			cModel.setTargetId(id);
		});
		
		add(dropDown);
		add(scrollPane);
		add(sendMessage);
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
	
	private static int getIdFromSelection(String selection) {
		String[] split = selection.split(",");
		String[] split2 = split[0].split(":");
		return Integer.parseInt(split2[1]);
	}

}

