package resources;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ai.pathfinding.Line;
import audio.MusicPlayer;
import gamemodes.GameModeFFA;
import gamemodes.Team;
import graphics.LayeredPane;
import resources.Map.Tile;

//import graphics.old.CharacterModel;

/*
 * These things are needed by UI, Physics and Graphics, and possibly Networking,
 *   so I've put them here for ease of access.
 *   If we'd prefer to just pass them to the relevant things, that's cool too.
 */
public class Resources {

	public static boolean silent = true; // so alex can run the game :)

	public enum Mode {
		Deathmatch, LastManStanding, HotPotato, Hockey, Debug
	};

	// Default game mode
	public Mode mode = Mode.HotPotato;
	public GameModeFFA gamemode;

	// Keybindings
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
	private String id;

	private ArrayList<Tile> bad_tiles; // tiles to path-find around

	// Counter of how many ticks have happened
	private int globalTimer = 0;

	// destination list for pathfinding
	private LinkedList<Point> destList = new LinkedList<Point>();

	//Colour of the various characters.
	private final Color cpuColour = new Color(110, 110, 110);
	private final Color p1Colour = new Color(238, 31, 52);
	private final Color p2Colour = new Color(2, 23, 255);
	private final Color p3Colour = new Color(3, 209, 38);
	private final Color p4Colour = new Color(255, 217, 2);
	private final Color p5Colour = new Color(3, 255, 234);
	private final Color p6Colour = new Color(226, 10, 229);
	private final Color p7Colour = new Color(245, 122, 37);
	private final Color p8Colour = new Color(98, 31, 187);

	private int requestId = 0;
	private List<networking.CharacterInfo> requests = new LinkedList<networking.CharacterInfo>();

	private int countdown = 3;
	private boolean finished = false;
	private int timer = 30;
	
	private boolean lowGraphics = false;
	private boolean isGameOver = false;
	
	/**
	 * Creates a new resources object and defines which tiles are not walkable ('bad')
	 */
	public Resources() {
		bad_tiles = new ArrayList<Tile>();
		bad_tiles.add(Tile.ABYSS);
		bad_tiles.add(Tile.EDGE_ABYSS);
		bad_tiles.add(Tile.WALL);
	}

	public void refresh() {
		powerupList = new ArrayList<Powerup>();
		globalTimer = 0;
		setFinished(false);
		setCountdown(3);
		setFinished(false);
		LayeredPane.victoryShowing = false;
		LayeredPane.splashShowing = true;
	}
	/**
	 * @return The current id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The new id.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return The current request id.
	 */
	public int getRequestId() {
		return this.requestId;
	}

	/**
	 * @param id
	 *            The new request id.
	 */
	public void setRequestId(int id) {
		this.requestId = id;
	}

	/**
	 * Increment the request id by 1.
	 */
	public void incRequestId() {
		this.requestId++;
	}

	/**
	 * Increments and returns the returns id.
	 * 
	 * @return The next request id.
	 */
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
	 *            The character to add to the player list.
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
	 * @return The current map.
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Set the map
	 * 
	 * @param map
	 *            The new map.
	 */

	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * @return An ArrayList of bad tiles.
	 */
	public ArrayList<Tile> getBadTiles() {
		return bad_tiles;
	}

	/**
	 * Deprecated? Creates a puck for the hockey game mode.
	 */
	public void createPuck() {
		// need new character class to represent puck?
		this.setPuck(new Puck());
	}

	/**
	 * @return The current time (total number of ticks).
	 */
	public int getGlobalTimer() {
		return globalTimer;
	}

	/**
	 * Increment the global timer (called each tick).
	 */
	public void incrementGlobalTimer() {
		globalTimer++;
	}

	/**
	 * DEBUG: set the list of waypoints for drawing by GameView
	 * 
	 * @param destList
	 *            waypoints determined by AI
	 */
	public void setDestList(LinkedList<Point> destList) {
		this.destList = destList;
	}

	/**
	 * DEBUG: Get the list of waypoints being followed by the AI
	 * 
	 * @return
	 */
	public LinkedList<Point> getDestList() {
		return this.destList;
	}

