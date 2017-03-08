package ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import audio.AudioFile;
import audio.MusicPlayer;
import resources.Resources;

public class OptionsMenu extends BaseMenu{

	public JPanel getOptionsPanel(MusicPlayer musicPlayer, AudioFile audioPlayer, Resources resources){
		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);
		addReturnButton(panel);
		addMusicSlider(panel, musicPlayer);
		addAudioSlider(panel, audioPlayer, resources);
		addControlsPanel(panel);
		addResetControlsButton(panel);
		return panel;
	}
}
