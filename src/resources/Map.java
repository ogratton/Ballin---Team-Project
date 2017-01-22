package resources;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Map {
	private double width, height;
	private Point2D origin;
	
	// friction = (1.0 / deceleration) * unit mass?
	//   so physics does 
	//   character.speed = character.inv_mass * character.speed * map.friction ?
	//   to calculate the new speed.
	private double friction;
	private double gravity;
	
	// any areas of different physics on the same map, starts at their origin.
	// Preferably we should avoid a third level of recursion.
	// Also, keep walls to the top-most map; lower level ones are 
	//   not guaranteed to be accounted for in calculations.
	// May not be implemented.
	private ArrayList<Map> terrains;
	// any walls.
	private ArrayList<Wall> walls;
	// powerups
	//private ArrayList<PowerUp> powerups;
	
	// TODO getters/setters/constructors
	public Map(Point2D origin, double width, double height, double friction, double gravity, ArrayList<Map> terrains, ArrayList<Wall> walls) {
		
	}
	
}