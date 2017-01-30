package graphics;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;

import resources.Character;
import resources.Map;

/**
 * Test the graphical renderer
 * 
 * @author George Kaye
 *
 */

public class GraphicsTest implements Observer {

	public static boolean fullScreen = true;
	public static JFrame frame;
	public static GameComponent comp;
	
	public static void main(String[] args) {

		frame = new JFrame(); // make a new frame

		// code I shamelessly nicked off stack overflow to get screen dimensions
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();

		frame.setSize((int) width, (int) height); // set the size to be full
													// screen
		frame.setUndecorated(true); // set the window as undecorated (to look
									// full screen)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the close
																// operation to
																// end program

		frame.setTitle("Ballin'");
		frame.setLayout(new BorderLayout());
		
		JButton button = new JButton("fullscreen");
		button.addActionListener(e -> toggleFullscreen());
		frame.add(button, BorderLayout.SOUTH);

		Character player = new Character(Character.Class.WIZARD);

		player.setX(46);
		player.setY(46);

		Character player1 = new Character(Character.Class.ELF);

		player1.setX(534);
		player1.setY(424);

		ArrayList<Character> players = new ArrayList<Character>();
		players.add(player);
		players.add(player1);

		Map map = new Map(null, 0, 0, 0, 0, null, null);

		comp = new GameComponent(players, map);

		/*comp.setOpaque(true);
		frame.setContentPane(comp);*/
		frame.add(comp, BorderLayout.CENTER);
		
		frame.setVisible(true);

	}

	/**
	 * Toggles fullscreen - dodgy
	 */
	
	public static void toggleFullscreen() {

		if (fullScreen) {
			frame.setSize(1000, 625);
			fullScreen = false;
			comp.setMultiplier(1);
		} else {
			
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int width = gd.getDisplayMode().getWidth();
			int height = gd.getDisplayMode().getHeight();
			frame.setSize(width, height);
			fullScreen = true;
			comp.setMultiplier(1.5);
		}
		
		comp.setFocusable(true);

	}

}
