package graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Character;
import ui.UIRes;

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
		
		stamina.setHorizontalAlignment(JLabel.CENTER);
		kda.setHorizontalAlignment(JLabel.CENTER);
		
		UIRes.setCustomFont(stamina, 20);
		UIRes.setCustomFont(kda, 20);
		
		add(stamina);
		add(kda);
		
	}
	
	public void updateStats(){
	
		stamina.setText("Stamina: " + character.getStamina());
		kda.setText("K/D/S: " + character.getKills() + "/" + character.getDeaths() + "/" + character.getSuicides());
		
	}
}
