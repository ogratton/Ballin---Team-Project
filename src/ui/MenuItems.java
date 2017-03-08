package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import audio.AudioFile;
import audio.MusicPlayer;
import gamemodes.PlayGame;
import graphics.PhysicsWithGraphicsDemo;
import networking.Client;
import networking.Port;
import resources.Resources;

public class MenuItems {
	
	void allignToCenter(JComponent comp){
		comp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		comp.setAlignmentY(JComponent.CENTER_ALIGNMENT);
	}
	
	void setCustomFont(JComponent comp, int size){
		Font customFont = new Font("Comic Sans", Font.PLAIN, 14);
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/04b.ttf"))
					.deriveFont((float) size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
		}
		comp.setFont(customFont);
	}
	
	void customiseLabel(JComponent comp){
		setCustomFont(comp, (int)(UIRes.labelSize.height * UIRes.labelRatio));
		allignToCenter(comp);
	}
	
	void customiseComponent(JComponent comp, Dimension size, double ratio){
		comp.setMaximumSize(size);
		setCustomFont(comp, (int)(size.height * ratio));
		allignToCenter(comp);
	}
	
	void customiseSlider(JSlider slider){
		customiseComponent(slider, UIRes.buttonSize, UIRes.sliderRatio);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}
	
	void switchPanel(JPanel newPanel){
		UIRes.mainPanel.removeAll();
		UIRes.mainPanel.add(newPanel);
		newPanel.setPreferredSize(UIRes.mainPanel.getSize());
		UIRes.mainPanel.revalidate();
		UIRes.mainPanel.repaint();
	}
	
	JLabel getLabel(String text){
		JLabel label = new JLabel(text);
		customiseLabel(label);
		return label;
	}
	
	JButton getPlaySingleplayerButton(){
		JButton startButton = new JButton("Start Singleplayer Game");
		customiseComponent(startButton, UIRes.buttonSize, UIRes.buttonRatio);
		startButton.addActionListener(e -> {
			PlayGame.start(UIRes.resources);
			// XXX change the song
			UIRes.musicPlayer.changePlaylist("thirty");
			UIRes.musicPlayer.nextSong();
			UIRes.musicPlayer.resume();
		});
		return startButton;
	}
	
	JButton getPlayMultiplayerButton(){
		JButton startButton = new JButton("Start Multiplayer Game");
		customiseComponent(startButton, UIRes.buttonSize, UIRes.buttonRatio);
		startButton.addActionListener(e -> {
			JFrame frame = new JFrame();
			String input = (String)JOptionPane.showInputDialog(frame, "Enter the server name:", "Input server", JOptionPane.PLAIN_MESSAGE);
			if (input != null){
				connectToServer(UIRes.username, "" + Port.number, UIRes.host);				
			}
			else{
				frame.dispose();
			}
		});
		return startButton;
	}
	
	void connectToServer(String username, String port, String host){
		Client client = new Client(username, port, host);
		client.start();
	}
	
	JButton getBackToStartMenuButton(){
		JButton button = new JButton("Back");
		customiseComponent(button, UIRes.buttonSize, UIRes.buttonRatio);
		button.addActionListener(e -> {
			switchPanel(UIRes.startPanel);
		});
		return button;
	}
	
	JButton getOptionsButton(){
		JButton optionsButton = new JButton("Options");
		customiseComponent(optionsButton, UIRes.buttonSize, UIRes.buttonRatio);
		optionsButton.addActionListener(e -> {
			switchPanel(UIRes.optionsPanel);
		});
		return optionsButton;
	}
	
