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

@SuppressWarnings("serial")
public class PlayerInfo extends JPanel {

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

		text = new TextHalf(character);
		sprites = new SpriteHalf(character);
		
		add(text,BorderLayout.CENTER);
		add(sprites,BorderLayout.WEST);
		
	}
	
	/**
	 * Update the score on the panel
	 */
	
	public void updateScore(){
		text.updateScore();
	}
	
	/**
	 * Repaint the sprites on the panel
	 */
	
	public void paint(){
		sprites.repaint();
	}

}
