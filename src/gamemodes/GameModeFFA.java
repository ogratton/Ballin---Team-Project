package gamemodes;

import java.util.ArrayList;

import resources.Character;

/**
 * Interface for all game modes which are not team based / free for all (FFA).
 * 
 * @author Luke
 *
 */
public interface GameModeFFA {
	/**
	 * @return The winning character
	 */
	public Character getWinner();
	
	/**
	 * @return An ArrayList of all characters, order by descending score
	 */
	public ArrayList<Character> getOrderedScores();

	/**
	 * @return Whether the game has ended
	 */
	public boolean isGameOver();

	/**
	 * Reset all player lives to maximum
	 */
	public void resetLives();
	
	/**
	 * Respawns all the players in a random location
	 */
	public void randomRespawn();
}
