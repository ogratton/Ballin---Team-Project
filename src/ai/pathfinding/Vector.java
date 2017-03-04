package ai.pathfinding;

import java.awt.Point;

/**
 * An attempt at vectors in Java
 * Tailored a bit for what I actually need
 * 
 * @author Oliver Gratton
 *
 */
public class Vector
{

	private double x, y;
	private Point centre;

	/**
	 * Make a vector from two points
	 * 
	 * @param source
	 * @param dest
	 */
	public Vector(Point source, Point dest)
	{
		this.x = dest.x - source.x;
		this.y = dest.y - source.y;
		// normalise
		double sum = Math.abs(x) + Math.abs(y);
		x = x / sum;
		y = y / sum;
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
		this.x = x / sum;
		this.y = y / sum;
		this.centre = centre;
	}

	public Point getCentre()
	{
		return centre;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	/**
	 * @param centre the point the new vector should be centred on
	 * @return the normal of this vector
	 */
	public Vector normal(Point centre)
	{
		return new Vector(y, -x, centre);
	}

	/**
	 * Is point p inside (i.e. to the right of) the vector?
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
		if (this.x < 0)
		{
			bool &= p.y <= centre.y;
		}
		if (this.x > 0)
		{
			bool &= p.y >= centre.y;
		}
		if (this.y < 0)
		{
			bool &= p.x <= centre.x;
		}
		if (this.y > 0)
		{
			bool &= p.x >= centre.x;
		}

		return bool;
	}

	public String toString()
	{
		return "[ " + x + " " + y + " ]";
	}

}
