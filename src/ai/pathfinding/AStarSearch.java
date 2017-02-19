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
	private Point goal;
	private SearchNode current;
	private double distTravelled;

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
		this.goal = goal;
		this.distTravelled = 0;
		this.current = new SearchNode(start, new SearchNode(), distTravelled, goal);

		// first check if we're already there somehow
		if (start.equals(goal))
		{
			return new LinkedList<Point>();
		}

		// generate frontier by getting 8 surrounding tiles
		addNeighbours();
//		System.out.println(frontier);
		LinkedList<Point> ll = new LinkedList<Point>();
		
		boolean success = false;
		// explore best looking (first) option in frontier until there are no more unexplored reachable states
		while (!frontier.isEmpty() && !success)
		{
			// 	set current to the head of the frontier
			this.current = frontier.poll();
			
			if (!visited.contains(current.getLocation()))
			{
				if (current.getLocation().equals(goal))
				{
					System.out.println("Found the goal!");
					ll = reconstructPath();
					success = true;
					break;
				}
//				System.out.println("Searching " + current.getLocation().getX() + ", " + current.getLocation().getY());
//				System.out.println(frontier);
				visited.add(current.getLocation());
				addNeighbours();
			}

		}

		
		// TODO just before returning the final list,
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
				if (!(dirs[i] == 0 && dirs[j] == 0))
				{
					int dirI = dirs[i];
					int dirJ = dirs[j];
					
					// TODO need to make sure I'm getting the columns and rows the right way round
					int neiX = curLoc.x + dirI;
					int neiY = curLoc.y + dirJ;
					Point neiLoc = new Point(neiX, neiY);
					Tile neiTile = getTileType(neiLoc);

					// we only care if we've not seen it yet and we can walk on it
					if (!visited.contains(neiLoc))
					{	
						if (!resources.getBadTiles().contains(neiTile))
						{
//							System.out.println(neiTile);
							int distMoved = Math.abs(dirI) + Math.abs(dirJ);
							neighbours.add(new SearchNode(neiLoc, current, distTravelled + distMoved, goal));
						}	
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
		// TODO this returns null
		return resources.getMap().tileAt(location.getX(), location.getY());
	}
	
	/**
	 * When we have found the goal as a search node, follow the parents back to
	 * the source
	 * Builds the list from the front so we don't need to reverse it
	 * 
	 * @return a list of points that lead from the start to the goal
	 */
	private LinkedList<Point> reconstructPath()
	{
		SearchNode node = current;
		LinkedList<Point> ll = new LinkedList<Point>();
		while (!node.isEmpty())
		{
			ll.addFirst(node.getLocation());
			node = node.getParent();
		}

		return ll;
	}

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