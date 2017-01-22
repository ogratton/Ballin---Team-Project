package graphics;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import graphics.sprites.SheetDeets;
import resources.Character;
import resources.Map;

/**
 * Test the graphical renderer
 * @author George Kaye
 *
 */

public class GraphicsTest {

	public static void main(String[] args){
		
		System.out.println(SheetDeets.CHAR_WIZ_ROLLING);
		
		JFrame frame = new JFrame();							// make a new frame
		
		// code I shamelessly nicked off stack overflow to get screen dimensions
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		
		
		frame.setSize((int)width, (int)height);					// set the size to be full screen
		frame.setUndecorated(true);								// set the window as undecorated (to look full screen)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// set the close operation to end program (I forgot this at first)
		
		Character player = new Character();						// default character
		Map map = new Map(null, 0,0, 0, 0, null, null);			// default map (is there one?)
		
		GameComponent comp = new GameComponent(player, map);	// create GameComponent to output the game
				
		frame.add(comp);										// add component to frame
		frame.setVisible(true);									// make everything visible (took me ages to remember this)
	}
	
}
