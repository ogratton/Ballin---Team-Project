package ui;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.esotericsoftware.kryonet.Client;

import graphics.MapPreview;
import networking.ClientInformation;
import networking.Command;
import networking.ConnectionData;
import networking.ConnectionDataModel;
import networking.Message;
import networking.Note;
import networking.Session;
import resources.FilePaths;
import resources.Map;
import resources.MapMetaData;
import resources.Resources;
import resources.Resources.Mode;

@SuppressWarnings("serial")
public class SessionListMenu extends JPanel implements Observer {

	private String lobbyName;
	private Mode gameMode;
	private String mapName;
	private Map.World tileSet;

	private Session session;
	private Client client;
	private ConnectionDataModel cModel;
	private InLobbyMenu lobby;
	private MapMetaData mmd = new MapMetaData();
	private File folder = new File(FilePaths.maps);
	private File[] listOfFiles = folder.listFiles();

	public SessionListMenu(Client client, ConnectionDataModel cModel) {
		this.cModel = cModel;
		cModel.addObserver(this);
		lobby = new InLobbyMenu(session, client, cModel, this);
		setOpaque(false);
		updateSessionsPanel(client);
		add(addSessionButtons(client, this));
		add(UIRes.sessionsPanels);

	}

	JButton createSessionButton(Client client) {
		JButton button = new JButton("Create Lobby");
		button.addActionListener(e -> {
			
			try {
				createLobbyWizard();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			Session newSession = new Session(this.lobbyName, new ClientInformation(cModel.getMyId(), UIRes.username),
					this.mapName, this.tileSet, gameMode, UIRes.username, 0);

			Message createMessage = new Message(Command.SESSION, Note.CREATE, cModel.getMyId(), "", "", "", newSession);

			try {
				cModel.getConnection().sendTCP(createMessage);
				System.out.println("Session creation sent.");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			lobby.setSession(newSession);
			UIRes.switchPanel(lobby);
			// }
			//
			// else
			// frame.dispose();

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

			Message joinMessage = new Message(Command.SESSION, Note.JOIN, cModel.getMyId(), "",
					cModel.getAllSessions().get(index).getId(), cModel.getAllSessions().get(index).getId());

			try {
				cModel.getConnection().sendTCP(joinMessage);

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			lobby.setSession(cModel.getAllSessions().get(index));
			UIRes.switchPanel(lobby);
		});

		UIRes.customiseButton(button, true);
		return button;
	}

	JButton refreshSessionList(Client client) {
		JButton button = new JButton("Refresh");
		button.addActionListener(e -> {
			Message message = new Message(Command.SESSION, Note.INDEX, cModel.getMyId(), null, cModel.getSessionId(),
					null);
			try {
				cModel.getConnection().sendTCP(message);
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
		JLabel sessionName = UIRes.getLabel(session.getSessionName(), 22);
		JLabel hostName = UIRes.getLabel(session.getHostName(), 22);
		JLabel mapName = UIRes.getLabel(session.getMapName(), 22);
		JLabel gameModeName = UIRes.getLabel("" + session.getGameMode(), 22);
		JLabel numberPlayers = UIRes.getLabel(session.getAllClients().size() + "/8", 22);
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
		for (int i = 0; i < cModel.getAllSessions().size(); i++) {
			JPanel session = getSessionPanel(cModel.getAllSessions().get(i));
			if (!cModel.getAllSessions().get(i).getAllClients().isEmpty()) {
				UIRes.sessionPanelsList.add(session);
				UIRes.sessionsPanels.add(session);
			}
		}

		UIRes.sessionsPanels.repaint();
		UIRes.sessionsPanels.revalidate();
	}

	JPanel addSessionButtons(Client client, JPanel sessionPanel) {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension((int) (UIRes.width * 0.95), (int) (UIRes.height * 0.12)));
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JButton createSession = createSessionButton(client);
		JButton joinSession = joinSessionButton(client);
		JButton refreshSession = refreshSessionList(client);
		JButton backToMainMenu = getBackToStartMenuButton();
		panel.add(Box.createHorizontalStrut(10));
		UIRes.getButtonAndIcon(panel, createSession);
		UIRes.getButtonAndIcon(panel, joinSession);
		UIRes.getButtonAndIcon(panel, refreshSession);
		UIRes.getButtonAndIcon(panel, backToMainMenu);
		panel.add(Box.createHorizontalStrut(10));
		return panel;
	}

	@SuppressWarnings("unchecked")
	void createLobbyWizard() throws IOException {
		JFrame lobbyFrame = new JFrame();
		JFrame mapFrame = new JFrame();
		JFrame gameModeFrame = new JFrame();

		JLabel lobbyNameLabel = new JLabel("Lobby name: ");
		JTextField lobbyNameInput = new JTextField(UIRes.username + "'s lobby");
		Object[] lobbyInfo = { lobbyNameLabel, lobbyNameInput };

		JLabel mapLabel = new JLabel("Map: ");
		JComboBox<MapPreview> mapChoice = new JComboBox<MapPreview>();
		String[] mapNames = null;
		mapNames = getMapFileNames();
		
		for (String map : mapNames) {
			ArrayList<Map> mapList = new ArrayList<Map>();
			mapList.add(new Map(1200, 650, Map.World.CAVE, map));
			mapChoice.add(new MapPreview(mapList));
		}

		JLabel tileLabel = new JLabel("Tile set: ");
		Choice tileChoice = new Choice();
		for (Map.World tile : Map.World.values()) {
			tileChoice.add(tile.toString());
		}

		Object[] mapInfo = { mapLabel, mapChoice, tileLabel, tileChoice };

		JLabel gameModeLabel = new JLabel("Game mode: ");
		Choice gameModeChoice = new Choice();
		ArrayList<Mode> gameModeList = new ArrayList<Mode>();
		gameModeList = getGameModes(this.mapName);
		for (int i = 0; i < gameModeList.size(); i++) {
			gameModeChoice.add(gameModeList.get(i).toString());
		}

		Object[] gameModeInfo = { gameModeLabel, gameModeChoice };

		UIRes.customiseLabel(lobbyNameLabel);
		UIRes.customiseLabel(mapLabel);
		UIRes.customiseLabel(tileLabel);
		UIRes.customiseLabel(gameModeLabel);

		int lobbyPane = JOptionPane.showConfirmDialog(lobbyFrame, lobbyInfo, "Name your lobby: ",
				JOptionPane.OK_CANCEL_OPTION);

		if (lobbyPane == JOptionPane.OK_OPTION) {

			this.lobbyName = ((JTextField) lobbyInfo[1]).getText();
			lobbyFrame.dispose();

			int mapPane = JOptionPane.showConfirmDialog(mapFrame, mapInfo,
					"Select the map you would like to play on. This will influence what game modes you can play.",
					JOptionPane.OK_CANCEL_OPTION);
			if (mapPane == JOptionPane.OK_OPTION) {
				this.mapName = ((JComboBox<MapPreview>) mapInfo[2]).getSelectedItem().toString();

				int gameModePane = JOptionPane.showConfirmDialog(gameModeFrame, gameModeInfo,
						"Select the game mode you would like to play: ", JOptionPane.OK_CANCEL_OPTION);

				if (gameModePane == JOptionPane.OK_OPTION)
					this.gameMode = mmd.correspondingMode(gameModeInfo[1].toString());
				else
					gameModeFrame.dispose();
			} else
				mapFrame.dispose();
		}

		else
			lobbyFrame.dispose();

	}

	JButton getBackToStartMenuButton() {
		JButton button = new JButton("Back");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {

			UIRes.cModel.getConnection().close();

			UIRes.switchPanel(UIRes.startPanel);
		});
		return button;
	}
	
	String[] getMapFileNames(){
		String[] maps = new String[listOfFiles.length];
		
		for(int i = 0; i < maps.length; i++){
			maps[i] = listOfFiles[i].toString().substring(0, listOfFiles[i].toString().length() - 4);
		}
		
		return maps;
	}

	ArrayList<Mode> getGameModes(String map) throws IOException {
		mmd.readMetaData(map);
		ArrayList<Mode> gameModes = mmd.getCompatibleModes();
		return gameModes;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		updateSessionsPanel(client);
		repaint();
	}

}
