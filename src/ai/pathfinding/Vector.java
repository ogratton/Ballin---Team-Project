package ai.pathfinding;

import java.awt.Point;

/**
 * An attempt at vectors in Java
 * Tailored a bit for what I actually need
 * Really more of a line with a single point
 * 
 * @author Oliver Gratton
 *
 */
public class Vector
{

	private double dx, dy;
	private Point centre;

	/**
	 * Make a vector from two points
	 * 
	 * @param source
	 * @param dest
	 */
	public Vector(Point source, Point dest)
	{
		this.dx = dest.x - source.x;
		this.dy = dest.y - source.y;
		// normalise
		double sum = Math.abs(dx) + Math.abs(dy);
		dx = dx / sum;
		dy = dy / sum;
		this.centre = dest;
	}

	/**
	 * Make a new vector directly
	 * Automatically normalises
	 * 
	 * @param x
	 * @param y
	 */
	public Vector(double x, double y, Point centre)
	{
		double sum = Math.abs(x) + Math.abs(y);
		this.dx = x / sum;
		this.dy = y / sum;
		this.centre = centre;
	}

	public Point getCentre()
	{
		return centre;
	}

	public double getX()
	{
		return dx;
	}

	public double getY()
	{
		return dy;
	}

	/**
	 * @param centre the point the new vector should be centred on
	 * @return the normal of this vector
	 */
	public Vector normal(Point centre)
	{
		return new Vector(dy, -dx, centre);
	}

	/**
	 * Is point p inside (i.e. to the left of) the vector?
	 * This is used on normals by AIs following waypoints.
	 * Being inside means that the waypoint is in the same
	 * direction as the goal, but being outside means that
	 * it should skip to the next waypoint as it has overshot
	 * 
	 * @param p
	 * @return
	 */
	public boolean pointInside(Point p)
	{
		boolean bool = true;

		// TODO I don't think we care if this.x or this.y are 0 but check
		if (this.dx < 0)
		{
			bool &= p.y >= getYfromX(p.x);
		}
		if (this.dx > 0)
		{
			bool &= p.y <= getYfromX(p.x);
		}
		if (this.dy < 0)
		{
			bool &= p.x <= getXfromY(p.y);
		}
		if (this.dy > 0)
		{
			bool &= p.x >= getXfromY(p.y);
		}

		return bool;
	}
	
	/**
	 * Work out the y coord on the vector from the centre given x
	 * @param x
	 * @return
	 */
	private double getYfromX(double x)
	{
		double xFromCentre = x - centre.getX();
		double times = xFromCentre/this.dx;
		double y = centre.getY() + (this.dy*times);
		
		return y;
	}
	
	/**
	 * Work out the x coord on the vector from the centre given y
	 * @param y
	 * @return
	 */
	private double getXfromY(double y)
	{
		double yFromCentre = y - centre.getY();
		double times = yFromCentre/this.dy;
		double x = centre.getX() + (this.dx*times);
		
		return x;
	}

	public String toString()
	{
		return "[ " + dx + " " + dy + " ]";
	}

}
