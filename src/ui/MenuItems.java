package ui;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Iterator;

import javax.swing.BorderFactory;
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
import javax.swing.JTextField;

import com.esotericsoftware.kryonet.Client;

import gamemodes.PlayGame;
import graphics.GameComponent;
import graphics.LayeredPane;
import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;
import graphics.sprites.Sprite.SheetType;
import networking.ClientInformation;
import networking.Command;
import networking.ConnectionDataModel;
import networking.Message;
import networking.NetworkingClient;
import networking.Note;
import networking.Session;
import resources.Character;
import resources.Map;
import resources.Resources;
import resources.Resources.Mode;

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

	void customiseLabel(JLabel label, int size) {
		setCustomFont(label, size);
		allignToCenter(label);
		label.setForeground(colour);
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
				}

				@Override
				public void mouseReleased(MouseEvent e) {
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

	JLabel getLabel(String text, int size) {
		JLabel label = new JLabel(text);
		customiseLabel(label, size);
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

			if (!Resources.silent) {
				// button sound effect
				audioPlayer.play();
				// change the song
//				resources.getMusicPlayer().changePlaylist("paris30");
//				resources.getMusicPlayer().resumeMusic();
			}

		});
		return startButton;
	}

	JButton getPlayMultiplayerButton() {
		JButton startButton = new JButton("Start Multiplayer Game");
		customiseButton(startButton, true);
		startButton.addActionListener(e -> {

			NetworkingClient.main(null);
			//
			// JFrame frame = new JFrame();
			// String input = (String) JOptionPane.showInputDialog(frame, "Enter
			// the server name:", "Input server",
			// JOptionPane.PLAIN_MESSAGE);
			// if (input != null) {
			// NetworkingClient client = new NetworkingClient(input, username);
			// client.run();
			// } else {
			// frame.dispose();
			// }
		});
		return startButton;
	}

	JButton getBackToStartMenuButton() {
		JButton button = new JButton("Back");
		customiseButton(button, true);
		button.addActionListener(e -> {
			switchPanel(startPanel);
		});
		return button;
	}
	
	JButton getBackToStartMenuButton(String name){
		JButton button = getBackToStartMenuButton();
		button.setText(name);
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

	JButton getResumeToGameButton(JPanel panel) {
		JButton button = new JButton("Resume");
		button.addActionListener(e -> {
			GameComponent.layers.setLayer(panel, new Integer(10));
			LayeredPane.menuShowing = !LayeredPane.menuShowing;
		});
		customiseButton(button, true);
		return button;
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
		if (!Resources.silent) {
			musicSlider.addChangeListener(e -> {
				int volume = musicSlider.getValue();
				if (volume == 0)
					resources.getMusicPlayer().mute();
				else
					resources.getMusicPlayer().setGain((float) ((VOL_MAX - volume) * (-0.33)));
			});
		}

		return musicSlider;
	}

	JPanel getSessionPanel(Session session) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel sessionName = getLabel(session.getSessionName(), 20);
		JLabel hostName = getLabel(session.getHostName(), 20);
		JLabel mapName = getLabel(session.getMapName(), 20);
		JLabel gameModeName = getLabel("" + session.getGameMode(), 20);
		JLabel numberPlayers = getLabel(session.getAllClients().size() + "/8", 20);
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
				for (int i = 0; i < sessionPanelsList.size(); i++) {
					if (sessionPanelsList.get(i).isFocusOwner())
						sessionPanelsList.get(i).setBackground(background);
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

	void updateSessionsPanel(ConnectionDataModel cModel, Client client) {
		sessionsPanels.removeAll();
		sessionPanelsList.removeAll(sessionPanelsList);
		sessionsPanels.setLayout(new BoxLayout(sessionsPanels, BoxLayout.Y_AXIS));
		System.out.println(cModel.getAllSessions().size());
		for(int i = 0; i < cModel.getAllSessions().size(); i++){
			JPanel session = getSessionPanel(cModel.getAllSessions().get(i));
			sessionPanelsList.add(session);
			sessionsPanels.add(session);
		}
		
		sessionsPanels.repaint();
		sessionsPanels.revalidate();
	}

	JButton joinSessionButton(ConnectionDataModel cModel, Client client, JPanel sessionPanel) {
		JButton button = new JButton("Join");
		button.addActionListener(e -> {
			int index = -1;
			for (int i = 0; i < sessionPanelsList.size(); i++) {
				if(sessionPanelsList.get(i).isFocusOwner())
					index = i;
			}
			
			Message joinMessage = new Message(Command.SESSION, Note.JOIN, cModel.getMyId(), "",
					cModel.getAllSessions().get(index).getId(), cModel.getAllSessions().get(index).getId());
			try {
				client.sendTCP(joinMessage);
				//addPlayerToLobby(sessionPanel, new ClientInformation(username), cModel.getAllSessions().get(index).getAllClients().size() + 1);
				updateInLobbyPanel(sessionPanel, cModel.getSession(cModel.getSessionId()), cModel, client);
			//	updateInLobbyPanel(inLobbyList.get(index), index, cModel, client);
				switchPanel(sessionPanel);
			
			} catch (Exception e1) {
				e1.printStackTrace();
			}
//			if(index != -1){
//					
//					updateSessionsPanel(cModel, client);
//			}
		});

		customiseButton(button, true);
		return button;
	}

	JButton createSessionButton(ConnectionDataModel cModel, Client client, JPanel sessionPanel) {
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

				Session newSession = new Session(lobbyName, new ClientInformation(username), mapName, mapTiles,
						gameMode, username, 0);

				Message createMessage = new Message(Command.SESSION, Note.CREATE, cModel.getMyId(), "", "", "",
						newSession);
				try {
					client.sendTCP(createMessage);
					System.out.println("Session creation sent.");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				updateSessionsPanel(cModel, client);
				updateInLobbyPanel(sessionPanel, cModel.getSession(cModel.getSessionId()), cModel, client);
				switchPanel(sessionPanel);
			}

			else
				frame.dispose();


		});
		customiseButton(button, true);
		return button;
	}

	JButton refreshSessionList(ConnectionDataModel cModel, Client client) {
		JButton button = new JButton("Refresh");
		button.addActionListener(e -> {
			Message message = new Message(Command.SESSION, Note.INDEX, cModel.getMyId(), null, cModel.getSessionId(),
					null);
			try {
				client.sendTCP(message);
				updateSessionsPanel(cModel, client);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		customiseButton(button, true);
		return button;
	}

	JPanel addSessionButtons(ConnectionDataModel cModel, Client client, JPanel sessionPanel) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JButton createSession = createSessionButton(cModel, client, sessionPanel);
		JButton joinSession = joinSessionButton(cModel, client, sessionPanel);
		JButton refreshSession = refreshSessionList(cModel, client);
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

		customiseLabel(lobbyNameLabel);
		customiseLabel(gameModeLabel);
		customiseLabel(mapLabel);
		Object[] inputs = { lobbyNameLabel, new JTextField(username + "'s lobby"), gameModeLabel, gameModeChoice,
				mapLabel, mapChoice };

		return inputs;
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

		Color colour = resources.getPlayerColor(index);
		playerPanel.setBorder(BorderFactory.createLineBorder(colour, 15));

		JButton readyCheck = new JButton("Ready");
		customiseButton(readyCheck, false);

		readyCheck.setForeground(Color.RED);
		readyCheck.addActionListener(e -> {
			if (readyCheck.getForeground() == Color.RED){
				readyCheck.setForeground(Color.GREEN);
				client.setReady(true);
			}
			else{
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
	
	JPanel updateInLobbyPanel(JPanel panel, Session session, ConnectionDataModel cModel, Client client){
		for(int i = 1; i < panel.getComponentCount(); i++){
			panel.remove(i);
		}
		//panel.removeAll();
		for(int i = 0; i < session.getAllClients().size(); i++){
			addPlayerToLobby(panel, session.getAllClients().get(i), i+1);
		}	
		panel.repaint();
		panel.revalidate();
		return panel;
	}

	JButton leaveLobbyButton(ConnectionDataModel cModel, Client client) {
		JButton button = new JButton("Leave Lobby");
		customiseButton(button, true);
		button.addActionListener(e -> {
			Message leaveMessage = new Message(Command.SESSION, Note.LEAVE, cModel.getMyId(), "", cModel.getSessionId(), cModel.getHighlightedSessionId());
			try {
				client.sendTCP(leaveMessage);
				cModel.setReady(false);
				SessionListMenu lobbyList = new SessionListMenu();
				JPanel lobby = lobbyList.getLobbyListPanel(cModel, client);
				updateSessionsPanel(cModel, client);
				
				switchPanel(lobby);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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

					@Override
					public void keyReleased(KeyEvent e) {
					}

					@Override
					public void keyTyped(KeyEvent e) {
					}
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

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

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
