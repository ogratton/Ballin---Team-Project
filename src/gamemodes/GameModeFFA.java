package gamemodes;

import resources.Character;

/**
 * Interface for all game modes which are not team based / free for all (FFA).
 * 
 * @author Luke
 *
 */
public interface GameModeFFA {
	public Character getWinner();

	public boolean isGameOver();

	public void setAllLives(int n);
}
