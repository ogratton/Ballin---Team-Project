package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import networking.Updater;
import resources.Character;
import resources.Map;
import resources.Map.World;
import resources.Resources;

/**
 * Contains everything on the screen that the player can see
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class GameComponent extends JFrame implements ActionListener {

	private ArrayList<Character> characters;
	private Timer timer;
	private GameView view;
	private JLabel label;
	private Resources resources;
	private int firstPlayerIndex = 0;
	private int secondPlayerIndex = 1;

	private boolean fullScreen = false;
	private boolean debugPaths = false;

	int width, height;
	int oldValueX, newValueX, oldValueY, newValueY;

	int labelX, labelY;
	int panelX, panelY;
	
	/**
	 * Create a new game component (which comprises everything the player can
	 * see!)
	 * 
	 * @param characters
	 *            an ArrayList of characters on the board
	 * @param map
	 *            the map the board is displaying
	 */

	public GameComponent(Resources resources, int width, int height, Updater updater, boolean debugPaths) {

		this.debugPaths = debugPaths;

		setLayout(new BorderLayout());

		// This code block below is just for testing!

		addKeyListener(new TAdapter());
		setFocusable(true);
		timer = new Timer(10, this);
		timer.start();

		// End test code block
		this.resources = resources;

		characters = resources.getPlayerList();

		this.width = width;
		this.height = height;

		view = new GameView(resources, debugPaths);
		
		for (Character model : resources.getPlayerList()) {
			model.addObserver(view);
		}

		if(resources != null) {
			for (int i = 0; i < characters.size(); i++) {
				if (characters.get(i).getId() != null && characters.get(i).getId().equals(resources.getId())) {
					secondPlayerIndex = i;
					break;
				}
			}
		}
		
		add(view, BorderLayout.CENTER);
		
		label = new JLabel("This is where the scores n shiz go");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Verdana", Font.PLAIN, 48));
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setOpaque(true);
		
		Dimension dim = label.getPreferredSize();
		labelX = (int)dim.getWidth();
		labelY = (int)dim.getHeight();
		
		add(label, BorderLayout.NORTH);
		
		pack();
		
		Dimension panel = this.getPreferredSize();
		panelX = (int)panel.getWidth();
		panelY = (int)panel.getHeight();
		
		setVisible(true);
	}

	// All code below here is for testing

	/**
	 * Testing keyboard inputs
	 */

	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	/**
	 * Switch between fullscreen and windowed
	 */

	public void toggleFullscreen() {

		dispose();
		
		if (fullScreen) {

			setUndecorated(false);
			
			view.setFullScreen(false);
			pack();
			setLocationRelativeTo(null);
			
			fullScreen = false;

		} else {

			setUndecorated(true);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
			
			setLocation(0, 0);
			view.setFullScreen(true);
			pack();
			
			fullScreen = true;

		}
		
		setFocusable(true);
		requestFocus();
		setVisible(true);
		
	}

	public void cycleWorld() {

		World world = resources.getMap().getWorldType();
		World newWorld = null;

		switch (world) {
		case CAVE:
			newWorld = Map.World.SPACE;
			break;
		case SPACE:
			newWorld = Map.World.LAVA;
			break;
		case LAVA:
			newWorld = Map.World.CAVE;
			break;
		}
		
		resources.getMap().setWorldType(newWorld);
		view.makeMap();
		view.setFullScreen(fullScreen);
		

	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_A:
				characters.get(firstPlayerIndex).setLeft(false);
				break;
			case KeyEvent.VK_D:
				characters.get(firstPlayerIndex).setRight(false);
				break;
			case KeyEvent.VK_W:
				characters.get(firstPlayerIndex).setUp(false);
				break;
			case KeyEvent.VK_S:
				characters.get(firstPlayerIndex).setDown(false);
				break;
			case KeyEvent.VK_UP:
				characters.get(secondPlayerIndex).setUp(false);
				break;
			case KeyEvent.VK_DOWN:
				characters.get(secondPlayerIndex).setDown(false);
				break;
			case KeyEvent.VK_LEFT:
				characters.get(secondPlayerIndex).setLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				characters.get(secondPlayerIndex).setRight(false);
				break;
			case KeyEvent.VK_ENTER:
				toggleFullscreen();
				break;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_A:
				characters.get(firstPlayerIndex).setLeft(true);
				break;
			case KeyEvent.VK_D:
				characters.get(firstPlayerIndex).setRight(true);
				break;
			case KeyEvent.VK_W:
				characters.get(firstPlayerIndex).setUp(true);
				break;
			case KeyEvent.VK_S:
				characters.get(firstPlayerIndex).setDown(true);
				break;
			case KeyEvent.VK_UP:
				characters.get(secondPlayerIndex).setUp(true);
				break;
			case KeyEvent.VK_DOWN:
				characters.get(secondPlayerIndex).setDown(true);
				break;
			case KeyEvent.VK_LEFT:
				characters.get(secondPlayerIndex).setLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				characters.get(secondPlayerIndex).setRight(true);
				break;
			case KeyEvent.VK_SHIFT:
				if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
					characters.get(firstPlayerIndex).setDashing(true);
				} else {
					characters.get(secondPlayerIndex).setDashing(true);
				}
				break;
			case KeyEvent.VK_CONTROL:
				if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
					characters.get(firstPlayerIndex).setBlocking(true);
				} else {
					characters.get(secondPlayerIndex).setBlocking(true);
				}
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			case KeyEvent.VK_Z:
				cycleWorld();
				break;
			}

		}
	}

}