package ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class OptionsMenu extends BaseMenu{

	public JPanel getOptionsPanel(){
		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);
		addReturnButton(panel);
		addMusicSlider(panel);
		addAudioSlider(panel);
		addControlsPanel(panel);
		addResetControlsButton(panel);
		return panel;
	}
}
