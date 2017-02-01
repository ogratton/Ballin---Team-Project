package networking;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class SessionListView extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private JList sessionList;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Reciever.
 */
	
	public SessionListView(ConnectionDataModel cModel) {
		super();
		
		this.cModel = cModel;
		
		List<Session> sessions = cModel.getAllSessions();
		String[] sessionIds = new String[sessions.size()];
		for(int i=0; i<sessions.size(); i++) {
			sessionIds[i] = "ID: " + sessions.get(i).getId();
		}
		
		sessionList = new JList<String>(sessionIds);
		sessionList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		sessionList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		sessionList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(sessionList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		add(sessionList);
		add(leaveSession);
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
