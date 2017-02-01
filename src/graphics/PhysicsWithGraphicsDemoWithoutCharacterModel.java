package graphics;

import java.awt.EventQueue;

import physics.Physics;
import resources.Character;
import resources.Map;
import resources.Resources;

/**
 * I try and smash graphics with physics. It works ish
 */

public class PhysicsWithGraphicsDemoWithoutCharacterModel {

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

		Resources.map = new Map(1920, 1200);

		// create physics thread
		Physics p = new Physics();
		p.start();

		// create ui thread
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				GraphicsWithoutCharacterModel g = new GraphicsWithoutCharacterModel(Resources.playerList, Resources.map);
				g.start();
			}
		});

	}
}
