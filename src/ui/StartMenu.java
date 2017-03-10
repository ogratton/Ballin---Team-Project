package ui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

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

	public static void main(String[] args) {
		JFrame frame = createFrame();
		UIRes.mainPanel.add(UIRes.startPanel);
		UIRes.startPanel.setPreferredSize(frame.getSize());
		UIRes.startPanel.setOpaque(false);
		frame.add(UIRes.mainPanel);
		frame.setVisible(true);
		UIRes.musicPlayer.run();
	}

}

