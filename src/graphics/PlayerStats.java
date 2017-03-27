package graphics;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import resources.Character;
import resources.Resources;
import ui.UIRes;

/**
 * Panel containing stats pertaining to the current player, such as stats, lives
 * and kds. Displays different text depending on gamemode (e.g bomb timer on
 * HotPotato)
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class PlayerStats extends JPanel {

	private Character character;
	private Resources resources;
	private JLabel stamina;
	private JLabel kda;
	private JLabel mode;

	/**
	 * Create a new player stats panel
	 * 
	 * @param character
	 *            the character the panel is detailing
	 * @param resources
	 *            the resources object
	 */

	public PlayerStats(Character character, Resources resources) {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.resources = resources;
		this.character = character;

		setPreferredSize(new Dimension(320, 100));

		// these labels are the same regardless of game mode
		stamina = new JLabel("Stamina: " + character.getStamina());
		kda = new JLabel(
				"K/D/S: " + character.getKills() + "/" + character.getDeaths() + "/" + character.getSuicides());

		// this text will show different info depending on game mode
		String modeText = "";

		switch (resources.mode) {
		case Deathmatch:
			modeText = "Time: " + resources.getTimer();
			break;
		case LastManStanding:
			modeText = "Lives: " + character.getLives();
			break;
		case HotPotato:
			modeText = "Bomb: " + (50 - (resources.getTimer() % 50));
		default:
			break;
		}

		mode = new JLabel(modeText);

		// set the text to be centered
		stamina.setAlignmentX(CENTER_ALIGNMENT);
		kda.setAlignmentX(CENTER_ALIGNMENT);
		mode.setAlignmentX(CENTER_ALIGNMENT);

		// set the font for the labels
		UIRes.setCustomFont(stamina, 20);
		UIRes.setCustomFont(kda, 20);
		UIRes.setCustomFont(mode, 20);

		// add all the labels
		add(stamina);
		add(kda);
		add(mode);

		// add some nice borders
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(15, 35, 25, 35)));

	}

	/**
	 * Update the text on the labels to reflect the current game situation
	 */

	public void update() {

		stamina.setText("Stamina: " + character.getStamina());
		kda.setText("K/D/S: " + character.getKills() + "/" + character.getDeaths() + "/" + character.getSuicides());

		String modeText = "";

		switch (resources.mode) {
		case Deathmatch:
			modeText = "Time: " + resources.getTimer();
			break;
		case LastManStanding:
			modeText = "Lives: " + character.getLives();
			break;
		case HotPotato:
			modeText = "Bomb: " + (50 - (resources.getTimer() % 50));
		default:
			break;
		}

		mode.setText(modeText);

	}
}
