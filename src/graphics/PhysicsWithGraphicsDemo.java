package graphics;

import java.io.IOException;

import ai.VeryBasicAI;
import ai.pathfinding.MapCosts;
import physics.Physics;
import resources.Character;
import resources.Map;
import resources.Map.Tile;
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
			tiles = mr.readMap("./resources/maps/map1.csv");
			System.out.println("Map Loaded");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
			
		}
		
		
		resources.setMap(new Map(1200, 675, tiles, Map.World.CAVE));
		
		MapCosts.genMapCostsMask(resources);
		
		Character player = new Character(Character.Class.WIZARD, 1);

		player.setX(400);
		player.setY(400);

		Character player1 = new Character(Character.Class.ELF, 2);

		player1.setX(534);
		player1.setY(454);

		Character player2 = new Character(Character.Class.WIZARD, 0);

		player2.setX(800);
		player2.setY(300);

		
		
		resources.addPlayerToList(player);
		resources.addPlayerToList(player1);
		resources.addPlayerToList(player2);

		/*for(int i = 0; i < 6; i++){
			
			Random r = new Random();
			
			Character playa = new Character(Character.Class.WIZARD, i+3);
			playa.setX(r.nextInt(1200));
			playa.setY(r.nextInt(675));
			resources.addPlayerToList(playa);
			
			VeryBasicAI ai = new VeryBasicAI(resources, playa);
			ai.start();
		}*/

		// create physics thread
		Physics p = new Physics(resources);
		p.start();

		VeryBasicAI ai1 = new VeryBasicAI(resources, player2);
		ai1.start();
		
		Graphics g = new Graphics(resources, null, false);
		g.start();

	}
	

}
