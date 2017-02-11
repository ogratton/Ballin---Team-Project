package resources;

public class Wall implements Collidable{
	//point1 is the leftmost point (x can be equal to p3, if rectangle)
	//point2 is the highest point (y can be equal to p1, if rectangle)
	//point3 is the lowest point (y can be equal to p4, if rectangle)
	//point4 is the rightmost point (x can be equal to p2, if )
	private double x1, x2, x3, x4;
	private double y1, y2, y3, y4;
	private double midx, midy; //midpoint
	//private double width, height;
	//private double heading; //angle from north, clockwise in rads
	
	private double inv_mass = 0; // infinite mass
	
	public Wall(double x, double y, double width, double height, double heading) {
		//TODO: calculate rotation properly
		//create box at origin
		x1 = 0;
		y1 = 0;
		x2 = width;
		y2 = 0;
		x3 = 0;
		y3 = height;
		x4 = width;
		y4 = height;
		//rotate box around point1 (0,0)
		//Math.cos(a)
		
		//translate box to location
		x1 += x;
		y1 += y;
		x2 += x;
		y2 += y;
		x3 += x;
		y3 += y;
		x4 += x;
		y4 += y;
		
		//calculate midpoint
		midx = (x4 - x1)/2;
		midy = (y4 - y1)/2;
	}

	/**
	 * returns the x position of point1 of the wall.
	 * point1.x <= point3.x
	 * point1.y <  point3.y
	 * @return
	 */
	public double getX1() {
		return x1;
	}
	
	/**
	 * returns the y position of point1 of the wall.
	 * point1.x <= point3.x
	 * point1.y <  point3.y
	 * @return
	 */
	public double getY1() {
		return y1;
	}

	/**
	 * returns the x position of point2 of the wall.
	 * point2.x >= point4.x
	 * point2.y <  point4.y
	 * @return
	 */
	public double getX2() {
		return x2;
	}
	
	/**
	 * returns the y position of point2 of the wall.
	 * point2.x >= point4.x
	 * point2.y <  point4.y
	 * @return
	 */
	public double getY2() {
		return y2;
	}

	/**
	 * returns the x position of point3 of the wall.
	 * point1.x <= point3.x
	 * point1.y <  point3.y
	 * @return
	 */
	public double getX3() {
		return x3;
	}
	
	/**
	 * returns the y position of point3 of the wall.
	 * point1.x <= point3.x
	 * point1.y <  point3.y
	 * @return
	 */
	public double getY3() {
		return y3;
	}

	/**
	 * returns the x position of point4 of the wall.
	 * point2.x >= point4.x
	 * point2.y <  point4.y
	 * @return
	 */
	public double getX4() {
		return x4;
	}
	
	/**
	 * returns the y position of point4 of the wall.
	 * point2.x >= point4.x
	 * point2.y <  point4.y
	 * @return
	 */
	public double getY4() {
		return y4;
	}
	
	/**
	 * returns the middle value of x
	 * @return
	 */
	public double getMidx() {
		return midx;
	}
	
	/**
	 * returns the middle value of y
	 * @return
	 */
	public double getMidy() {
		return midy;
	}
	
	

	@Override
	public double getInvMass() {
		return inv_mass;
	}

	@Override
	public double getRestitution() {
		return 0;
	}

	@Override
	public double getDx() {
		return 0;
	}

	@Override
	public double getDy() {
		return 0;
	}

	@Override
	public void setDx(double dx) {
		return;
	}

	@Override
	public void setDy(double dx) {
		return;
	}
}
