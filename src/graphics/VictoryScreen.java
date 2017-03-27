package graphics;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import networking.Command;
import networking.Message;
import networking.Note;
import resources.Character;
import resources.Resources;
import resources.Resources.Mode;
import ui.UIRes;

/**
 * A class to hold the victory screen
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class VictoryScreen extends JPanel {

	/**
	 * Makes a placeholder empty victory screen
	 * @param resources
	 * @param placeholder
	 */
	
	public VictoryScreen(){
		
	}
	
	/**
	 * Create a new victory screen
	 * 
	 * @param resources
	 *            the resources object
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
		if (resources.gamemode.getWinners().contains(character)) {
			text = "Winner!";
			winner = true;
		} else {
			text = "Loser...";
		}

//		System.out.println("VICTORY!!!" + resources.gamemode.getWinners());

		// add the winner text to a label
		JLabel label = new JLabel(text);
		UIRes.setCustomFont(label, 64);
		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);

		ArrayList<Character> winners = resources.gamemode.getWinners();

		String winnerText = "";

		try {
			if (winners.size() == 1) {
				winnerText = "The winner was " + winners.get(0).getName();
			} else {
				winnerText = "The winners were " + winners.get(0).getName();

				for (int i = 1; i < winners.size(); i++) {
					winnerText += ", " + winners.get(i).getName();
				}

			}
		} catch (NullPointerException e) {
			winnerText = "Player";
		}

		JLabel label4 = new JLabel(winnerText);
		UIRes.setCustomFont(label4, 32);
		label4.setBorder(new EmptyBorder(10, 10, 10, 10));
		label4.setAlignmentX(CENTER_ALIGNMENT);
		add(label4);

		// add the player's stats to a label
		String kds = "";

		if (mode != Mode.HotPotato) {
			kds = "Kills: " + character.getKills() + " / Deaths: " + character.getDeaths() + " / Suicides: "
					+ character.getSuicides();
		}

		JLabel label2 = new JLabel(kds);
		UIRes.setCustomFont(label2, 18);
		label2.setBorder(new EmptyBorder(10, 10, 10, 10));
		label2.setAlignmentX(CENTER_ALIGNMENT);
		add(label2);

		// provide different text depending on game mode
		String modeText = "";

		switch (mode) {
		case Deathmatch:

			int puntos = character.getScore();

			if (puntos == 1) {
				modeText = "You finished with " + puntos + " point";
			} else {

				modeText = "You finished with " + puntos + " points";
			}
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

				int tiempo = character.getTimeOfDeath();

				if (tiempo == 1) {
					modeText = "You survived for " + (tiempo / 100) + " second";
				} else {
					modeText = "You survived for " + (tiempo / 100) + " seconds";

				}
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

		JButton exit = new JButton("Back");
		UIRes.customiseButton(exit, true);
		UIRes.getButtonAndIcon(this, exit);
		exit.addActionListener(e -> {
			resources.setFinished(true);

			try {
				Message refresh = new Message(Command.SESSION, Note.INDEX, UIRes.cModel.getMyId(), "", "", "");
				UIRes.cModel.getConnection().sendTCP(refresh);
			} catch (NullPointerException e1) {

			}
			UIRes.reset();
		});

		// nice border
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(50, 50, 50, 50)));

	}

}
