package resources;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.sprites.SheetDeets;

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
		TEST, DEFAULT,
	};
	
	/**
	 * To set the type of tiles used
	 * Ideally we have sets of tiles for each world type and swap them out
	 */
	public enum World {
		TEST, ICE, LAVA, DESERT,
	};

	private Tile[][] tiles;
	private final int TILE_SIZE = 60;
	private World world;

	// TODO getters/setters/constructors
	
	/**
	 * Create a default map with given width and height
	 * @param width the width
	 * @param height the height
	 */
	
	public Map(int width, int height) {

		this(new Point2D.Double(0, 0), width, height, 0.99, 0.0, new ArrayList<Wall>());
	}

	/**
	 * Create a default map with given physics and size
	 * @param origin the origin point
	 * @param width the width
	 * @param height the height
	 * @param friction the friction
	 * @param gravity the gravity
	 * @param walls the list of walls
	 */
	
	public Map(Point2D origin, int width, int height, double friction, double gravity, ArrayList<Wall> walls) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.gravity = gravity;
		this.walls = walls;
		this.world = World.TEST;
		
		tiles = new Tile[height][width];

		for (int i = 0; i < height; i++) {

			for (int j = 0; j < width; j++) {
				tiles[i][j] = Tile.TEST;
			}
		}
	}

	/**
	 * Create a default map with given physics, size and tileset
	 * @param origin the origin point
	 * @param width the width
	 * @param height the height
	 * @param friction friction
	 * @param gravity gravity
	 * @param walls the list of walls
	 * @param tiles the array of tiles
	 */
	
	public Map(Point2D origin, int width, int height, double friction, double gravity, ArrayList<Wall> walls,
			Tile[][] tiles) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.gravity = gravity;
		this.walls = walls;

		this.tiles = tiles;
		this.world = World.TEST;
	}

	/**
	 * Get the origin point of this map
	 * @return the origin point
	 */
	
	public Point2D getOrigin() {
		return origin;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getFriction() {
		return friction;
	}

	public double getGravity() {
		return gravity;
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public Tile[][] getTiles() {
		return this.tiles;
	}
	
	public int getTileSize(){
		return this.TILE_SIZE;
	}

	/**
	 * Get the tile sprite of a specific tile on the map
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the tile sprite
	 */
	
	public BufferedImage getTileSprite(int x, int y) {

		BufferedImage sprite = SheetDeets.getSpriteFromTile(tiles[y][x]);
		return sprite;

	}
	
	/**
	 * Get the world type of this map
	 * @return the world type
	 */
	
	public World getWorldType(){
		return this.world;
	}
}