package gamemodes;

import ai.AITemplate;
import ai.FightingAI;
import ai.HotPotatoAI;
import audio.MusicPlayer;
import resources.Character;
import resources.Map;
import resources.MapCosts;
import resources.Resources;
import resources.Resources.Mode;

/**
 * Set up the game objects and launch the game.
 */

public class PlayGame {

	/**
	 * Start the game directly for testing purposes only.
	 */
	public static void main(String[] args) {

		Resources resources = new Resources();

		resources.setMusicPlayer(new MusicPlayer(resources, "grandma"));

		start(resources);

	}

	/**
	 * Initialise game objects and start the game.
	 * 
	 * @param resources
	 *            The resources object being used for the game.
	 */
	public static void start(Resources resources) {

		// TODO these should be parameters for start
		String mapName = "asteroid";
		resources.mode = Mode.Deathmatch;
		Map.World style = Map.World.CAVE;

		// Music setting:

		if (!Resources.silent) {
			// 30 second gamemode needs 30 seconds of music
			if (resources.mode == Mode.Deathmatch) {
				if (style == Map.World.DESERT) {
					resources.getMusicPlayer().changePlaylist("paris30");
				} else if (style == Map.World.SPACE) {
					resources.getMusicPlayer().changePlaylist("ultrastorm30");
				} else {
					resources.getMusicPlayer().changePlaylist("thirty");
				}
			}
			// looping music
			else {
				if (style == Map.World.DESERT) {
					resources.getMusicPlayer().changePlaylist("parisLoop");
				} else if (style == Map.World.SPACE) {
					resources.getMusicPlayer().changePlaylist("ultrastorm");
				} else {
					resources.getMusicPlayer().changePlaylist("frog");
				}
			}
		}
		
		resources.clearPlayerList();
		
		resources.setMap(new Map(1200, 650, style, mapName));
		new MapCosts(resources);
		// Create and add players
		Character player = new Character(Character.Class.WIZARD, 1, "Player");
		// Will want a way to choose how many AIs to add

		resources.addPlayerToList(player);


		for (int i = 1; i < 8; i++) {
			Character character = new Character(Character.Class.getRandomClass(), i+1, "CPU" + i);
			resources.addPlayerToList(character);
			AITemplate ai = (resources.mode == Mode.HotPotato) ? new HotPotatoAI(resources, character) : new FightingAI(resources, character);
			character.setAI(ai);
		}
		GameModeFFA mode;
		switch (resources.mode) {
		case Deathmatch:
			mode = new Deathmatch(resources);
			break;
		case LastManStanding:
			mode = new LastManStanding(resources, 5);
			break;
		case HotPotato:
			mode = new HotPotato(resources);
			break;
		default:
			mode = new Deathmatch(resources);
			break;
		}
		((Thread) mode).start();

		if (!Resources.silent) {
			// must resume after changing playlist
			if (resources.getMusicPlayer().isAlive()) {
				resources.getMusicPlayer().resumeMusic();
			} else {
				resources.getMusicPlayer().start();
			}
		}
	}
}
