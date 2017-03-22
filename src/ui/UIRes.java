package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import audio.AudioFile;
import audio.MusicPlayer;
import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;
import graphics.sprites.Sprite.SheetType;
import networking.ConnectionData;
import networking.ConnectionDataModel;
import resources.Character;
import resources.FilePaths;
import resources.Map;
import resources.Resources;

public class UIRes {
	
	public static Resources resources = new Resources();

	public static final int width = getScreenWidth();
	public static final int height = getScreenHeight();

	public static String username = "Player";
	public static final Dimension buttonSize = new Dimension((int) (width * 0.8), (int) (height * 0.10));
	public static final Dimension labelSize = new Dimension((int) (width * 0.8), (int) (height * 0.13));
	public static MenuItems menuItems = new MenuItems();
	public static String host;
	public static final int VOL_MAX = 100;
	public static final int VOL_INIT = 75;
	public static final int VOL_MIN = 0;
	public static boolean isPressed;
	public static ArrayList<String> controlsList = new ArrayList<String>();
	public static ArrayList<JButton> buttonsList = new ArrayList<JButton>();
	public static final double buttonRatio = 0.25;
	public static final double labelRatio = 0.3;
	public static final double sliderRatio = 0.25;
	
	public String lobbyName;
	public String gameModeName;
	public String mapName;

	public static ConnectionData data = new ConnectionData();
	public static ConnectionDataModel cModel = new ConnectionDataModel(data);
	
	public static final Color colour = Color.BLACK;
	
	public static JPanel sessionsPanels = new JPanel();
	public static JPanel playersPanel = new JPanel();
	public static InLobbyMenu lobby;
	
	public static ArrayList<JPanel> sessionPanelsList = new ArrayList<JPanel>();
	
	
	public static int numberIcons = Character.Class.values().length;
	
	public static AudioFile audioPlayer = Resources.silent ? null: new AudioFile(resources, FilePaths.sfx + "ding.wav", "Ding");
	
	public static JPanel mainPanel = new JPanel();
	public static StartMenu startPanel = new StartMenu();
	public static OptionsMenu optionsPanel = new OptionsMenu(startPanel);
	
	
	public static int getScreenWidth()
	{
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}

	public static int getScreenHeight()
	{
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}
	
	static JPanel addSpace(JPanel panel, double widthRatio, double heightRatio)
	{
		panel.add(Box.createRigidArea(new Dimension((int) (width * widthRatio), (int) (height * heightRatio))));
		return panel;
	}
	
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
							new File(System.getProperty("user.dir") + FilePaths.fonts +"04b.TTF"))
					.deriveFont((float) size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
		}
		comp.setFont(customFont);
	}
	
	static void allignToCenter(JComponent comp) {
		comp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
//		comp.setAlignmentY(JComponent.CENTER_ALIGNMENT);
	}

	static Color getRandomColour() {
		int r = 0, g = 0, b = 0;
		SecureRandom rand = new SecureRandom();
		while ((r < 150 && g < 150) || (b < 150 & g < 150) || (r < 150 & b < 150)) {
			r = rand.nextInt(255);
			g = rand.nextInt(255);
			b = rand.nextInt(255);
		}
		Color color = new Color(r, g, b);
		return color;
	}

	static void customiseLabel(JComponent comp) {
		setCustomFont(comp, (int) (labelSize.height * labelRatio));
		allignToCenter(comp);
		comp.setForeground(colour);
	}

	static void customiseLabel(JLabel label, int size) {
		setCustomFont(label, size);
		allignToCenter(label);
		label.setForeground(colour);
	}

	static void customiseComponent(JComponent comp, Dimension size, double ratio) {
		comp.setMaximumSize(size);
		setCustomFont(comp, (int) (size.height * ratio));
		allignToCenter(comp);
		comp.setOpaque(false);
		comp.setForeground(colour);
	}

	public static void customiseButton(JButton button, boolean addHoverEffect) {
		customiseComponent(button, buttonSize, buttonRatio);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setFocusable(false);
		button.setForeground(colour);
		if (addHoverEffect) {
			button.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					button.setForeground(getRandomColour());

				}

				@Override
				public void mouseExited(MouseEvent e) {
					button.setForeground(colour);

				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}

			});
		}
	}

	static void customiseSlider(JSlider slider) {
		customiseComponent(slider, buttonSize, sliderRatio);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}

	static JLabel getLabel(String text) {
		JLabel label = new JLabel(text);
		customiseLabel(label);
		return label;
	}

	static JLabel getLabel(String text, int size) {
		JLabel label = new JLabel(text);
		customiseLabel(label, size);
		return label;
	}

	static JLabel getSpriteLabel(int x) {
		BufferedImage icon = Sprite.getSprite(SheetDeets.CHARACTERS, 0, x,
				50, 50);
		JLabel iconLabel = new JLabel(new ImageIcon(icon));
		return iconLabel;
	}
	
	static ImageIcon getSpriteIcon(int x) {
		BufferedImage icon = Sprite.getSprite(SheetDeets.CHARACTERS, 0, x,
				50, 50);
		ImageIcon spriteIcon = new ImageIcon(icon);
		return spriteIcon;
	} 

	public static JPanel getButtonAndIcon(JPanel panel, JButton button) {
		JPanel buttonPanel = new JPanel();
		int x = new SecureRandom().nextInt(numberIcons);
		buttonPanel.setMaximumSize(buttonSize);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(getSpriteLabel(x));
		buttonPanel.add(button);
		buttonPanel.add(getSpriteLabel(x));
		buttonPanel.setOpaque(false);
		panel.add(buttonPanel);
		return panel;
	}
	
	static JFrame createFrame()
	{
		JFrame frame = new JFrame();
		JLabel map = new JLabel(new ImageIcon(Sprite.createMap(new Map(getScreenWidth(), getScreenHeight(), ""))));
		frame.setResizable(true);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((getScreenWidth() - width) / 2, (getScreenHeight() - height) / 2);
		frame.setLayout(new BorderLayout());
		frame.setContentPane(map);
		frame.setLayout(new FlowLayout());
		frame.setSize(width, height);
		customiseMainPanel(frame);
		frame.add(mainPanel);
		return frame;
	}

	static void customiseMainPanel(JFrame frame)
	{
		customiseAllPanels(frame);
		//mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setOpaque(false);
		mainPanel.add(startPanel);

		if (!Resources.silent)
		{
			MusicPlayer musicPlayer = new MusicPlayer(resources, "grandma");
			resources.setMusicPlayer(musicPlayer);
			musicPlayer.start();
		}

	}

	static void customisePanel(JPanel panel, JFrame frame)
	{
		panel.setOpaque(false);
		panel.setPreferredSize(frame.getSize());

	}

	static void customiseAllPanels(JFrame frame)
	{
		customisePanel(startPanel, frame);
		customisePanel(optionsPanel, frame);
		customisePanel(sessionsPanels , frame);

	}

}
