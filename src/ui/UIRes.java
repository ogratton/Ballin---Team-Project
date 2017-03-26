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
import graphics.sprites.SheetDetails;
import graphics.sprites.Sprite;
import networking.ConnectionData;
import networking.ConnectionDataModel;
import resources.Character;
import resources.FilePaths;
import resources.Map;
import resources.Resources;

/**
 * Class containing variables and methods needed in other classes.
 * 
 * @author Andreea Diana Dinca
 *
 */
public class UIRes {

	public static Resources resources = new Resources();

	public static final int frameWidth = getScreenWidth();
	public static final int frameHeight = getScreenHeight();

	public static String username = "Player";
	public static final Dimension buttonSize = new Dimension((int) (frameWidth * 0.8), (int) (frameHeight * 0.10));
	public static final Dimension labelSize = new Dimension((int) (frameWidth * 0.8), (int) (frameHeight * 0.13));

	public static final int VOL_MAX = 100;
	public static final int VOL_INIT = 75;
	public static final int VOL_MIN = 0;

	public static final double buttonRatio = 0.25;
	public static final double labelRatio = 0.3;
	public static final double sliderRatio = 0.25;

	public static MusicPlayer musicPlayer;

	public static ConnectionData data = new ConnectionData();
	public static ConnectionDataModel cModel = new ConnectionDataModel(data);

	public static final Color fontColour = Color.BLACK;

	public static JPanel sessionsPanels = new JPanel();
	public static JPanel playersPanel = new JPanel();
	public static InLobbyMenu lobby;

	public static ArrayList<JPanel> sessionPanelsList = new ArrayList<JPanel>();

	/**
	 * Just reset everything that might have changed
	 */
	public static void reset() {
		resources = new Resources();
	}

	/**
	 * Just reset everything that might have changed
	 */
	public static void fullReset() {
		resources = new Resources();
		data = new ConnectionData();
		cModel = new ConnectionDataModel(data);
	}

	public static int numberIcons = Character.Class.values().length;

	// we need to make the audio statically because we can only make like 16
	// LineListeners before the sound card cries or something
	public static AudioFile dingSound = Resources.silent ? null : new AudioFile(FilePaths.sfx + "ding.wav", "Ding");
	public static AudioFile deathSound1 = Resources.silent ? null
			: new AudioFile(FilePaths.sfx + "death1.wav", "Death1");
	public static AudioFile deathSound2 = Resources.silent ? null
			: new AudioFile(FilePaths.sfx + "death2.wav", "Death2");
	public static AudioFile deathSound3 = Resources.silent ? null
			: new AudioFile(FilePaths.sfx + "death3.wav", "Death3");
	public static AudioFile playerOut = Resources.silent ? null
			: new AudioFile(FilePaths.sfx + "playerOut.wav", "PlayerOut");
	public static AudioFile explode = Resources.silent ? null : new AudioFile(FilePaths.sfx + "explode.wav", "Explode");

	public static JPanel mainPanel = new JPanel();
	public static StartMenu startPanel = new StartMenu();
	public static OptionsMenu optionsPanel = new OptionsMenu(startPanel, musicPlayer);

