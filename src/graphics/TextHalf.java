package graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import resources.Character;
import resources.Resources;
import resources.Resources.Mode;
import ui.UIRes;

/**
 * The text half of a panel detailing a player
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class TextHalf extends JPanel {

	private Resources resources;
	private Character character;
	private JLabel name;
	private JLabel score;

	/**
	 * Create a new text half
	 * 
	 * @param character
	 *            the character being detailed
	 * @param resources
	 *            the resources object
	 */

	public TextHalf(Character character, Resources resources) {

		setLayout(new BorderLayout());

		this.resources = resources;
		this.character = character;

		name = new JLabel(character.getName());
		score = new JLabel("");

		if (resources.mode == Mode.Deathmatch) {
			score.setText(character.getScore() + "");
		} else if (resources.mode == Mode.LastManStanding) {
			int lives = character.getLives();

			String blobs = "";

			for (int i = 0; i < lives; i++) {
				blobs += "o";
			}

			score.setText(blobs);
		} else if (resources.mode == Mode.HotPotato) {
			if (character.isDead()) {
				score.setText("dead");
			}
		}

		name.setAlignmentX(LEFT_ALIGNMENT);
		score.setAlignmentX(RIGHT_ALIGNMENT);

		score.setBorder(new EmptyBorder(0, 0, 0, 10));

		UIRes.setCustomFont(name, 20);
		UIRes.setCustomFont(score, 20);

		add(name, BorderLayout.CENTER);
		add(score, BorderLayout.EAST);
		
		setPreferredSize(new Dimension(250, 50));

	}

	/**
	 * Update the score on the panel
	 */

	public void updateScore() {

		if (resources.mode == Mode.Deathmatch) {
			score.setText(character.getScore() + "");
		} else if (resources.mode == Mode.LastManStanding) {
			int lives = character.getLives();

			String blobs = "";

			for (int i = 0; i < lives; i++) {
				blobs += "o";
			}

			score.setText(blobs);
		} else if (resources.mode == Mode.HotPotato) {
			if (character.isDead()) {
				score.setText("dead");
			}
		}

	}

}
