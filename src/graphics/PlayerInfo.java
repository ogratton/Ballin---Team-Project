package graphics;

import java.awt.BorderLayout;

import javax.swing.JPanel;

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
	private SpriteHalf sprites;
	private TextHalf text;
	
	/**
	 * Create a new player info panel
	 * 
	 * @param character
	 *            the character
	 */

	public PlayerInfo(Character character) {

		super();
		
		setLayout(new BorderLayout());
		
		this.character = character;

		text = new TextHalf(character);
		sprites = new SpriteHalf(character);
		
		add(text,BorderLayout.CENTER);
		add(sprites,BorderLayout.WEST);
		
	}
	
	public void updateScore(){
		text.updateScore();
	}

	/**
	 * Paint the player info panel
	 */

	/*public void paintComponent(Graphics g) {

		super.paintComponent(g);

		// draw the arrowhead and player picture

		g.drawImage(character.getArrow(false), 0, -15, this);
		g.drawImage(character.getFirstFrame(), 50, 0, this);

		// draw the powerup if there is one

		if (character.hasPowerup()) {
			//g.drawImage(SheetDeets.getPowerUpSpriteFromType(character.getLastPowerup()), 100, 0, this);
		}

		int size = 20;

		if (character.getName().length() > 9) {
			size = 27 - (int) (character.getName().length() * 1.1);
		}

		// get the custom font working on all machines
		UIRes.setCustomFont(this, size);

		// draw the character name and score
		g.drawString(character.getName(), 150, 33);

		UIRes.setCustomFont(this, 20);
		g.drawString(character.getScore() + "", 300, 33);

	}*/

	public void paint(){
		sprites.repaint();
	}
	
	/**
	 * Set the character this panel is detailing
	 * 
	 * @param c
	 *            the character
	 */

	public void setCharacter(Character c) {
		this.character = c;
	}

}
