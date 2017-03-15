package networking;


import java.awt.Point;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;

import ai.pathfinding.MapCosts;
import gamemodes.Deathmatch;
import gamemodes.GameModeFFA;
import gamemodes.HotPotato;
import gamemodes.LastManStanding;
import resources.Character;
import resources.Map;
import resources.MapReader;
import resources.Resources;

/**
 * I try and smash graphics with physics. It works ish
 */

public class NetworkingDemo {

	public static void startServerGame(Session session, ConcurrentMap<String, Resources> resourcesMap, ConcurrentMap<String, Session> sessions, ConcurrentMap<String, Connection> connections) {
		
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
		
		Map map = new Map(1200, 650, tiles, Map.World.CAVE, "");
		
		
		Character newPlayer;
		Resources resources = new Resources();
		resources.setMap(map);
		new MapCosts(resources);
		
		GameModeFFA mode;
		switch(resources.mode) {
		case Deathmatch:
			mode = new Deathmatch(resources);
			break;
		case LastManStanding:
			mode = new LastManStanding(resources, 5);
			break;
		case HotPotato:
			mode = new HotPotato(resources);
			break;
		default:
			mode = new Deathmatch(resources);
			break;
		}
		
		System.out.println(resources.gamemode);
		
		List<ClientInformation> clients = session.getAllClients();
		for(int i=0; i<clients.size(); i++) {
			Point coords = resources.getMap().randPointOnMap();
			Point tile = resources.getMap().tileCoords(coords.x, coords.y);
			coords = resources.getMap().tileCoordsToMapCoords(tile.x, tile.y);
			
			String id = clients.get(i).getId();
			newPlayer = new Character(Character.Class.ARCHER, i+1);
			newPlayer.setX(coords.x);
			newPlayer.setY(coords.y);
			newPlayer.setId(id);
			newPlayer.addObserver(new ClientUpdater(session.getId(), resourcesMap, sessions, connections));
			resources.addPlayerToList(newPlayer);
		}

		((Thread) mode).start();
		
		resourcesMap.put(session.getId(), resources);

		//SwingUtilities.invokeLater(new Graphics(resources, null, false));
	}
}