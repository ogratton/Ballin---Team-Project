package ui;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;

import networking.ClientInformation;
import networking.Command;
import networking.ConnectionDataModel;
import networking.Message;
import networking.Note;
import networking.Session;
import resources.Character;

@SuppressWarnings("serial")
public class InLobbyMenu extends JPanel implements Observer{
	
	private Session session;
	private Client client;
	private ConnectionDataModel cModel;
	private SessionListMenu sessionList;
	
	public InLobbyMenu(Session session, Client client, ConnectionDataModel cModel, SessionListMenu sessionList){
		this.session = session;
		this.client = client;
		this.cModel = cModel;
		this.sessionList = sessionList;
		
		cModel.addObserver(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		buttonPanel.add(leaveLobbyButton(client));
		add(buttonPanel, BorderLayout.PAGE_START);
		updateInLobbyPanel();
		add(UIRes.playersPanel, BorderLayout.CENTER);
	}
	
	JButton leaveLobbyButton(Client client) {
		JButton button = new JButton("Leave Lobby");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			
			Message leaveMessage = new Message(Command.SESSION, Note.LEAVE, cModel.getMyId(), "", cModel.getSessionId(),
					cModel.getHighlightedSessionId());
			try {
				cModel.getConnection().sendTCP(leaveMessage);
				cModel.setReady(false);
				//SessionListMenu lobbyList = new SessionListMenu(client, cModel);
				System.out.println("model changed: " + cModel.hasChanged());
				updateInLobbyPanel();
				UIRes.switchPanel(sessionList);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		return button;
	}
	
	void updateInLobbyPanel() {
		Session session;
		UIRes.playersPanel.removeAll();
		if(cModel.getSessionId() != null && (!cModel.getSessionId().equals(""))) {
			session = cModel.getSession(cModel.getSessionId());
			System.out.println("Clients in this session at " + new Timestamp(System.currentTimeMillis()) + " :" + session.getAllClients().size());
			for (int i = 0; i < session.getAllClients().size(); i++) {
				addPlayerToLobby(session.getAllClients().get(i), i + 1);
			}
		}
		else {
			session = getSession();
		}

	}
	
	void addPlayerToLobby(ClientInformation client, int index) {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension((int)(this.getWidth() * 0.95), (int)(this.getHeight() * 0.15)));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel playerLabel = new JLabel(client.getName());

		Choice characterClass = new Choice();
		for (Character.Class character : Character.Class.values()) {
			characterClass.add(character + "");
		}

		Color colour = UIRes.resources.getPlayerColor(index);
		panel.setBorder(BorderFactory.createLineBorder(colour, 15));

		JButton readyCheck = new JButton("Ready");
		UIRes.customiseButton(readyCheck, false);

		
		readyCheck.setForeground(Color.RED);
		readyCheck.addActionListener(e -> {
			if (readyCheck.getForeground() == Color.RED) {
				System.out.println("Got here");
				readyCheck.setForeground(Color.GREEN);
				client.setReady(true);
				if(cModel.getSession(cModel.getSessionId()).getAllClients().size() > 0) {
					if(!cModel.isGameInProgress()) {
						Message message = new Message(Command.GAME, Note.START, cModel.getMyId(), null, cModel.getSessionId(), null);
						try {
							cModel.getConnection().sendTCP(message);
							cModel.setReady(true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			} else {
				readyCheck.setForeground(Color.RED);
				client.setReady(false);
				if(cModel.getSession(cModel.getSessionId()).getAllClients().size() > 0) {
					if(!cModel.isGameInProgress()) {
						Message message = new Message(Command.GAME, Note.STOP, cModel.getMyId(), null, cModel.getSessionId(), null);
						try {
							cModel.getConnection().sendTCP(message);
							cModel.setReady(false);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			System.out.println(client.isReady());
		});

		panel.add(Box.createHorizontalGlue());
		panel.add(playerLabel);
		panel.add(Box.createHorizontalGlue());
		panel.add(characterClass);
		panel.add(Box.createHorizontalGlue());
		panel.add(readyCheck);
		panel.add(Box.createHorizontalGlue());
		UIRes.playersPanel.add(panel);
		UIRes.playersPanel.add(Box.createVerticalStrut(20));

	}
	
	void setSession(Session session){
		this.session = session;
	}
	
	Session getSession(){
		return this.session;
	}
	
	void setModel(ConnectionDataModel cModel){
		this.cModel = cModel;
	}
	
	ConnectionDataModel getModel(){
		return this.cModel;
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(UIRes.username + " lobby update reached");
		updateInLobbyPanel();
		repaint();
		validate();
	}

}
