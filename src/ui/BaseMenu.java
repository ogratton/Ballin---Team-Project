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

import com.esotericsoftware.kryonet.Client;

import audio.MusicPlayer;
import graphics.sprites.Sprite;
import networking.ConnectionDataModel;
import resources.Map;
import resources.Resources;

public abstract class BaseMenu extends MenuItems
{

	

	JPanel addSpriteIcon(JPanel panel, int x)
	{
		panel.add(getSpriteLabel(x));
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
	 
	 JPanel addReturnButton(JPanel panel, JPanel backToPanel){
		 JButton backButton = goBack(backToPanel);
		 getButtonAndIcon(panel,backButton);
		 return panel;
	 }
	 
	 JPanel addResumeToGameButton(JPanel panel, JPanel panel2){
		 JButton resumeButton = getResumeToGameButton(panel2);
		 getButtonAndIcon(panel, resumeButton);
		 return panel;
	 }
}
