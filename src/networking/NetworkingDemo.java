package networking;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import javax.swing.SwingUtilities;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Map;
import resources.MapReader;
import resources.Resources;

/**
 * I try and smash graphics with physics. It works ish
 */

public class NetworkingDemo {

	public static void startServerGame(Session session, ConcurrentMap<UUID, Resources> resourcesMap, ConcurrentMap<UUID, Session> sessions) {
		
		Character newPlayer;
		Random r = new Random();
		Resources resources = new Resources();
		double x;
		double y;
		List<ClientInformation> clients = session.getAllClients();
		CharacterInfo info;
		for(int i=0; i<clients.size(); i++) {
			x = r.nextInt(1200);
			y = r.nextInt(675);
			UUID id = clients.get(i).getId();
			newPlayer = new Character(Character.Class.ELF, 1);
			newPlayer.setX(x);
			newPlayer.setY(y);
			newPlayer.setId(id);
			newPlayer.addObserver(new ClientUpdater(session.getId(), resourcesMap, sessions));
			resources.addPlayerToList(newPlayer);
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

		resourcesMap.put(session.getId(), resources);

		SwingUtilities.invokeLater(new Graphics(resources, null, false));
	}
}
