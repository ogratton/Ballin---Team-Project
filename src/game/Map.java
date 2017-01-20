package game;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Map {
	private double width, height;
	private Point2D origin;
	
	// friction = (1.0 / deceleration) * unit mass?
	//   so physics does character.inv_mass * character.speed * map.friction
	//   to calculate the new speed.
	private double friction;
	private double gravity;
	
	// any areas of different physics on the same map, starts at their origin.
	// May not be implemented.
	private ArrayList<Map> terrains;
	// any walls.
	private ArrayList<Wall> walls;
	
	// TODO getters/setters/constructors
}

class Wall {
	private Point2D origin;
	private double width, height;
	private double heading; //degrees from north, anticlockwise
	
	// TODO getters/setters/constructors
}