package gamemodes;

import java.util.ArrayList;

import resources.Character;

/**
 * Interface for all game modes which are team based.
 * 
 * @author Luke
 *
 */
public interface GameModeTeams {
	public Team getTeam(int t);
	public Team getWinner();
	public ArrayList<Character> getOrderedScores();
	public ArrayList<Character> getOrderedScores(int i);
	public boolean isGameOver();
	public void resetLives();
	public void randomRespawn();
}
