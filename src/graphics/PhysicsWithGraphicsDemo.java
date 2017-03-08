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
			tiles = mr.readMap("./resources/maps/map1.csv");
			System.out.println("Map Loaded");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
			
		}
		
		
		resources.setMap(new Map(1200, 675, tiles, Map.World.CAVE, "Test Map"));
		new MapCosts(resources);
		
		Character player = new Character(Character.Class.WIZARD, 1);
		resources.getMap().spawn(player, new Point(400,400));
		resources.addPlayerToList(player);

		Character player1 = new Character(Character.Class.WARRIOR, 0);
		resources.getMap().spawn(player1);
		VeryBasicAI ai1 = new VeryBasicAI(resources, player1);
		ai1.setBehaviour("aggressive");
		resources.addPlayerToList(player1);

//		Character player2 = new Character(Character.Class.ARCHER, 2);
//		resources.getMap().spawn(player2);
//		VeryBasicAI ai2 = new VeryBasicAI(resources, player2);
//		resources.addPlayerToList(player2);
//
//		Character player3 = new Character(Character.Class.MONK, 3);
//		resources.getMap().spawn(player2);
//		VeryBasicAI ai3 = new VeryBasicAI(resources, player3);
//		resources.addPlayerToList(player3);

		/*for(int i = 0; i < 6; i++){
			
			
			Character playa = new Character(Character.Class.WIZARD, i+3);
			Point loc = resources.getMap().randPointOnMap();
			playa.setX(loc.getX());
			playa.setY(loc.getY());
			resources.addPlayerToList(playa);
			
			VeryBasicAI ai = new VeryBasicAI(resources, playa);
			ai.start();
		}*/

		// create physics thread
		Physics p = new Physics(resources, true);
		p.start();

		
		ai1.start();
//		ai2.start();
//		ai3.start();
		
		/*Graphics g = new Graphics(resources, null, false);
		g.start();*/
		SwingUtilities.invokeLater(new Graphics(resources, null, true));



	}
	

}
