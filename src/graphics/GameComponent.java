package graphics;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

import audio.MusicPlayer;
import graphics.old.MapModel;
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
	private Map map;
	private Timer timer;
	private GameView view;
	private Resources resources;
	private int firstPlayerIndex = 0;
	private int secondPlayerIndex = 1;

	private boolean fullScreen = false;
	private boolean debugPaths = false;

	int width, height;
	int oldValueX, newValueX, oldValueY, newValueY;

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
		timer = new Timer(17, this);
		timer.start();

		// End test code block
		this.resources = resources;

		characters = resources.getPlayerList();

		this.width = width;
		this.height = height;

		view = new GameView(resources, debugPaths);

		if (updater != null) {
			for (Character model : resources.getPlayerList()) {
				model.addObserver(view);
				if (model.getId().equals(resources.getId())) {
					model.addObserver(updater);
				}
			}
		}
		
		for (int i = 0; i < characters.size(); i++) {
			if (characters.get(i).getId().equals(resources.getId())) {
				secondPlayerIndex = i;
				// System.out.println("Index: " + secondPlayerIndex);
				break;
			}
		}

		add(view, BorderLayout.CENTER);

	}

	// All code below here is for testing

	/**
	 * Testing keyboard inputs
	 */

	public void actionPerformed(ActionEvent arg0) {

		repaint();

	}

	/**
	 * Set the mulitplier
	 * 
	 * @param mult
	 *            the multiplier
	 */

	public void setMultiplier(double newWidth, double newHeight) {

		double mult = newWidth / width;
		this.view.setMultiplier(mult);
	}

	/**
	 * Switch between fullscreen and windowed
	 */

	public void toggleFullscreen() {

		if (fullScreen) {

			int newWidth = (int) (1200);
			int newHeight = (int) (675);

			setMultiplier(newWidth, newHeight);
			setSize(newWidth, newHeight);
			setLocationRelativeTo(null);
			fullScreen = false;

		} else {

			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int width = gd.getDisplayMode().getWidth();
			int height = gd.getDisplayMode().getHeight();
			setLocation(0, 0);
			setMultiplier(width, height);
			setSize(width, height);
			fullScreen = true;

		}

		setFocusable(true);
		requestFocus();

	}

	public void cycleWorld() {

		World world = resources.getMap().getWorldType();
		World newWorld = null;

		switch (world) {
		case CAVE:
			newWorld = Map.World.SPACE;
			break;
		case SPACE:
			newWorld = Map.World.CAVE;
			break;
		}

		resources.getMap().setWorldType(newWorld);
		view.makeMap();

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