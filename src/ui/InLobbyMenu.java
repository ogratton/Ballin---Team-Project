package ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class InLobbyMenu extends BaseMenu{
	
	public JPanel getInLobbyMenu(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		return panel;
	}

}
