package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import audio.MusicPlayer;
import graphics.sprites.Sprite;
import resources.Map;
import resources.Resources;

public abstract class BaseMenu extends MenuItems
{

	public static int getScreenWidth()
	{
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}

	public static int getScreenHeight()
	{
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}

	static JFrame createFrame()
	{
		JFrame frame = new JFrame();
		JLabel map = new JLabel(new ImageIcon(Sprite.createMap(new Map(width, height, ""))));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((getScreenWidth() - width) / 2, (getScreenHeight() - height) / 2);
		frame.setLayout(new BorderLayout());
		frame.setContentPane(map);
		frame.setLayout(new FlowLayout());
		frame.setSize(width, height);
		customiseMainPanel(frame);
		frame.add(mainPanel);
		return frame;
	}

	static void customiseMainPanel(JFrame frame)
	{
		customiseAllPanels(frame);
		mainPanel.setOpaque(false);
		mainPanel.add(startPanel);

		if (!Resources.silent)
		{
			MusicPlayer musicPlayer = new MusicPlayer(resources, "grandma");
			resources.setMusicPlayer(musicPlayer);
			musicPlayer.start();
		}

	}

	static void customisePanel(JPanel panel, JFrame frame)
	{
		panel.setOpaque(false);
		panel.setPreferredSize(frame.getSize());

	}

	static void customiseAllPanels(JFrame frame)
	{
		customisePanel(startPanel, frame);
		customisePanel(optionsPanel, frame);

	}

	JPanel addSpriteIcon(JPanel panel, int x)
	{
		panel.add(getSpriteIcon(x));
		return panel;
	}

	JPanel addGameTitle(JPanel panel)
	{
		JLabel titleLabel = getLabel("Ballin'");
		addSpace(panel, 0, 0.1);
		panel.add(titleLabel);
		return panel;
	}

	JPanel addStartSingleplayerButton(JPanel panel)
	{
		JButton startButton = getPlaySingleplayerButton();
		addSpace(panel, 0, 0.1);
		getButtonAndIcon(panel, startButton);
		return panel;
	}

	JPanel addStartMultiplayerButton(JPanel panel)
	{
		JButton startButton = getPlayMultiplayerButton();
		addSpace(panel, 0, 0.02);
		getButtonAndIcon(panel, startButton);
		return panel;
	}

	JPanel addOptionsButton(JPanel panel)
	{
		JButton optionsButton = getOptionsButton();
		addSpace(panel, 0, 0.02);
		getButtonAndIcon(panel, optionsButton);
		return panel;
	}

	JPanel addExitButton(JPanel panel)
	{
		JButton exitButton = getExitButton();
		addSpace(panel, 0, 0.02);
		getButtonAndIcon(panel, exitButton);
		return panel;
	}

	JPanel addSpace(JPanel panel, double widthRatio, double heightRatio)
	{
		panel.add(Box.createRigidArea(new Dimension((int) (width * widthRatio), (int) (height * heightRatio))));
		return panel;
	}

	JPanel addUsernameButton(JPanel panel)
	{
		JLabel usernameLabel = getLabel("Welcome, Player!");
		JButton usernameButton = getUsername(usernameLabel);
		panel.add(usernameLabel, 0);
		addSpace(panel, 0, 0.02);
		getButtonAndIcon(panel, usernameButton);
		return panel;
	}

	JPanel addMusicSlider(JPanel panel)
	{
		JLabel musicLabel = getLabel("Music Volume");
		JSlider musicSlider = getMusicSlider();
		addSpace(panel, 0, 0.03);
		panel.add(musicLabel);
		addSpace(panel, 0, 0.01);
		panel.add(musicSlider);
		return panel;
	}

	JPanel addAudioSlider(JPanel panel)
	{
		JLabel audioLabel = getLabel("Audio Volume");
		JSlider audioSlider = getAudioSlider();
		addSpace(panel, 0, 0.03);
		panel.add(audioLabel);
		addSpace(panel, 0, 0.01);
		panel.add(audioSlider);
		return panel;
	 }
	 
	 JPanel addControlsPanel(JPanel panel){
		 JPanel controlPanel = getControlsPanel();
		 JLabel label = getLabel("Controls");
		 addSpace(panel, 0, 0.03);
		 panel.add(label);
		 addSpace(panel, 0, 0.01);
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
		 getButtonAndIcon(panel,backButton);
		 return panel;
	 }
	 
	 JPanel addResumeToGameButton(JLayeredPane layeredPane, JPanel panel){
		 JButton resumeButton = getResumeToGameButton(layeredPane, panel);
		 getButtonAndIcon(panel, resumeButton);
		 return panel;
	 }
	 
//	 JPanel addLobbyListButtons(JPanel panel, JTable table, ConnectionDataModel cdmodel, ObjectOutputStream toServer){
//		 JPanel buttonsPanel = new JPanel();
//		 JButton joinLobby = joinSessionButton(cdmodel, toServer);
//		 JButton createLobby = createSessionButton(table, cdmodel, toServer);
//		 JButton refreshLobby = refreshSessionList(table, cdmodel, toServer);
//		 buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
//		 getButtonAndIcon(buttonsPanel, joinLobby);
//		 getButtonAndIcon(buttonsPanel, createLobby);
//		 getButtonAndIcon(buttonsPanel, refreshLobby);
//		 panel.add(buttonsPanel);
//		 return panel;
//	 }
}
