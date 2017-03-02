package gamemodes;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Resources;

/**
 * Play until only one player remains.
 * 
 * @author Luke
 *
 */
public class LastManStanding extends Thread implements GameModeFFA {

	private int maxLives;
	private boolean gameOver = false;
	private Character winner;
	private Resources resources;

	public LastManStanding(Resources resources, int maxLives) {
		this.maxLives = maxLives;
		this.resources = resources;

		// Set up game
		setAllLives(maxLives);
		randomRespawn();
	}

	public void run() {
		// Start game
		Physics p = new Physics(resources);
		p.start();
		SwingUtilities.invokeLater(new Graphics(resources, null, false));

		while (!isGameOver()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Game has ended
		p.pause();
		System.out.println("WE HAVE A WINNER");
		System.out.println("Player " + winner.getPlayerNumber() + " survived the longest and reached a score of "
				+ winner.getScore() + "!");
		ArrayList<Character> scores = getOrderedScores();
		for (Character c : scores) {
			System.out.print("Player " + c.getPlayerNumber() + " had score " + c.getScore() + ", ");
		}
		System.out.println();
	}

	/**
	 * Set the number of lives for all players to the specified value
	 * 
	 * @param n
	 *            Number of lives
	 */
	private void setAllLives(int n) {
		for (Character c : resources.getPlayerList()) {
			c.setLives(n);
		}
	}

	/**
	 * @return maxLives
	 */
	public int getMaxLives() {
		return maxLives;
	}

	/**
	 * Get the total number of lives of all players
	 * 
	 * @return The combined number of lives of all players
	 */
	public int getCombinedLives() {
		int total = 0;
		for (Character c : resources.getPlayerList()) {
			total += c.getLives();
		}
		return total;
	}

	/**
	 * @return Whether the game has ended
	 */
	public boolean isGameOver() {
		checkWinner();
		return gameOver;
	}

	/**
	 * @return The winning character
	 */
	public Character getWinner() {
		checkWinner();
		return winner;
	}

	private void checkWinner() {
		if (playersRemaining() == 1) {
			gameOver = true;
			findWinner();
		}
	}

	/**
	 * Finds the last remaining player in the game
	 */
	private void findWinner() {
		for (Character c : resources.getPlayerList()) {
			if (c.getLives() > 0) {
				winner = c;
				break;
			}
		}
	}

	/**
	 * @return The number of players alive
	 */
	public int playersRemaining() {
		int remaining = 0;
		for (Character c : resources.getPlayerList()) {
			if (c.getLives() > 0) {
				remaining++;
			}
		}
		return remaining;
	}

	@Override
	public ArrayList<Character> getOrderedScores() {
		ArrayList<Character> scores = new ArrayList<Character>();
		scores.addAll(resources.getPlayerList());
		scores.sort((a, b) -> (a.getScore() > b.getScore()) ? -1 : (a.getScore() < b.getScore()) ? 1 : 0);
		return scores;
	}

	@Override
	public void resetLives() {
		setAllLives(maxLives);
	}

	@Override
	public void randomRespawn() {
		for (Character c : resources.getPlayerList()) {
			resources.getMap().spawn(c);
		}
	}
}
