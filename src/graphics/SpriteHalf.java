package graphics;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import graphics.sprites.SheetDeets;
import resources.Character;

public class SpriteHalf extends JPanel {

	private Character character;
	private boolean powerupPresent = false;

	public SpriteHalf(Character character) {
		
		this.character = character;
		repaint();
		
		setPreferredSize(new Dimension(175, 50));
		
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.drawImage(character.getArrow(false), 25, -15, this);
		g.drawImage(character.getFirstFrame(), 75, 0, this);
		
		if (character.hasPowerup()) {

			g.drawImage(SheetDeets.getPowerUpSpriteFromType(character.getLastPowerup()), 125, 0, this);
			powerupPresent = true;
		} else if (powerupPresent) {
			g.clearRect(125, 0, 50, 50);
			powerupPresent = false;
		}
	}

}
