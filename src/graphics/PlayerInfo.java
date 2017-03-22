package graphics;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import resources.Character;
import resources.Resources;

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

	public PlayerInfo(Character character, Resources resources) {

		super();
		
		setLayout(new BorderLayout());

		text = new TextHalf(character, resources);
		sprites = new SpriteHalf(character, resources);
		
		add(text,BorderLayout.CENTER);
		add(sprites,BorderLayout.WEST);
		
		setBorder(new LineBorder(Color.BLACK, 1));
		
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
