package gamemodes;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Map;
import resources.Resources;
import resources.Resources.Mode;
import ui.UIRes;

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
	private boolean isServer;
	private boolean singlePlayer = false;
	private boolean endGame = false;

	/**
	 * Create a new hot potato game mode.
	 * 
	 * @param resources
	 *            The resources object being used for the game.
	 */
	public HotPotato(Resources resources) {
		this.resources = resources;

		// Set up game
		setAllLives(1);
		randomRespawn();

		resources.mode = Mode.HotPotato;
		resources.gamemode = this;
		this.rand = new Random();
	}

	/**
	 * Create a new hot potato game mode.
	 * 
	 * @param resources
	 *            The resources object being used for the game.
	 */
	public HotPotato(Resources resources, boolean isServer, boolean singlePlayer) {
		this.resources = resources;

		// Set up game
		setAllLives(1);
		if(!isServer) {
			randomRespawn();
		}

		this.singlePlayer = singlePlayer;

		resources.mode = Mode.HotPotato;
		resources.gamemode = this;
		this.rand = new Random();
		this.isServer = isServer;
	}

	/*
	 * Run the logic of this game mode.
	 */
	public void run() {
		resources.setTimer(0);
		// start the game
		Physics p = new Physics(resources, false);
		if (!isServer) {
			Graphics g = new Graphics(resources, null, false);
			SwingUtilities.invokeLater(g);
		}

		if (singlePlayer) {

			try {
				if (!Resources.silent) UIRes.dingSound.play();
				Thread.sleep(1000);
				resources.setCountdown(2);
				if (!Resources.silent) UIRes.dingSound.play();
				Thread.sleep(1000);
				resources.setCountdown(1);
				if (!Resources.silent) UIRes.dingSound.play();
				Thread.sleep(1000);
				resources.setCountdown(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		p.start();
		
		// TODO change to be specific to tile type
		resources.setSong("frog");

		placeBomb();
		while (!isGameOver() && !endGame) {
			try {
				Thread.sleep(100);
				resources.incrementTimer(1);
				// Detonate bomb every 5 seconds
				if (resources.getTimer() % 50 == 0 && playersRemaining() > 1) {
					explodeBomb();
					placeBomb();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Game has ended
		p.pause();
		resources.setGameOver(true);
		resources.setSong("grandma");
	}

	/**
	 * Place a bomb on a random character. (Public for JUnit)
	 */
	public void placeBomb() {
		boolean success = false;
		int p;
		ArrayList<Character> players = resources.getPlayerList();
		Character c;
		while (!success) {
			p = rand.nextInt(players.size());
			c = players.get(p);
			if (!c.isExploding() && !c.isDead() && !c.hasBomb()) {
				c.hasBomb(true);
				success = true;
			}
		}
	}

	/**
	 * Find the character with a bomb placed on them and kill them. (Public for
	 * JUnit)
	 */
	public void explodeBomb() {
		for (Character c : resources.getPlayerList()) {
			if (c.hasBomb()) {
				c.setExploding(true);
				c.setTimeOfDeath(resources.getGlobalTimer());
				if (!Resources.silent) UIRes.explode.play();
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
	public ArrayList<Character> getWinners() {
		checkWinner();
		ArrayList<Character> winners = new ArrayList<>();
		winners.add(winner);
		return winners;
	}

	/**
	 * Check if the game is over and if so find the last player alive.
	 */
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

	/**
	 * @return An ArrayList of each character's time of death, in ascending
	 *         order.
	 */
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
	public void setEndGame(boolean b) {
		endGame = b;
	}
}
