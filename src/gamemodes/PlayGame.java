package gamemodes;

import java.io.IOException;

import ai.VeryBasicAI;
import ai.pathfinding.MapCosts;
import resources.Character;
import resources.Map;
import resources.MapReader;
import resources.Resources;

/**
 * I try and smash graphics with physics. It works ish
 */

public class PlayGame {

	public static void main(String[] args){
		
		Resources resources = new Resources();
		
		start(resources);
		
	}
	
	public static void start(Resources resources) {
		
		// Create default map in case the following fails
		Map.Tile[][] tiles = null;	
		// Create map
		MapReader mr = new MapReader();	
		try
		{
			tiles = mr.readMap("./resources/maps/map1.csv");
			System.out.println("Map Loaded");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
			
		}
		
		resources.setMap(new Map(1200, 675, tiles, Map.World.CAVE, "Map"));
		new MapCosts(resources);
		// Create and add players
		Character player = new Character(Character.Class.WIZARD, 1);
		Character player1 = new Character(Character.Class.ARCHER, 2);
		Character player2 = new Character(Character.Class.WIZARD, 0);
		resources.addPlayerToList(player);
		resources.addPlayerToList(player1);
		resources.addPlayerToList(player2);
		// Create AIs
		VeryBasicAI ai1 = new VeryBasicAI(resources, player1);
		ai1.start();
		VeryBasicAI ai2 = new VeryBasicAI(resources, player2);
		ai2.start();
		
		// Create game mode (starts physics and graphics)
		//LastManStanding mode = new LastManStanding(resources, 5);
		Deathmatch mode = new Deathmatch(resources);
		//HotPotato mode = new HotPotato(resources);
		mode.start();
	}
}
