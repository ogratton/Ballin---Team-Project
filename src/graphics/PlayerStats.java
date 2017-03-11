package graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Character;

public class PlayerStats extends JPanel {

	private Character character;
	private JLabel stamina;
	private JLabel kda;
	
	public PlayerStats(Character character){
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.character = character;
		setPreferredSize(new Dimension(320, 100));
		
		stamina = new JLabel("Stamina: " + character.getStamina());
		kda = new JLabel("K/D/S: " + character.getKills() + "/" + character.getDeaths() + "/" + character.getSuicides());
		
		stamina.setFont(setFont(20));
		kda.setFont(setFont(20));
		
		stamina.setHorizontalAlignment(JLabel.CENTER);
		kda.setHorizontalAlignment(JLabel.CENTER);
		
		add(stamina);
		add(kda);
		
	}
	
	public void updateStats(){
	
		stamina.setText("Stamina: " + character.getStamina());
		kda.setText("K/D/S: " + character.getKills() + "/" + character.getDeaths() + "/" + character.getSuicides());
		
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
}