	JButton getExitButton(){
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> {
			System.exit(0);
		});
		customiseComponent(exitButton, UIRes.buttonSize, UIRes.buttonRatio);
		return exitButton;
	}
	
	JButton getUsername(JLabel label){
		JButton usernameButton = new JButton("Change username");
		customiseComponent(usernameButton, UIRes.buttonSize, UIRes.buttonRatio);		
		usernameButton.addActionListener(e ->{
			JFrame frame = new JFrame();
			String input = (String)JOptionPane.showInputDialog(frame, "Enter your username:", "Input username",JOptionPane.PLAIN_MESSAGE);
			if(input != null){
				UIRes.username = input;
				label.setText("Welcome, " + input + "!");
			}
			else{
				frame.dispose();
			}
		});
		return usernameButton;
	}
	
	JSlider getMusicSlider(){
		JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, UIRes.VOL_MIN, UIRes.VOL_MAX, UIRes.VOL_MAX);
		customiseSlider(musicSlider);
		musicSlider.addChangeListener(e -> {
			int volume = musicSlider.getValue();
			if (volume == 0)
				UIRes.musicPlayer.mute();
			else
				UIRes.musicPlayer.setGain((float) ((UIRes.VOL_MAX - volume) * (-0.33)));
		});
		return musicSlider;
	}
	
	JSlider getAudioSlider(){
		JSlider audioSlider = new JSlider(JSlider.HORIZONTAL, UIRes.VOL_MIN, UIRes.VOL_MAX, UIRes.VOL_MAX);
		customiseSlider(audioSlider);
		audioSlider.addChangeListener(e -> {
			int volume = audioSlider.getValue();
			if (volume == 0)
				UIRes.resources.setSFXGain(-80);
			else
				UIRes.resources.setSFXGain((int) ((UIRes.VOL_MAX - volume) * (-0.33)));
		});
		return audioSlider;
	}
	
	JPanel getControlButton(String buttonLabel, String buttonName, String name){
		JPanel panel = new JPanel();	
		GridLayout controlsGrid = new GridLayout(0, 2);
		panel.setLayout(controlsGrid);
		panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
		JLabel label = getLabel(buttonLabel);
		
		JButton button = new JButton(buttonName);
		customiseComponent(button, UIRes.buttonSize, UIRes.buttonRatio);
		button.setName(name);
		setKeyRebindable(button);
		UIRes.controlsList.add(button.getText());
		UIRes.buttonsList.add(button);
		
		panel.add(label);
		panel.add(button);
		return panel;
	}
	
	JPanel getControlsPanel(){
		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		panel.add(getControlButton("Move up:", KeyEvent.getKeyText(UIRes.resources.getDefaultUp()).toUpperCase(), "up"));
		panel.add(getControlButton("Move down:", KeyEvent.getKeyText(UIRes.resources.getDefaultDown()).toUpperCase(), "down"));
		panel.add(getControlButton("Move left:", KeyEvent.getKeyText(UIRes.resources.getDefaultLeft()).toUpperCase(), "left"));
		panel.add(getControlButton("Move right:", KeyEvent.getKeyText(UIRes.resources.getDefaultRight()).toUpperCase(), "right"));
		panel.add(getControlButton("Dash:", KeyEvent.getKeyText(UIRes.resources.getDefaultDash()).toUpperCase(), "dash"));
		panel.add(getControlButton("Block:", KeyEvent.getKeyText(UIRes.resources.getDefaultBlock()).toUpperCase(), "block"));
		
		return panel;
	}
	
	void resetButton(JButton button) {
		if (button.getName().equals("up")) {
			UIRes.resources.setUp(UIRes.resources.getDefaultUp());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultUp()).toUpperCase());
		} else if (button.getName().equals("down")) {
			UIRes.resources.setDown(UIRes.resources.getDefaultDown());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultDown()).toUpperCase());
		} else if (button.getName().equals("left")) {
			UIRes.resources.setLeft(UIRes.resources.getDefaultLeft());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultLeft()).toUpperCase());
		} else if (button.getName().equals("right")) {
			UIRes.resources.setRight(UIRes.resources.getDefaultRight());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultRight()).toUpperCase());
		} else if (button.getName().equals("dash")) {
			UIRes.resources.setDash(UIRes.resources.getDefaultDash());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultDash()).toUpperCase());
		} else if (button.getName().equals("block")) {
			UIRes.resources.setBlock(UIRes.resources.getDefaultBlock());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultBlock()).toUpperCase());
		}
	}
	
	JButton getResetControlsButton(){
		JButton resetControlsButton = new JButton("Reset controls to default");
		customiseComponent(resetControlsButton, UIRes.buttonSize, UIRes.buttonRatio);
		resetControlsButton.addActionListener(e -> {
			Iterator<JButton> i = UIRes.buttonsList.iterator();
			while(i.hasNext()){
				JButton button = i.next();
				resetButton(button);
			}
		
			UIRes.controlsList.removeAll(UIRes.controlsList);
			UIRes.controlsList.add("" + Character.toUpperCase((char) UIRes.resources.getDefaultUp()));
			UIRes.controlsList.add("" + Character.toUpperCase((char) UIRes.resources.getDefaultDown()));
			UIRes.controlsList.add("" + Character.toUpperCase((char) UIRes.resources.getDefaultLeft()));
			UIRes.controlsList.add("" + Character.toUpperCase((char) UIRes.resources.getDefaultRight()));
			UIRes.controlsList.add(("" + UIRes.resources.getDefaultDash()).toUpperCase());
			UIRes.controlsList.add(("" + UIRes.resources.getDefaultBlock()).toUpperCase());
			
		});
		return resetControlsButton;
	}
	
	void setKeyRebindable(JButton button) {

		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				UIRes.isPressed = true;
				button.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (UIRes.isPressed) {
							UIRes.controlsList.remove(button.getText());
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
								if (!checkKey("" + Character.toUpperCase(e.getKeyChar())))
									button.setText("" + Character.toUpperCase(e.getKeyChar()));
							}
							UIRes.isPressed = false;
							UIRes.controlsList.add(button.getText());

							if (button.getName().equals("up"))
								UIRes.resources.setUp(e.getKeyCode());
							else if (button.getName().equals("down"))
								UIRes.resources.setDown(e.getKeyCode());
							else if (button.getName().equals("left"))
								UIRes.resources.setLeft(e.getKeyCode());
							else if (button.getName().equals("right"))
								UIRes.resources.setRight(e.getKeyCode());
							else if (button.getName().equals("dash"))
								UIRes.resources.setDash(e.getKeyCode());
							else if (button.getName().equals("block"))
								UIRes.resources.setBlock(e.getKeyCode());
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

			}

			@Override
			public void mouseExited(MouseEvent e) {

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
		if (UIRes.controlsList.contains(string)) {
			JOptionPane.showMessageDialog(new JFrame(),
					"This key is already assigned for another control. Please assign another key!",
					"Key already assigned!", JOptionPane.ERROR_MESSAGE);
			return true;
		} else
			return false;
	}
}
