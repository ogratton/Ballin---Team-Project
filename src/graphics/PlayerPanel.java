package graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import resources.Character;
import resources.Resources;

/**
 * Class to display a panel detailing all players in game
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {

	private ArrayList<PlayerInfo> boxes;

	/**
	 * Create a new player panel
	 * 
	 * @param resources
	 *            the resources object
	 */

	public PlayerPanel(Resources resources) {

		super();

		setLayout(new GridLayout(2, 1));

		ArrayList<Character> characters = resources.getOrderedScores();
		Collections.reverse(characters);
		boxes = new ArrayList<>();

		int i = 0;

		for (Character c : characters) {

			PlayerInfo box = new PlayerInfo(c, resources);
			boxes.add(box);
			add(box, i / 4, i % 4);

		}

		setPreferredSize(new Dimension(1600, 100));

	}

	public void paint(){
		for(PlayerInfo box : boxes){
			box.paint();
		}
	}
	
	/**
	 * Update all the scores in the boxes
	 */
	
	public void updateScores() {
		for (PlayerInfo box : boxes) {

			box.updateScore();
		}

	}
}
