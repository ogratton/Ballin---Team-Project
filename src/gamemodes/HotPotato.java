package gamemodes;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Resources;
import resources.Resources.Mode;

/**
 * Random player is given a bomb to hold. After a few seconds the person holding
 * the bomb explodes. Collide with other players to pass the bomb. Play until
 * only one player remains.
 * 
 * @author Luke
 *
 */
public class HotPotato extends Thread implements GameModeFFA {

	private boolean gameOver = false;
	private Character winner;
	private Resources resources;
	private Random rand;

	public HotPotato(Resources resources) {
		this.resources = resources;

		// Set up game
		setAllLives(1);
		randomRespawn();

		resources.mode = Mode.HotPotato;
		resources.gamemode = this;
		this.rand = new Random();
	}

	public void run() {
		// Start game
		Physics p = new Physics(resources, false);
		p.start();
		SwingUtilities.invokeLater(new Graphics(resources, null, false));

		int timer = 0; // 10*speed of normal timer
		placeBomb();
		while (!isGameOver()) {
			try {
				Thread.sleep(100);
				timer += 1;
				// Detonate bomb every 5 seconds
				if (timer % 50 == 0 && playersRemaining() > 1) {
					explodeBomb();
					placeBomb();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Game has ended
		p.pause();
		System.out.println("WE HAVE A WINNER");
		System.out.println(winner.getName() + " survived the gauntlet!");
		for (Character c : getOrderedTimesOfDeath()) {
			if (c.getTimeOfDeath() != -1) {
				System.out.println(c.getName() + " survived " + c.getTimeOfDeath() / 100 + " seconds.");
			}
		}
	}

	private void placeBomb() {
		boolean success = false;
		int p;
		ArrayList<Character> players = resources.getPlayerList();
		Character c;
		while (!success) {
			p = rand.nextInt(players.size());
			c = players.get(p);
			if (!c.isExploding() && !c.isDead() && !c.hasBomb()) {
				c.hasBomb(true);
				System.out.println("Player " + c.getPlayerNumber() + " has been given the bomb!");
				success = true;
			}
		}
	}

	private void explodeBomb() {
		for (Character c : resources.getPlayerList()) {
			if (c.hasBomb()) {
				c.setExploding(true);
				c.setTimeOfDeath(resources.getGlobalTimer());
				System.out.println("Player " + c.getPlayerNumber() + " exploded!");
				break;
			}
		}
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

	public ArrayList<Character> getOrderedTimesOfDeath() {
		ArrayList<Character> times = new ArrayList<Character>();
		times.addAll(resources.getPlayerList());
		times.sort((a, b) -> (a.getTimeOfDeath() > b.getTimeOfDeath()) ? -1
				: (a.getTimeOfDeath() < b.getTimeOfDeath()) ? 1 : 0);
		return times;
	}

	@Override
	public void resetLives() {
		setAllLives(1);
	}

	@Override
	public void randomRespawn() {
		for (Character c : resources.getPlayerList()) {
			resources.getMap().spawn(c);
		}
	}

	@Override
	public int getTime() {
		// TODO Auto-generated method stub
		return 0;
	}
}
