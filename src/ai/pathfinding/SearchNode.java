package ai.pathfinding;

import java.awt.Point;
import java.util.Comparator;

import resources.Map.Tile;

public class SearchNode
{
	private Point location; // TODO make fields final when ready
	private Tile type;
	private SearchNode parent;
	private double distTravelled;
	private double distToGo;

	private boolean isEmpty;

	/**
	 * Null constructor
	 */
	public SearchNode()
	{
		isEmpty = true;
	}

	/**
	 * Recursive contructor
	 * 
	 * @param location
	 * @param type
	 * @param parent
	 * @param distTravelled
	 * @param goal
	 */
	public SearchNode(Point location, Tile type, SearchNode parent, double distTravelled, Point goal)
	{
		isEmpty = false;
		this.location = location;
		this.type = type;
		this.parent = parent;
		this.distTravelled = distTravelled;
		this.distToGo = StaticHeuristics.euclidean(location, goal);
	}

	/**
	 * See how far we've come from the start
	 * 
	 * @return
	 */
	public double distanceTravelled()
	{
		return distTravelled;
	}

	/**
	 * An optimistic guess of how far we have to go
	 * 
	 * @return
	 */
	public double distanceToGo()
	{
		return distToGo;
	}

	public Point getLocation()
	{
		return location;
	}

	public static Comparator<SearchNode> priorityComparator()
	{
		return new SearchNodePriorityComparator();
	}

	public boolean isEmpty()
	{
		return isEmpty;
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