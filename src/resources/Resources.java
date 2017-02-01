package resources;

import java.util.ArrayList;

import graphics.CharacterModel;

/*
 * These things are needed by UI, Physics and Graphics, and possibly Networking,
 *   so I've put them here for ease of access.
 *   If we'd prefer to just pass them to the relevant things, that's cool too.
 */
public class Resources {
	// what number player am I?
	public static int me = -1;
	
	// I like arraylists 
	public static ArrayList<CharacterModel> models = new ArrayList<CharacterModel>();
	public static ArrayList<Character> playerList = new ArrayList<Character>();
	public static Character[] players;
	public static Map map;
	
}
