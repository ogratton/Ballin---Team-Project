package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import audio.MusicPlayer;
import resources.Resources;

/**
 * Class for the Options Menu panel.
 * 
 * @author Andreea Diana Dinca
 *
 */
@SuppressWarnings("serial")
public class OptionsMenu extends JPanel{
	
	private JPanel backToPanel = UIRes.startPanel;
	private MusicPlayer musicPlayer;
	
	private ArrayList<String> controlsList = new ArrayList<String>();
	private ArrayList<JButton> buttonsList = new ArrayList<JButton>();
	
	private boolean isPressed;

	/**
	 * Constructor of the options panel.
	 * 
	 * @param backToPanel 
	 * 		the panel the user is sent to after clicking the back button
	 * @param musicPlayer 
	 * 		the music player
	 */
	public OptionsMenu(JPanel backToPanel, MusicPlayer musicPlayer){
		this.backToPanel = backToPanel;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel musicLabel = UIRes.getLabel("Music Volume");
		JLabel soundLabel = UIRes.getLabel("Sound Volume");
		JLabel controlsLabel = UIRes.getLabel("Controls");
		add(Box.createVerticalStrut(10));
		add(musicLabel);
		add(Box.createVerticalStrut(3));
		add(getMusicSlider());
		add(Box.createVerticalStrut(10));
		add(soundLabel);
		add(Box.createVerticalStrut(3));
		add(getAudioSlider());
		add(Box.createVerticalStrut(10));
		add(getGraphicsSettings());
		add(Box.createVerticalStrut(10));
		add(controlsLabel);
		add(Box.createVerticalStrut(3));
		add(getControlsPanel());
		add(Box.createVerticalStrut(10));
		UIRes.getButtonAndIcon(this, getResetControlsButton());
		add(Box.createVerticalStrut(3));
		UIRes.getButtonAndIcon(this, new BackButton(this.backToPanel, "Back"));
		this.musicPlayer = musicPlayer;
	}
	
	/**
	 * Creates a slider that controls the volume of the music player.
	 * 
	 * @return 
	 * 		the slider
	 */
	JSlider getMusicSlider() {
		JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, UIRes.VOL_MIN, UIRes.VOL_MAX, UIRes.VOL_INIT);
		UIRes.customiseSlider(musicSlider);
		if (!Resources.silent) {
			musicSlider.addChangeListener(e -> {
				int volume = musicSlider.getValue();
				if (volume == 0)
					musicPlayer.mute();
				else
					musicPlayer.setGain((float) ((UIRes.VOL_MAX - volume) * (-0.33)));
			});
		}

