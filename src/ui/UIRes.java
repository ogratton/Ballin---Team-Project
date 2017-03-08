package ui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import audio.AudioFile;
import audio.MusicPlayer;
import resources.Resources;

public class UIRes {
	public static Resources resources = new Resources();
	public static final int width = 600;
	public static final int height = 800;
	public static String username;
	public static final Dimension buttonSize = new Dimension((int) (width * 0.8), (int) (height * 0.1));
	public static final Dimension labelSize = new Dimension((int) (width * 0.8), (int) (height * 0.09));
	public static MenuItems menuItems = new MenuItems();
	public static String host;
	public static final int VOL_MAX = 100;
	public static final int VOL_MIN = 0;
	public static boolean isPressed;
	public static ArrayList<String> controlsList = new ArrayList<String>();
	public static ArrayList<JButton> buttonsList = new ArrayList<JButton>();
	public static final double buttonRatio = 0.28;
	public static final double labelRatio = 0.32;
	public static final double sliderRatio = 0.25;
	
	public static MusicPlayer musicPlayer = new MusicPlayer(resources, "guile");
	public static AudioFile audioPlayer = new AudioFile(resources, "resources/audio/ding.wav", "Ding");
	
	public static StartMenu start = new StartMenu();
	public static OptionsMenu options = new OptionsMenu();
	
	public static JPanel mainPanel = new JPanel();
	public static JPanel startPanel = start.getStartMenuPanel();
	public static JPanel optionsPanel = options.getOptionsPanel(musicPlayer, audioPlayer, resources);

}
