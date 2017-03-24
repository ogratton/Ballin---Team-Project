package resources;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import graphics.sprites.SheetDetails;
import graphics.sprites.Sprite;

public class Map {
	private int width, height;
	private Point2D origin;
	private final static double default_friction = 0.02;

	// friction = 1.0 / deceleration
	// so physics does
	// character.speed = character.speed * map.friction
	// to calculate the new speed.
	private double friction;
	
	private int[][] proxMask;
	private double[][] costMask;

	private String filename; // filename minus extension
	
	// powerups
	// private ArrayList<PowerUp> powerups;

	public enum Tile {
		ABYSS, // tile players can fall in
		FLAT, // normal tile
		EDGE_N, EDGE_E, EDGE_S, EDGE_W, // tiles with one edge
		EDGE_NE, EDGE_SE, EDGE_SW, EDGE_NW, // tiles with two edges, continuous
		EDGE_NS, EDGE_EW, // tiles with two edges, opposite
		EDGE_NES, EDGE_ESW, EDGE_SWN, EDGE_WNE, // tiles with three edges
		EDGE_NESW, // tiles with four edges
		EDGE_ABYSS, // tile representing the 'front' on the arena
		WALL, // tile representing a wall.
	};

	/**
	 * To set the type of tiles used Ideally we have sets of tiles for each
	 * world type and swap them out
	 */
	public enum World {
		ICE, LAVA, DESERT, CAVE, SPACE, CAKE;
	};

	private Tile[][] tiles;
	private World world;
	private BufferedImage tileSet;

	/**
	 * Create a default map with given width and height
	 * 
	 * @param width the width
	 * @param height the height
	 * @param filename the filename minus extension
	 */
	public Map(int width, int height, String filename) {

		this(new Point2D.Double(0, 0), width, height, default_friction, filename);
	}

	/**
	 * Same as default but allows you to specify the tiles of the map and the
	 * world
	 * 
	 * @param width the width
	 * @param height the height
	 * @param tile the array of tiles
	 * @param world the world type
	 * @param filename the filename minus extension
	 */

	public Map(int width, int height, World world, String filename) {
		this(new Point2D.Double(0, 0), width, height, default_friction, world, filename);
	}

	/**
	 * Create a default map with given physics and size
	 * 
	 * @param origin the origin point
	 * @param width the width
	 * @param height the height
	 * @param friction the friction
	 * @param gravity the gravity
	 * @param walls the list of walls
	 * @param filename the filename minus extension
	 */

	public Map(Point2D origin, int width, int height, double friction, String filename) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.world = World.CAVE;
		this.filename = filename;
		setFriction();

		tiles = new Tile[height][width];
		tileSet = SheetDetails.getTileSetFromWorld(world);

