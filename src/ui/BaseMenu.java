package ui;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import audio.AudioFile;
import audio.MusicPlayer;
import resources.Resources;

public abstract class BaseMenu {
	
	public static int getScreenWidth() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}

	public static int getScreenHeight() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}
	
	 static JFrame createFrame(){
		JFrame frame = new JFrame();
		frame.setSize(UIRes.width, UIRes.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((getScreenWidth() - frame.getWidth()) / 2, (getScreenHeight() - frame.getHeight()) / 2);
		return frame;
	}
	
	JPanel addGameTitle(JPanel panel){
		JLabel titleLabel = UIRes.menuItems.getLabel("Ballin'");
		addSpace(panel, 0, 0.1);
		panel.add(titleLabel);
		return panel;
	}

	JPanel addStartSingleplayerButton(JPanel panel){
		JButton startButton = UIRes.menuItems.getPlaySingleplayerButton();
		addSpace(panel, 0, 0.1);
		panel.add(startButton);
		return panel;
	}
	
	JPanel addStartMultiplayerButton(JPanel panel){
		JButton startButton = UIRes.menuItems.getPlayMultiplayerButton();
		addSpace(panel, 0, 0.02);
		panel.add(startButton);
		return panel;
	}
	
	JPanel addOptionsButton(JPanel panel){
		JButton optionsButton = UIRes.menuItems.getOptionsButton();
		addSpace(panel, 0, 0.02);
		panel.add(optionsButton);
		return panel;
	}

	JPanel addExitButton(JPanel panel){
		JButton exitButton = UIRes.menuItems.getExitButton();
		addSpace(panel, 0, 0.02);
		panel.add(exitButton);
		return panel;
	}
	
	JPanel addSpace(JPanel panel, double widthRatio, double heightRatio){
		panel.add(Box.createRigidArea(new Dimension((int) (UIRes.width * widthRatio), (int) (UIRes.height * heightRatio))));
		return panel;
	}
	
	JPanel addUsernameButton(JPanel panel){
		JLabel usernameLabel = UIRes.menuItems.getLabel("Welcome, Player!");
		JButton usernameButton = UIRes.menuItems.getUsername(usernameLabel);
		panel.add(usernameLabel,0);
		addSpace(panel, 0, 0.02);
		panel.add(usernameButton);
		return panel;
	}
	
	JPanel addMusicSlider(JPanel panel, MusicPlayer musicPlayer){
		JLabel musicLabel = UIRes.menuItems.getLabel("Music Volume");
		JSlider musicSlider = UIRes.menuItems.getMusicSlider(musicPlayer);
		addSpace(panel, 0, 0.05);
		panel.add(musicLabel);
		addSpace(panel, 0, 0.01);
		panel.add(musicSlider);
		return panel;
	}
	 JPanel addAudioSlider(JPanel panel, AudioFile audioPlayer, Resources resources){
		JLabel audioLabel = UIRes.menuItems.getLabel("Audio Volume");
		JSlider audioSlider = UIRes.menuItems.getAudioSlider(audioPlayer, resources);
		addSpace(panel, 0, 0.05);
		panel.add(audioLabel);
		addSpace(panel, 0, 0.01);
		panel.add(audioSlider);
		return panel;
	 }
	 
	 JPanel addControlsPanel(JPanel panel){
		 JPanel controlPanel = UIRes.menuItems.getControlsPanel();
		 JLabel label = UIRes.menuItems.getLabel("Controls");
		 addSpace(panel, 0, 0.05);
		 panel.add(label);
		 addSpace(panel, 0, 0.05);
		 panel.add(controlPanel);
		 return panel;
	 }
	 
	 JPanel addResetControlsButton(JPanel panel){
		 JButton resetButton = UIRes.menuItems.getResetControlsButton();
		 addSpace(panel, 0, 0.01);
		 panel.add(resetButton);
		 return panel;
	 }
	 
	 JPanel addReturnButton(JPanel panel){
		 JButton backButton = UIRes.menuItems.getBackToStartMenuButton();
		 addSpace(panel, 0, 0.01);
		 panel.add(backButton);
		 return panel;
	 }
	
}
