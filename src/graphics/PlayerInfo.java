package graphics;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.JPanel;

import graphics.sprites.SheetDeets;
import resources.Character;

/**
 * Class to hold a panel containing player information for the top of the screen
 * Displays coloured arrow, player picture, powerup, name and score
 * 
 * @author George Kaye
 *
 */

public class PlayerInfo extends JPanel {

	private Character character;

	/**
	 * Create a new player info panel
	 * @param character the character
	 */
	
	public PlayerInfo(Character character) {
		
		super();
		this.character = character;

	}

	/**
	 * Paint the player info panel
	 */
	
	public void paintComponent(Graphics g) {

		// draw the arrowhead and player picture
		g.drawImage(character.getArrow(false), 0, -15, this);
		g.drawImage(character.getFirstFrame(), 50, 0, this);

		// draw the powerup if there is one
		if (character.hasPowerup()) {

			g.drawImage(SheetDeets.getPowerUpSpriteFromType(character.getLastPowerup()), 100, 0, this);

		}

		int size = 20;
		
		if(character.getName().length() > 9){
			size = 27 - (int)(character.getName().length() * 1.1);
		}
		
		// get the custom font working on all machines
		g.setFont(setFont(size));		
		
		// draw the character name and score
		g.drawString(character.getName(), 150, 33);
		
		g.setFont(setFont(20));
		g.drawString(character.getScore() + "", 300, 33);

	}

	/**
	 * Set the custom font based on size
	 * @param size the size
	 * @return the font
	 */
	
	public Font setFont(int size){
		Font customFont = new Font("Comic Sans", Font.PLAIN, 14);
		try {
			customFont = Font
					.createFont(Font.TRUETYPE_FONT,
							new File(System.getProperty("user.dir") + "/resources/fonts/04b.ttf"))
					.deriveFont(Font.PLAIN, size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customFont;
	}
	
	/**
	 * Set the character this panel is detailing
	 * @param c the character
	 */
	
	public void setCharacter(Character c) {
		this.character = c;
		repaint();
	}

}
