package graphics;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import graphics.sprites.Sprite;
import resources.Map;
import resources.Resources;
import resources.Resources.Mode;
import ui.InGameMenu;
import ui.UIRes;

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
	public static JPanel optionsPanel;
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
		splash.setBounds(((x - 1200) / 2), ((y - 550) / 2), 1200, 450);

		
		JLabel map = new JLabel(new ImageIcon(Sprite.createMap(new Map(UIRes.width, UIRes.height, ""))));
		map.setLayout(new BorderLayout());
		JPanel panel2 = new JPanel();
		BoxLayout box = new BoxLayout(panel2, BoxLayout.Y_AXIS);
		
		optionsPanel = UIRes.optionsPanel;
		JButton backToInGameButton = new JButton("Back");
		UIRes.customiseButton(backToInGameButton, true);
		UIRes.getButtonAndIcon(optionsPanel, backToInGameButton);
		backToInGameButton.addActionListener(e -> {
			GameComponent.layers.setLayer(panel2, new Integer(5));
		});
		panel2.setBounds(0, 0, UIRes.width, UIRes.height);
		
		optionsPanel.remove(0);
		panel2.setOpaque(false);
		
		map.add(optionsPanel, BorderLayout.CENTER);
		
		panel2.add(Box.createHorizontalStrut(50));
		panel2.add(map);
		
		InGameMenu inGameMenu = new InGameMenu(frame, panel2, 600, 500);
		LayeredPane.inGameMenu = inGameMenu;
		inGameMenu.setBounds(((x - 600) / 2), ((y - 500) / 2), 600, 500);
		
		add(panel2, new Integer(5));
		add(inGameMenu, new Integer(10));
		add(view, new Integer(15));
		add(splash, new Integer(20));

		if(resources.mode == Mode.Debug){
			setLayer(splash, new Integer(5));
		}
		
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
	 * Repaint the views contained within this layered pane, updates the
	 * countdown and removes the splash screen if necessary
	 */

	public void repaint() {
		view.repaint();

		if (splashShowing) {
			splash.setCountdown(resources.getCountdown());

			if (resources.getCountdown() == 0) {
				setLayer(splash, new Integer(5));
				splashShowing = false;
			}
		}
	}

	/**
	 * Display the victory splash screen
	 */

	public void victory() {
		VictoryScreen victory = new VictoryScreen(resources);
		victory.setBounds(((x - 1100) / 2), ((y - 500) / 2), 1100, 400);
		add(victory, new Integer(25));
		victoryShowing = true;

	}

}
