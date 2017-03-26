package ui;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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

import graphics.sprites.Sprite;
import networking.ClientInformation;
import networking.Command;
import networking.ConnectionDataModel;
import networking.Message;
import networking.Note;
import networking.Session;
import resources.Map;
import resources.MapMetaData;
import resources.Resources.Mode;

/**
 * Class for the Session List Menu panel.
 * 
 * @author Andreea Diana Dinca
 *
 */
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

	/**
	 * Constructor of the Session List Menu panel.
	 * 
	 * @param client
	 * 		the client of the user connected to the server
	 * @param cModel
	 * 		the connection data model that the server uses
	 */
	public SessionListMenu(Client client, ConnectionDataModel cModel) {
		this.cModel = cModel;
		cModel.addObserver(this);
		lobby = new InLobbyMenu(session, client, cModel, this);
		setOpaque(false);
		updateSessionsPanel(client);
		add(addSessionButtons(client));
		add(UIRes.sessionsPanels);

	}

	/**
	 * Creates a button that allows the user to create a session. Starts the lobby wizard which 
	 * allows the user to select the name of the lobby, game mode, map and tile set for the game.
	 * 
	 * @param client
	 * 		the client of the user connected to the server
	 * @return
	 * 		the button
	 */
	JButton createSessionButton(Client client) {
		JButton button = new JButton("Create Lobby");
		button.addActionListener(e -> {

			createLobbyWizard();

			if (this.lobbyName != null && this.mapName != null && this.tileSet != null && this.gameMode != null) {

				Session newSession = new Session(this.lobbyName,
						new ClientInformation(cModel.getMyId(), UIRes.username), this.mapName, this.tileSet,
						this.gameMode, UIRes.username, 0);

				Message createMessage = new Message(Command.SESSION, Note.CREATE, cModel.getMyId(), "", "", "",
						newSession);

				try {
					cModel.getConnection().sendTCP(createMessage);
					System.out.println("Session creation sent.");
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				lobby.setSession(newSession);
				UIRes.switchPanel(lobby);
			}

		});
		UIRes.customiseButton(button, true);
		return button;
	}

	/**
	 * Creates a button that allows the user to join a session they have selected from the session list.
	 * 
	 * @param client
	 * 		the client of the user connected to the server
	 * @return
	 * 		the button
	 */
	JButton joinSessionButton(Client client) {
		JButton button = new JButton("Join");
		button.addActionListener(e -> {
			int index = -1;
			for (int i = 0; i < UIRes.sessionPanelsList.size(); i++) {
				if (UIRes.sessionPanelsList.get(i).isFocusOwner()){
					index = i;
					UIRes.sessionPanelsList.get(i).transferFocus();
				}
			}
			
			System.out.println(index);
			
			if(index == -1){
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "Please select a lobby from the list before clicking on the join button.");
			}
			
			else{
				
				Message joinMessage = new Message(Command.SESSION, Note.JOIN, cModel.getMyId(), "",
						cModel.getAllSessions().get(index).getId(), cModel.getAllSessions().get(index).getId());

				try {
					cModel.getConnection().sendTCP(joinMessage);

				} catch (Exception e1) {
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "Please select a lobby from the list before clicking on the join button.");
				}

				lobby.setSession(cModel.getAllSessions().get(index));
				UIRes.switchPanel(lobby);
			}
		});

		UIRes.customiseButton(button, true);
		return button;
	}

	/**
	 * Creates a button that allows the user to refresh the lobby list if it does not update on its own.
	 * 
	 * @param client
	 * 		the client of the user connected to the server
	 * @return
	 * 		the button
	 */
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

	/**
	 * Creates a panel that displays all the details of a specific session.
	 * 
	 * @param session
	 * 		the session the panel is made for
	 * @param inProgress
	 * 		flag for the session being in progress or not
	 * @return
	 * 		the panel
	 */
	JPanel getSessionPanel(Session session, boolean inProgress) {
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

		if (!inProgress) {
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
		}
		else
			panel.setBackground(Color.LIGHT_GRAY);
		return panel;
	}

	/**
	 * Updates the session panel when the connection data model changes in order to display the correct list of sessions.
	 * 
	 * @param client
	 * 		the client of the user connected to the server
	 */
	void updateSessionsPanel(Client client) {
		UIRes.sessionsPanels.removeAll();
		UIRes.sessionPanelsList.removeAll(UIRes.sessionPanelsList);
		UIRes.sessionsPanels.setLayout(new BoxLayout(UIRes.sessionsPanels, BoxLayout.Y_AXIS));
		for (int i = 0; i < cModel.getAllSessions().size(); i++) {
			JPanel session = getSessionPanel(cModel.getAllSessions().get(i), cModel.getAllSessions().get(i).isGameInProgress());
			UIRes.sessionPanelsList.add(session);
			UIRes.sessionsPanels.add(session);
		}
		UIRes.sessionsPanels.revalidate();
		UIRes.sessionsPanels.repaint();
	}

	/**
	 * Creates a panel containing the create session button, the join session button, the refresh button and the back button.
	 * 
	 * @param client
	 * 		the client of the user connected to the server
	 * @return
	 * 		the panel
	 */
	JPanel addSessionButtons(Client client) {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension((int) (UIRes.frameWidth * 0.95), (int) (UIRes.frameHeight * 0.12)));
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JButton createSession = createSessionButton(client);
		JButton joinSession = joinSessionButton(client);
		JButton refreshSession = refreshSessionList(client);
		UIRes.getButtonAndIcon(panel, createSession);
		UIRes.getButtonAndIcon(panel, joinSession);
		UIRes.getButtonAndIcon(panel, refreshSession);
		UIRes.getButtonAndIcon(panel, new BackButton(UIRes.startPanel, "Back"));
		return panel;
	}

	/**
	 * Starts the lobby wizard which pops up a number of frames asking the user to choose the lobby name, game mode, map and set of tiles for their game.
	 */
	void createLobbyWizard() {
		JFrame lobbyFrame = new JFrame();
		JFrame mapFrame = new JFrame();
		JFrame tileFrame = new JFrame();
		JFrame gameModeFrame = new JFrame();

		JLabel lobbyNameLabel = new JLabel("Lobby name: ");
		JTextField lobbyNameInput = new JTextField(UIRes.username + "'s lobby");
		Object[] lobbyInfo = { lobbyNameLabel, lobbyNameInput };

		UIRes.customiseLabel(lobbyNameLabel);

		int lobbyPane = JOptionPane.showConfirmDialog(lobbyFrame, lobbyInfo, "Name your lobby: ",
				JOptionPane.OK_CANCEL_OPTION);

		if (lobbyPane == JOptionPane.OK_OPTION) {
			this.lobbyName = lobbyNameInput.getText();

			JLabel gameModeLabel = new JLabel("Game mode: ");
			UIRes.customiseLabel(gameModeLabel);

			Choice gameModeChoice = new Choice();
			ArrayList<String> gameModeList = new ArrayList<String>();
			gameModeList = mmd.gamemodeNames;
			for (int i = 0; i < gameModeList.size(); i++) {
				if (!gameModeList.get(i).equals("Hockey") && !gameModeList.get(i).equals("Debug"))
					gameModeChoice.add(gameModeList.get(i));
			}

			Object[] gameModeInfo = { gameModeLabel, gameModeChoice };

			int gameModePane = JOptionPane.showConfirmDialog(gameModeFrame, gameModeInfo, "Select the game mode: ",
					JOptionPane.OK_CANCEL_OPTION);

			if (gameModePane == JOptionPane.OK_OPTION) {

				this.gameMode = mmd.correspondingMode(gameModeChoice.getSelectedItem());

				JLabel mapLabel = new JLabel("Map: ");
				UIRes.customiseLabel(mapLabel);

				JComboBox<ImageIcon> mapChoice = new JComboBox<ImageIcon>();
				mapChoice.setMaximumSize(new Dimension(150, 100));

				HashSet<String> mapNames = null;
				try {
					mapNames = MapMetaData.getTable().get(this.gameMode);
				} catch (IOException e) {

					e.printStackTrace();
				}

				for (String map : mapNames) {
					ImageIcon icon = new ImageIcon(Sprite.createMap(new Map(1200, 650, Map.World.DESERT, map)));
					Image image = icon.getImage();
					Image mapIcon = image.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
					mapChoice.addItem(new ImageIcon(mapIcon));
				}

				Object[] mapInfo = { mapLabel, mapChoice };

				int mapPane = JOptionPane.showConfirmDialog(mapFrame, mapInfo, "Select the map: ",
						JOptionPane.OK_CANCEL_OPTION);

				if (mapPane == JOptionPane.OK_OPTION) {
					Iterator<String> iterator = mapNames.iterator();
					for (int i = 0; i < mapChoice.getSelectedIndex(); i++) {
						iterator.next();
					}
					this.mapName = iterator.next();
					System.out.println(this.mapName);

					JLabel tileLabel = new JLabel("Tiles: ");
					UIRes.customiseLabel(tileLabel);

					JComboBox<ImageIcon> tileChoice = new JComboBox<ImageIcon>();
					for (Map.World tile : Map.World.values()) {
						ImageIcon icon = new ImageIcon(Sprite.createMap(new Map(1200, 650, tile, this.mapName)));
						Image image = icon.getImage();
						Image tileIcon = image.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
						tileChoice.addItem(new ImageIcon(tileIcon));
					}

					Object[] tileInfo = { tileLabel, tileChoice };

					int tilePane = JOptionPane.showConfirmDialog(tileFrame, tileInfo, "Select the tiles: ",
							JOptionPane.OK_CANCEL_OPTION);

					if (tilePane == JOptionPane.OK_OPTION) {
						this.tileSet = Map.World.values()[tileChoice.getSelectedIndex()];

					} else {
						tileFrame.dispose();
					}

				} else
					mapFrame.dispose();
			} else
				gameModeFrame.dispose();

		}

		else
			lobbyFrame.dispose();

	}

	/**
	 * Whenever the connection data model is changed this method is called in order to update everything that has changed.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		updateSessionsPanel(client);
		repaint();
	}

}
