package graphics;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graphics.sprites.SheetDetails;
import resources.Character;
import resources.Resources;

/**
 * The sprite half of a player info panel Containing player picture, powerup and
 * hot potato
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class SpriteHalf extends JPanel {

	private Character character;
	private JLabel arrowLabel;
	private JLabel spriteLabel;
	private JLabel powerupLabel;
	private BufferedImage sprite;
	private BufferedImage arrow;

	/**
	 * Create a new sprite half
	 * 
	 * @param character
	 *            the character detailed by the panel
	 */

	public SpriteHalf(Character character, Resources resources) {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.character = character;
		
		sprite = character.getFirstFrame();
		arrow = SheetDetails.getArrowFromPlayer(character.getPlayerNumber());

		arrowLabel = new JLabel(new ImageIcon(arrow));
		spriteLabel = new JLabel(new ImageIcon(sprite));
		powerupLabel = new JLabel();
		

		add(arrowLabel);
		add(spriteLabel);
		add(powerupLabel);
		
		setPreferredSize(new Dimension(150, 50));

	}

	/**
	 * Add a powerup
	 */
	
	public void addPowerup(){
		powerupLabel.setIcon(new ImageIcon(SheetDetails.getPowerUpSpriteFromType(character.getLastPowerup())));
		
	}
	
	/**
	 * Remove a powerup
	 */
	
	public void removePowerup(){
		powerupLabel.setIcon(null);
	}

}