	/**
	 * @return An ArrayList of all powerups.
	 */
	public ArrayList<Powerup> getPowerupList() {
		return powerupList;
	}
	
	/**
	 * @param list An ArrayList of all powerups.
	 */
	public void setPowerUpList(ArrayList<Powerup> list) {
		powerupList = list;
	}

	/**
	 * @param p
	 *            A powerup to be added to the powerup list.
	 */
	public void addPowerup(Powerup p) {
		powerupList.add(p);
	}

	/**
	 * @param p
	 *            Make the specified powerup inactive.
	 */
	public void removePowerup(Powerup p) {
		p.setActive(false);
	}

	// Deprecated?
	public Puck getPuck() {
		return puck;
	}

	// Deprecated?
	public void setPuck(Puck puck) {
		this.puck = puck;
	}

	// Deprecated?
	public Team[] getTeams() {
		return teams;
	}

	public void setTeams(Team[] teams) {
		this.teams = teams;
	}

	/**
	 * Get the player colour for a given player (0 for cpu)
	 * 
	 * @param no
	 *            the player number
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
	 * 
	 * @return
	 */
	public Point getProjectedPos() {
		return projectedPos;
	}

	/**
	 * XXX Debug for drawing predicted position of AI
	 * 
	 * @param pos
	 */
	public void setProjectedPos(Point pos) {
		projectedPos = pos;
	}

	private Point AINextDest;

	/**
	 * DEBUG: Get the point which the AI is trying to get to
	 * 
	 * @return
	 */
	public Point getAINextdest() {
		return AINextDest;
	}

	/**
	 * DEBUG: Set the point that the AI is trying to get to
	 * 
	 * @param nd
	 */
	public void setAINextDest(Point nd) {
		AINextDest = nd;
	}

	private Line normal;

	/**
	 * DEBUG: Get the normal to the next AI destination
	 * 
	 * @return
	 */
	public Line getNormal() {
		return normal;
	}

	/**
	 * DEBUG: Set the normal to the next AI destination
	 * 
	 * @param n
	 */
	public void setNormal(Line n) {
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
		while (!clientMoves.isEmpty()) {
			move = clientMoves.remove();
			sentClientMoves.offer(move);
		}
	}

	/**
	 * @return The character being controlled by this computer.
	 */
	public Character getMyCharacter() {
		for (int i = 0; i < this.getPlayerList().size(); i++) {
			if (this.getPlayerList().get(i).getId().equals(this.getId())) {
				return this.getPlayerList().get(i);
			}
		}
		return null;
	}

	/**
	 * @return the music player object
	 */
	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}

	/**
	 * set the music player object
	 * 
	 * @param mp
	 */
	public void setMusicPlayer(MusicPlayer mp) {
		this.musicPlayer = mp;
	}
	
	public void clearPlayerList(){
		this.playerList = new ArrayList<>();
	}
	
	public void setCountdown(int i){
		this.countdown = i;
	}
	
	public void decCountdown(){
		this.countdown -= 1;
	}
	
	public int getCountdown(){
		return this.countdown;
	}
	
	public void setFinished(boolean finished){
		this.finished = finished;
	}
	
	public boolean isFinished(){
		return this.finished;
	}
	
	/**
	 * @return An ArrayList of all characters, order by descending score
	 */
	public ArrayList<Character> getOrderedScores() {
		ArrayList<Character> scores = new ArrayList<Character>();
		scores.addAll(getPlayerList());
		scores.sort((a, b) -> (a.getScore() > b.getScore()) ? -1 : (a.getScore() < b.getScore()) ? 1 : 0);
		return scores;
	}

	public void setTimer(int n) {
		timer = n;
	}
	
	public void resetTimer() {
		timer = 0;
	}
	
	public void incrementTimer(int i) {
		timer+=i;		
	}

	public int getTimer() {
		return this.timer;
	}
	
	public boolean isLowGraphics(){
		return this.lowGraphics;
	}
	
	public void setLowGraphics(boolean low){
		this.lowGraphics = low;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}
	
	
}
