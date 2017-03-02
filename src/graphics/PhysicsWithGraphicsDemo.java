package graphics;

import java.awt.Point;
import java.io.IOException;

import javax.swing.SwingUtilities;

import ai.VeryBasicAI;
import ai.pathfinding.MapCosts;
import physics.Physics;
import resources.Character;
import resources.Map;
import resources.MapReader;
import resources.Resources;

/**
 * I try and smash graphics with physics. It works ish
 */

public class PhysicsWithGraphicsDemo {

	public static void main(String[] args){
		
		Resources resources = new Resources();
		
		start(resources);
		
	}
	
	public static void start(Resources resources) {
		
		// make the map the default just in case the following fails
		Map.Tile[][] tiles = null;	
		MapReader mr = new MapReader();	
		try
		{
			tiles = mr.readMap("./resources/maps/map0.csv");
			System.out.println("Map Loaded");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
			
		}
		
		
		resources.setMap(new Map(1200, 675, tiles, Map.World.CAVE));
		new MapCosts(resources);
		
		Character player = new Character(Character.Class.WIZARD, 1);

		Point coords = new Point(400,400);
		Point tile = resources.getMap().tileCoords(coords.x, coords.y);
		coords = resources.getMap().tileCoordsToMapCoords(tile.x, tile.y);
		
		player.setX(coords.x);
		player.setY(coords.y);

		Character player1 = new Character(Character.Class.WARRIOR, 2);

		player1.setX(534);
		player1.setY(454);

		Character player2 = new Character(Character.Class.ARCHER, 3);

		player2.setX(780);
		player2.setY(400);

		Character player3 = new Character(Character.Class.MONK, 4);
		player3.setX(1023);
		player3.setY(500);

		
		resources.addPlayerToList(player);
		resources.addPlayerToList(player1);
		resources.addPlayerToList(player2);
		resources.addPlayerToList(player3);

		for(int i = 0; i < 6; i++){
			
			
			Character playa = new Character(Character.Class.WIZARD, i+3);
			Point loc = resources.getMap().randPointOnMap();
			playa.setX(loc.getX());
			playa.setY(loc.getY());
			resources.addPlayerToList(playa);
			
			VeryBasicAI ai = new VeryBasicAI(resources, playa);
			ai.start();
		}

		// create physics thread
		Physics p = new Physics(resources);
		p.start();

		VeryBasicAI ai1 = new VeryBasicAI(resources, player2);
		ai1.start();
		
		VeryBasicAI ai2 = new VeryBasicAI(resources, player3);
		ai2.start();
		
		VeryBasicAI ai3 = new VeryBasicAI(resources, player1);
		ai3.start();
		
		SwingUtilities.invokeLater(new Graphics(resources, null, false));



	}
	

}
