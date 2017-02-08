package resources;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.sprites.SheetDeets;

public class Map {
	private int width, height;
	private Point2D origin;
	
	// friction = 1.0 / deceleration
	//   so physics does 
	//   character.speed = character.speed * map.friction
	//   to calculate the new speed.
	private double friction;
	private double gravity;
	
	// any areas of different physics on the same map, starts at their origin.
	// Preferably we should avoid a third level of recursion. 
	// Terrain stored in terrain will be ignored
	//   if we have a hole inside a patch of ice, 
	//   create the patch of ice and the hole. If the player is on the hole,
	//   that effect will override the ice one.
	
	// any walls.
	// keep walls to the top-most map; lower level ones are 
	//   not guaranteed to be accounted for in calculations.
	private ArrayList<Wall> walls;

	// powerups
	//private ArrayList<PowerUp> powerups;
	
	public enum Tile{
		TEST,
		DEFAULT,
	};
	
	private ArrayList<Tile[]> tiles;
	
	// TODO getters/setters/constructors
	public Map(int width, int height) {
		
		this(
			new Point2D.Double(0,0),
			width,
			height,
			0.99,
			0.0,
			new ArrayList<Wall>()
		);
	}
	
	public Map(Point2D origin, int width, int height, double friction, double gravity, ArrayList<Wall> walls) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.gravity = gravity;
		this.walls = walls;

		tiles = new ArrayList<Tile[]>();
		
		for(int i = 0; i < 9; i++){
			
			Tile[] array = new Tile[16 + i%2];
			
			for(int j = 0; j < array.length; j++){
				array[j] = Tile.TEST;
			}
			
			tiles.add(array);
			
		}
	}
	
	public Map(Point2D origin, int width, int height, double friction, double gravity, ArrayList<Wall> walls, ArrayList<Tile[]> tiles) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.gravity = gravity;
		this.walls = walls;

		this.tiles = tiles;
	}
	
	public Point2D origin() {
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
	
	public ArrayList<Tile[]> getTiles(){
		return this.tiles;
	}
	
	public BufferedImage getTileSprite(int x, int y){
		
		BufferedImage sprite = SheetDeets.getSpriteFromTile(tiles.get(y)[x]);
		return sprite;
		
	}
}