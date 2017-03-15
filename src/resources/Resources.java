package resources;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import ai.pathfinding.Line;
import audio.MusicPlayer;
import gamemodes.GameModeFFA;
import gamemodes.Team;
import resources.Map.Tile;

//import graphics.old.CharacterModel;

/*
 * These things are needed by UI, Physics and Graphics, and possibly Networking,
 *   so I've put them here for ease of access.
 *   If we'd prefer to just pass them to the relevant things, that's cool too.
 */
public class Resources {
	
	public static final boolean silent = true; // so alex can run the game :)
	
	public enum Mode { Deathmatch, LastManStanding, HotPotato, Hockey, Debug };
	// Deathmatch by default
	public Mode mode = Mode.Deathmatch;
	public GameModeFFA gamemode;

	// keybindings
	private int default_up = KeyEvent.VK_W;
	private int default_down = KeyEvent.VK_S;
	private int default_left = KeyEvent.VK_A;
	private int default_right = KeyEvent.VK_D;
	private int default_dash = KeyEvent.VK_SPACE;
	private int default_block = KeyEvent.VK_CONTROL;
	private int up = default_up;
	private int down = default_down;
	private int left = default_left;
	private int right = default_right;
	private int dash = default_dash;
	private int block = default_block;

	// sound effect "volume" (0 is normal)
	// can be as negative as you like but no larger than about 10 I think
	// let's just agree to have 0 as the max
	private int sfx_gain = 0;

	// max deaths a character can have.
	private int maxDeaths = 4;

	private LinkedList<NetworkMove> clientMoves = new LinkedList<NetworkMove>();
	private LinkedList<NetworkMove> sentClientMoves = new LinkedList<NetworkMove>();
	
	// characters
	private ArrayList<Character> playerList = new ArrayList<Character>();
	// powerups in play
	private ArrayList<Powerup> powerupList = new ArrayList<Powerup>();

	// puck used in hockey game mode, using character class to make things easy
	private Puck puck;
	// are we playing a hockey game?
	private Team[] teams;

	// map
	private Map map;

	private MusicPlayer musicPlayer;

	// client ID
	private UUID id;
	
	private ArrayList<Tile> bad_tiles; // tiles to path-find around 

	// Counter of how many ticks have happened
	private int globalTimer = 0;

	// destination list for pathfinding
	private LinkedList<Point> destList = new LinkedList<Point>();

	private Color cpuColour = new Color(110, 110, 110);
	private Color p1Colour = new Color(238, 31, 52);
	private Color p2Colour = new Color(2, 23, 255);
	private Color p3Colour = new Color(3, 209, 38);
	private Color p4Colour = new Color(255, 217, 2);
	private Color p5Colour = new Color(3, 255, 234);
	private Color p6Colour = new Color(226, 10, 229);
	private Color p7Colour = new Color(245, 122, 37);
	private Color p8Colour = new Color(98, 31, 187);
	
	private int requestId = 0;
	private List<networking.CharacterInfo> requests = new LinkedList<networking.CharacterInfo>();

