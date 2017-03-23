package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graphics.sprites.SheetDetails;
import resources.Character;
import resources.Powerup.Power;
import resources.Resources;
import ui.UIRes;

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
	private Resources resources;
	private boolean powerupPresent = false;
	private boolean bombPresent = false;
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
		this.resources = resources;
		
		sprite = character.getFirstFrame();
		arrow = SheetDetails.getArrowFromPlayer(character.getPlayerNumber());

		arrowLabel = new JLabel(new ImageIcon(arrow));
		//arrowLabel.setMaximumSize(new Dimension(50, 50));
		spriteLabel = new JLabel(new ImageIcon(sprite));
		//spriteLabel.setMaximumSize(new Dimension(50, 50));
		powerupLabel = new JLabel();
		//powerupLabel.setMaximumSize(new Dimension(50, 50));
		

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
