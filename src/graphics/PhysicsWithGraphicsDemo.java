package graphics;

import java.awt.EventQueue;
import java.util.Random;

import physics.Physics;
import resources.Character;
import resources.Map;
import resources.Resources;

/**
 * I try and smash graphics with physics. It works ish
 */

public class PhysicsWithGraphicsDemo {

	public static void main(String[] args) {

		Character player = new Character(Character.Class.WIZARD);

		player.setX(46);
		player.setY(46);

		Character player1 = new Character(Character.Class.ELF);

		player1.setX(534);
		player1.setY(424);

		Character player2 = new Character(Character.Class.WIZARD);

		player2.setX(999);
		player2.setY(234);

		Resources.playerList.add(player);
		Resources.playerList.add(player1);
		Resources.playerList.add(player2);

		for(int i = 0; i < 6; i++){
			
			Random r = new Random();
			
			Character playa = new Character(Character.Class.WIZARD);
			playa.setX(r.nextInt(1920));
			playa.setY(r.nextInt(1080));
			Resources.playerList.add(playa);
		}
		
		Resources.map = new Map(1920, 1200);

		// create physics thread
		Physics p = new Physics();
		p.start();

		// create ui thread
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Graphics g = new Graphics(Resources.playerList, Resources.map);
				g.start();
			}
		});

	}
}
