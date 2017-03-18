package graphics;

import java.awt.Point;

import javax.swing.SwingUtilities;

import ai.FightingAI;
import ai.pathfinding.MapCosts;
import physics.Physics;
import resources.Character;
import resources.Map;
import resources.Resources;

/**
 * I try and smash graphics with physics. It works ish
 */

@Deprecated
public class PhysicsWithGraphicsDemo {

	public static void main(String[] args){
		
		Resources resources = new Resources();
		
		start(resources);
		
	}
	
	public static void start(Resources resources) {
				
		
		resources.setMap(new Map(1200, 650, Map.World.CAVE, "map1"));
		new MapCosts(resources);
		
		// Player 1 is the actual human
		Character player = new Character(Character.Class.WIZARD, 1);
		resources.getMap().spawn(player, new Point(400,400));
		resources.addPlayerToList(player);
		
		// player 0 is for our debug paths
		Character player1 = new Character(Character.Class.WARRIOR, 0);
		resources.getMap().spawn(player1);
		FightingAI ai1 = new FightingAI(resources, player1);
//		ai1.setBehaviour("aggressive");
		resources.addPlayerToList(player1);
		
		int numPlayers = 2;
		
		Character player2 = new Character(Character.Class.ARCHER, numPlayers);
		resources.getMap().spawn(player2);
		FightingAI ai2 = new FightingAI(resources, player2);
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
