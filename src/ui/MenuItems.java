package ui;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import gamemodes.PlayGame;
import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;
import graphics.sprites.Sprite.SheetType;
import networking.Client;
import networking.ClientInformation;
import networking.Command;
import networking.ConnectionDataModel;
import networking.Message;
import networking.Note;
import networking.Port;
import networking.Session;
import resources.Map;
import resources.Resources;
import resources.Character;

public class MenuItems extends UIRes {

	void allignToCenter(JComponent comp) {
		comp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		comp.setAlignmentY(JComponent.CENTER_ALIGNMENT);
	}

	Color getRandomColour() {
		int r = 0, g = 0, b = 0;
		SecureRandom rand = new SecureRandom();
		while ((r < 150 && g < 150) || (b < 150 & g < 150) || (r < 150 & b < 150)) {
			r = rand.nextInt(255);
			g = rand.nextInt(255);
			b = rand.nextInt(255);
		}
		Color color = new Color(r, g, b);
		return color;
	}

	void customiseLabel(JComponent comp) {
		setCustomFont(comp, (int) (labelSize.height * labelRatio));
		allignToCenter(comp);
		comp.setForeground(colour);
	}

	void customiseComponent(JComponent comp, Dimension size, double ratio) {
		comp.setMaximumSize(size);
		setCustomFont(comp, (int) (size.height * ratio));
		allignToCenter(comp);
		comp.setOpaque(false);
		comp.setForeground(colour);
	}

	void customiseButton(JButton button, boolean addListener) {
		customiseComponent(button, buttonSize, buttonRatio);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setFocusable(false);
		button.setForeground(colour);
		if (addListener) {
			button.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					button.setForeground(getRandomColour());

				}

				@Override
				public void mouseExited(MouseEvent e) {
					button.setForeground(colour);

				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

			});
		}
	}

	void customiseSlider(JSlider slider) {
		customiseComponent(slider, buttonSize, sliderRatio);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}

	JLabel getLabel(String text) {
		JLabel label = new JLabel(text);
		customiseLabel(label);
		return label;
	}

	JLabel getSpriteIcon(int x) {
		BufferedImage icon = Sprite.getSprite(Sprite.loadSpriteSheet(SheetType.CHARACTER), 0, x,
				SheetDeets.CHARACTERS_SIZEX, SheetDeets.CHARACTERS_SIZEY);
		JLabel iconLabel = new JLabel(new ImageIcon(icon));
		return iconLabel;
	}

	JPanel getButtonAndIcon(JPanel panel, JButton button) {
		JPanel buttonPanel = new JPanel();
		int x = new SecureRandom().nextInt(numberIcons);
		buttonPanel.setMaximumSize(buttonSize);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(getSpriteIcon(x));
		buttonPanel.add(button);
		buttonPanel.add(getSpriteIcon(x));
		buttonPanel.setOpaque(false);
		panel.add(buttonPanel);
		return panel;
	}

	JButton getPlaySingleplayerButton() {
		JButton startButton = new JButton("Start Singleplayer Game");
		customiseButton(startButton, true);
		startButton.addActionListener(e -> {
			PlayGame.start(resources);
			if(!Resources.silent) { // play music
				// button sound effect
				audioPlayer.play();
				// change the song
				// TODO volume defined by user is not kept here...
				resources.getMusicPlayer().changePlaylist("thirty");
				resources.getMusicPlayer().resumeMusic();
			}
		});
		return startButton;
	}

	JButton getPlayMultiplayerButton() {
		JButton startButton = new JButton("Start Multiplayer Game");
		customiseButton(startButton, true);
		startButton.addActionListener(e -> {
			JFrame frame = new JFrame();
			String input = (String) JOptionPane.showInputDialog(frame, "Enter the server name:", "Input server",
					JOptionPane.PLAIN_MESSAGE);
			if (input != null) {
				connectToServer(username, "" + Port.number, host);
			} else {
				frame.dispose();
			}
		});
		return startButton;
	}

	void connectToServer(String username, String port, String host) {
		Client client = new Client(username, port, host);
		client.start();
	}

	JButton getBackToStartMenuButton() {
		JButton button = new JButton("Back");
		customiseButton(button, true);
		button.addActionListener(e -> {
			switchPanel(startPanel);
		});
		return button;
	}

	JButton getOptionsButton() {
		JButton optionsButton = new JButton("Options");
		customiseButton(optionsButton, true);
		optionsButton.addActionListener(e -> {
			switchPanel(optionsPanel);
		});
		return optionsButton;
	}

	JButton getExitButton() {
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> {
			System.exit(0);
		});
		customiseButton(exitButton, true);
		return exitButton;
	}

	JButton getUsername(JLabel label) {
		JButton usernameButton = new JButton("Change username");
		customiseButton(usernameButton, true);
		usernameButton.addActionListener(e -> {
			JFrame frame = new JFrame();
			String input = (String) JOptionPane.showInputDialog(frame, "Enter your username:", "Input username",
					JOptionPane.PLAIN_MESSAGE);
			if (input != null) {
				username = input;
				label.setText("Welcome, " + username + "!");
			} else {
				frame.dispose();
			}
		});
		return usernameButton;
	}

	JSlider getMusicSlider() {
		JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, VOL_MIN, VOL_MAX, VOL_MAX);
		customiseSlider(musicSlider);
		musicSlider.addChangeListener(e -> {
			int volume = musicSlider.getValue();
			if (volume == 0)
				resources.getMusicPlayer().mute();
			else
				resources.getMusicPlayer().setGain((float) ((VOL_MAX - volume) * (-0.33)));
		});
		return musicSlider;
	}
	
	JPanel getSessionInfo(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel sessionName = new JLabel("Session");
		JLabel hostName = new JLabel("Host Name");
		JLabel mapName = new JLabel("Map Name");
		JLabel gameModeName = new JLabel("Game Mode");
		JLabel numberPlayers = new JLabel("0/8");
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
		
		panel.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println(panel.isFocusOwner());
				for(int i = 0; i < sessionList.size(); i++){
					if(sessionList.get(i).isFocusOwner())
						sessionList.get(i).setBackground(background);
				}
				panel.requestFocus();
				panel.setBackground(Color.RED);
			}
			
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
			
		});
		panel.requestFocus();
		return panel;
	}
	
	JPanel getSessionsTable(JPanel panel){
		panel.removeAll();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for(int i = 0; i < numberSessions; i++){
			JPanel session = getSessionInfo();
			sessionList.add(session);
			panel.add(session);
		}
		panel.repaint();
		panel.revalidate();
		return panel;
	}

