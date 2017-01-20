package game;

import java.awt.geom.Point2D;

public class Character {
	private static final double default_mass = 1;
	private static final double default_radius = 1;
	private static final double default_max_speed_x = 10;
	private static final double default_max_speed_y = 7;
	
	public enum Heading{N,E,S,W,NE,NW,SE,SW};
	
	// this will have all the classes in use.
	public enum Class{DEFAULT}; // add to this as we develop more classes.
	
	// flags for keys pressed. 
	// e.g. if up is true, then the player/ai is holding the up button.
	// jump is currently not being used, but is there if we need it.
	// jump punch and/or block may be replaced by a single 'special' flag,
	//   which does an action based on the class of the character.
	private boolean up, right, left, down, jump, punch, block;
	
	//these are for the physics engine.
	private double mass, inv_mass, speed_x, speed_y, max_speed_x, max_speed_y, acc;
	
	// these are for the physics engine and the graphics engine.
	// Characters are circles.
	// centre is the position of the centre of the circle, relative to the
	//   top-left of the arena.
	// radius is the radius of the circle, in arbitrary units.
	// facing is the direction that the character's facing
	//   (this is entirely for graphics)
	private Point2D centre;
	private double radius;
	private Heading facing;
	
	public Character() {
		new Character(default_mass, new Point2D.Double(0.0, 0.0), default_radius, Heading.N);
	}
	
	public Character(double mass, Point2D position, Double radius, Heading facing) {
		new Character(false, false, false, false, false, false, false, // control flags
				mass, 
				0.0, // speed_x 
				0.0, // speed_y 
				default_max_speed_x,
				default_max_speed_y, 
				1.0, // acceleration (TODO: calculate this)
				position, radius, facing);
	}
	
	/** 
	 * create character based on it's class.
	 * @param c the class of the character
	 */
	public Character(Class c) {
		switch(c) {
		case DEFAULT:
			new Character();
		}
	}
	
	// master constructor. Any other constructors should eventually call this.
	private Character
	(
	boolean up, boolean right, boolean left, boolean down, boolean jump, 
	boolean punch, boolean block, double mass, double speed_x, double speed_y, 
	double max_speed_x, double max_speed_y, double acceleration, 
	Point2D centre, double radius, Heading facing
	) {
		this.up = up;
		this.right = right; 
		this.left = left;
		this.down = down;
		this.jump = jump;
		this.punch = punch;
		this.block = block;
		
		this.mass = mass;
		this.inv_mass = 1.0/mass;
		
		this.speed_x = speed_x;
		this.speed_y = speed_y;
		this.max_speed_x = max_speed_x;
		this.max_speed_y = max_speed_y;
		this.acc= acceleration;
		this.centre = centre;
		this.radius = radius;
		this.facing = facing;
	}
	
	
	// setters/getters for control flags
	public void setControls(boolean up, boolean down, boolean left, boolean right, boolean jump, boolean punch, boolean block) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.jump = jump;
		this.punch = punch;
		this.block = block;
	}
	public void setUp(boolean up) {
		this.up = up;
	}
	public void setDown(boolean down) {
		this.down = down;
	}
	public void setLeft(boolean left) {
		this.left = left;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	public void setPunch(boolean punch) {
		this.punch = punch;
	}
	public void setBlock(boolean block) {
		this.block = block;
	}
	
	public boolean getUp() {
		return up;
	}
	public boolean getDown() {
		return down;
	}
	public boolean getLeft() {
		return left;
	}
	public boolean getRight() {
		return right;
	}
	public boolean getJump() {
		return jump;
	}
	public boolean getPunch() {
		return punch;
	}
	public boolean getBlock() {
		return block;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
		inv_mass = 1.0/mass;
	}
	public void setSpeed_x(double speed_x) {
		this.speed_x = speed_x;
	}
	public void setSpeed_y(double speed_y) {
		this.speed_y = speed_y;
	}
	public void setMaxSpeed_x(double max_speed_x) {
		this.max_speed_x = max_speed_x;
	}
	public void setMaxSpeed_y(double max_speed_y) {
		this.max_speed_y = max_speed_y;
	}
	public void setAcc(double acceleration) {
		acc = acceleration;
	}
	public void setPos(Point2D position) {
		centre = position;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public void setFacing(Heading facing) {
		this.facing = facing;
	}

	public double getMass() {
		return mass;
	}
	public double getSpeed_x() {
		return speed_x;
	}
	public double getSpeed_y() {
		return speed_y;
	}
	public double getMaxSpeed_x() {
		return max_speed_x;
	}
	public double getMaxSpeed_y() {
		return max_speed_y;
	}
	public double getAcc() {
		return acc;
	}
	public Point2D getPos() {
		return centre;
	}
	public double getRadius() {
		return radius;
	}
	public Heading getFacing() {
		return facing;
	}
}
