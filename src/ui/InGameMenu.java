package ui;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class InGameMenu extends BaseMenu {

	public JPanel getInGameMenuPanel(JLayeredPane layeredPane){
		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);
		addResumeToGameButton(layeredPane, panel);
		addOptionsButton(panel);
		addExitButton(panel);
		return panel;
	}
	
}
