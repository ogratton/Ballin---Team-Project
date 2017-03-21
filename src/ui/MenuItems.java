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
import java.io.BufferedReader;
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

	JButton getPlaySingleplayerButton() {
		JButton startButton = new JButton("Start Singleplayer Game");
		customiseButton(startButton, true);
		
		startButton.addActionListener(e -> {
			PlayGame.start(resources);

			if (!Resources.silent) {
				// button sound effect
				audioPlayer.play();
				// change the song
				// resources.getMusicPlayer().changePlaylist("paris30");
				// resources.getMusicPlayer().resumeMusic();
			}

		});
		return startButton;
	}

	JButton getPlayMultiplayerButton() {
		JButton startButton = new JButton("Start Multiplayer Game");
		customiseButton(startButton, true);
		startButton.addActionListener(e -> {
//			JFrame frame = new JFrame();
//			String input = JOptionPane.showInputDialog(frame, "Enter the server name:", "Input server",
//					JOptionPane.PLAIN_MESSAGE);
//			if (input != null) {
				NetworkingClient client = new NetworkingClient("localhost", username);
				client.start();
//			} else {
//				frame.dispose();
//			}
		});
		return startButton;
	}

	public static JButton getBackToStartMenuButton() {
		JButton button = new JButton("Back");
		customiseButton(button, true);
		button.addActionListener(e -> {
			switchPanel(startPanel);
		});
		return button;
	}

	JButton getBackToStartMenuButton(String name) {
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
		JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, VOL_MIN, VOL_MAX, VOL_INIT);
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

	JSlider getAudioSlider() {
		JSlider audioSlider = new JSlider(JSlider.HORIZONTAL, VOL_MIN, VOL_MAX, VOL_INIT);
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
