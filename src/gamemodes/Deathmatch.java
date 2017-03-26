package gamemodes;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Map;
import resources.Map.World;
import resources.Powerup;
import resources.Resources;
import resources.Resources.Mode;
import ui.UIRes;

/**
 * Play until the timer runs out.
 * 
 * @author Luke
 *
 */
public class Deathmatch extends Thread implements GameModeFFA {

	private Character winner;
	private Resources resources;
	private boolean isServer = false;
	private boolean singlePlayer = false;
	private boolean endGame = false;

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
	public Deathmatch(Resources resources, boolean isServer, boolean singlePlayer) {
		this.resources = resources;
		this.singlePlayer = singlePlayer;

		// Set up game
		setAllLives(-1);
		if (!isServer) {
			randomRespawn();
		}
		resources.mode = Mode.Deathmatch;
		resources.gamemode = this;
		this.isServer = isServer;
	}

	/*
	 * Run the logic of this game mode.
	 */
	public void run() {
		resources.setTimer(30);
		// Start game
		Physics p = new Physics(resources, false);

		if (!isServer) {
			Graphics g = new Graphics(resources, null, false);
			SwingUtilities.invokeLater(g);
		}

		if (singlePlayer)
		{

			try
			{
				if (!Resources.silent)
					UIRes.dingSound.play(resources.getSFXGain());
				Thread.sleep(1000);
				resources.setCountdown(2);
				if (!Resources.silent)
					UIRes.dingSound.play(resources.getSFXGain());
				Thread.sleep(1000);
				resources.setCountdown(1);
				if (!Resources.silent)
					UIRes.dingSound.play(resources.getSFXGain());
				Thread.sleep(1000);
				resources.setCountdown(0);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		p.start();
		
		World style = resources.getMap().getWorldType();
		switch (style)
		{
			case SPACE:
				resources.setSong("ultrastorm30");
				break;
			case CAKE:
				resources.setSong("rage30");
				break;
			default:
				resources.setSong("thirty");
				break;
		}

		// Graphics g = new Graphics(resources, null, false);
		// g.start();

		while (!isGameOver() && !endGame) {
			try {
				if (resources.getTimer() == 1) {
					Thread.sleep(1000);
					resources.incrementTimer(-1);
					//Thread.sleep(1000);
					break;
				} else {

					if (resources.getTimer() % 5 == 0) {
						spawnPowerup();
					}

					Thread.sleep(1000);
					resources.incrementTimer(-1);

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Game has ended
		p.pause();
		
		resources.setSong(victoryMusic);
		resources.setGameOver(true);
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
		return resources.getTimer() <= 0;
	}

	/**
	 * @return The winning character
	 */
	public ArrayList<Character> getWinners() {
		ArrayList<Character> scores = resources.getOrderedScores();
		winner = scores.get(0);
		ArrayList<Character> winners = new ArrayList<>();
		int score = winner.getScore();
		for (Character c : scores) {
			if (score == c.getScore()) {
				winners.add(c);
			}
		}
		return winners;
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

	@Override
	public void setEndGame(boolean b) {
		endGame = b;
	}

}
