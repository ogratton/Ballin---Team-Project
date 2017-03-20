package ui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
		panel.setBorder(new EmptyBorder(50, 50, 50, 50));
		return panel;
	}

	public static void main(String[] args) {
		JFrame frame = createFrame();
		frame.setVisible(true);	
	}

}