	/**
	 * Gets the width of the screen.
	 * 
	 * @return
	 * 		the width of the screen
	 */
	public static int getScreenWidth() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}

	/**
	 * Gets the height of the screen.
	 * 
	 * @return
	 * 		the height of the screen
	 */
	public static int getScreenHeight() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}

	/**
	 * Adds space to a panel.
	 * 
	 * @param panel
	 * 		the panel to be added space to
	 * @param widthRatio
	 * 		the ratio of the width of the space
	 * @param heightRatio
	 * 		the ratio of the height of the space
	 * @return
	 * 		the panel with the space
	 */
	public static JPanel addSpace(JPanel panel, double widthRatio, double heightRatio) {
		panel.add(
				Box.createRigidArea(new Dimension((int) (frameWidth * widthRatio), (int) (frameHeight * heightRatio))));
		return panel;
	}

	/**
	 * Switches the current panel to a new one.
	 * 
	 * @param newPanel
	 * 		the panel to be switched to
	 */
	public static void switchPanel(JPanel newPanel) {
		mainPanel.removeAll();
		mainPanel.add(newPanel);
		newPanel.setPreferredSize(mainPanel.getSize());
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	/**
	 * Installs the custom font and sets it to a given component with a given size.
	 * 
	 * @param comp
	 * 		the component to set the font to
	 * @param size
	 * 		the size of the font
	 */
	public static void setCustomFont(JComponent comp, int size) {
		Font customFont = new Font("Comic Sans", Font.PLAIN, 14);
		try {
			customFont = Font
					.createFont(Font.TRUETYPE_FONT,
							new File(FilePaths.fonts +"04b.TTF"))
					.deriveFont((float) size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
		}
		comp.setFont(customFont);
	}

	/**
	 * Gets a random bright colour.
	 * 
	 * @return
	 * 		a random bright colour.
	 */
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

	/**
	 * Customises the label with particular settings.
	 * 
	 * @param label
	 * 		the label
	 */
	static void customiseLabel(JLabel label) {
		label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		setCustomFont(label, (int) (labelSize.height * labelRatio));
		label.setForeground(fontColour);
	}

	/**
	 * Customises the label with particular settings.
	 * 
	 * @param label
	 * 		the label
	 * @param size
	 * 		the size of the font for the label
	 */
	static void customiseLabel(JLabel label, int size) {
		label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		setCustomFont(label, size);
		label.setForeground(fontColour);
	}

	/**
	 * Customises a component with particular settings.
	 * 
	 * @param comp
	 * 		the component to be customised
	 * @param size
	 * 		the size of the component
	 * @param ratio
	 * 		the ratio of the size of the component for the font
	 */
	static void customiseComponent(JComponent comp, Dimension size, double ratio) {
		comp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		comp.setMaximumSize(size);
		setCustomFont(comp, (int) (size.height * ratio));
		comp.setOpaque(false);
		comp.setForeground(fontColour);
	}

	/**
	 * Customises a button with particular settings.
	 * @param button
	 * 		the button to be customised
	 * @param addHoverEffect
	 * 		if this is true then the button will change its colour when hovered-over with the mouse.
	 */
	public static void customiseButton(JButton button, boolean addHoverEffect) {
		customiseComponent(button, buttonSize, buttonRatio);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setFocusable(false);
		button.setForeground(fontColour);
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
					button.setForeground(fontColour);

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

	/**
	 * Customises the slider with particular settings.
	 * 
	 * @param slider
	 * 		the slider
	 */
	static void customiseSlider(JSlider slider) {
		customiseComponent(slider, buttonSize, sliderRatio);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}

	/**
	 * Gets a customised label.
	 * 
	 * @param text
	 * 		the text of the label
	 * @return
	 * 		the label
	 */
	static JLabel getLabel(String text) {
		JLabel label = new JLabel(text);
		customiseLabel(label);
		return label;
	}

	/**
	 * Gets a customised label.
	 * 
	 * @param text
	 * 		the text of the label
	 * @param size
	 * 		the size of the label
	 * @return
	 * 		the label
	 */
	static JLabel getLabel(String text, int size) {
		JLabel label = new JLabel(text);
		customiseLabel(label, size);
		return label;
	}

	/**
	 * Gets the sprite icon as a label.
	 * @param x
	 * 		the index of the sprite
	 * @return
	 * 		the label
	 */
	static JLabel getSpriteLabel(int x) {
		BufferedImage icon = Sprite.getSprite(SheetDetails.CHARACTERS, 0, x, 50, 50);
		JLabel iconLabel = new JLabel(new ImageIcon(icon));
		return iconLabel;
	}

	/**
	 * Gets the sprite icon.
	 * @param x
	 * 		the index of the sprite
	 * @return
	 * 		the icon
	 */
	static ImageIcon getSpriteIcon(int x) {
		BufferedImage icon = Sprite.getSprite(SheetDetails.CHARACTERS, 0, x, 50, 50);
		ImageIcon spriteIcon = new ImageIcon(icon);
		return spriteIcon;
	}

	/**
	 * Creates a panel containing a button and sprite icons on each side of the button.
	 * 
	 * @param panel
	 * 		the panel the button and icons are added to
	 * @param button
	 * 		the button that the icons are added to
	 * @return
	 * 		the panel after the button and icons are added to it
	 */
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

	/*
	 * Customises the frame with particular settings
	 */
	static JFrame createFrame() {
		JFrame frame = new JFrame();
		JLabel map = new JLabel(new ImageIcon(Sprite.createMap(new Map(getScreenWidth(), getScreenHeight(), ""))));
		frame.setResizable(true);
		// frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((getScreenWidth() - frameWidth) / 2, (getScreenHeight() - frameHeight) / 2);
		frame.setLayout(new BorderLayout());
		frame.setContentPane(map);
		frame.setLayout(new FlowLayout());
		frame.setSize(frameWidth, frameHeight);
		customiseMainPanel(frame);
		frame.add(mainPanel);
		return frame;
	}

	/*
	 * Customises the panel thats on the frame and the panel that contains all the other panels.
	 */
	static void customiseMainPanel(JFrame frame) {
		customiseAllPanels(frame);
		mainPanel.setOpaque(false);
		mainPanel.add(startPanel);

	}

	/**
	 * Customises a panel with particular settings
	 * 
	 * @param panel
	 * 		the panel to be customised
	 * @param frame
	 * 		the frame
	 */
	static void customisePanel(JPanel panel, JFrame frame) {
		panel.setOpaque(false);
		panel.setPreferredSize(frame.getSize());

	}

	/**
	 * Customises all the panels.
	 * 
	 * @param frame
	 * 		the frame
	 */
	static void customiseAllPanels(JFrame frame) {
		customisePanel(startPanel, frame);
		customisePanel(optionsPanel, frame);
		customisePanel(sessionsPanels, frame);
	}

}
