package graphics;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import resources.Character;
import resources.Resources;
import ui.InGameMenu;
import ui.UIRes;

/**
 * A class to hold the victory screen
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class VictoryScreen extends JPanel {

	/**
	 * Create a new victory screen
	 * @param resources the resources object
	 */
	
	public VictoryScreen(Resources resources) {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Resources.Mode mode = resources.mode;
		Character character = null;

		// get the character this machine is controlling
		try {
			character = UIRes.cModel.getMyCharacter();
		} catch (NullPointerException e) {
			character = resources.getPlayerList().get(0);
		}

		String text = "";
		boolean winner = false;

		// check if this machine's character is the winner
		if (character.equals(resources.gamemode.getWinner())) {
			text = "Winner!";
			winner = true;
		} else {
			text = "Loser...";
		}

		// add the winner text to a label
		JLabel label = new JLabel(text);
		UIRes.setCustomFont(label, 64);
		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);

		// add the player's stats to a label
		JLabel label2 = new JLabel("Kills: " + character.getKills() + " / Deaths: " + character.getDeaths()
				+ " / Suicides: " + character.getSuicides());
		UIRes.setCustomFont(label2, 18);
		label2.setBorder(new EmptyBorder(10, 10, 10, 10));
		label2.setAlignmentX(CENTER_ALIGNMENT);
		add(label2);

		// provide different text depending on game mode
		String modeText = "";

		switch (mode) {
		case Deathmatch:
			modeText = "You finished with " + character.getScore() + " points";
			break;
		case HotPotato:
			if (winner) {
				modeText = "You survived the gauntlet!";
			} else {
				modeText = "You survived for " + (character.getTimeOfDeath() / 100) + " seconds";
			}
			break;
		case LastManStanding:
			if (winner) {
				modeText = "You survived the longest!";
			} else {
				modeText = "You survived for " + (character.getTimeOfDeath() / 100) + " seconds";
			}
			break;
		default:
			break;
		}

		// add mode text to a label
		JLabel label3 = new JLabel(modeText);
		UIRes.setCustomFont(label3, 18);
		label3.setAlignmentX(CENTER_ALIGNMENT);
		label3.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(label3);
		
		JButton exit = InGameMenu.getBackToStartMenuButton();
		exit.addActionListener(e -> resources.setFinished(true));
		add(exit);

		// nice border
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(50, 50, 50, 50)));

	}

}
