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
		
		resources.setMap(new Map(1200, 675, tiles, Map.World.CAKE, "Map"));
		new MapCosts(resources);
		// Create and add players
		Character player = new Character(Character.Class.WIZARD, 1, "You!");
		Character player1 = new Character(Character.Class.ARCHER, 0, "CPU1");
		Character player2 = new Character(Character.Class.HORSE, 0, "CPU2");
		resources.addPlayerToList(player);
		resources.addPlayerToList(player1);
		resources.addPlayerToList(player2);
		// Create AIs
		VeryBasicAI ai1 = new VeryBasicAI(resources, player1);
		player1.setAI(ai1);
		ai1.start();
		VeryBasicAI ai2 = new VeryBasicAI(resources, player2);
		ai2.start();
		player2.setAI(ai2);
		
		for(int i = 3; i < 8; i++){
			Character character = new Character(Character.Class.MONK, 0, "CPU" + i);
			resources.addPlayerToList(character);
			VeryBasicAI ai = new VeryBasicAI(resources, character);
			character.setAI(ai);
			ai.start();
		}
		
		// Create game mode (starts physics and graphics)
		//LastManStanding mode = new LastManStanding(resources, 5);
		Deathmatch mode = new Deathmatch(resources);
		//HotPotato mode = new HotPotato(resources);
		mode.start();
	}
}
