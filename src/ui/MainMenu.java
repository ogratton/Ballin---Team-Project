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
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import audio.AudioFile;
import audio.MusicPlayer;
import graphics.PhysicsWithGraphicsDemo;
import networking.Client;
import networking.Port;
import resources.Resources;

public class MainMenu extends JFrame {

	MainMenu() {
		JFrame frame = new JFrame();
		resources = new Resources();
		mPanel = new JPanel();
		frame.setName("Main Menu");
		frame.setSize(frameSize);
		frame.setLocation((getScreenWidth() - frame.getWidth()) / 2, (getScreenHeight() - frame.getHeight()) / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(false);
		changeState(defaultState);
		frame.add(mPanel);
		frame.setVisible(true);
		musicPlayer = new MusicPlayer(resources, "guile");
		musicPlayer.run();
	}

	private static MusicPlayer musicPlayer;
	private static boolean isPressed;
	private static ViewState defaultState = ViewState.USERNAME_STATE;
	private final static Font font = makeFont(20);
	public static String username;
	private static Dimension frameSize = new Dimension(600, 800);
	private JPanel mainMenuPanel = mainMenuPanel();
	private JPanel usernamePanel = getUsername();
	private JPanel optionsPanel = optionPanel();
	private JPanel mPanel;
	private static ArrayList<String> controlsList;
	private static Resources resources;

	public enum ViewState {
		MAINMENU_STATE, OPTIONS_STATE, USERNAME_STATE;
	}

	public static int getScreenWidth() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}

