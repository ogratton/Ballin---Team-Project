package graphics;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import graphics.sprites.SheetDeets;
import resources.Character;

/**
 * The sprite half of a player info panel
 * Containing player picture, powerup and hot potato
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class SpriteHalf extends JPanel {

	private Character character;
	private boolean powerupPresent = false;
	private boolean bombPresent = false;

	/**
	 * Create a new sprite half
	 * @param character the character detailed by the panel
	 */
	
	public SpriteHalf(Character character) {
		
		this.character = character;
		repaint();
		
		setPreferredSize(new Dimension(150, 50));
		
	}

	/**
	 * Repaint the panel
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.drawImage(character.getArrow(false), 0, -15, this);
		g.drawImage(character.getFirstFrame(), 50, 0, this);
		
		if (character.hasPowerup()) {

			g.drawImage(SheetDeets.getPowerUpSpriteFromType(character.getLastPowerup()), 100, 0, this);
			powerupPresent = true;
		} else if (powerupPresent) {
			g.clearRect(100, 0, 50, 50);
			powerupPresent = false;
		}
		
		if (character.hasBomb()) {

			g.drawImage(SheetDeets.getBombSprite(), 100, 0, this);
			bombPresent = true;
		} else if (bombPresent) {
			g.clearRect(100, 0, 50, 50);
			bombPresent = false;
		}
	}

}
