package gamemodes;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Resources;
// Deprecated?
public class Hockey extends Thread implements GameModeTeams {

	private Resources resources;
	private int timer = 30;
	private Team winner;
	private boolean draw = false;

	public Hockey(Resources resources) {
		this.resources = resources;
		// -1 lives = infinite
		resources.getTeams()[0].setAllLives(-1);
		resources.getTeams()[1].setAllLives(-1);
		setTeamNumbers(0);
		setTeamNumbers(1);
		// Spawn all members
	}

	private void setTeamNumbers(int i) {
		for (Character c : resources.getTeams()[i].getMembers()) {
			c.setTeamNumber(i+1);
		}
	}

	public void run() {
		// Start game
		Physics p = new Physics(resources, false);
		p.start();
		SwingUtilities.invokeLater(new Graphics(resources, null, false));
		while (!isGameOver()) {
			try {
				System.out.println("Time remaining: " + timer + " seconds");
				timer--;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Game has ended
		p.pause();
		System.out.println("TIME'S UP");
		getWinner();
		if (draw) {
			System.out.println("The game was a draw!");
		} else if (winner == resources.getTeams()[0]) {
			Character mvp = getOrderedScores(1).get(0);
			System.out.println("Team 1 wins! Their MVP was player " + mvp.getPlayerNumber() + " who scored " + mvp.getScore() + " goals!");
		} else {
			Character mvp = getOrderedScores(2).get(0);
			System.out.println("Team 2 wins! Their MVP was player " + mvp.getPlayerNumber() + " who scored " + mvp.getScore() + " goals!");
		}
	}

	public Team getWinner() {
		Team[] teams = resources.getTeams();
		int t1s = teams[0].getScore();
		int t2s = teams[1].getScore();
		if (t1s == t2s) {
			draw = true;
		} else {
			winner = t1s > t2s ? teams[0] : teams[1];
		}
		return winner;
	}

	public boolean isGameOver() {
		return timer <= 0;
	}
	
	public boolean isDraw() {
		return draw;
	}

	/**
	 * @param t
	 *            1 for team 1, 2 for team 2
	 * @return The specified team
	 */
	public Team getTeam(int t) {
		if (t == 1) {
			return resources.getTeams()[0];
		} else if (t == 2) {
			return resources.getTeams()[1];
		}
		return null;
	}

	@Override
	public ArrayList<Character> getOrderedScores() {
		ArrayList<Character> scores = new ArrayList<Character>();
		scores.addAll(resources.getPlayerList());
		scores.sort((a, b) -> (a.getScore() > b.getScore()) ? -1 : (a.getScore() < b.getScore()) ? 1 : 0);
		return scores;
	}

	@Override
	public ArrayList<Character> getOrderedScores(int i) {
		ArrayList<Character> scores = new ArrayList<Character>();
		if (i == 1) {
			scores.addAll(resources.getTeams()[0].getMembers());
		} else if (i == 2) {
			scores.addAll(resources.getTeams()[1].getMembers());
		}
		scores.sort((a, b) -> (a.getScore() > b.getScore()) ? -1 : (a.getScore() < b.getScore()) ? 1 : 0);
		return scores;
	}

	@Override
	public void resetLives() {
		for (Character c : resources.getPlayerList()) {
			c.setLives(-1);
		}
	}

	@Override
	public void randomRespawn() {
		for (Character c : resources.getPlayerList()) {
			// Random respawn for now, probably want to spawn players either
			// randomly in their own half or in set positions
			resources.getMap().spawn(c);
		}
		resources.getMap().spawn(resources.getPuck());
	}
}