	public static int getScreenHeight() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}

	private static Font makeFont(int size) {
		Font customFont = new Font("Comic Sans", Font.PLAIN, 14);
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/04b.ttf"))
					.deriveFont((float) size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customFont;
	}

	private void setKeyRebindable(JButton button) {

		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				isPressed = true;
				button.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (isPressed) {
							controlsList.remove(button.getText());
							if (e.getKeyCode() == KeyEvent.VK_UP) {
								if (!checkKey("up arrow".toUpperCase()))
									button.setText("up arrow".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
								if (!checkKey("down arrow".toUpperCase()))
									button.setText("down arrow".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
								if (!checkKey("left arrow".toUpperCase()))
									button.setText("left arrow".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
								if (!checkKey("right arrow".toUpperCase()))
									button.setText("right arrow".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
								if (!checkKey("space".toUpperCase()))
									button.setText("space".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
								if (!checkKey("ctrl".toUpperCase()))
									button.setText("ctrl".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
								if (!checkKey("shift".toUpperCase()))
									button.setText("shift".toUpperCase());
							} else if (e.getKeyCode() == KeyEvent.VK_ALT) {
								if (!checkKey("alt".toUpperCase()))
									button.setText("alt".toUpperCase());
							} else if (((e.getKeyChar() >= 'a') && (e.getKeyChar() <= 'z'))
									|| ((e.getKeyChar() >= '0') && (e.getKeyChar() <= '9'))) {
								if (!checkKey("" + Character.toUpperCase(e.getKeyChar())))
									button.setText("" + Character.toUpperCase(e.getKeyChar()));
							}
							isPressed = false;
							controlsList.add(button.getText());

							if (button.getName().equals("up"))
								resources.setUp(e.getKeyCode());
							else if (button.getName().equals("down"))
								resources.setDown(e.getKeyCode());
							else if (button.getName().equals("left"))
								resources.setLeft(e.getKeyCode());
							else if (button.getName().equals("right"))
								resources.setRight(e.getKeyCode());
							else if (button.getName().equals("dash"))
								resources.setDash(e.getKeyCode());
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

	public boolean checkKey(String string) {
		if (controlsList.contains(string)) {
			JOptionPane.showMessageDialog(mPanel,
					"This key is already assigned for another control. Please assign another key!",
					"Key already assigned!", JOptionPane.ERROR_MESSAGE);
			return true;
		} else
			return false;
	}

	public static void resetButton(JButton button) {
		if (button.getName().equals("up")) {
			resources.setUp(resources.getDefaultUp());
			button.setText(KeyEvent.getKeyText(resources.getDefaultUp()).toUpperCase());
		} else if (button.getName().equals("down")) {
			resources.setDown(resources.getDefaultDown());
			button.setText(KeyEvent.getKeyText(resources.getDefaultDown()).toUpperCase());
		} else if (button.getName().equals("left")) {
			resources.setLeft(resources.getDefaultLeft());
			button.setText(KeyEvent.getKeyText(resources.getDefaultLeft()).toUpperCase());
		} else if (button.getName().equals("right")) {
			resources.setRight(resources.getDefaultRight());
			button.setText(KeyEvent.getKeyText(resources.getDefaultRight()).toUpperCase());
		} else if (button.getName().equals("dash")) {
			resources.setDash(resources.getDefaultDash());
			button.setText(KeyEvent.getKeyText(resources.getDefaultDash()).toUpperCase());
		}
		controlsList.removeAll(controlsList);
		controlsList.add("" + Character.toUpperCase((char) resources.getDefaultUp()));
		controlsList.add("" + Character.toUpperCase((char) resources.getDefaultDown()));
		controlsList.add("" + Character.toUpperCase((char) resources.getDefaultLeft()));
		controlsList.add("" + Character.toUpperCase((char) resources.getDefaultRight()));
		controlsList.add(("" + resources.getDefaultDash()).toUpperCase());
	}

	public void changeState(ViewState viewState) {

		switch (viewState) {
		case MAINMENU_STATE:
			mPanel.removeAll();
			mainMenuPanel.setPreferredSize(frameSize);
			mPanel.add(mainMenuPanel);
			mPanel.revalidate();
			mPanel.repaint();
			break;
		case OPTIONS_STATE:
			mPanel.removeAll();
			mPanel.add(optionsPanel);
			optionsPanel.setPreferredSize(frameSize);
			mPanel.revalidate();
			mPanel.repaint();
			break;
		case USERNAME_STATE:
			mPanel.removeAll();
			mPanel.add(usernamePanel);
			usernamePanel.setPreferredSize(frameSize);
			mPanel.revalidate();
			mPanel.repaint();
			break;
		default:
			System.out.println("UNKNOWN STATE!");
			break;
		}
	}

	public JPanel getUsername() {

		JPanel panel = new JPanel();

		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		JLabel label = new JLabel("Enter your username:");
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		label.setFont(font.deriveFont((float) 25));

		JTextField textField = new JTextField();
		textField.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		textField
				.setMaximumSize(new Dimension((int) (frameSize.getWidth() * 0.8), (int) (frameSize.getHeight() * 0.1)));
		textField.setFont(font);
		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					username = textField.getText();
					textField.setText("");
					AudioFile audioPlayer = new AudioFile(resources, "resources/audio/ding.wav", "Ding");
					audioPlayer.play();
					audioPlayer.setGain(resources.getSFXGain());
					changeState(ViewState.MAINMENU_STATE);
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

		// JButton button = new JButton("Ok");
		// button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		// button.setFont(font);
		// button.setMaximumSize(new Dimension((int) (frameSize.getWidth() *
		// 0.6), (int) (frameSize.getHeight() * 0.1)));
		// button.addActionListener(e -> {
		// username = textField.getText();
		// textField.setText("");
		// AudioFile audioPlayer = new AudioFile(resources,
		// "resources/audio/ding.wav", "Ding");
		// audioPlayer.play();
		// audioPlayer.setGain(resources.getSFXGain());
		// changeState(ViewState.MAINMENU_STATE);
		// });

		panel.add(Box
				.createRigidArea(new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.2))));
		panel.add(label);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.05))));
		panel.add(textField);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		// panel.add(button);
		return panel;
	}

	public JPanel mainMenuPanel() {

		Dimension buttonSize = new Dimension(
				new Dimension((int) (frameSize.getWidth() * 0.8), (int) (frameSize.getHeight() * 0.1)));

		JPanel panel = new JPanel();

		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		JLabel gameTitle = new JLabel("Ballin'");
		gameTitle.setFont(font.deriveFont((float) 28));
		gameTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JButton startButton = new JButton("Start Game");
		startButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		startButton.setMaximumSize(buttonSize);
		startButton.setFont(font);
		startButton.addActionListener(e -> {
			// PhysicsWithGraphicsDemo.main(null);
			Client client = new Client(username, "" + Port.number, "localhost");
			client.start();
			System.out.println("Finished starting Client");
		});

		JButton changeUsername = new JButton("Change Username");
		changeUsername.setAlignmentX(JButton.CENTER_ALIGNMENT);
		changeUsername.setMaximumSize(buttonSize);
		changeUsername.setFont(font);
		changeUsername.addActionListener(e -> {
			changeState(ViewState.USERNAME_STATE);
		});

		JButton optionsButton = new JButton("Options");
		optionsButton.addActionListener(e -> {
			changeState(ViewState.OPTIONS_STATE);
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

		panel.add(Box
				.createRigidArea(new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.1))));
		panel.add(gameTitle);
		panel.add(Box
				.createRigidArea(new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.1))));
		panel.add(startButton);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		panel.add(changeUsername);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		panel.add(optionsButton);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		panel.add(exitButton);

		return panel;

	}

	public JPanel optionPanel() {
		final int VOL_MAX = 100;
		final int VOL_MIN = 0;

		controlsList = new ArrayList<String>();

		JPanel panel = new JPanel();

		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		JButton back = new JButton("Back to Main Menu");
		back.addActionListener(e -> {
			changeState(ViewState.MAINMENU_STATE);
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
		soundSlider.addChangeListener(e -> {
			int volume = soundSlider.getValue();
			if (volume == 0)
				resources.setSFXGain(-80);
			else
				resources.setSFXGain((int) ((VOL_MAX - volume) * (-0.33)));

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
		musicSlider.addChangeListener(e -> {
			int volume = musicSlider.getValue();
			if (volume == 0)
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
		moveUp.setName("up");
		setKeyRebindable(moveUp);
		controlsList.add(moveUp.getText());

		JLabel down = new JLabel("Move down: ");
		down.setFont(font);

		JButton moveDown = new JButton("S");
		moveDown.setFont(font);
		moveDown.setName("down");
		setKeyRebindable(moveDown);
		controlsList.add(moveDown.getText());

		JLabel left = new JLabel("Move left: ");
		left.setFont(font);

		JButton moveLeft = new JButton("A");
		moveLeft.setFont(font);
		moveLeft.setName("left");
		setKeyRebindable(moveLeft);
		controlsList.add(moveLeft.getText());

		JLabel right = new JLabel("Move right: ");
		right.setFont(font);

		JButton moveRight = new JButton("D");
		moveRight.setFont(font);
		moveRight.setName("right");
		setKeyRebindable(moveRight);
		controlsList.add(moveRight.getText());

		JLabel dash = new JLabel("Dash: ");
		dash.setFont(font);

		JButton dashButton = new JButton("SPACE");
		dashButton.setFont(font);
		dashButton.setName("dash");
		setKeyRebindable(dashButton);
		controlsList.add(dashButton.getText());

		JButton resetControls = new JButton("Reset controls to default");
		resetControls.setFont(font);
		resetControls.addActionListener(e -> {
			resetButton(moveUp);
			resetButton(moveDown);
			resetButton(moveLeft);
			resetButton(moveRight);
			resetButton(dashButton);
		});
		resetControls.setAlignmentX(JButton.CENTER_ALIGNMENT);

		controlsPanel.setLayout(controlsGrid);
		controlsPanel.add(up);
		controlsPanel.add(moveUp);
		controlsPanel.add(down);
		controlsPanel.add(moveDown);
		controlsPanel.add(left);
		controlsPanel.add(moveLeft);
		controlsPanel.add(right);
		controlsPanel.add(moveRight);
		controlsPanel.add(dash);
		controlsPanel.add(dashButton);

		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		panel.add(back);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.05))));
		panel.add(soundLabel);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		panel.add(soundSlider);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.05))));
		panel.add(musicLabel);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		panel.add(musicSlider);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.05))));
		panel.add(controlsLabel);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		panel.add(controlsPanel);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));
		panel.add(resetControls);
		panel.add(Box.createRigidArea(
				new Dimension((int) (frameSize.getWidth() * 0), (int) (frameSize.getHeight() * 0.01))));

		return panel;

	}

	public static void main(String args[]) {
		MainMenu menu = new MainMenu();
	}

}
