package networking;


import java.awt.Point;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;

import ai.FightingAI;
import gamemodes.Deathmatch;
import gamemodes.GameModeFFA;
import gamemodes.HotPotato;
import gamemodes.LastManStanding;
import resources.Character;
import resources.Map;
import resources.MapCosts;
import resources.MapReader;
import resources.Resources;
import resources.Resources.Mode;


/**
 * NetworkingDemo is the class which starts the Game on the server.
 * It initialises all the variables and starts the physics.
 * @author aaquibnaved
 *
 */
public class NetworkingDemo {

	/**
	 * Starts the game on the server.
	 * 
	 * @param session The session which the game needs to start on.
	 * @param resourcesMap The Hash Map of resources which the new resources for the session needs to be stored in.
	 * @param sessions The Hash Map of all the sessions on the server.
	 * @param connections The Hash Map of connections which the server has to clients.
	 */
	public static void startServerGame(Session session, ConcurrentMap<String, Resources> resourcesMap, ConcurrentMap<String, Session> sessions, ConcurrentMap<String, Connection> connections) {
		
		// Fetches the variables needed to generate the map from the session object.
		String mapName = session.getMapName();
		mapName = "map0";
		Map.World style = session.getTileset();
		
		// Fetches the game mode from the session object.
		Mode modeName = session.getGameMode();
		
		// Crates the map using the variables.
		Map map = new Map(1200, 650, style, mapName);
		
		// Creates a new resources object for the session and adds the map and map costs to it.
		Character newPlayer;
		Resources resources = new Resources();
		resources.setMap(map);
		new MapCosts(resources);
		
		// Select the correct game mode type.
		GameModeFFA mode;
		switch(modeName) {
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
		
		// Get the list of clients on teh session and put them on the map randomly.
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
		
		// Put the specified number of AIs on the number randomly.
		for(int i = 0; i < session.getNumberOfAI(); i++){
			Character character = new Character(Character.Class.MONK, 0, "CPU" + i);
			resources.addPlayerToList(character);
			FightingAI ai = new FightingAI(resources, character);
			character.setAI(ai);
			ai.start();
		}

		((Thread) mode).start();
		
		// Add the resources object to the resources HashMap.
		resourcesMap.put(session.getId(), resources);

		//SwingUtilities.invokeLater(new Graphics(resources, null, false));
	}
}