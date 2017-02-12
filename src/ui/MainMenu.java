package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import audio.AudioFile;
import audio.MusicPlayer;

public class MainMenu extends JFrame {

	MainMenu() {
		JFrame frame = new JFrame();
		frame.setName("Main Menu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(false);
		frame.setSize(400, 600);
		frame.setLocation((getScreenWorkingWidth() - frame.getWidth()) / 2,
				(getScreenWorkingHeight() - frame.getHeight()) / 2);
		frame.add(mainMenuPanel());
		frame.setVisible(true);
		musicPlayer = new MusicPlayer("pokemon");
		musicPlayer.run();
	}

	static MusicPlayer musicPlayer;
	static AudioFile audioPlayer;
	static boolean isPressed;
	private static ViewState viewState = ViewState.MAINMENU_STATE;
	final static Font font = makeFont(20);

	public enum ViewState {
		MAINMENU_STATE, OPTIONS_STATE;
	}

	public static int getScreenWorkingWidth() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}

	public static int getScreenWorkingHeight() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}

	private static Font makeFont(int size) {
		Font customFont = new Font("Comic Sans MS", Font.PLAIN, 14);
		try {
			// create the font to use. Specify the size!
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources\\fonts\\04b.ttf")).deriveFont((float)size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(customFont);
			//return customFont;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customFont;
	}

	private static void setKeyRebindable(JButton button) {

		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				isPressed = true;
				button.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (isPressed) {
							if (e.getKeyCode() == KeyEvent.VK_UP)
								button.setText("up arrow");
							else if (e.getKeyCode() == KeyEvent.VK_DOWN)
								button.setText("down arrow");
							else if (e.getKeyCode() == KeyEvent.VK_LEFT)
								button.setText("left arrow");
							else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
								button.setText("right arrow");
							else if (((e.getKeyChar() >= 'a') && (e.getKeyChar() <= 'z'))
									|| ((e.getKeyChar() >= '0') && (e.getKeyChar() <= '9')))
								button.setText("" + Character.toUpperCase(e.getKeyChar()));
							isPressed = false;

						}
					}

					@Override
					public void keyReleased(KeyEvent e) {

					}

					@Override
					public void keyTyped(KeyEvent e) {

					}
				});

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

		});
	}

	public static void changeState(JPanel panel) {

		switch (viewState) {
		case MAINMENU_STATE:
			panel.removeAll();
			panel.add(mainMenuPanel());
			panel.revalidate();
			panel.repaint();
			break;
		case OPTIONS_STATE:
			panel.removeAll();
			panel.add(optionPanel());
			panel.revalidate();
			panel.repaint();
			break;
		default:
			System.out.println("UNKNOWN STATE!");
			break;
		}
	}

	public static JPanel mainMenuPanel() {

		Dimension buttonSize = new Dimension(250, 100);

		JPanel panel = new JPanel();

		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		JLabel gameTitle = new JLabel("Insert game name!");
		gameTitle.setFont(font.deriveFont((float) 28));
		gameTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JButton startButton = new JButton("Start Game");
		startButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		startButton.setMaximumSize(buttonSize);
		startButton.setFont(font);

		JButton optionsButton = new JButton("Options");
		optionsButton.addActionListener(e -> {
			viewState = ViewState.OPTIONS_STATE;
			changeState(panel);
		});
		optionsButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		optionsButton.setMaximumSize(buttonSize);
		optionsButton.setFont(font);

		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> {
			System.exit(0);
		});
		exitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		exitButton.setMaximumSize(buttonSize);
		exitButton.setFont(font);

		JPanel empty = new JPanel();
		empty.setSize(500, 100);

		panel.add(Box.createRigidArea(new Dimension(0, 50)));
		panel.add(gameTitle);
		panel.add(Box.createRigidArea(new Dimension(0, 100)));
		panel.add(startButton);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(optionsButton);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(exitButton);

		return panel;

	}

	private static JPanel optionPanel() {
		final int VOL_MAX = 100;
		final int VOL_MIN = 0;

		JPanel panel = new JPanel();

		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		JButton back = new JButton("Back to Main Menu");
		back.addActionListener(e -> {
			viewState = ViewState.MAINMENU_STATE;
			changeState(panel);
		});
		back.setAlignmentX(JButton.CENTER_ALIGNMENT);
		back.setFont(font);

		JLabel soundLabel = new JLabel("Sounds Volume", BoxLayout.X_AXIS);
		soundLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		soundLabel.setFont(font);

		JSlider soundSlider = new JSlider(JSlider.HORIZONTAL, VOL_MIN, VOL_MAX, VOL_MAX);
		soundSlider.setMajorTickSpacing(20);
		soundSlider.setMinorTickSpacing(10);
		soundSlider.setPaintTicks(true);
		soundSlider.setPaintLabels(true);
		soundSlider.setFont(font.deriveFont((float) 16));
		soundSlider.addChangeListener(e ->{
	        int volume = soundSlider.getValue();
	     // TODO: set Resources.sfx_gain instead of this --Oliver
	        if(volume == 0)
	        	audioPlayer.setGain(-80);
	        else
	        	audioPlayer.setGain((float) ((VOL_MAX - volume) * (-0.33)));
		});

		JLabel musicLabel = new JLabel("Music Volume");
		musicLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		musicLabel.setFont(font);

		JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, VOL_MIN, VOL_MAX, VOL_MAX);
		musicSlider.setMajorTickSpacing(20);
		musicSlider.setMinorTickSpacing(10);
		musicSlider.setPaintTicks(true);
		musicSlider.setPaintLabels(true);
		musicSlider.setFont(font.deriveFont((float) 16));
		musicSlider.addChangeListener(e ->{
		        int volume = musicSlider.getValue();
		        if(volume == 0)
		        	musicPlayer.mute();
		        else
		        	musicPlayer.setGain((float) ((VOL_MAX - volume) * (-0.33)));
		});
		
		JLabel controlsLabel = new JLabel("Controls");
		controlsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		controlsLabel.setFont(font);

		JPanel controlsPanel = new JPanel();
		GridLayout controlsGrid = new GridLayout(0, 2);
		controlsPanel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel up = new JLabel("Move up: ");
		up.setFont(font);

		JButton moveUp = new JButton("W");
		moveUp.setFont(font);
		setKeyRebindable(moveUp);

		JLabel down = new JLabel("Move down: ");
		down.setFont(font);

		JButton moveDown = new JButton("S");
		moveDown.setFont(font);
		setKeyRebindable(moveDown);

		JLabel left = new JLabel("Move left: ");
		left.setFont(font);

		JButton moveLeft = new JButton("A");
		moveLeft.setFont(font);
		setKeyRebindable(moveLeft);

		JLabel right = new JLabel("Move right: ");
		right.setFont(font);

		JButton moveRight = new JButton("D");
		moveRight.setFont(font);
		setKeyRebindable(moveRight);

		controlsPanel.setLayout(controlsGrid);
		controlsPanel.add(up);
		controlsPanel.add(moveUp);
		controlsPanel.add(down);
		controlsPanel.add(moveDown);
		controlsPanel.add(left);
		controlsPanel.add(moveLeft);
		controlsPanel.add(right);
		controlsPanel.add(moveRight);

		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(back);
		panel.add(Box.createRigidArea(new Dimension(0, 20)));
		panel.add(soundLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(soundSlider);
		panel.add(Box.createRigidArea(new Dimension(0, 20)));
		panel.add(musicLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(musicSlider);
		panel.add(Box.createRigidArea(new Dimension(0, 20)));
		panel.add(controlsLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(controlsPanel);

		return panel;

	}

	public static void main(String args[]) {
		MainMenu menu = new MainMenu();
	}

}
