package graphics;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import resources.Character;
import resources.Resources;
import ui.UIRes;

/**
 * A class to hold the top bar, which contains all the player scores and stats
 * 
 * @author George Kaye
 *
 */
@SuppressWarnings("serial")
public class TopBar extends JPanel {

	private PlayerPanel players;
	private PlayerStats stats;

	/**
	 * Make a new top bar
	 * 
	 * @param resources
	 *            the resources object
	 */

	public TopBar(Resources resources) {

		super();

		setLayout(new BorderLayout());
		
		Character me = null;
		
		try{
			me = UIRes.cModel.getMyCharacter();
		}catch(NullPointerException e){
			me = resources.getPlayerList().get(0);
		}

		players = new PlayerPanel(resources);
		stats = new PlayerStats(me, resources);

		add(players, BorderLayout.CENTER);
		add(stats, BorderLayout.EAST);

	}

	/**
	 * Update the score on the players part of the bar
	 */

	public void update() {
		players.update();
		stats.update();
		
	}

}
