package ui;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
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
import networking.Message;
import networking.Note;
import networking.Session;
import resources.Character;

@SuppressWarnings("serial")
public class InLobbyMenu extends JPanel implements Observer {
	
	private Session session;
	
	public InLobbyMenu(Session session, Client client){
		this.session = session;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		buttonPanel.add(leaveLobbyButton(client));
		add(buttonPanel);
		updateInLobbyPanel();
	}
	
	JButton leaveLobbyButton(Client client) {
		JButton button = new JButton("Leave Lobby");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			Message leaveMessage = new Message(Command.SESSION, Note.LEAVE, UIRes.cModel.getMyId(), "", UIRes.cModel.getSessionId(),
					UIRes.cModel.getHighlightedSessionId());
			try {
				client.sendTCP(leaveMessage);
				UIRes.cModel.setReady(false);
				SessionListMenu lobbyList = new SessionListMenu(client);
				UIRes.switchPanel(lobbyList);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		return button;
	}
	
	void updateInLobbyPanel() {
		for (int i = 1; i < this.getComponentCount(); i++) {
			this.remove(i);
		}
		for (int i = 0; i < session.getAllClients().size(); i++) {
			addPlayerToLobby(this, session.getAllClients().get(i), i + 1);
		}
		System.out.println("Clients in this session:" + session.getAllClients().size());
		repaint();
	}
	
	JPanel addPlayerToLobby(JPanel panel, ClientInformation client, int index) {
		JPanel playerPanel = new JPanel();
		playerPanel.setPreferredSize(new Dimension(panel.getWidth(), playerPanel.getHeight()));
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
		JLabel playerLabel = new JLabel(client.getName());

		Choice characterClass = new Choice();
		for (Character.Class character : Character.Class.values()) {
			characterClass.add(character + "");
		}

		Color colour = UIRes.resources.getPlayerColor(index);
		playerPanel.setBorder(BorderFactory.createLineBorder(colour, 15));

		JButton readyCheck = new JButton("Ready");
		UIRes.customiseButton(readyCheck, false);

		readyCheck.setForeground(Color.RED);
		readyCheck.addActionListener(e -> {
			if (readyCheck.getForeground() == Color.RED) {
				readyCheck.setForeground(Color.GREEN);
				client.setReady(true);
			} else {
				readyCheck.setForeground(Color.RED);
				client.setReady(false);
			}
			System.out.println(client.isReady());
		});

		playerPanel.add(Box.createHorizontalGlue());
		playerPanel.add(playerLabel);
		playerPanel.add(Box.createHorizontalGlue());
		playerPanel.add(characterClass);
		playerPanel.add(Box.createHorizontalGlue());
		playerPanel.add(readyCheck);
		playerPanel.add(Box.createHorizontalGlue());
		panel.add(playerPanel);
		panel.add(Box.createVerticalStrut(20));
		return panel;
	}

	@Override
	public void update(Observable o, Object arg) {
		updateInLobbyPanel();
		repaint();
	}

}
