package graphics;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JLayeredPane;

import resources.Resources;

/**
 * Wrapper class to store the various views the player can switch through during the game
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class LayeredPane extends JLayeredPane {

	private GameView view;

	/**
	 * Create a new layered pane wrapper
	 * @param resources the resources object
	 * @param debugPaths the debugpaths flag
	 */
	
	public LayeredPane(Resources resources, boolean debugPaths) {

		super();
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int x = gd.getDisplayMode().getWidth();
		int y = gd.getDisplayMode().getHeight();

		view = new GameView(resources, debugPaths);
		view.setBounds(0, 0, x, y);
		
		add(view, new Integer(10));

		setVisible(true);

	}

	/**
	 * Get the game view object (useful for accessing methods within GameView)
	 * @return the gameview object
	 */
	
	public GameView getView() {
		return this.view;
	}

	/**
	 * Repaint the views contained within this layered pane
	 */
	
	public void repaint() {
		view.repaint();
	}

}
