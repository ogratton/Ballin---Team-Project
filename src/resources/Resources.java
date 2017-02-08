package resources;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
	
}
