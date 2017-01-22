package resources;

import java.awt.geom.Point2D;

public class Wall {
	private Point2D origin;
	private double width, height;
	private double heading; //degrees from north, anticlockwise
	
	// TODO getters/setters/constructors
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
}
