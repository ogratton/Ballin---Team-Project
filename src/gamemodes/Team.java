package gamemodes;

import java.util.ArrayList;
import resources.Character;

/**
 * Class to hold data for teams, intended to be used in team-based game modes
 * such as hockey. Contains some helpful functions which could be used in other
 * game modes, e.g. all players stored as a "team"
 * 
 * @author Luke
 *
 */
public class Team {

	private ArrayList<Character> members;
	private String name;
	private int score;

	public Team() {
		this.members = new ArrayList<Character>();
		this.score = 0;
	}

	/**
	 * Add a single character to the team
	 * 
	 * @param c
	 *            Character to be added to the team
	 */
	public void addMember(Character c) {
		members.add(c);
	}

	/**
	 * Add multiple characters to the team (does not overwrite previous members)
	 * 
	 * @param cs
	 *            A list of characters to be added to the team
	 */
	public void addMembers(ArrayList<Character> cs) {
		for (Character c : cs) {
			members.add(c);
		}
	}

	/**
	 * Get the score of the team, e.g. total goals scored
	 * 
	 * @return The score of the team
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Get the total score of all members of the team, could be used for team
	 * deathmatch etc
	 * 
	 * @return The combined score of all members of the team
	 */
	public int getCombinedScore() {
		int total = 0;
		for (Character c : members) {
			total += c.getScore();
		}
		return total;
	}

	/**
	 * Get the total number of lives of all members of the team
	 * 
	 * @return The combined number of lives of all members of the team
	 */
	public int getCombinedLives() {
		int total = 0;
		for (Character c : members) {
			total += c.getLives();
		}
		return total;
	}

	/**
	 * Increment the team's score by 1
	 */
	public void incrementScore() {
		score++;
	}

	/**
	 * @return The current team name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The new name of the team
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The number of members on the team
	 */
	public int getNoOfPlayers() {
		return members.size();
	}

	/**
	 * Set the number of lives for all members on the team to the specified
	 * value
	 * 
	 * @param n
	 *            Number of lives
	 */
	public void setAllLives(int n) {
		for (Character c : members) {
			c.setLives(n);
		}
	}
	
	/**
	 * @return All members of the team
	 */
	public ArrayList<Character> getMembers() {
		return members;
	}
}
