package graphics;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import javax.swing.JFrame;

import resources.Character;
import resources.Map;

/**
 * Test the graphical renderer
 * 
 * @author George Kaye
 *
 */

public class GraphicsTest {

	public static void main(String[] args) {

		JFrame frame = new JFrame(); // make a new frame

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

		Character player = new Character(); // default character

		player.setX(20);
		player.setY(5);

		Character player1 = new Character(); // default character

		player1.setX(12);
		player1.setY(2);

		ArrayList<Character> players = new ArrayList<Character>();
		players.add(player);
		players.add(player1);

		Map map = new Map(null, 0, 0, 0, 0, null, null);

		GameComponent comp = new GameComponent(players, map);
		
		comp.setOpaque(true);
		frame.setContentPane(comp);
		frame.setVisible(true);

	}

}
