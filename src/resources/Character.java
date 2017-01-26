package resources;

public class Character {
	private static final double default_mass = 1.0;
	private static final int default_radius = 20; //pixels
	private static final double default_max_speed_x = 5;
	private static final double default_max_speed_y = 5;
	private static final double default_acc = 0.2;
	
	public enum Heading{N,E,S,W,NE,NW,SE,SW};
	
	// this will have all the Character classes in use.
	public enum Class{DEFAULT}; // add to this as we develop more classes.
	
	// flags for keys pressed. 
	// e.g. if up is true, then the player/ai is holding the up button.
	// jump is currently not being used, but is there if we need it.
	// jump punch and/or block may be replaced by a single 'special' flag,
	//   which does an action based on the class of the character.
	// Collided flag added to help with collision calculations
	private boolean up, right, left, down, jump, punch, block, collided = false;
	
	//these are for the physics engine.
	private double mass, inv_mass,  dx, dy, maxdx, maxdy, acc = 0.0;
	
	// these are for the physics engine and the graphics engine.
	// Characters are circles.
	// x and y are the coordinates of the centre of the circle, relative to the
	//   top-left of the arena.
	// radius is the radius of the circle, in arbitrary units.
	// facing is the direction that the character's facing
	//   (this is entirely for graphics)
	private double x,y = 0.0;
	private int radius = 0;
	private Heading facing = Heading.N;
	
	public Character() {
		this(default_mass, 0, 0, default_radius, Heading.N);
	}
	
	public Character(double mass, double x, double y, int radius, Heading facing){
		this(false, false, false, false, false, false, false, // control flags
				mass, 
				x,   // x
				y,   // y
				0.0, // speed_x 
				0.0, // speed_y 
				default_max_speed_x * (1/mass),
				default_max_speed_y * (1/mass), 
				default_acc, // acceleration (TODO: calculate this)
				radius, facing);
	}
	
	/** 
	 * create character based on it's class.
	 * @param c the class of the character
	 */
	public static Character character(Class c) {
		switch(c) {
		case DEFAULT:
			return new Character();
		}
		return new Character();
	}
	
	// master constructor. Any other constructors should eventually call this.
	private Character
	(
	boolean up, boolean right, boolean left, boolean down, boolean jump, 
	boolean punch, boolean block, double mass, double x, double y, double speed_x, double speed_y,
	double max_speed_x, double max_speed_y, double acceleration, int radius, Heading facing
	) {
		//new Character();
		this.up = up;
		this.right = right; 
		this.left = left;
		this.down = down;
		this.jump = jump;
		this.punch = punch;
		this.block = block;
		
		this.mass = mass;
		this.inv_mass = 1.0/mass;
		
		this.x = x;
		this.y = y;
		this.dx = speed_x;
		this.dy = speed_y;
		this.maxdx = max_speed_x;
		this.maxdy = max_speed_y;
		this.acc = acceleration;
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
	//getters
	public boolean up() {
		return up;
	}
	public boolean down() {
		return down;
	}
	public boolean left() {
		return left;
	}
	public boolean right() {
		return right;
	}
	public boolean jump() {
		return jump;
	}
	public boolean punch() {
		return punch;
	}
	public boolean block() {
		return block;
	}
	//setters
	public void mass(double mass) {
		this.mass = mass;
		inv_mass = 1.0/mass;
	}
	public void x(double x) {
		this.x = x;
	}
	public void y(double y) {
		this.y = y;
	}
	public void dx(double speed_x) {
		this.dx = speed_x;
	}
	public void dy(double speed_y) {
		this.dy = speed_y;
	}
	public void maxdx(double max_speed_x) {
		this.maxdx = max_speed_x;
	}
	public void maxdy(double max_speed_y) {
		this.maxdy = max_speed_y;
	}
	public void acc(double acceleration) {
		this.acc = acceleration;
	}
	public void radius(int radius) {
		this.radius = radius;
	}
	public void setFacing(Heading facing) {
		this.facing = facing;
	}
	//getters
	public double mass() {
		return mass;
	}
	public double inv_mass() {
		return inv_mass;
	}
	public double x() {
		return x;
	}
	public double y() {
		return y;
	}
	public double dx() {
		return dx;
	}
	public double dy() {
		return dy;
	}
	public double maxdx() {
		return maxdx;
	}
	public double maxdy() {
		return maxdy;
	}
	public double acc() {
		return acc;
	}
	public int radius() {
		return radius;
	}
	public Heading getFacing() {
		return facing;
	}

	public boolean collided() {
		return collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}
}
