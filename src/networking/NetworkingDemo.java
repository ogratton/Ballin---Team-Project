package networking;

import java.awt.EventQueue;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import javax.swing.SwingUtilities;

import ai.pathfinding.MapCosts;
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
		
		Map map = new Map(1200, 675, tiles, Map.World.CAVE);
		
		
		Character newPlayer;
		Resources resources = new Resources();
		resources.setMap(map);
		new MapCosts(resources);
		
		Point coords = new Point(400,400);
		Point tile = resources.getMap().tileCoords(coords.x, coords.y);
		coords = resources.getMap().tileCoordsToMapCoords(tile.x, tile.y);
		
		List<ClientInformation> clients = session.getAllClients();
		for(int i=0; i<clients.size(); i++) {
			Point point = map.randPointOnMap();
			UUID id = clients.get(i).getId();
			newPlayer = new Character(Character.Class.ARCHER, 1);
			newPlayer.setX(coords.x);
			newPlayer.setY(coords.y);
			newPlayer.setId(id);
			newPlayer.addObserver(new ClientUpdater(session.getId(), resourcesMap, sessions));
			resources.addPlayerToList(newPlayer);
		}
		

		

		// create physics thread
		Physics p = new Physics(resources);
		p.start();

		resourcesMap.put(session.getId(), resources);

		SwingUtilities.invokeLater(new Graphics(resources, null, false));
	}
}
