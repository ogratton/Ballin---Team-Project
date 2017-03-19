package ui;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.esotericsoftware.kryonet.Client;

import networking.ClientInformation;
import networking.Command;
import networking.Message;
import networking.Note;
import networking.Session;
import resources.Map;
import resources.Resources;
import resources.Resources.Mode;

@SuppressWarnings("serial")
public class SessionListMenu extends JPanel implements Observer{
	
	private String lobbyName;
	private String gameModeName;
	private String mapName;
	private Session session;
	private InLobbyMenu lobbyPanel;
	
	public SessionListMenu(Client client){
		updateSessionsPanel(client);
		setLayout(new BorderLayout());
		add(addSessionButtons(client, this), BorderLayout.PAGE_START);
		add(UIRes.sessionsPanels, BorderLayout.CENTER);

	}
	
	JButton createSessionButton(Client client) {
		JButton button = new JButton("Create Lobby");
		button.addActionListener(e -> {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Object[] inputs = createLobbyWizard();
			int optionPane = JOptionPane.showConfirmDialog(frame, inputs, "Create new lobby",
					JOptionPane.OK_CANCEL_OPTION);

			if (optionPane == JOptionPane.OK_OPTION) {
				lobbyName = ((JTextField) inputs[1]).getText();

				gameModeName = ((Choice) inputs[3]).getSelectedItem();

				mapName = ((Choice) inputs[5]).getSelectedItem();

				Map.World mapTiles = null;

				for (Map.World map : Map.World.values()) {
					if (map.toString().compareTo(mapName) == 0)
						mapTiles = map;
				}

				Mode gameMode = null;
				for (Resources.Mode mode : Resources.Mode.values()) {
					if (mode.toString().compareTo(gameModeName) == 0)
						gameMode = mode;
				}

				Session newSession = new Session(lobbyName, new ClientInformation(UIRes.username), mapName, mapTiles,
						gameMode, UIRes.username, 0);
				
				this.session = newSession;

				Message createMessage = new Message(Command.SESSION, Note.CREATE, UIRes.cModel.getMyId(), "", "", "",
						newSession);
				try {
					client.sendTCP(createMessage);
					System.out.println("Session creation sent.");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				InLobbyMenu lobby = new InLobbyMenu(newSession, client);

				updateSessionsPanel(client);
				UIRes.switchPanel(lobby);
			}

			else
				frame.dispose();

		});
		UIRes.customiseButton(button, true);
		return button;
	}
	
	JButton joinSessionButton(Client client) {
		JButton button = new JButton("Join");
		button.addActionListener(e -> {
			int index = -1;
			for (int i = 0; i < UIRes.sessionPanelsList.size(); i++) {
				if (UIRes.sessionPanelsList.get(i).isFocusOwner())
					index = i;
			}

			Message joinMessage = new Message(Command.SESSION, Note.JOIN, UIRes.cModel.getMyId(), "",
					UIRes.cModel.getAllSessions().get(index).getId(), UIRes.cModel.getAllSessions().get(index).getId());
			
			
					
			try {
				client.sendTCP(joinMessage);
				session = UIRes.cModel.getAllSessions().get(index);
				InLobbyMenu lobby = new InLobbyMenu(session, client);
				UIRes.switchPanel(lobby);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		UIRes.customiseButton(button, true);
		return button;
	}

	JButton refreshSessionList(Client client) {
		JButton button = new JButton("Refresh");
		button.addActionListener(e -> {
			Message message = new Message(Command.SESSION, Note.INDEX, UIRes.cModel.getMyId(), null, UIRes.cModel.getSessionId(),
					null);
			try {
				client.sendTCP(message);
				updateSessionsPanel(client);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		UIRes.customiseButton(button, true);
		return button;
	}
	
	JPanel getSessionPanel(Session session) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel sessionName = UIRes.getLabel(session.getSessionName(), 20);
		JLabel hostName = UIRes.getLabel(session.getHostName(), 20);
		JLabel mapName = UIRes.getLabel(session.getMapName(), 20);
		JLabel gameModeName = UIRes.getLabel("" + session.getGameMode(), 20);
		JLabel numberPlayers = UIRes.getLabel(session.getAllClients().size() + "/8", 20);
		panel.add(Box.createHorizontalGlue());
		panel.add(sessionName);
		panel.add(Box.createHorizontalGlue());
		panel.add(hostName);
		panel.add(Box.createHorizontalGlue());
		panel.add(mapName);
		panel.add(Box.createHorizontalGlue());
		panel.add(gameModeName);
		panel.add(Box.createHorizontalGlue());
		panel.add(numberPlayers);
		panel.add(Box.createHorizontalGlue());
		panel.setFocusable(true);
		Color background = panel.getBackground();

		panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				for (int i = 0; i < UIRes.sessionPanelsList.size(); i++) {
					if (UIRes.sessionPanelsList.get(i).isFocusOwner())
						UIRes.sessionPanelsList.get(i).setBackground(background);
				}
				panel.requestFocus();
				panel.setBackground(Color.RED);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});
		panel.requestFocus();
		return panel;
	}
	
	void updateSessionsPanel(Client client) {
		UIRes.sessionsPanels.removeAll();
		UIRes.sessionPanelsList.removeAll(UIRes.sessionPanelsList);
		UIRes.sessionsPanels.setLayout(new BoxLayout(UIRes.sessionsPanels, BoxLayout.Y_AXIS));
		for (int i = 0; i < UIRes.cModel.getAllSessions().size(); i++) {
			JPanel session = getSessionPanel(UIRes.cModel.getAllSessions().get(i));
			if (!UIRes.cModel.getAllSessions().get(i).getAllClients().isEmpty()) {
				UIRes.sessionPanelsList.add(session);
				UIRes.sessionsPanels.add(session);
			}
		}

		UIRes.sessionsPanels.repaint();
		UIRes.sessionsPanels.revalidate();
	}
	
	JPanel addSessionButtons(Client client, JPanel sessionPanel) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JButton createSession = createSessionButton(client);
		JButton joinSession = joinSessionButton(client);
		JButton refreshSession = refreshSessionList(client);
		JButton backToMainMenu = getBackToStartMenuButton();
		panel.add(createSession);
		panel.add(joinSession);
		panel.add(refreshSession);
		panel.add(backToMainMenu);
		return panel;
	}
	
	Object[] createLobbyWizard() {
		JLabel lobbyNameLabel = new JLabel("Lobby name: ");

		JLabel gameModeLabel = new JLabel("Game mode: ");
		Choice gameModeChoice = new Choice();
		for (Resources.Mode gameMode : Resources.Mode.values()) {
			gameModeChoice.add(gameMode.toString());
		}

		JLabel mapLabel = new JLabel("Map: ");
		Choice mapChoice = new Choice();
		for (Map.World map : Map.World.values()) {
			mapChoice.add(map.toString());
		}

		UIRes.customiseLabel(lobbyNameLabel);
		UIRes.customiseLabel(gameModeLabel);
		UIRes.customiseLabel(mapLabel);
		Object[] inputs = { lobbyNameLabel, new JTextField(UIRes.username + "'s lobby"), gameModeLabel, gameModeChoice,
				mapLabel, mapChoice };

		return inputs;
	}
	
	JButton getBackToStartMenuButton() {
		JButton button = new JButton("Back");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			UIRes.switchPanel(UIRes.startPanel);
		});
		return button;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		repaint();
		
	}

}
