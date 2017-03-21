package graphics;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import resources.Resources;

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

		players = new PlayerPanel(resources);
		stats = new PlayerStats(resources.getPlayerList().get(0), resources);

		add(players, BorderLayout.CENTER);
		add(stats, BorderLayout.EAST);

	}

	/**
	 * Paint the players part of the bar
	 */

	public void paint() {
		players.paint();
	}

	/**
	 * Update the score on the players part of the bar
	 */

	public void updateScores() {
		players.updateScores();
	}

	/**
	 * Update the stats in the bar
	 */

	public void updateStats() {
		stats.updateStats();
	}

}
