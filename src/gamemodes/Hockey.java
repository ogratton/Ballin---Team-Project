package gamemodes;

import java.util.ArrayList;

public class Hockey implements GameModeTeams {

	private Team team1;
	private Team team2;

	public Hockey(Team team1, Team team2) {
		this.team1 = team1;
		this.team2 = team2;
		// -1 lives = infinite
		team1.setAllLives(-1);
		team2.setAllLives(-1);
	}

	/**
	 * @param t 1 for team 1, 2 for team 2
	 * @return The specified team
	 */
	public Team getTeam(int t) {
		if (t == 1) {
			return team1;
		} else if (t == 2) {
			return team2;
		}
		return null;
	}
}
