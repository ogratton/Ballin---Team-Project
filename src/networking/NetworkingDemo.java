package networking;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Random;

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

	public static void startGame(ConnectionDataModel cModel, ObjectOutputStream toServer) {
		
		
		Character newPlayer;
		Random r = new Random();
		Resources resources = new Resources();
		resources.setId(cModel.getMyId());
		Updater updater = new Updater(cModel, toServer, resources);
		double x;
		double y;
		List<ClientInformation> clients = cModel.getSession(cModel.getSessionId()).getAllClients();
		List<CharacterInfo> charactersList = cModel.getCharactersList();
		CharacterInfo info;
		for(int i=0; i<clients.size(); i++) {
			x = r.nextInt(1200);
			y = r.nextInt(675);
			int id = clients.get(i).getId();
			newPlayer = new Character(Character.Class.ELF, 1);
			newPlayer.setX(x);
			newPlayer.setY(y);
			newPlayer.setId(id);
			resources.addPlayerToList(newPlayer);
			cModel.getCharacters().put(id, newPlayer);
			info = new CharacterInfo(id, x, y);
			charactersList.add(info);
		}
		
		cModel.setCharactersList(charactersList);
		
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
		cModel.setResources(resources);

		// create physics thread
		Physics p = new Physics(resources);
		p.start();

		// create ui thread
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Graphics g = new Graphics(resources, updater, false);
				g.start();
			}
		});
	}
	
	public static void setGame(ConnectionDataModel cModel, GameData gameData, ObjectOutputStream toServer) {
		
		Character newPlayer;
		double x;
		double y;
		int id;
		Resources resources = new Resources();
		resources.setId(cModel.getMyId());
		Updater updater = new Updater(cModel, toServer, resources);
		List<CharacterInfo> charactersList = gameData.getCharactersList();
		CharacterInfo info;
		for(int i=0; i<charactersList.size(); i++) {
			info = charactersList.get(i);
			x = info.getX();
			y = info.getY();
		    id = info.getId();
			newPlayer = new Character(Character.Class.ELF, 1);
			newPlayer.setX(x);
			newPlayer.setY(y);
			newPlayer.setId(id);
			resources.addPlayerToList(newPlayer);;
			cModel.getCharacters().put(id, newPlayer);
		}
		
		cModel.setCharactersList(charactersList);
		
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
		cModel.setResources(resources);
			
		// create physics thread
		Physics p = new Physics(resources);
		p.start();

		// create ui thread
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Graphics g = new Graphics(resources, updater, false);
				g.start();
			}
		});
	}
}
