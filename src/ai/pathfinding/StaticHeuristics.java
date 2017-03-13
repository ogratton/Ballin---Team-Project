package ai.pathfinding;

import java.awt.Point;

/**
 * Public methods I may need to access in both AStarSearch and SearchNode, and
 * maybe elsewhere
 * 
 * @author Oliver Gratton
 *
 */
public class StaticHeuristics
{

	/**
	 * @param a
	 * @param b
	 * @return the Euclidean distance between two points, a and b
	 */
	public static double euclidean(Point a, Point b)
	{
		double dist = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
		return dist;
	}
	
	public static double manhattan(Point a, Point b)
	{
		double dist = Math.abs((Math.abs(a.getX())-Math.abs(b.getX())) + (Math.abs(a.getY()) - Math.abs(b.getY())));
		return dist;
	}
}
