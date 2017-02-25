package gamemodes;

import java.util.ArrayList;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Resources;

/**
 * Play until the timer runs out.
 * 
 * @author Luke
 *
 */
public class Deathmatch extends Thread implements GameModeFFA {

	private boolean gameOver = false;
	private Character winner;
	private Resources resources;
	private int timer = 30;

	public Deathmatch(Resources resources) {
		this.resources = resources;

		// Set up game
		setAllLives(-1);
		randomRespawn();
	}

	public void run() {
		// Start game
		Physics p = new Physics(resources);
		p.start();
		Graphics g = new Graphics(resources, null, false);
		g.start();
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
		System.out.println("WE HAVE A WINNER");
		getWinner();
		System.out.println(
				"Player " + winner.getPlayerNumber() + " achieved the highest score of  " + winner.getScore() + "!");
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
	 * @return Whether the game has ended
	 */
	public boolean isGameOver() {
		return timer <= 0;
	}

	/**
	 * @return The winning character
	 */
	public Character getWinner() {
		winner = getOrderedScores().get(0);
		return winner;
	}

	@Override
	public ArrayList<Character> getOrderedScores() {
		ArrayList<Character> scores = new ArrayList<Character>();
		scores.addAll(resources.getPlayerList());
		scores.sort((a, b) -> (a.getScore() > b.getScore()) ? -1 : (a.getScore() < b.getScore()) ? 1 : 0);
		return scores;
	}

	// Useless for this game mode
	@Override
	public void resetLives() {
		setAllLives(-1);
	}

	@Override
	public void randomRespawn() {
		for (Character c : resources.getPlayerList()) {
			resources.getMap().spawn(c);
		}
	}
}
