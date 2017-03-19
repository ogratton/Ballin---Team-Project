package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;

import audio.AudioFile;
import networking.ClientListener;
import networking.ConnectionData;
import networking.ConnectionDataModel;
import networking.NetworkingClient;
import networking.Session;
import resources.Character;
import resources.FilePaths;
import resources.Resources;

public class UIRes {
	
	public static Resources resources = new Resources();
	public static final int width = 800;
	public static final int height = 1000;
	public static String username = "Player";
	public static final Dimension buttonSize = new Dimension((int) (width * 0.8), (int) (height * 0.1));
	public static final Dimension labelSize = new Dimension((int) (width * 0.8), (int) (height * 0.09));
	public static MenuItems menuItems = new MenuItems();
	public static String host;
	public static final int VOL_MAX = 100;
	public static final int VOL_INIT = 75;
	public static final int VOL_MIN = 0;
	public static boolean isPressed;
	public static ArrayList<String> controlsList = new ArrayList<String>();
	public static ArrayList<JButton> buttonsList = new ArrayList<JButton>();
	public static final double buttonRatio = 0.23;
	public static final double labelRatio = 0.3;
	public static final double sliderRatio = 0.25;
	
	public String lobbyName;
	public String gameModeName;
	public String mapName;

	public static ConnectionData data = new ConnectionData();
	public static ConnectionDataModel cModel = new ConnectionDataModel(data);
	public static final Color colour = Color.BLACK;
	
	public static JPanel sessionsPanels = new JPanel();
	
	public static ArrayList<JPanel> sessionPanelsList = new ArrayList<JPanel>();
	public ArrayList<JPanel> inLobbyList = new ArrayList<JPanel>();
	
	public static int numberIcons = Character.Class.values().length - 1;
	
	public static AudioFile audioPlayer = Resources.silent ? null: new AudioFile(resources, FilePaths.sfx + "ding.wav", "Ding");
	
	public static StartMenu start = new StartMenu();
	public static OptionsMenu options = new OptionsMenu();
	
	public static JPanel mainPanel = new JPanel();
	public static JPanel startPanel = start.getStartMenuPanel();
	public static JPanel optionsPanel = options.getOptionsPanel();
	
	public static void switchPanel(JPanel newPanel) {
		mainPanel.removeAll();
		mainPanel.add(newPanel);
		newPanel.setPreferredSize(mainPanel.getSize());
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public static void setCustomFont(JComponent comp, int size) {
		Font customFont = new Font("Comic Sans", Font.PLAIN, 14);
		try {
			customFont = Font
					.createFont(Font.TRUETYPE_FONT,
							new File(System.getProperty("user.dir") + "/resources/fonts/04b.TTF"))
					.deriveFont((float) size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
		}
		comp.setFont(customFont);
	}

}
