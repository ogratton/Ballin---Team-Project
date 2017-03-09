package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import gamemodes.PlayGame;
import networking.Client;
import networking.Port;

public class MenuItems extends UIRes{
	
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
		setCustomFont(comp, (int)(labelSize.height * labelRatio));
		allignToCenter(comp);
	}
	
	void customiseComponent(JComponent comp, Dimension size, double ratio){
		comp.setMaximumSize(size);
		setCustomFont(comp, (int)(size.height * ratio));
		allignToCenter(comp);
	}
	
	void customiseButton(JButton button){
		customiseComponent(button, buttonSize, buttonRatio);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setFocusable(false);
		button.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setForeground(Color.BLUE);
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setForeground(Color.BLACK);
				
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
	
	void customiseSlider(JSlider slider){
		customiseComponent(slider, buttonSize, sliderRatio);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}
	
	void switchPanel(JPanel newPanel){
		mainPanel.removeAll();
		mainPanel.add(newPanel);
		newPanel.setPreferredSize(mainPanel.getSize());
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	JLabel getLabel(String text){
		JLabel label = new JLabel(text);
		customiseLabel(label);
		return label;
	}
	
	JLabel getRandomSpriteIcon(){
		BufferedImage icon = iconList[new Random().nextInt(iconList.length)];
		JLabel iconLabel = new JLabel(new ImageIcon(icon));
		return iconLabel;
	}
	
	JPanel getButtonAndIcon(JPanel panel, JButton button){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setMaximumSize(buttonSize);
		BorderLayout border = new BorderLayout();
		buttonPanel.setLayout(border);
		buttonPanel.add(getRandomSpriteIcon(), BorderLayout.LINE_START);
		buttonPanel.add(button, BorderLayout.CENTER);
		buttonPanel.add(getRandomSpriteIcon(), BorderLayout.LINE_END);
		panel.add(buttonPanel);
		return panel;
	}
	
	JButton getPlaySingleplayerButton(){
		JButton startButton = new JButton("Start Singleplayer Game");
		customiseButton(startButton);
		startButton.addActionListener(e -> {
			PlayGame.start(resources);
			// button sound effect
			audioPlayer.play();
			// XXX change the song
			resources.getMusicPlayer().changePlaylist("thirty");
			resources.getMusicPlayer().resumeMusic();
		});
		return startButton;
	}
	
	JButton getPlayMultiplayerButton(){
		JButton startButton = new JButton("Start Multiplayer Game");
		customiseButton(startButton);
		startButton.addActionListener(e -> {
			JFrame frame = new JFrame();
			String input = (String)JOptionPane.showInputDialog(frame, "Enter the server name:", "Input server", JOptionPane.PLAIN_MESSAGE);
			if (input != null){
				connectToServer(username, "" + Port.number, host);				
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
		customiseButton(button);
		button.addActionListener(e -> {
			switchPanel(startPanel);
		});
		return button;
	}
	
	JButton getOptionsButton(){
		JButton optionsButton = new JButton("Options");
		customiseButton(optionsButton);
		optionsButton.addActionListener(e -> {
			switchPanel(optionsPanel);
		});
		return optionsButton;
	}
	
	JButton getExitButton(){
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> {
			System.exit(0);
		});
		customiseButton(exitButton);
		return exitButton;
	}
	
	JButton getUsername(JLabel label){
		JButton usernameButton = new JButton("Change username");
		customiseButton(usernameButton);		
		usernameButton.addActionListener(e ->{
			JFrame frame = new JFrame();
			String input = (String)JOptionPane.showInputDialog(frame, "Enter your username:", "Input username",JOptionPane.PLAIN_MESSAGE);
			if(input != null){
				username = input;
				label.setText("Welcome, " + username + "!");
			}
			else{
				frame.dispose();
			}
		});
		return usernameButton;
	}
	
	JSlider getMusicSlider(){
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
	
	JSlider getAudioSlider(){
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
	
	JPanel getControlButton(String buttonLabel, String buttonName, String name){
		JPanel panel = new JPanel();	
		GridLayout controlsGrid = new GridLayout(0, 2);
		panel.setLayout(controlsGrid);
		panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
		JLabel label = getLabel(buttonLabel);
		
		JButton button = new JButton(buttonName);
		customiseButton(button);
		button.setName(name);
		setKeyRebindable(button);
		controlsList.add(button.getText());
		buttonsList.add(button);
		
		panel.add(label);
		panel.add(button);
		return panel;
	}
	
	JPanel getControlsPanel(){
		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		panel.add(getControlButton("Move up:", KeyEvent.getKeyText(resources.getDefaultUp()).toUpperCase(), "up"));
		panel.add(getControlButton("Move down:", KeyEvent.getKeyText(resources.getDefaultDown()).toUpperCase(), "down"));
		panel.add(getControlButton("Move left:", KeyEvent.getKeyText(resources.getDefaultLeft()).toUpperCase(), "left"));
		panel.add(getControlButton("Move right:", KeyEvent.getKeyText(resources.getDefaultRight()).toUpperCase(), "right"));
		panel.add(getControlButton("Dash:", KeyEvent.getKeyText(resources.getDefaultDash()).toUpperCase(), "dash"));
		panel.add(getControlButton("Block:", KeyEvent.getKeyText(resources.getDefaultBlock()).toUpperCase(), "block"));
		
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
	
	JButton getResetControlsButton(){
		JButton resetControlsButton = new JButton("Reset controls");
		customiseButton(resetControlsButton);
		resetControlsButton.addActionListener(e -> {
			Iterator<JButton> i = buttonsList.iterator();
			while(i.hasNext()){
				JButton button = i.next();
				resetButton(button);
			}
		
			controlsList.removeAll(controlsList);
			controlsList.add("" + Character.toUpperCase((char) resources.getDefaultUp()));
			controlsList.add("" + Character.toUpperCase((char) resources.getDefaultDown()));
			controlsList.add("" + Character.toUpperCase((char) resources.getDefaultLeft()));
			controlsList.add("" + Character.toUpperCase((char) resources.getDefaultRight()));
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
								if (!checkKey("" + Character.toUpperCase(e.getKeyChar())))
									button.setText("" + Character.toUpperCase(e.getKeyChar()));
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
		if (controlsList.contains(string)) {
			JOptionPane.showMessageDialog(new JFrame(),
					"This key is already assigned for another control. Please assign another key!",
					"Key already assigned!", JOptionPane.ERROR_MESSAGE);
			return true;
		} else
			return false;
	}
}
