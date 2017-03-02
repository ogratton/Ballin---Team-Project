package networking;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ListView extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectionDataModel  cModel;
	private JList<String> sessionList;
	private JList<String> clientList;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Receiver.
 */
	
	public ListView(ConnectionDataModel cModel) {
		super();
		
		this.cModel = cModel;
		
		List<Session> sessions = cModel.getAllSessions();
		
		sessionList = new JList<String>();
		sessionList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		//sessionList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		sessionList.setVisibleRowCount(-1);
		DefaultListModel<String> sessionModel = new DefaultListModel<String>();
		for(int i=0; i<sessions.size(); i++) {
			sessionModel.addElement("ID: " + sessions.get(i).getId());
		}
		
		sessionList.setModel(sessionModel);
		
		JScrollPane listScroller = new JScrollPane(sessionList);
		listScroller.setPreferredSize(new Dimension(150, 80));
		sessionList.addListSelectionListener(e -> {
			if(sessionList.getSelectedValue() != null) {
				UUID sessionId = parseId((String)sessionList.getSelectedValue());
				System.out.println(sessionId);
				System.out.println(cModel.getSession(sessionId).getAllClients().size());
				cModel.setHighlightedSessionId(sessionId);
			}
		});
		
		clientList = new JList<String>();
		DefaultListModel<String> clientModel = new DefaultListModel<String>();
		clientList.setModel(clientModel);
		clientList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		//clientList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		clientList.setVisibleRowCount(-1);
		JScrollPane clientListScroller = new JScrollPane(clientList);
		clientListScroller.setPreferredSize(new Dimension(250, 80));
		clientList.addListSelectionListener(e->{
			if(clientList.getSelectedValue() != null) {
				UUID clientId = parseId((String)clientList.getSelectedValue());
				cModel.setHighlightedClientId(clientId);
				System.out.println("Client ID: " + clientId);
			}
		});
		
		add(listScroller);
		add(clientListScroller);
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
		System.out.println("Lists Updated");
		List<Session> sessions = cModel.getAllSessions();
		DefaultListModel<String> sessionModel = (DefaultListModel<String>) sessionList.getModel();
		sessionModel.clear();
		for(int i=0; i<sessions.size(); i++) {
			sessionModel.addElement("ID: " + sessions.get(i).getId());
		}
		
		List<ClientInformation> clients;
		if (cModel.getHighlightedSessionId() != null) {
			UUID sessionId = cModel.getHighlightedSessionId();
			Session session = cModel.getSession(sessionId);
			clients = session.getAllClients();
		}
		else {
			clients = new ArrayList<ClientInformation>();
		}
		
		DefaultListModel<String> clientModel = (DefaultListModel<String>) clientList.getModel();
		clientModel.clear();
		for(int i=0; i<clients.size(); i++) {
			clientModel.addElement("ID: " + clients.get(i).getId() + ": Name: " + clients.get(i).getName());
		}
		
		repaint();
	}
	
	private static UUID parseId(String s) {
		String[] split = s.split(":");
		UUID id = UUID.fromString(split[1].substring(1));
		return id;
	}

}
