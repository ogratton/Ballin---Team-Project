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

public class GraphicsTest {

	public static boolean fullScreen = true;
	public static GameComponent comp;

	public static void main(String[] args) {

		Character player = new Character(Character.Class.WIZARD);

		player.setX(46);
		player.setY(46);

		Character player1 = new Character(Character.Class.ELF);

		player1.setX(534);
		player1.setY(424);

		Character player2 = new Character(Character.Class.WIZARD);

		player2.setX(999);
		player2.setY(234);

		ArrayList<Character> players = new ArrayList<Character>();
		players.add(player);
		players.add(player1);
		players.add(player2);

		Map map = new Map(null, 0, 0, 0, 0, null, null);

		Graphics g = new Graphics(players, map);
		g.run();
		
		
	}
}
