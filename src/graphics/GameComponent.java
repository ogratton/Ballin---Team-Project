package graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import resources.Character;
import resources.Map;

/**
 * Contains everything on the screen that the player can see
 * @author George Kaye
 *
 */

public class GameComponent extends JPanel implements ActionListener{

	private ArrayList<Character> characters;
	private ArrayList<CharacterModel> characterModels;
	private Map map;
	private MapModel mapModel;
	private Timer timer;
	private GameView view;
	
	private boolean fullScreen = true;
	
	int oldValueX, newValueX, oldValueY, newValueY;

	/**
	 * Create a new game component (which comprises everything the player can see!)
	 * @param characters an ArrayList of characters on the board
	 * @param map the map the board is displaying
	 */
	
	public GameComponent(ArrayList<Character> characters, Map map) {

		setLayout(new BorderLayout());

		// This code block below is just for testing!
		
		addKeyListener(new TAdapter());
		setFocusable(true);
		timer = new Timer(30, this);
		timer.start();
		
		// End test code block
		
		this.map = map;
		mapModel = new MapModel(map);

		JButton button = new JButton("exit");
		button.addActionListener(e -> System.exit(0));
		add(button, BorderLayout.SOUTH);

		characterModels = new ArrayList<CharacterModel>();

		for (Character character : characters) {

			CharacterModel model = new CharacterModel(character);
			characterModels.add(model);

		}

		view = new GameView(characterModels, mapModel);

		for (CharacterModel model : characterModels) {
			model.addObserver(view);
		}

		add(view, BorderLayout.CENTER);
		setVisible(true);

	}

	// All code below here is for testing
	
	/**
	 * Testing keyboard inputs
	 */
	
	public void actionPerformed(ActionEvent arg0) {
		
		for(CharacterModel character : characterModels){
		character.move();
		}
		
		repaint();
		

	}
	
	public void setMultiplier(double mult){
		this.view.setMultiplier(mult);
	}
	
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_A:
				characterModels.get(0).setLeft(false);
				break;
			case KeyEvent.VK_D:
				characterModels.get(0).setRight(false);
				break;
			case KeyEvent.VK_W:
				characterModels.get(0).setUp(false);
				break;
			case KeyEvent.VK_S:
				characterModels.get(0).setDown(false);
				break;
			case KeyEvent.VK_UP:
				characterModels.get(1).setUp(false);
				break;
			case KeyEvent.VK_DOWN:
				characterModels.get(1).setDown(false);
				break;
			case KeyEvent.VK_LEFT:
				characterModels.get(1).setLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				characterModels.get(1).setRight(false);
				break;
			case KeyEvent.VK_CONTROL:
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_A:
				characterModels.get(0).setLeft(true);
				break;
			case KeyEvent.VK_D:
				characterModels.get(0).setRight(true);
				break;
			case KeyEvent.VK_W:
				characterModels.get(0).setUp(true);
				break;
			case KeyEvent.VK_S:
				characterModels.get(0).setDown(true);
				break;
			case KeyEvent.VK_UP:
				characterModels.get(1).setUp(true);
				break;
			case KeyEvent.VK_DOWN:
				characterModels.get(1).setDown(true);
				break;
			case KeyEvent.VK_LEFT:
				characterModels.get(1).setLeft(true);
				System.out.println("kek");
				break;
			case KeyEvent.VK_RIGHT:
				characterModels.get(1).setRight(true);
				break;
			}
		}
	}

}