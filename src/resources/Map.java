package resources;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;

public class Map {
	private int width, height;
	private Point2D origin;

	// friction = 1.0 / deceleration
	// so physics does
	// character.speed = character.speed * map.friction
	// to calculate the new speed.
	private double friction;
	private double gravity;

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
		ABYSS, FLAT, EDGE_N, EDGE_NE, EDGE_E, EDGE_SE, EDGE_S, EDGE_SW, EDGE_W, EDGE_NW, EDGE_ABYSS, TEST,
	};

	/**
	 * To set the type of tiles used Ideally we have sets of tiles for each
	 * world type and swap them out
	 */
	public enum World {
		ICE, LAVA, DESERT, CAVE,
	};

	private Tile[][] tiles;
	private final int TILE_SIZE = 75;
	private World world;
	private BufferedImage tileSet;

	// TODO getters/setters/constructors

	/**
	 * Create a default map with given width and height
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */

	public Map(int width, int height) {

		this(new Point2D.Double(0, 0), width, height, 0.99, 0.0, new ArrayList<Wall>());
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
		return this.TILE_SIZE;
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
		case EDGE_NE:
			a = 3;
			b = 0;
			break;
		case EDGE_E:
			a = 4;
			b = 0;
			break;
		case EDGE_SE:
			a = 5;
			b = 0;
			break;
		case EDGE_S:
			a = 1;
			b = 0;
			break;
		case EDGE_SW:
			a = 1;
			b = 1;
			break;
		case EDGE_W:
			a = 2;
			b = 1;
			break;
		case EDGE_NW:
			a = 3;
			b = 1;
			break;
		case EDGE_ABYSS:
			a = 4;
			b = 1;
			break;
		case TEST:
			a = 5;
			b = 1;
			break;
		}

		return Sprite.getSprite(tileSet, a, b, TILE_SIZE, TILE_SIZE);

	}

	/**
	 * Get the world type of this map
	 * 
	 * @return the world type
	 */

	public World getWorldType() {
		return this.world;
	}
}