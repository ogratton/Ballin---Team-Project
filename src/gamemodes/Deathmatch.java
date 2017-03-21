package gamemodes;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Powerup;
import resources.Resources;
import resources.Resources.Mode;

/**
 * Play until the timer runs out.
 * 
 * @author Luke
 *
 */
public class Deathmatch extends Thread implements GameModeFFA {

	private Character winner;
	private Resources resources;
	private int timer = 30;
	private boolean isServer = false;

	private String victoryMusic = "grandma";

	/**
	 * Create a new deathmatch game.
	 * 
	 * @param resources
	 *            The resources object being used for the game.
	 */
	public Deathmatch(Resources resources) {
		this.resources = resources;

		// Set up game
		setAllLives(-1);
		randomRespawn();
		resources.mode = Mode.Deathmatch;
		resources.gamemode = this;
	}

	/**
	 * Create a new deathmatch game.
	 * 
	 * @param resources
	 *            The resources object being used for the game.
	 */
	public Deathmatch(Resources resources, boolean isServer) {
		this.resources = resources;

		// Set up game
		setAllLives(-1);
		randomRespawn();
		resources.mode = Mode.Deathmatch;
		resources.gamemode = this;
		this.isServer = isServer;
	}

	@Override
	public int getTime() {
		return this.timer;
	}

	/*
	 * Run the logic of this game mode.
	 */
	public void run() {
		// Start game
		Physics p = new Physics(resources, false);
		Graphics g = new Graphics(resources, null, false);
		if (!isServer) {
			SwingUtilities.invokeLater(g);
		}

		try {
			Thread.sleep(1500);
			g.setCountdown(2);
			Thread.sleep(1500);
			g.setCountdown(1);
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		g.begin();
		p.start();

		// Graphics g = new Graphics(resources, null, false);
		// g.start();

		while (!isGameOver()) {
			try {
				System.out.println("Time remaining: " + timer + " seconds");
				if (timer % 5 == 0) {
					spawnPowerup();
				}
				Thread.sleep(1000);
				timer--;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Game has ended
		p.pause();
		// TODO pause/change music too
		if (!Resources.silent) {
			resources.getMusicPlayer().changePlaylist(victoryMusic);
			resources.getMusicPlayer().resumeMusic();
		}

		System.out.println("WE HAVE A WINNER");
		getWinner();
		System.out.println(
				"Player " + winner.getPlayerNumber() + " achieved the highest score of  " + winner.getScore() + "!");
		ArrayList<Character> scores = resources.getOrderedScores();
		for (Character c : scores) {
			System.out.print("Player " + c.getPlayerNumber() + " had score " + c.getScore() + ", ");
		}
		System.out.println();
	}

	/**
	 * Spawn a random powerup in a random location.
	 */
	private void spawnPowerup() {
		Powerup p = new Powerup();
		resources.addPowerup(p);
		resources.getMap().spawnPowerup(p);
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
		winner = resources.getOrderedScores().get(0);
		return winner;
	}

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