		return musicSlider;
	}

	/**
	 * Creates a slider that controls the volume of the audio sounds.
	 * 
	 * @return 
	 * 		the slider
	 */
	JSlider getAudioSlider() {
		JSlider audioSlider = new JSlider(JSlider.HORIZONTAL, UIRes.VOL_MIN, UIRes.VOL_MAX, UIRes.VOL_INIT);
		UIRes.customiseSlider(audioSlider);
		audioSlider.addChangeListener(e -> {
			int volume = audioSlider.getValue();
			if (volume == 0)
				UIRes.resources.setSFXGain(-80);
			else
				UIRes.resources.setSFXGain((int) ((UIRes.VOL_MAX - volume) * (-0.33)));
		});
		return audioSlider;
	}
	/**
	 * Creates a panel containing a label for the Graphics Settings and a button that controls the Graphics Settings.
	 * 
	 * @return 
	 * 		the panel
	 */
	JPanel getGraphicsSettings(){
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension((int)(UIRes.frameWidth * 0.85), (int)(UIRes.frameHeight * 0.1)));
		panel.setOpaque(false);
		panel.setLayout(new GridLayout(1,0));
		JLabel graphicsQuality = UIRes.getLabel("Graphics Quality");
		JButton qualityButton = new JButton("High Quality");
		UIRes.customiseButton(qualityButton, true);
		qualityButton.addActionListener(e -> {
			if(UIRes.resources.isLowGraphics()){
				UIRes.resources.setLowGraphics(false);
				qualityButton.setText("High Quality");
			}
			else {
				UIRes.resources.setLowGraphics(true);
				qualityButton.setText("Low Quality");
			}
		});
		
		panel.add(graphicsQuality);
		panel.add(qualityButton);
		return panel;
		
	}

	/**
	 * Creates a panel containing the label of the control and a button that changes the binding for the control mentioned in the label.
	 * 
	 * @param buttonLabel 
	 * 		the label of the control
	 * @param buttonName 
	 * 		the name of the button
	 * @param name 
	 * 		the name that connects the button to the control
	 * @return 
	 * 		the panel
	 */
	JPanel getControlButton(String buttonLabel, String buttonName, String name) {
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension((int) (UIRes.frameWidth * 0.85), (int) (UIRes.frameHeight * 0.15)));
		panel.setOpaque(false);
		GridLayout controlsGrid = new GridLayout(0, 2);
		panel.setLayout(controlsGrid);
		panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		JLabel label = UIRes.getLabel(buttonLabel);

		JButton button = new JButton(buttonName);
		UIRes.customiseButton(button, false);
		button.setFocusable(true);
		button.setName(name);
		setKeyRebindable(button);
		controlsList.add(button.getText());
		buttonsList.add(button);

		panel.add(label);
		panel.add(button);
		return panel;
	}

	/**
	 * Creates a panel that contains all the controls.
	 * 
	 * @return 
	 * 		the panel
	 */
	JPanel getControlsPanel() {
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension((int) (UIRes.frameWidth * 0.85), (int) (UIRes.frameHeight * 0.30)));
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);

		panel.add(getControlButton("Move up:", KeyEvent.getKeyText(UIRes.resources.getDefaultUp()).toUpperCase(), "up"));
		panel.add(
				getControlButton("Move down:", KeyEvent.getKeyText(UIRes.resources.getDefaultDown()).toUpperCase(), "down"));
		panel.add(
				getControlButton("Move left:", KeyEvent.getKeyText(UIRes.resources.getDefaultLeft()).toUpperCase(), "left"));
		panel.add(getControlButton("Move right:", KeyEvent.getKeyText(UIRes.resources.getDefaultRight()).toUpperCase(),
				"right"));
		panel.add(getControlButton("Dash:", KeyEvent.getKeyText(UIRes.resources.getDefaultDash()).toUpperCase(), "dash"));

		panel.setOpaque(false);
		return panel;
	}

	/**
	 * Resets a specific button to the default value in the resources object.
	 * 
	 * @param button 
	 * 		the button to be reset
	 */
	void resetButton(JButton button) {
		if (button.getName().equals("up")) {
			UIRes.resources.setUp(UIRes.resources.getDefaultUp());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultUp()).toUpperCase());
		} else if (button.getName().equals("down")) {
			UIRes.resources.setDown(UIRes.resources.getDefaultDown());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultDown()).toUpperCase());
		} else if (button.getName().equals("left")) {
			UIRes.resources.setLeft(UIRes.resources.getDefaultLeft());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultLeft()).toUpperCase());
		} else if (button.getName().equals("right")) {
			UIRes.resources.setRight(UIRes.resources.getDefaultRight());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultRight()).toUpperCase());
		} else if (button.getName().equals("dash")) {
			UIRes.resources.setDash(UIRes.resources.getDefaultDash());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultDash()).toUpperCase());
		} else if (button.getName().equals("block")) {
			UIRes.resources.setBlock(UIRes.resources.getDefaultBlock());
			button.setText(KeyEvent.getKeyText(UIRes.resources.getDefaultBlock()).toUpperCase());
		}
	}

	/**
	 * Creates a button that resets all the controls to their default values that are stores in the resources object.
	 * 
	 * @return 
	 * 		the button
	 */
	JButton getResetControlsButton() {
		JButton resetControlsButton = new JButton("Reset controls");
		UIRes.customiseButton(resetControlsButton, true);
		resetControlsButton.addActionListener(e -> {
			Iterator<JButton> i = buttonsList.iterator();
			while (i.hasNext()) {
				JButton button = i.next();
				resetButton(button);
			}

			controlsList.removeAll(controlsList);
			controlsList.add(("" + UIRes.resources.getDefaultUp()).toUpperCase());
			controlsList.add(("" + UIRes.resources.getDefaultDown()).toUpperCase());
			controlsList.add(("" + UIRes.resources.getDefaultLeft()).toUpperCase());
			controlsList.add(("" + UIRes.resources.getDefaultRight()).toUpperCase());
			controlsList.add(("" + UIRes.resources.getDefaultDash()).toUpperCase());
			controlsList.add(("" + UIRes.resources.getDefaultBlock()).toUpperCase());

		});
		return resetControlsButton;
	}

	/**
	 * Makes a button change its text based on the key the user has pressed after clicking the button. 
	 * It connects the button to the resources object in order to change the control in game.
	 * 
	 * @param button 
	 * 		the button to be changed
	 */
	void setKeyRebindable(JButton button) {

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
								if (!checkKey(("" + (e.getKeyChar())).toUpperCase()))
									button.setText(("" + e.getKeyChar()).toUpperCase());
							}
							isPressed = false;
							controlsList.add(button.getText());

							if (button.getName().equals("up"))
								UIRes.resources.setUp(e.getKeyCode());
							else if (button.getName().equals("down"))
								UIRes.resources.setDown(e.getKeyCode());
							else if (button.getName().equals("left"))
								UIRes.resources.setLeft(e.getKeyCode());
							else if (button.getName().equals("right"))
								UIRes.resources.setRight(e.getKeyCode());
							else if (button.getName().equals("dash"))
								UIRes.resources.setDash(e.getKeyCode());
							else if (button.getName().equals("block"))
								UIRes.resources.setBlock(e.getKeyCode());
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
				button.setForeground(UIRes.getRandomColour());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setForeground(UIRes.fontColour);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

		});
	}

	/**
	 * Checks if a key has already been set to another control button.
	 * @param string
	 * 		the key that is being checked
	 * @return
	 * 		if the key has already been rebound or not
	 */		
	boolean checkKey(String string) {
		if (controlsList.contains(string)) {
			JOptionPane.showMessageDialog(new JFrame(),
					"This key is already assigned for another control. Please assign another key!",
					"Key already assigned!", JOptionPane.ERROR_MESSAGE);
			return true;
		} else
			return false;
	}

	public JPanel getBackToPanel() {
		return backToPanel;
	}

	public void setBackToPanel(JPanel backToPanel) {
		this.backToPanel = backToPanel;
	}
}
