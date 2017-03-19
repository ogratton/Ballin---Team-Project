package graphics;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import resources.Resources;

@SuppressWarnings("serial")
public class TopBar extends JPanel {

	private PlayerPanel players;
	private PlayerStats stats;

	public TopBar(Resources resources) {

		super();

		setLayout(new BorderLayout());

		players = new PlayerPanel(resources);
		stats = new PlayerStats(resources.getPlayerList().get(0), resources);

		add(players, BorderLayout.CENTER);
		add(stats, BorderLayout.EAST);

	}

	public void paint() {
		players.paint();
	}

	public void updateScores() {
		players.updateScores();
	}

	public void updateStats() {
		stats.updateStats();
	}

}
