package graphics;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.JPanel;

import graphics.sprites.SheetDeets;
import resources.Character;

public class PlayerInfo extends JPanel {

	private Character character;

	public PlayerInfo(Character character) {

		this.character = character;

	}
	
	public void paintComponent(Graphics g){
		
		//g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		g.drawImage(character.getArrow(false), 0, -15, this);
		g.drawImage(character.getFirstFrame(), 50, 0, this);

		Font customFont = new Font("Comic Sans", Font.PLAIN, 14);
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "/resources/fonts/04b.ttf"))
					.deriveFont(Font.PLAIN, 20);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
		}
		g.setFont(customFont);

		if(character.hasPowerup()){
			
			g.drawImage(SheetDeets.getPowerUpSpriteFromType(character.getLastPowerup()), 100, 0, this);
			
		}
		
		g.drawString(character.getName(), 150, 33);
		g.drawString(character.getScore() + "", 300, 33);
		
		
	}
	
	public void setCharacter(Character c){
		this.character = c;
		repaint();
	}

}
