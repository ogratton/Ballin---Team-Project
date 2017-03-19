package gamemodes;

import ai.FightingAI;
import audio.MusicPlayer;
import resources.Character;
import resources.Map;
import resources.MapCosts;
import resources.Resources;
import resources.Resources.Mode;

/**
 * I try and smash graphics with physics. It works ish
 */

public class PlayGame {

	public static void main(String[] args){
		
		Resources resources = new Resources();
		
		resources.setMusicPlayer(new MusicPlayer(resources, "grandma"));
		
		start(resources);
		
	}
	
	public static void start(Resources resources) {
		
		// TODO these should be parameters for start
		String mapName = "asteroid";
		resources.mode = Mode.LastManStanding; 
		Map.World style = Map.World.DESERT;
		
		
		// Music setting:
		
		if (!Resources.silent)
		{
			// 30 second gamemode needs 30 seconds of music
			if (resources.mode == Mode.Deathmatch)
			{
				if (style == Map.World.DESERT)
				{
					resources.getMusicPlayer().changePlaylist("paris30");
				}
				else if (style == Map.World.SPACE)
				{
					resources.getMusicPlayer().changePlaylist("ultrastorm30");
				}
				else
				{
					resources.getMusicPlayer().changePlaylist("thirty");
				}
			}
			// looping music
			else
			{
				if (style == Map.World.DESERT)
				{
					resources.getMusicPlayer().changePlaylist("parisLoop");
				}
				else if (style == Map.World.SPACE)
				{
					resources.getMusicPlayer().changePlaylist("ultrastorm");
				}
				else
				{
					resources.getMusicPlayer().changePlaylist("frog");
				}
			} 
		}
		
		
		resources.setMap(new Map(1200, 650, style, mapName));
		new MapCosts(resources);
		// Create and add players
		Character player = new Character(Character.Class.WIZARD, 1, "Player");
		// Will want a way to choose how many AIs to add
		Character player1 = new Character(Character.Class.ARCHER, 0, "CPU1");
		Character player2 = new Character(Character.Class.HORSE, 0, "CPU2");
		resources.addPlayerToList(player);
		resources.addPlayerToList(player1);
		resources.addPlayerToList(player2);
		// Create AIs
		FightingAI ai1 = new FightingAI(resources, player1);
		player1.setAI(ai1);
		ai1.start();
		FightingAI ai2 = new FightingAI(resources, player2);
		ai2.start();
		player2.setAI(ai2);
		
		for(int i = 3; i < 8; i++){
			Character character = new Character(Character.Class.MONK, 0, "CPU" + i);
			resources.addPlayerToList(character);
			FightingAI ai = new FightingAI(resources, character);
			character.setAI(ai);
			ai.start();
		}
		GameModeFFA mode;
		switch(resources.mode) {
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
		
		if (!Resources.silent)
		{
			// must resume after changing playlist
			if (resources.getMusicPlayer().isAlive())
			{
				resources.getMusicPlayer().resumeMusic();
			}
			else
			{
				resources.getMusicPlayer().start();
			} 
		}
		
		
	}
}
