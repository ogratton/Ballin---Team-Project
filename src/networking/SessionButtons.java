package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SessionButtons extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private JButton joinSession;
	private JButton leaveSession;
	private JButton createSession;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Reciever.
 */
	
	public SessionButtons(ConnectionDataModel cModel, ObjectOutputStream toServer) {
		super();
		
		this.cModel = cModel;
		
		joinSession = new JButton("Join Session");
		joinSession.addActionListener(e -> {
			Message joinMessage = new Message(Command.SESSION, "joinSession", cModel.getMyId(), cModel.getHighlightedSessionId());
			try {
				toServer.writeObject(joinMessage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		createSession = new JButton("Create Session");
		createSession.addActionListener(e -> {
			Message createMessage = new Message(Command.SESSION, "createSession", cModel.getMyId(), cModel.getMyId());
			try {
				toServer.writeObject(createMessage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

//	    Message message = new Message(Command.SESSION, "getSessions", cModel.getMyId(), -1);
//	    try {
//			toServer.writeObject(message);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
		leaveSession = new JButton("Leave Session");
		leaveSession.addActionListener(e -> {
			Message leaveMessage = new Message(Command.SESSION, "leaveSession", cModel.getMyId(), cModel.getHighlightedSessionId());
			try {
				toServer.writeObject(leaveMessage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		add(joinSession);
		add(leaveSession);
		add(createSession);
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

