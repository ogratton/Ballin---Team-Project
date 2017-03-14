package graphics;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import resources.Character;
import resources.Resources;
import resources.Resources.Mode;
import ui.UIRes;

@SuppressWarnings("serial")
public class PlayerStats extends JPanel {

	private Character character;
	private Resources resources;
	private JLabel stamina;
	private JLabel kda;
	private JLabel time;
	
	/**
	 * Create a new player stats panel
	 * @param character the character the panel is detailing
	 * @param resources the resources object
	 */
	
	public PlayerStats(Character character, Resources resources){
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.resources = resources;
		this.character = character;
		
		setPreferredSize(new Dimension(320, 100));
		
		// make the labels
		stamina = new JLabel("Stamina: " + character.getStamina());
		kda = new JLabel("K/D/S: " + character.getKills() + "/" + character.getDeaths() + "/" + character.getSuicides());
		time = new JLabel("Time: " + resources.gamemode.getTime());
		
		// this doesn't work. why
		stamina.setHorizontalAlignment(JLabel.CENTER);
		kda.setHorizontalAlignment(JLabel.CENTER);
		time.setHorizontalAlignment(JLabel.CENTER);
		
		// set the font for the labels
		UIRes.setCustomFont(stamina, 20);
		UIRes.setCustomFont(kda, 20);
		UIRes.setCustomFont(time, 20);
		
		// stamina and kda will feature in every gamemode
		add(stamina);
		add(kda);

		// any gamemode with time will need time to display
		if(resources.mode == Mode.Deathmatch){
			add(time);
		}
		
		setBorder(new EmptyBorder(15, 35, 25, 25));
		
	}
	
	/**
	 * Update the text on the labels to reflect the current game situation
	 */
	
	public void updateStats(){
	
		stamina.setText("Stamina: " + character.getStamina());
		kda.setText("K/D/S: " + character.getKills() + "/" + character.getDeaths() + "/" + character.getSuicides());
		
		if(resources.mode == Mode.Deathmatch){
			time.setText("Time: " + resources.gamemode.getTime());
		}
		
	}
}
