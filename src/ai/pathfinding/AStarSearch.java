package ai.pathfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeSet;

import resources.Map.Tile;
import resources.Resources;

public class AStarSearch
{
	private PriorityQueue<SearchNode> frontier;
	private TreeSet<Point> visited;
	private Point start;
	private Point goal;
	private SearchNode current;
	private double distTravelled;
	private double distToGo;

	private Resources resources;

	public AStarSearch(Resources resources)
	{
		this.resources = resources;
	}

	public LinkedList<Point> search(Point start, Point goal)
	{
		// reset all the fields
		this.frontier = new PriorityQueue<SearchNode>(SearchNode.priorityComparator()); // sorts by cost + heuristic.
		this.visited = new TreeSet<Point>(new PointComparator());
		this.start = start;
		this.goal = goal;
		this.distTravelled = 0;
		this.distToGo = StaticHeuristics.euclidean(start, goal);
		this.current = new SearchNode(start, getTileType(start), new SearchNode(), distTravelled, goal);

		// first check if we're already there somehow
		if (start.equals(goal))
		{
			return new LinkedList<Point>();
		}

		// generate frontier by getting 8 surrounding tiles
		addNeighbours();
		// explore best looking option in frontier (thank you priority queues)
		while (!frontier.isEmpty())
		{
			// 	set current to the head of the frontier
			this.current = frontier.poll();
			if (current.getLocation().equals(goal))
			{
				// TODO make path
			}
			visited.add(current.getLocation());
			addNeighbours();
		}

		LinkedList<Point> ll = new LinkedList<Point>();
		// just before returning the final list,
		// make as sparse as possible so that the only
		// elements are corners.
		// this may be awkward with linked lists
		return ll;
	}

	/**
	 * Add neighbours of tile to frontier (don't include 'bad' tiles e.g. abyss,
	 * wall, etc.)
	 * 
	 * @param tile tile whose (up to) 8 neighbours we are to add to the frontier
	 * @return
	 */
	private void addNeighbours()
	{
		ArrayList<SearchNode> neighbours = new ArrayList<SearchNode>();
		Point curLoc = current.getLocation();
		int[] dirs = new int[] { 0, -1, 1 };
		for (int i = 0; i < dirs.length; i++)
		{
			for (int j = 0; j < dirs.length; j++)
			{
				// don't want to add the current node in again
				if (!(i == 0 && j == 0))
				{
					// TODO need to make sure I'm getting the columns and rows the right way round
					int neiX = curLoc.x + i;
					int neiY = curLoc.y + i;
					Point neiLoc = new Point(neiX, neiY);

					// we only care if we've not seen it yet
					if (!visited.contains(neiLoc))
					{
						Tile neiTile = getTileType(neiLoc);
						int distMoved = Math.abs(i) + Math.abs(j);
						neighbours.add(new SearchNode(neiLoc, neiTile, current, distTravelled + distMoved, goal));
					}

				}
			}
		}

		frontier.addAll(neighbours);
	}

	/**
	 * Get the type of tile at a given grid reference
	 * 
	 * @param location
	 * @return
	 */
	private Tile getTileType(Point location)
	{
		return resources.getMap().tileAt(location.x, location.y);
	}

	// TODO reconstruct path function (just follow parents)

	// TODO minimise path function (see commments in search func)
}

/*
 * So we can have a set of Points
 * (Author: Alex from first year robot project :P)
 */
class PointComparator implements Comparator<Point>
{
	@Override
	public int compare(Point p1, Point p2)
	{
		if (p1.equals(p2))
			return 0;
		else if (p1.x < p2.x)
			return -1;
		else if (p1.x > p2.x)
			return 1;
		else if (p1.y < p2.y)
			return -1;
		else
			return 1;
	}

}