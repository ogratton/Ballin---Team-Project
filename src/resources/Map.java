package resources;

import java.awt.geom.Point2D;
import java.util.ArrayList;

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
	// May not be implemented.
	private ArrayList<Map> terrains;
	
	// any walls.
	// keep walls to the top-most map; lower level ones are 
	//   not guaranteed to be accounted for in calculations.
	private ArrayList<Wall> walls;
	// powerups
	//private ArrayList<PowerUp> powerups;
	
	// TODO getters/setters/constructors
	public Map(int width, int height) {
		this(
			new Point2D.Double(0,0),
			width,
			height,
			0.99,
			0.0,
			new ArrayList<Map>(),
			new ArrayList<Wall>()
		);
	}
	
	public Map(Point2D origin, int width, int height, double friction, double gravity, ArrayList<Map> terrains, ArrayList<Wall> walls) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.gravity = gravity;
		this.terrains = terrains;
		this.walls = walls;
	}
	
	public Point2D origin() {
		return origin;
	}
	public double width() {
		return width;
	}
	public double height() {
		return height;
	}
	public double friction() {
		return friction;
	}
	public double gravity() {
		return gravity;
	}
	public ArrayList<Map> terrains(){
		return terrains;
	}
	public ArrayList<Wall> walls() {
		return walls;
	}
}