//	DefaultTableModel getSessionTableModel(ConnectionDataModel cModel) {
//		List<Session> sessions = cModel.getAllSessions();
//		System.out.println(sessions.size());
//		Iterator<Session> iterator = sessions.iterator();
//		String[] columnNames = { "Lobby Name", "Owner", "Map", "Game Mode", "No. of players" };
//		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
//		while (iterator.hasNext()) {
//			Object[] lobbyInfo = { iterator.next().getId(), null, mapName, null};
//			model.addRow(lobbyInfo);
//		}
//		return model;
//	}
//
//	JTable getSessionTable(DefaultTableModel model) {
//		JTable table = new JTable(model);
//		table.setDefaultEditor(Object.class, null);
//		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		table.setOpaque(true);
//		table.setFillsViewportHeight(true);
//
//		return table;
//	}
//
//	JTable updateSessionTable(ConnectionDataModel cModel, JTable table) {
//		DefaultTableModel tableModel = getSessionTableModel(cModel);
//		table.removeAll();
//		table.setModel(tableModel);
//		return table;
//	}

	JButton joinSessionButton(ConnectionDataModel cModel, ObjectOutputStream toServer) {
		JButton button = new JButton("Join");
		button.addActionListener(e -> {
			if (cModel.getSessionId() != cModel.getHighlightedSessionId()) {
				Message joinMessage = new Message(Command.SESSION, Note.JOIN, cModel.getMyId(), null,
						cModel.getSessionId(), cModel.getHighlightedSessionId());
				try {
					toServer.reset();
					toServer.writeUnshared(joinMessage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});

		customiseButton(button, true);
		return button;
	}

	//JButton createSessionButton(JTable table, ConnectionDataModel cModel, ObjectOutputStream toServer) {
	JButton createSessionButton(JPanel panel) {	
		JButton button = new JButton("Create Lobby");
		button.addActionListener(e -> {
			
			numberSessions++;
			sessionsPanel = getSessionsTable(sessionsPanel);
			
//			JFrame frame = new JFrame();
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			Object[] inputs = createLobbyWizard();
//			mapName = ((Choice) inputs[5]).getSelectedItem();
//			((Choice) inputs[5]).addItemListener(new ItemListener() {
//
//				@Override
//				public void itemStateChanged(ItemEvent e) {
//					if(e.getStateChange() == e.SELECTED){
//						mapName = ((Choice) inputs[5]).getSelectedItem();
//					System.out.println(mapName);}
//
//				}
//			});
//			int optionPane = JOptionPane.showConfirmDialog(frame, inputs, "Create new lobby",
//					JOptionPane.OK_CANCEL_OPTION);
//			if (optionPane == JOptionPane.OK_OPTION) {
//				Message createMessage = new Message(Command.SESSION, Note.CREATE, cModel.getMyId(), null, null, null,
//						cModel.getClientInformation());
//
//				try {
//					toServer.reset();
//					toServer.writeUnshared(createMessage);
//					//updateSessionTable(cModel, table);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
		});
		customiseButton(button, true);
		return button;
	}

	JButton refreshSessionList(JTable table, ConnectionDataModel cModel, ObjectOutputStream toServer) {
		JButton button = new JButton("Refresh");
		button.addActionListener(e -> {
			Message message = new Message(Command.SESSION, Note.INDEX, cModel.getMyId(), null, cModel.getSessionId(),
					null);
			try {
				toServer.writeUnshared(message);
				//updateSessionTable(cModel, table);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		customiseButton(button, true);
		return button;
	}

	Object[] createLobbyWizard() {
		JLabel lobbyNameLabel = new JLabel("Lobby name: ");

		JLabel gameModeLabel = new JLabel("Game mode: ");
		Choice gameModeChoice = new Choice();
		gameMode = gameModeChoice.getSelectedItem();
		System.out.println(gameMode);

		JLabel mapLabel = new JLabel("Map: ");
		Choice mapChoice = new Choice();
		for (Map.World map : Map.World.values()) {
			mapChoice.add(map + "");
		}

		System.out.println(mapName);
		customiseLabel(lobbyNameLabel);
		customiseLabel(gameModeLabel);
		customiseLabel(mapLabel);
		Object[] inputs = { lobbyNameLabel, new JTextField(username + "'s lobby"), gameModeLabel, gameModeChoice,
				mapLabel, mapChoice };

		return inputs;
	}

	// JTable getLobbyTableModel(ConnectionDataModel cModel){
	// List<Session> sessions = cModel.getAllSessions();
	// String[] columnNames = { "Player", "Character", "Ready"};
	// DefaultTableModel model = new DefaultTableModel(columnNames, 0);
	// if (cModel.getHighlightedSessionId() != null) {
	// UUID sessionId = cModel.getHighlightedSessionId();
	// Session session = cModel.getSession(sessionId);
	// clients = session.getAllClients();
	// }
	// else {
	// clients = new ArrayList<ClientInformation>();
	// }
	// return model;
	// }

	JPanel addPlayerToLobby(JPanel panel, String playerName) {
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
		JLabel playerLabel = new JLabel(playerName);

		Choice characterClass = new Choice();
		for (Character.Class character : Character.Class.values()) {
			characterClass.add(character + "");
		}

		playerPanel.add(playerLabel);
		playerPanel.add(characterClass);
		panel.add(playerPanel);
		return panel;
	}

	JButton backToLobbyListPanel(ConnectionDataModel cModel, ObjectOutputStream toServer) {
		JButton button = new JButton("Leave Lobby");
		customiseButton(button, true);
		button.addActionListener(e -> {
			// SessionListMenu lobbyList = new SessionListMenu();
			// JPanel lobby = lobbyList.getLobbyListPanel(cModel, toServer);
			// switchPanel(lobby);
		});
		return button;

	}

	JSlider getAudioSlider() {
		JSlider audioSlider = new JSlider(JSlider.HORIZONTAL, VOL_MIN, VOL_MAX, VOL_MAX);
		customiseSlider(audioSlider);
		audioSlider.addChangeListener(e -> {
			int volume = audioSlider.getValue();
			if (volume == 0)
				resources.setSFXGain(-80);
			else
				resources.setSFXGain((int) ((VOL_MAX - volume) * (-0.33)));
		});
		return audioSlider;
	}

	JPanel getControlButton(String buttonLabel, String buttonName, String name) {
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension((int) (width * 0.85), (int) (height * 0.1)));
		panel.setOpaque(false);
		GridLayout controlsGrid = new GridLayout(0, 2);
		panel.setLayout(controlsGrid);
		panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		JLabel label = getLabel(buttonLabel);

		JButton button = new JButton(buttonName);
		customiseButton(button, false);
		button.setFocusable(true);
		button.setName(name);
		setKeyRebindable(button);
		controlsList.add(button.getText());
		buttonsList.add(button);

		panel.add(label);
		panel.add(button);
		return panel;
	}

	JPanel getControlsPanel() {
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension((int) (width * 0.85), (int) (height * 0.32)));
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		panel.add(getControlButton("Move up:", KeyEvent.getKeyText(resources.getDefaultUp()).toUpperCase(), "up"));
		panel.add(
				getControlButton("Move down:", KeyEvent.getKeyText(resources.getDefaultDown()).toUpperCase(), "down"));
		panel.add(
				getControlButton("Move left:", KeyEvent.getKeyText(resources.getDefaultLeft()).toUpperCase(), "left"));
		panel.add(getControlButton("Move right:", KeyEvent.getKeyText(resources.getDefaultRight()).toUpperCase(),
				"right"));
		panel.add(getControlButton("Dash:", KeyEvent.getKeyText(resources.getDefaultDash()).toUpperCase(), "dash"));
		panel.add(getControlButton("Block:", KeyEvent.getKeyText(resources.getDefaultBlock()).toUpperCase(), "block"));

		panel.setOpaque(false);
		return panel;
	}

	void resetButton(JButton button) {
		if (button.getName().equals("up")) {
			resources.setUp(resources.getDefaultUp());
			button.setText(KeyEvent.getKeyText(resources.getDefaultUp()).toUpperCase());
		} else if (button.getName().equals("down")) {
			resources.setDown(resources.getDefaultDown());
			button.setText(KeyEvent.getKeyText(resources.getDefaultDown()).toUpperCase());
		} else if (button.getName().equals("left")) {
			resources.setLeft(resources.getDefaultLeft());
			button.setText(KeyEvent.getKeyText(resources.getDefaultLeft()).toUpperCase());
		} else if (button.getName().equals("right")) {
			resources.setRight(resources.getDefaultRight());
			button.setText(KeyEvent.getKeyText(resources.getDefaultRight()).toUpperCase());
		} else if (button.getName().equals("dash")) {
			resources.setDash(resources.getDefaultDash());
			button.setText(KeyEvent.getKeyText(resources.getDefaultDash()).toUpperCase());
		} else if (button.getName().equals("block")) {
			resources.setBlock(resources.getDefaultBlock());
			button.setText(KeyEvent.getKeyText(resources.getDefaultBlock()).toUpperCase());
		}
	}

	JButton getResetControlsButton() {
		JButton resetControlsButton = new JButton("Reset controls");
		customiseButton(resetControlsButton, true);
		resetControlsButton.addActionListener(e -> {
			Iterator<JButton> i = buttonsList.iterator();
			while (i.hasNext()) {
				JButton button = i.next();
				resetButton(button);
			}

			controlsList.removeAll(controlsList);
			controlsList.add(("" + resources.getDefaultUp()).toUpperCase());
			controlsList.add(("" + resources.getDefaultDown()).toUpperCase());
			controlsList.add(("" + resources.getDefaultLeft()).toUpperCase());
			controlsList.add(("" + resources.getDefaultRight()).toUpperCase());
			controlsList.add(("" + resources.getDefaultDash()).toUpperCase());
			controlsList.add(("" + resources.getDefaultBlock()).toUpperCase());

		});
		return resetControlsButton;
	}

	void setKeyRebindable(JButton button) {

		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				isPressed = true;
				button.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (isPressed) {
							controlsList.remove(button.getText());
							if (e.getKeyCode() == KeyEvent.VK_UP) {
								if (!checkKey("up arrow".toUpperCase()))
									button.setText("up arrow".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
								if (!checkKey("down arrow".toUpperCase()))
									button.setText("down arrow".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
								if (!checkKey("left arrow".toUpperCase()))
									button.setText("left arrow".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
								if (!checkKey("right arrow".toUpperCase()))
									button.setText("right arrow".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
								if (!checkKey("space".toUpperCase()))
									button.setText("space".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
								if (!checkKey("ctrl".toUpperCase()))
									button.setText("ctrl".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
								if (!checkKey("shift".toUpperCase()))
									button.setText("shift".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_ALT) {
								if (!checkKey("alt".toUpperCase()))
									button.setText("alt".toUpperCase());
							} else if (((e.getKeyChar() >= 'a') && (e.getKeyChar() <= 'z'))
									|| ((e.getKeyChar() >= '0') && (e.getKeyChar() <= '9'))) {
								if (!checkKey(("" + (e.getKeyChar())).toUpperCase()))
									button.setText(("" + e.getKeyChar()).toUpperCase());
							}
							isPressed = false;
							controlsList.add(button.getText());

							if (button.getName().equals("up"))
								resources.setUp(e.getKeyCode());
							else if (button.getName().equals("down"))
								resources.setDown(e.getKeyCode());
							else if (button.getName().equals("left"))
								resources.setLeft(e.getKeyCode());
							else if (button.getName().equals("right"))
								resources.setRight(e.getKeyCode());
							else if (button.getName().equals("dash"))
								resources.setDash(e.getKeyCode());
							else if (button.getName().equals("block"))
								resources.setBlock(e.getKeyCode());
						}

					}

					@Override public void keyReleased(KeyEvent e) {}
					@Override public void keyTyped(KeyEvent e) {}
				});

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setForeground(getRandomColour());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setForeground(colour);
			}

			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}

		});
	}

	boolean checkKey(String string) {
		if (controlsList.contains(string)) {
			JOptionPane.showMessageDialog(new JFrame(),
					"This key is already assigned for another control. Please assign another key!",
					"Key already assigned!", JOptionPane.ERROR_MESSAGE);
			return true;
		} else
			return false;
	}
}
