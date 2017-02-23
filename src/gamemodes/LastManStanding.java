package gamemodes;

import java.util.ArrayList;

import resources.Character;

/**
 * Play until only one player remains.
 * 
 * @author Luke
 *
 */
public class LastManStanding implements GameModeFFA {

	private int maxLives;
	private ArrayList<Character> players;
	private boolean gameOver = false;
	private Character winner;

	public LastManStanding(int maxLives, ArrayList<Character> players) {
		this.maxLives = maxLives;
		this.players = players;

		setAllLives(maxLives);
	}

	/**
	 * Set the number of lives for all players to the specified value
	 * 
	 * @param n
	 *            Number of lives
	 */
	public void setAllLives(int n) {
		for (Character c : players) {
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
		for (Character c : players) {
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
		for (Character c : players) {
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
		for (Character c : players) {
			if (c.getLives() > 0) {
				remaining++;
			}
		}
		return remaining;
	}
}
