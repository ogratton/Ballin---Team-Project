package resources;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;

//import graphics.old.CharacterModel;

/*
 * These things are needed by UI, Physics and Graphics, and possibly Networking,
 *   so I've put them here for ease of access.
 *   If we'd prefer to just pass them to the relevant things, that's cool too.
 */
public class Resources {
	//keybinding things
	public static int default_up = KeyEvent.VK_W;
	public static int default_down = KeyEvent.VK_S;
	public static int default_left = KeyEvent.VK_A;
	public static int default_right = KeyEvent.VK_D;
	public static int up = KeyEvent.VK_W;
	public static int down = KeyEvent.VK_S;
	public static int left = KeyEvent.VK_A;
	public static int right = KeyEvent.VK_D;

	// what number player am I?
	public static int me = -1;
	
	// I like arraylists 
	//public static ArrayList<CharacterModel> models = new ArrayList<CharacterModel>();
	public static ArrayList<Character> playerList = new ArrayList<Character>();
	public static Character[] players;
	public static Map map;
	
	//public static MapReader mapReader = new MapReader();
	//public static Map.Tile[][] map1 = mapReader.readMap("./resources/maps/map1.csv");
	
	// a painstakingly made map
	public static Map.Tile[][] default_map =  {{Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS}, 
										{Map.Tile.ABYSS, Map.Tile.EDGE_NW, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_N, Map.Tile.EDGE_NE, Map.Tile.ABYSS}, 
										{Map.Tile.ABYSS, Map.Tile.EDGE_W, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.EDGE_E, Map.Tile.ABYSS}, 
										{Map.Tile.ABYSS, Map.Tile.EDGE_W, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.EDGE_E, Map.Tile.ABYSS}, 
										{Map.Tile.ABYSS, Map.Tile.EDGE_W, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.EDGE_E, Map.Tile.ABYSS}, 
										{Map.Tile.ABYSS, Map.Tile.EDGE_W, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.EDGE_E, Map.Tile.ABYSS},
										{Map.Tile.ABYSS, Map.Tile.EDGE_W, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.FLAT, Map.Tile.EDGE_E, Map.Tile.ABYSS}, 
										{Map.Tile.ABYSS, Map.Tile.EDGE_SW, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_S, Map.Tile.EDGE_SE, Map.Tile.ABYSS}, 
										{Map.Tile.ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.EDGE_ABYSS, Map.Tile.ABYSS}, 
										{Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS, Map.Tile.ABYSS}};

}