	public Resources() {
		bad_tiles = new ArrayList<Tile>();
		bad_tiles.add(Tile.ABYSS);
		bad_tiles.add(Tile.EDGE_ABYSS);
		bad_tiles.add(Tile.WALL);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public int getRequestId() {
		return this.requestId;
	}
	
	public void setRequestId(int id) {
		this.requestId = id;
	}
	
	public void incRequestId() {
		this.requestId++;
	}
	
	public int getNextRequestId() {
		this.incRequestId();
		return this.requestId;
	}
	
	public List<networking.CharacterInfo> getRequests() {
		return requests;
	}
	
	public void setRequests(List<networking.CharacterInfo> list) {
		requests = list;
	}

	/**
	 * Get the default up keybinding
	 * 
	 * @return the default up keybinding
	 */

	public int getDefaultUp() {
		return default_up;
	}

	/**
	 * Set the default up keybinding
	 * 
	 * @param default_up
	 *            the default up keybinding
	 */

	public void setDefaultUp(int default_up) {
		this.default_up = default_up;
	}

	/**
	 * Get the default down keybinding
	 * 
	 * @return the default down keybinding
	 */

	public int getDefaultDown() {
		return default_down;
	}

	/**
	 * Set the default down keybinding
	 * 
	 * @param default_down
	 *            the default down keybinding
	 */

	public void setDefaultDown(int default_down) {
		this.default_down = default_down;
	}

	/**
	 * Get the deault left keybinding
	 * 
	 * @return the default left keybinding
	 */

	public int getDefaultLeft() {
		return default_left;
	}

	/**
	 * Set the default left keybinding
	 * 
	 * @param default_left
	 *            the default left keybinding
	 */

	public void setDefaultLeft(int default_left) {
		this.default_left = default_left;
	}

	/**
	 * Get the default right keybinding
	 * 
	 * @return the default right keybinding
	 */

	public int getDefaultRight() {
		return default_right;
	}

	/**
	 * Set the default right keybinding
	 * 
	 * @param default_right
	 *            the default right keybinding
	 */

	public void setDefaultRight(int default_right) {
		this.default_right = default_right;
	}

	/**
	 * Get the default dash keybinding
	 * 
	 * @return the default dash keybinding
	 */

	public int getDefaultDash() {
		return default_dash;
	}

	/**
	 * Set the default dash keybinding
	 * 
	 * @param default_right
	 *            the default dash keybinding
	 */

	public void setDefaultDash(int default_dash) {
		this.default_dash = default_dash;
	}
	
	/**
	 * Get the default block keybinding
	 * 
	 * @return the default block keybinding
	 */

	public int getDefaultBlock() {
		return default_block;
	}

	/**
	 * Set the default block keybinding
	 * 
	 * @param default_block
	 *            the default block keybinding
	 */

	public void setDefaultBlock(int default_block) {
		this.default_block = default_block;
	}

	/**
	 * Get the up keybinding
	 * 
	 * @return the up keybinding
	 */

	public int getUp() {
		return up;
	}

	/**
	 * Set the up keybinding
	 * 
	 * @param up
	 *            the up keybinding
	 */

	public void setUp(int up) {
		this.up = up;
	}

	/**
	 * Get the down keybinding
	 * 
	 * @return the down keybinding
	 */

	public int getDown() {
		return down;
	}

	/**
	 * Set the down keybinding
	 * 
	 * @param down
	 *            the down keybinding
	 */

	public void setDown(int down) {
		this.down = down;
	}

	/**
	 * Get the left keybinding
	 * 
	 * @return the left keybinding
	 */

	public int getLeft() {
		return left;
	}

	/**
	 * Set the left keybinding
	 * 
	 * @param left
	 *            the left keybinding
	 */

	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * Get the right keybinding
	 * 
	 * @return the right keybinding
	 */

	public int getRight() {
		return right;
	}

	/**
	 * Set the right keybinding
	 * 
	 * @param right
	 *            the right keybinding
	 */

	public void setRight(int right) {
		this.right = right;
	}

	/**
	 * Get the dash keybinding
	 * 
	 * @return the dash keybinding
	 */

	public int getDash() {
		return dash;
	}
	
	/**
	 * Set the dash keybinding
	 * 
	 * @param dash
	 *            the dash keybinding
	 */

	public void setDash(int dash) {
		this.dash = dash;
	}

	/**
	 * Set the block keybinding
	 * 
	 * @param block
	 *            the block keybinding
	 */

	public void setBlock(int block) {
		this.block = block;
	}
	
	/**
	 * Get the block keybinding
	 * 
	 * @return the block keybinding
	 */

	public int getBlock() {
		return block;
	}

	/**
	 * Get the SFX gain
	 * 
	 * @return the SFX gain
	 */

	public int getSFXGain() {
		return sfx_gain;
	}

	/**
	 * Set the SFX gain
	 * 
	 * @param sfx_gain
	 *            the SFX gain
	 */

	public void setSFXGain(int sfx_gain) {
		this.sfx_gain = sfx_gain;
	}

	/**
	 * Get the player list
	 * 
	 * @return the player list
	 */

	public ArrayList<Character> getPlayerList() {
		return playerList;
	}

	/**
	 * Add a character to the player list
	 * 
	 * @param character
	 *            the character
	 */

	public void addPlayerToList(Character character) {
		playerList.add(character);
	}

	/**
	 * Set a player list
	 * 
	 * @param playerList
	 *            the player list
	 */

	public void setPlayerList(ArrayList<Character> playerList) {
		this.playerList = playerList;
	}

	/**
	 * Get the map
	 * 
	 * @return the map
	 */

	public Map getMap() {
		return map;
	}

	/**
	 * Set the map
	 * 
	 * @param map
	 *            the map
	 */

	public void setMap(Map map) {
		this.map = map;
	}

	public ArrayList<Tile> getBadTiles() {
		return bad_tiles;
	}

	/**
	 * set the maximum number of lives for each character.
	 * 
	 * @param maxDeaths
	 */
	public void setMaxDeaths(int maxDeaths) {
		this.maxDeaths = maxDeaths;
	}

	/**
	 * get the maximum number of lives for each character.
	 * 
	 * @return
	 */
	public int maxDeaths() {
		return maxDeaths;
	}

	/**
	 * Creates a puck for the hockey game mode.
	 */
	public void createPuck() {
		// need new character class to represent puck?
		this.setPuck(new Puck());
	}

	public int getGlobalTimer() {
		return globalTimer;
	}

	public void incrementGlobalTimer() {
		globalTimer++;
	}

	public void setDestList(LinkedList<Point> destList) {
		this.destList = destList;
	}

	public LinkedList<Point> getDestList() {
		return this.destList;
	}

	public ArrayList<Powerup> getPowerupList() {
		return powerupList;
	}

	public void addPowerup(Powerup p) {
		powerupList.add(p);
	}

	public void removePowerup(Powerup p) {
		p.setActive(false);
	}

	public Puck getPuck() {
		return puck;
	}

	public void setPuck(Puck puck) {
		this.puck = puck;
	}

	public Team[] getTeams() {
		return teams;
	}

	public void setTeams(Team[] teams) {
		this.teams = teams;
	}

	/**
	 * Get the player colour for a given player (0 for cpu)
	 * @param no the player number
	 * @return the player colour
	 */
	
	public Color getPlayerColor(int no) {
		switch (no) {
		case 0:
			return cpuColour;
		case 1:
			return p1Colour;
		case 2:
			return p2Colour;
		case 3:
			return p3Colour;
		case 4:
			return p4Colour;
		case 5:
			return p5Colour;
		case 6:
			return p6Colour;
		case 7:
			return p7Colour;
		case 8:
			return p8Colour;
		default:
			return cpuColour;
		}
	}
	
	// XXX Debug
	private Point projectedPos;
	
	/**
	 * XXX Debug for drawing predicted position of AI
	 * @return
	 */
	public Point getProjectedPos()
	{
		return projectedPos;
	}
	
	/**
	 * XXX Debug for drawing predicted position of AI
	 * @param pos
	 */
	public void setProjectedPos(Point pos)
	{
		projectedPos = pos;
	}
	
	private Point AINextDest;
	
	public Point getAINextdest()
	{
		return AINextDest;
	}
	
	public void setAINextDest(Point nd)
	{
		AINextDest = nd;
	}
	
	private Line normal;
	
	public Line getNormal()
	{
		return normal;
	}
	
	public void setNormal(Line n)
	{
		this.normal = n;
	}

	public LinkedList<NetworkMove> getClientMoves() {
		return clientMoves;
	}

	public void setClientMoves(LinkedList<NetworkMove> clientMoves) {
		this.clientMoves = clientMoves;
	}
	
	public LinkedList<NetworkMove> getSentClientMoves() {
		return sentClientMoves;
	}

	public void setSentClientMoves(LinkedList<NetworkMove> sentClientMoves) {
		this.sentClientMoves = sentClientMoves;
	}
	
	public void transferMoves() {
		NetworkMove move;
		while(!clientMoves.isEmpty()) {
			move = clientMoves.remove();
			sentClientMoves.offer(move);
		}
	}
	
	public Character getMyCharacter() {
		for(int i=0; i<this.getPlayerList().size(); i++) {
			if(this.getPlayerList().get(i).getId().equals(this.getId())) {
				return this.getPlayerList().get(i);
			}
		}
		return null;
	}

	public MusicPlayer getMusicPlayer()
	{
		return musicPlayer;
	}
	
	public void setMusicPlayer(MusicPlayer mp)
	{
		this.musicPlayer = mp;
	}

}
