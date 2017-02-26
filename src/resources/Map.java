package resources;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Function;

import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;
import resources.Map.Tile;

public class Map {
	private int width, height;
	private Point2D origin;

	// friction = 1.0 / deceleration
	// so physics does
	// character.speed = character.speed * map.friction
	// to calculate the new speed.
	private double friction;
	private double gravity;
	
	private int[][] proxMask;
	private int[][] costMask;

	// any areas of different physics on the same map, starts at their origin.
	// Preferably we should avoid a third level of recursion.
	// Terrain stored in terrain will be ignored
	// if we have a hole inside a patch of ice,
	// create the patch of ice and the hole. If the player is on the hole,
	// that effect will override the ice one.

	// any walls.
	// keep walls to the top-most map; lower level ones are
	// not guaranteed to be accounted for in calculations.
	private ArrayList<Wall> walls;

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
		ICE, LAVA, DESERT, CAVE, SPACE,
	};

	private Tile[][] tiles;
	private World world;
	private BufferedImage tileSet;

	/**
	 * Create a default map with given width and height
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */

	public Map(int width, int height) {

		this(new Point2D.Double(0, 0), width, height, 5, 0.0, new ArrayList<Wall>());
	}

	/**
	 * Same as default but allows you to specify the tiles of the map and the
	 * world
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param tile
	 *            the array of tiles
	 * @param world
	 *            the world type
	 */

	public Map(int width, int height, Tile[][] tile, World world) {
		this(new Point2D.Double(0, 0), width, height, 0.99, 0.0, new ArrayList<Wall>(), tile, world);
	}

	/**
	 * Create a default map with given physics and size
	 * 
	 * @param origin
	 *            the origin point
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param friction
	 *            the friction
	 * @param gravity
	 *            the gravity
	 * @param walls
	 *            the list of walls
	 */

	public Map(Point2D origin, int width, int height, double friction, double gravity, ArrayList<Wall> walls) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.gravity = gravity;
		this.walls = walls;
		this.world = World.CAVE;

		tiles = new Tile[height][width];
		tileSet = SheetDeets.getTileSetFromWorld(world);

		for (int i = 0; i < height; i++) {

			for (int j = 0; j < width; j++) {
				tiles[i][j] = Tile.FLAT;
			}
		}
	}

	/**
	 * Create a default map with given physics, size and tileset
	 * 
	 * @param origin
	 *            the origin point
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param friction
	 *            friction
	 * @param gravity
	 *            gravity
	 * @param walls
	 *            the list of walls
	 * @param tiles
	 *            the array of tiles
	 */

	public Map(Point2D origin, int width, int height, double friction, double gravity, ArrayList<Wall> walls,
			Tile[][] tile, World worldType) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.gravity = gravity;
		this.walls = walls;

		this.tiles = tile;
		this.world = worldType;

		tileSet = SheetDeets.getTileSetFromWorld(world);
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

	/**
	 * Get the gravity of this map
	 * 
	 * @return the gravity
	 */

	public double getGravity() {
		return gravity;
	}

	/**
	 * Get the list of walls of this map
	 * 
	 * @return the list of walls
	 */

	public ArrayList<Wall> getWalls() {
		return walls;
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
		return SheetDeets.TILES_SIZEX;
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
		}

		return Sprite.getSprite(tileSet, a, b, SheetDeets.TILES_SIZEX, SheetDeets.TILES_SIZEY);

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
		tileSet = SheetDeets.getTileSetFromWorld(world);
	}

	/**
	 * returns the tile at a point x,y. If x,y is outside the map, returns null.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile tileAt(double x, double y) {
		int row = (int) (x / SheetDeets.TILES_SIZEX);
		int column = (int) (y / SheetDeets.TILES_SIZEY);
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
		int row = (int) (x / SheetDeets.TILES_SIZEX);
		int column = (int) (y / SheetDeets.TILES_SIZEY);
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
		return new Point((int)p.getX() * SheetDeets.TILES_SIZEX, (int)p.getY() * SheetDeets.TILES_SIZEY);
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
		//reset all 'character state' flags
		c.setDead(false);
		c.setFalling(false);
		c.setDashing(false);
		c.setBlocking(false);
		c.setVisible(true);
		c.setDyingStep(0);
		//set velocity
		c.setDx(0);
		c.setDy(0);
		//set location
		Point p = randPointOnMap();
		c.setX(p.x);
		c.setY(p.y);
	}
	
	public Point randPointOnMap()
	{
		//set location
		double randX = 0.0;
		double randY = 0.0;
		int i = 0;
		int j = 0;
		Tile t = null;
		do
		{
			randX = Math.random() * width;
			randY = Math.random() * height;
			t = tileAt(randX, randY);
			
			Point randP = tileCoords(randX, randY);
			i = randP.x;
			j = randP.y;
		}
		while (proxMask[i][j] < 3); // never spawn anyone fewer than 3 tiles from the edge
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
	
	public int[][] getCostMask()
	{
		return costMask;
	}
	
	public void setCostMask(int[][] costMask)
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

	
}