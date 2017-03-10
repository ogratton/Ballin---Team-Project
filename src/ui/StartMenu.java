package ui;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import audio.MusicPlayer;

public class StartMenu extends BaseMenu{
	
	public JPanel getStartMenuPanel(){
		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);
		addGameTitle(panel);
		addStartSingleplayerButton(panel);
		addStartMultiplayerButton(panel);
		addUsernameButton(panel);
		addOptionsButton(panel);
		addExitButton(panel);
		return panel;
	}

	public static void main(String args[]) {
		JFrame frame = createFrame();
		customiseMainPanel(frame);
		frame.add(mainPanel);
		frame.setVisible(true);	
	}

}

