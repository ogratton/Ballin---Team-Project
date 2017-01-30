package resources;

import java.awt.geom.Point2D;

public class Wall implements Collidable{
	private Point2D origin;
	private double width, height;
	private double heading; //angle from north, anticlockwise. 
	//0 is default orientation. 1 and -1 are rotated 180 degrees.
	
	private double inv_mass = 0; // infinite mass
	
	public Wall(Point2D origin, double width, double height, double heading) {
		this.origin = origin;
		this.width = width;
		this.height = height;
		this.heading = heading;
	}
	
	public Point2D origin() {
		return origin;
	}
	
	public void origin(Point2D origin) {
		this.origin = origin;
	}
	
	public double width() {
		return width;
	}
	
	public double height() {
		return height;
	}
	
	public double heading() {
		return heading;
	}
	
	public void width(double width) {
		this.width = width;
	}
	
	public void height(double height) {
		this.height = height;
	}
	
	public void heading(double heading) {
		this.heading = heading;
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
