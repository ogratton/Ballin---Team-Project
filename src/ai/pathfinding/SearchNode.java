package ai.pathfinding;

import java.awt.Point;
import java.util.Comparator;

public class SearchNode
{
	private Point location;
	private SearchNode parent;
	private double costSoFar;
	private double distToGo;

	private final boolean isEmpty;

	/**
	 * Null constructor
	 */
	public SearchNode()
	{
		isEmpty = true;
	}

	/**
	 * Recursive constructor
	 * 
	 * @param location
	 * @param type
	 * @param parent
	 * @param costSoFar
	 * @param goal
	 */
	public SearchNode(Point location, SearchNode parent, double costSoFar, Point goal)
	{
		isEmpty = false;
		this.location = location;
		this.parent = parent;
		this.costSoFar = costSoFar;
		this.distToGo = StaticHeuristics.euclidean(location, goal);

		
	}

	/**
	 * See how far we've come from the start
	 * 
	 * @return number of squares traversed to get to this point
	 */
	public double distanceTravelled()
	{
		return costSoFar;
	}

	/**
	 * An optimistic guess of how far we have to go
	 * (Must be admissible as a requirement of A*)
	 * 
	 * @return Euclidean distance to goal
	 */
	public double distanceToGo()
	{
		return distToGo;
	}

	/**
	 * @return grid reference of this node on the map
	 */
	public Point getLocation()
	{
		return location;
	}

	/**
	 * Enables the frontier to order its nodes in priority of heuristics
	 */
	public static Comparator<SearchNode> priorityComparator()
	{
		return new SearchNodePriorityComparator();
	}

	/**
	 * @return the node that we got to this node from
	 */
	public SearchNode getParent()
	{
		return parent;
	}

	/**
	 * Empty nodes are what the start nodes' parents are
	 */
	public boolean isEmpty()
	{
		return isEmpty;
	}
	
	public String toString()
	{
		return ("("+location.getX()+","+location.getY()+")");
	}

}

/**
 * This tells us how to sort the frontier (which is a PriorityQueue)
 * Favour smaller distances
 * 
 * @author Oliver Gratton
 *
 */
class SearchNodePriorityComparator implements Comparator<SearchNode>
{
	@Override
	public int compare(SearchNode n1, SearchNode n2)
	{
		int val = (int) ((n1.distanceTravelled() + n1.distanceToGo()) - (n2.distanceTravelled() + n2.distanceToGo()));
		if (val > 1)
			val = 1;
		if (val < -1)
			val = -1;
		return val;
	}
}