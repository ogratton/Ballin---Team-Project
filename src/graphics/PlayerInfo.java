package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

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
	private Character character;
	private boolean hasPowerup;

	/**
	 * Create a new player info panel
	 * 
	 * @param character
	 *            the character
	 */

	public PlayerInfo(Character character, Resources resources) {

		super();

		this.character = character;

		setLayout(new BorderLayout());

		text = new TextHalf(character, resources);
		sprites = new SpriteHalf(character, resources);

		add(text, BorderLayout.CENTER);
		add(sprites, BorderLayout.WEST);

		setBorder(new LineBorder(Color.BLACK, 1));
		
		setPreferredSize(new Dimension(400, 50));
		
	}

	/**
	 * Update the score on the panel
	 */

	public void update() {
		text.updateScore();

		if (!hasPowerup) {

			if (character.hasPowerup()) {
				sprites.addPowerup();
				hasPowerup = true;
			}
		}

		else {
			if (!character.hasPowerup()) {
				sprites.removePowerup();
				hasPowerup = false;
			}
		}
	}

}
