package graphics;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import resources.Resources;
import resources.Character;
import ui.UIRes;

public class VictoryScreen extends JPanel {

	public VictoryScreen(Resources resources) {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Resources.Mode mode = resources.mode;
		Character character = null;
		
		try{
			character = UIRes.cModel.getMyCharacter();
		}catch(NullPointerException e){
			character = resources.getPlayerList().get(0);
		}

		String text = "";
		boolean winner = false;

		if (character.equals(resources.gamemode.getWinner())) {
			text = "Winner!";
			winner = true;
		} else {
			text = "Loser...";
		}

		JLabel label = new JLabel(text);
		UIRes.setCustomFont(label, 64);
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);

		JLabel label2 = new JLabel("Kills: " + character.getKills() + " / Deaths: " + character.getDeaths()
				+ " / Suicides: " + character.getSuicides());
		UIRes.setCustomFont(label2, 18);
		label2.setAlignmentX(CENTER_ALIGNMENT);
		add(label2);

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

		JLabel label3 = new JLabel(modeText);
		UIRes.setCustomFont(label3, 18);
		label3.setAlignmentX(CENTER_ALIGNMENT);
		add(label3);

		JLabel label4 = new JLabel("Press any key to exit");
		UIRes.setCustomFont(label4, 32);
		label4.setAlignmentX(CENTER_ALIGNMENT);
		add(label4);
		
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(50, 50, 50, 50)));

	}

}
