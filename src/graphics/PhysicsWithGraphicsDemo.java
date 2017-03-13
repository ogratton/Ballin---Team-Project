package graphics;

import java.awt.Point;
import java.io.IOException;

import javax.swing.SwingUtilities;

import ai.BasicAI;
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
		
		
		resources.setMap(new Map(1200, 650, tiles, Map.World.CAVE, "Test Map"));
		new MapCosts(resources);
		
		// Player 1 is the actual human
		Character player = new Character(Character.Class.WIZARD, 1);
		resources.getMap().spawn(player, new Point(400,400));
		resources.addPlayerToList(player);
		
		// player 0 is for our debug paths
		Character player1 = new Character(Character.Class.WARRIOR, 0);
		resources.getMap().spawn(player1);
		BasicAI ai1 = new BasicAI(resources, player1);
//		ai1.setBehaviour("aggressive");
		resources.addPlayerToList(player1);
		
		int numPlayers = 2;
		
		Character player2 = new Character(Character.Class.ARCHER, numPlayers);
		resources.getMap().spawn(player2);
		BasicAI ai2 = new BasicAI(resources, player2);
		resources.addPlayerToList(player2);
		numPlayers++;
//
//		Character player3 = new Character(Character.Class.MONK, numPlayers);
//		resources.getMap().spawn(player2);
//		VeryBasicAI ai3 = new VeryBasicAI(resources, player3);
//		resources.addPlayerToList(player3);
//		numPlayers++;

		/*for(int i = 0; i < 6; i++){
			
			
			Character playa = new Character(Character.Class.WIZARD, numPlayers);
			Point loc = resources.getMap().randPointOnMap();
			playa.setX(loc.getX());
			playa.setY(loc.getY());
			resources.addPlayerToList(playa);
			numPlayers++;
			
			VeryBasicAI ai = new VeryBasicAI(resources, playa);
			ai.setBehaviour("aggressive");
			ai.start();
		}*/

		// create physics thread
		Physics p = new Physics(resources, false);
		p.start();

		
		ai1.start();
		ai2.start();
//		ai3.start();
		
		/*Graphics g = new Graphics(resources, null, false);
		g.start();*/
		SwingUtilities.invokeLater(new Graphics(resources, null, true));



	}
	

}
