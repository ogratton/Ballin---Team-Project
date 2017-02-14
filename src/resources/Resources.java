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

	
	// keybindings
	private int default_up = KeyEvent.VK_W;
	private int default_down = KeyEvent.VK_S;
	private int default_left = KeyEvent.VK_A;
	private int default_right = KeyEvent.VK_D;
	private int up = KeyEvent.VK_W;
	private int down = KeyEvent.VK_S;
	private int left = KeyEvent.VK_A;
	private int right = KeyEvent.VK_D;

	// sound effect "volume" (0 is normal)
	// can be as negative as you like but no larger than about 10 I think
	// let's just agree to have 0 as the max
	private int sfx_gain = 0;
	
	// what number player am I?
	private int me = -1;
	
	// characters
	private ArrayList<Character> playerList = new ArrayList<Character>();
	
	// map
	private Map map;
	
	
	
	public int getDefaultUp() {
		return default_up;
	}
	public void setDefaultUp(int default_up) {
		this.default_up = default_up;
	}
	public int getDefaultDown() {
		return default_down;
	}
	public void setDefaultDown(int default_down) {
		this.default_down = default_down;
	}
	public int getDefaultLeft() {
		return default_left;
	}
	public void setDefaultLeft(int default_left) {
		this.default_left = default_left;
	}
	public int getDefaultRight() {
		return default_right;
	}
	public void setDefaultRight(int default_right) {
		this.default_right = default_right;
	}
	public int getUp() {
		return up;
	}
	public void setUp(int up) {
		this.up = up;
	}
	public int getDown() {
		return down;
	}
	public void setDown(int down) {
		this.down = down;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getSFXGain() {
		return sfx_gain;
	}
	public void setSFXGain(int sfx_gain) {
		this.sfx_gain = sfx_gain;
	}
	public int getMe() {
		return me;
	}
	public void setMe(int me) {
		this.me = me;
	}
	public ArrayList<Character> getPlayerList() {
		return playerList;
	}
	
	public void addPlayerToList(Character character){
		playerList.add(character);
	}
	
	public void setPlayerList(ArrayList<Character> playerList) {
		this.playerList = playerList;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	
	//public static MapReader mapReader = new MapReader();
	//public static Map.Tile[][] map1 = mapReader.readMap("./resources/maps/map1.csv");
	
}
