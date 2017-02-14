package graphics;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Random;

import ai.RunRight;
import audio.MusicPlayer;
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
		
		Character player = new Character(Character.Class.TEST);

		player.setX(100);
		player.setY(100);

		Character player1 = new Character(Character.Class.ELF);

		player1.setX(534);
		player1.setY(454);

		Character player2 = new Character(Character.Class.WIZARD);

		player2.setX(500);
		player2.setY(100);

		
		
		resources.addPlayerToList(player);
		resources.addPlayerToList(player1);
		resources.addPlayerToList(player2);

		for(int i = 0; i < 6; i++){
			
			Random r = new Random();
			
			Character playa = new Character(Character.Class.WIZARD);
			playa.setX(r.nextInt(1200));
			playa.setY(r.nextInt(675));
			resources.addPlayerToList(playa);
		}
		
		// make the map the default just in case the following fails
		Map.Tile[][] tiles = null;	
		MapReader mr = new MapReader();	
		try
		{
			tiles = mr.readMap("./resources/maps/map1.csv");
			System.out.println("I guess it worked then");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
			
		}
		
		resources.setMap(new Map(1200, 675, tiles, Map.World.CAVE));

		// create physics thread
		Physics p = new Physics(resources);
		p.start();

		RunRight run = new RunRight(player2);
		run.start();
		
		// create ui thread
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Graphics g = new Graphics(resources);
				g.start();
			}
		});

	}
}