		for (int i = 0; i < height; i++) {

			for (int j = 0; j < width; j++) {
				tiles[i][j] = Tile.FLAT;
			}
		}
	}

	/**
	 * Create a default map with given physics, size and tileset
	 * 
	 * @param origin the origin point
	 * @param width the width
	 * @param height the height
	 * @param friction friction
	 * @param gravity gravity
	 * @param walls the list of walls
	 * @param tiles the array of tiles
	 * @param filename the filename minus extension
	 */

	public Map(Point2D origin, int width, int height, double friction,
			World worldType, String filename) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.filename = filename;
		
		// Create default map in case the following fails
		Map.Tile[][] tiles = null;	
		// Create map
		MapReader mr = new MapReader();	
		try
		{

			tiles = mr.readMap(FilePaths.maps+filename+".csv");
//			System.out.println("Map Loaded");
		}
		catch (IOException e)
		{
			System.out.println("File not found: " + filename + ".csv");
			e.printStackTrace();
			
		}

		this.tiles = tiles;
		this.world = worldType;
		setFriction();

		tileSet = SheetDetails.getTileSetFromWorld(world);
	}

	/**
	 * Get the origin point of this map
	 * 
	 * @return the origin point
	 */

	public Point2D getOrigin() {
		return origin;
	}

	/**
	 * Get the width of this map
	 * 
	 * @return the width
	 */

	public double getWidth() {
		return width;
	}

	/**
	 * Get the height of this map
	 * 
	 * @return the height
	 */

	public double getHeight() {
		return height;
	}

	/**
	 * Get the friction of this map
	 * 
	 * @return the friction
	 */

	public double getFriction() {
		return friction;
	}

	private void setFriction() {
		switch(world) {
		case CAKE:
			friction = default_friction;
			break;
		case CAVE:
			friction = default_friction;
			break;
		case DESERT:
			friction = default_friction * 4;
			break;
		case ICE:
			friction = default_friction / 2;
			break;
		case LAVA:
			friction = default_friction;
			break;
		case SPACE:
			friction = default_friction;
			break;
		default:
			break;
		}
	}

	/**
	 * Get the array of tiles of this map
	 * 
	 * @return the array of tiles
	 */

	public Tile[][] getTiles() {
		return this.tiles;
	}

	/**
	 * Get the tile size of this map
	 * 
	 * @return the tile size
	 */

	public int getTileSize() {
		return SheetDetails.TILES_SIZEX;
	}

	/**
	 * Get the tile sprite of a specific tile on the map
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the tile sprite
	 */

	public BufferedImage getTileSprite(int x, int y) {

		Tile tile = tiles[y][x];

		int a = 0;
		int b = 0;

		switch (tile) {
		case ABYSS:
			a = 0;
			b = 0;
			break;
		case FLAT:
			a = 1;
			b = 0;
			break;
		case EDGE_N:
			a = 2;
			b = 0;
			break;
		case EDGE_E:
			a = 3;
			b = 0;
			break;
		case EDGE_S:
			a = 4;
			b = 0;
			break;
		case EDGE_W:
			a = 5;
			b = 0;
			break;
		case EDGE_NE:
			a = 0;
			b = 1;
			break;
		case EDGE_SE:
			a = 1;
			b = 1;
			break;
		case EDGE_SW:
			a = 2;
			b = 1;
			break;
		case EDGE_NW:
			a = 3;
			b = 1;
			break;
		case EDGE_NS:
			a = 4;
			b = 1;
			break;
		case EDGE_EW:
			a = 5;
			b = 1;
			break;
		case EDGE_NES:
			a = 0;
			b = 2;
			break;
		case EDGE_ESW:
			a = 1;
			b = 2;
			break;
		case EDGE_SWN:
			a = 2;
			b = 2;
			break;
		case EDGE_WNE:
			a = 3;
			b = 2;
			break;
		case EDGE_NESW:
			a = 4;
			b = 2;
			break;
		case EDGE_ABYSS:
			a = 5;
			b = 2;
			break;
		case WALL:
			a = 0;
			b = 3;
		}

		return Sprite.getSprite(tileSet, a, b, SheetDetails.TILES_SIZEX, SheetDetails.TILES_SIZEY);

	}

	/**
	 * Get the world type of this map
	 * 
	 * @return the world type
	 */

	public World getWorldType() {
		return this.world;
	}

	/**
	 * Set the world type of this map
	 * 
	 * @param world
	 *            the world type
	 */

	public void setWorldType(World world) {
		this.world = world;
		setFriction();
		tileSet = SheetDetails.getTileSetFromWorld(world);
	}

	/**
	 * returns the tile at a point x,y. If x,y is outside the map, returns null.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile tileAt(double x, double y) {
		int row = (int) (x / SheetDetails.TILES_SIZEX);
		int column = (int) (y / SheetDetails.TILES_SIZEY);
		// may crash if tiles not initialised.
		// check if column,row is in the tile array:
		return tileAt(column, row);
	}

	/**
	 * returns the tile at given row and column indices
	 * 
	 * @param column
	 * @param row
	 * @return
	 */
	public Tile tileAt(int column, int row) {
		if (column >= 0 && row >= 0 && column < tiles.length && row < tiles[0].length) {
			return tiles[column][row];
		}
		// if not in map
		return null;
	}

	/**
	 * Return the row and column of the tile at point x y Used by the AI
	 * 
	 * @param x
	 * @param y
	 * @return A point with x as column and y as row
	 */
	public Point tileCoords(double x, double y) {
		int row = (int) (x / SheetDetails.TILES_SIZEX);
		int column = (int) (y / SheetDetails.TILES_SIZEY);
		// may crash if tiles not initialised.
		// check if column,row is in the tile array:
		if (column >= 0 && row >= 0 && column < tiles.length && row < tiles[0].length) {
			return new Point(column, row);
		}
		// if not in map
		return null;
	}
	

	/**
	 * Return the top-left coordinates of the tile at x,y
	 * @param x
	 * @param y
	 * @return
	 */
	public Point tileCoordsOnMap(double x, double y) {
		Point p = tileCoords(x,y);
		return new Point((int)p.getX() * SheetDetails.TILES_SIZEX, (int)p.getY() * SheetDetails.TILES_SIZEY);
	}
	
	/**
	 * Converts from tile coords to map coords
	 * Gives the coords of the centre of the tile
	 * @param row
	 * @param col
	 * @return
	 */
	public Point tileCoordsToMapCoords(int row, int col)
	{
		int x = (int) (col * SheetDetails.TILES_SIZEX + 0.5 * SheetDetails.TILES_SIZEX);
		int y = (int) (row * SheetDetails.TILES_SIZEY + 0.5 * SheetDetails.TILES_SIZEX);
		return new Point(x,y);
		
	}

	/**
	 * Check whether some coordinates are on the map.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean onMap(double x, double y) {
		return x >= origin.getX() || y >= origin.getY() || x <= origin.getX() + width || y <= origin.getY() + height;
	}
	
	/**
	 * Spawns a character on a random point.
	 * 
	 * @param c
	 */
	public void spawn(Character c) {
		spawn(c,randPointOnMap());
	}

	/**
	 * Spawns a character on a set point.
	 * 
	 * @param c
	 */
	public void spawn(Character c, Point p) {
		//reset all 'character state' flags
		c.setLastCollidedWith(null, 0);
		c.setDead(false);
		c.setFalling(false);
		c.setDashing(false);
		c.setBlocking(false);
		c.setVisible(true);
		c.setDyingStep(0);
		c.hasPowerup(false);
		//set velocity
		c.setDx(0);
		c.setDy(0);
		// set location
		c.setX(p.x);
		c.setY(p.y);
	}
	
	public void spawn(Puck puck) {
		puck.setLastCollidedWith(null, 0);
		puck.setDead(false);
		puck.setFalling(false);
		puck.setVisible(true);
		puck.setDyingStep(0);
		puck.setDx(0);
		puck.setDy(0);
		//TODO Get middle of map for puck spawn point
		Point p = new Point(200,200);
		puck.setX(p.x);
		puck.setY(p.y);
	}
	
	public void spawnPowerup(Powerup p) {
		Point point = randPointOnMap();
		p.setX(point.x);
		p.setY(point.y);
	}
	
	public Point randPointOnMap()
	{
		//set location 		
		double randX = 0.0;
		double randY = 0.0;
		int i = 0;
		int j = 0;
		do
		{
			randX = Math.random() * width;
			randY = Math.random() * height;
			Point randP = tileCoords(randX, randY);
			i = randP.x;
			j = randP.y;
		}
		while (proxMask[i][j] < 2); // XXX never spawn anyone fewer than 2 tiles from the edge
		return new Point((int) randX, (int) randY);
	}

	/**
	 * Checks whether a given tile is a 'killing' tile.
	 * @param tile
	 * @return true if it is a 'killing' tile, false otherwise.
	 */
	public static boolean tileCheck(Tile tile) {
		return (tile == null || tile == Tile.ABYSS || tile == Tile.EDGE_ABYSS);
	}
	
	public double[][] getCostMask()
	{
		return costMask;
	}
	
	public void setCostMask(double[][] costMask)
	{
		this.costMask = costMask;
	}
	
	public int[][] getProxMask()
	{
		return proxMask;
	}
	
	public void setProxMask(int[][] proxMask)
	{
		this.proxMask = proxMask;
	}

	public String getName(){
		return this.filename;
	}
	
}
