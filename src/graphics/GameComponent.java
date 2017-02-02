package graphics;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import graphics.old.MapModel;
import resources.Character;
import resources.Map;
import resources.Resources;

/**
 * Contains everything on the screen that the player can see
 * 
 * @author George Kaye
 *
 */

public class GameComponent extends JFrame implements ActionListener{

	private ArrayList<Character> characters;
	//private ArrayList<CharacterModel> characterModels;
	private Map map;
	private MapModel mapModel;
	private Timer timer;
	private GameView view;

	private boolean fullScreen = true;
	
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

	public GameComponent(ArrayList<Character> characters, Map map, int width, int height) {
		
		setLayout(new BorderLayout());
		
		// This code block below is just for testing!

		addKeyListener(new TAdapter());
		setFocusable(true);
		timer = new Timer(30, this);
		timer.start();

		// End test code block
		this.characters = characters;
		
		this.width = width;
		this.height = height;
		
		this.map = map;
		mapModel = new MapModel(map);

		JButton button = new JButton("exit");
		button.addActionListener(e -> System.exit(0));
		add(button, BorderLayout.SOUTH);


		view = new GameView(characters, mapModel);

		for (Character model : characters) {
			model.addObserver(view);
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
	 * @param mult the multiplier
	 */
	
	public void setMultiplier(double mult) {
		this.view.setMultiplier(mult);
	}
	
	/**
	 * Switch between fullscreen and windowed
	 */
	
	public void toggleFullscreen() {

		if (fullScreen) {
			
			double screenChange = 0.5;
			
			int newWidth = (int)(width * screenChange);
			int newHeight = (int)(height * screenChange);
			
			setSize(newWidth, newHeight);
			fullScreen = false;
			setMultiplier(screenChange);
		} else {
			
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int width = gd.getDisplayMode().getWidth();
			int height = gd.getDisplayMode().getHeight();
			setSize(width, height);
			fullScreen = true;
			setMultiplier(1);
		}
		
		setFocusable(true);
		requestFocus();

	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_A:
				characters.get(0).setLeft(false);
				break;
			case KeyEvent.VK_D:
				characters.get(0).setRight(false);
				break;
			case KeyEvent.VK_W:
				characters.get(0).setUp(false);
				break;
			case KeyEvent.VK_S:
				System.out.println("test");
				characters.get(0).setDown(false);
				break;
			case KeyEvent.VK_UP:
				characters.get(1).setUp(false);
				break;
			case KeyEvent.VK_DOWN:
				characters.get(1).setDown(false);
				break;
			case KeyEvent.VK_LEFT:
				characters.get(1).setLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				characters.get(1).setRight(false);
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
				characters.get(0).setLeft(true);
				break;
			case KeyEvent.VK_D:
				characters.get(0).setRight(true);
				break;
			case KeyEvent.VK_W:
				characters.get(0).setUp(true);
				break;
			case KeyEvent.VK_S:
				characters.get(0).setDown(true);
				break;
			case KeyEvent.VK_UP:
				characters.get(1).setUp(true);
				break;
			case KeyEvent.VK_DOWN:
				characters.get(1).setDown(true);
				break;
			case KeyEvent.VK_LEFT:
				characters.get(1).setLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				characters.get(1).setRight(true);
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			
			}
		}
	}

}
