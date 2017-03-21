package graphics;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import resources.Resources;
import ui.InGameMenu;

/**
 * Wrapper class to store the various views the player can switch through during
 * the game
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class LayeredPane extends JLayeredPane {

	public GameView view;
	public static boolean menuShowing = false;
	public static boolean splashShowing = true;
	public static boolean victoryShowing = false;
	public static JPanel inGameMenu;
	public static SplashScreen splash;
	private Resources resources;
	private int x;
	private int y;

	/**
	 * Create a new layered pane wrapper
	 * 
	 * @param resources
	 *            the resources object
	 * @param debugPaths
	 *            the debugpaths flag
	 */

	public LayeredPane(Resources resources, boolean debugPaths, JFrame frame) {

		super();

		this.resources = resources;
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		x = gd.getDisplayMode().getWidth();
		y = gd.getDisplayMode().getHeight();

		view = new GameView(resources, debugPaths);
		view.setBounds(0, 0, x, y);
		
		splash = new SplashScreen(resources);
		splash.setBounds(((x - 1200) / 2) , ((y - 400) / 2), 1200, 400);
		
		InGameMenu menu = new InGameMenu();
		inGameMenu = menu.getInGameMenuPanel(frame, 600, 500); 
		inGameMenu.setBounds(((x - 600) / 2) , ((y - 500) / 2), 600, 500);
		add(inGameMenu, new Integer(10));
		add(view, new Integer(15));
		add(splash, new Integer(20));
		
		setVisible(true);

	}

	/**
	 * Get the game view object (useful for accessing methods within GameView)
	 * 
	 * @return the gameview object
	 */

	public GameView getView() {
		return this.view;
	}

	/**
	 * Set the countdown
	 * @param i the countdown
	 */
	
	public void setCountdown(int i){
		splash.setCountdown(i);
	}
	
	/**
	 * Repaint the views contained within this layered pane
	 */

	public void repaint() {
		view.repaint();
	}
	
	public void victory(){
		VictoryScreen victory = new VictoryScreen(resources);
		victory.setBounds(((x - 1100) / 2) , ((y - 350) / 2), 1100, 350);
		add(victory, new Integer(30));
		victoryShowing = true;
		
	}

}
