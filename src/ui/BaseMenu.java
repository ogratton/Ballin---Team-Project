package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import audio.MusicPlayer;

public abstract class BaseMenu extends MenuItems{
	
	public static int getScreenWidth() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}

	public static int getScreenHeight() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}
	
	static JFrame createFrame(){
		JFrame frame = new JFrame();
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((getScreenWidth() - frame.getWidth()) / 2, (getScreenHeight() - frame.getHeight()) / 2);
		return frame;
	}
	
	static void customiseMainPanel(JFrame frame){
		mainPanel.add(startPanel);
		startPanel.setPreferredSize(frame.getSize());
		MusicPlayer musicPlayer = new MusicPlayer(resources, "guile");
		resources.setMusicPlayer(musicPlayer);
		musicPlayer.start();
	}
	
	static void customisePanels(){
		
	}
	
	JPanel addSpriteIcon(JPanel panel, int x){
		panel.add(getSpriteIcon(x));
		return panel;
	}
	
	JPanel addGameTitle(JPanel panel){
		JLabel titleLabel = getLabel("Ballin'");
		addSpace(panel, 0, 0.1);
		panel.add(titleLabel);
		return panel;
	}

	JPanel addStartSingleplayerButton(JPanel panel){
		JButton startButton = getPlaySingleplayerButton();
		addSpace(panel, 0, 0.1);
		getButtonAndIcon(panel,startButton);
		return panel;
	}
	
	JPanel addStartMultiplayerButton(JPanel panel){
		JButton startButton = getPlayMultiplayerButton();
		addSpace(panel, 0, 0.02);
		getButtonAndIcon(panel,startButton);
		return panel;
	}
	
	JPanel addOptionsButton(JPanel panel){
		JButton optionsButton = getOptionsButton();
		addSpace(panel, 0, 0.02);
		getButtonAndIcon(panel,optionsButton);
		return panel;
	}

	JPanel addExitButton(JPanel panel){
		JButton exitButton = getExitButton();
		addSpace(panel, 0, 0.02);
		getButtonAndIcon(panel,exitButton);
		return panel;
	}
	
	JPanel addSpace(JPanel panel, double widthRatio, double heightRatio){
		panel.add(Box.createRigidArea(new Dimension((int) (width * widthRatio), (int) (height * heightRatio))));
		return panel;
	}
	
	JPanel addUsernameButton(JPanel panel){
		JLabel usernameLabel = getLabel("Welcome, Player!");
		JButton usernameButton = getUsername(usernameLabel);
		panel.add(usernameLabel,0);
		addSpace(panel, 0, 0.02);
		getButtonAndIcon(panel,usernameButton);
		return panel;
	}
	
	JPanel addMusicSlider(JPanel panel){
		JLabel musicLabel = getLabel("Music Volume");
		JSlider musicSlider = getMusicSlider();
		addSpace(panel, 0, 0.05);
		panel.add(musicLabel);
		addSpace(panel, 0, 0.01);
		panel.add(musicSlider);
		return panel;
	}
	 JPanel addAudioSlider(JPanel panel){
		JLabel audioLabel = getLabel("Audio Volume");
		JSlider audioSlider = getAudioSlider();
		addSpace(panel, 0, 0.05);
		panel.add(audioLabel);
		addSpace(panel, 0, 0.01);
		panel.add(audioSlider);
		return panel;
	 }
	 
	 JPanel addControlsPanel(JPanel panel){
		 JPanel controlPanel = getControlsPanel();
		 JLabel label = getLabel("Controls");
		 addSpace(panel, 0, 0.05);
		 panel.add(label);
		 addSpace(panel, 0, 0.02);
		 panel.add(controlPanel);
		 return panel;
	 }
	 
	 JPanel addResetControlsButton(JPanel panel){
		 JButton resetButton = getResetControlsButton();
		 addSpace(panel, 0, 0.01);
		 getButtonAndIcon(panel,resetButton);
		 addSpace(panel, 0, 0.01);
		 return panel;
	 }
	 
	 JPanel addReturnButton(JPanel panel){
		 JButton backButton = getBackToStartMenuButton();
		 addSpace(panel, 0, 0.01);
		 getButtonAndIcon(panel,backButton);
		 return panel;
	 }
	
}
