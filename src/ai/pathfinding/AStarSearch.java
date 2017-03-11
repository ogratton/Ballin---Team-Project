package ai.pathfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeSet;

import resources.Map.Tile;
import resources.Resources;

/**
 * Perform A* search on a 2D array of tiles
 * @author Oliver Gratton
 *
 */
public class AStarSearch
{
	private PriorityQueue<SearchNode> frontier;
	private TreeSet<Point> visited;
	private Point goal;
	private SearchNode current;
	private double costSoFar;
	
	private final int width, height;

	private Resources resources;

	public AStarSearch(Resources resources)
	{
		this.resources = resources;
		
		width = resources.getMap().getCostMask().length;
		height = resources.getMap().getCostMask()[0].length;
	}

	/**
	 * Returns a list of waypoints between the start and goal points
	 * @param start
	 * @param goal
	 * @return
	 */
	public LinkedList<Point> search(Point start, Point goal)
	{
		// reset all the fields
		this.frontier = new PriorityQueue<SearchNode>(SearchNode.priorityComparator()); // sorts by cost + heuristic.
		this.visited = new TreeSet<Point>(new PointComparator());
		this.goal = goal;
		this.costSoFar = 0;
		this.current = new SearchNode(start, new SearchNode(), costSoFar, goal);

		// first check if we're already there somehow
		if (start.equals(goal))
		{
			return new LinkedList<Point>();
		}

		// generate frontier by getting 8 surrounding tiles
		addNeighbours();
		//System.out.println(frontier);
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
					//System.out.println("Found the goal!");
					ll = reconstructPath();
					success = true;
					break;
				}
				//System.out.println("Searching " + current.getLocation().getX() + ", " + current.getLocation().getY());
				//System.out.println(frontier);
				visited.add(current.getLocation());
				addNeighbours();
			}

		}

		// just before returning the final list,
		// make as sparse as possible so that the only
		// elements are corners.
		// this may be awkward with linked lists
		ll = smoothPath(ll);
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
		for (int i = -1; i < 2; i++)
		{
			if ((curLoc.x + i) < width && (curLoc.x + i) >= 0)
			{
				for (int j = -1; j < 2; j++)
				{
					if ((curLoc.y + j) < height && (curLoc.y + j) >= 0)
					{
						// don't want to add the current node in again
						if (!(i == 0 && j == 0))
						{
							int neiX = curLoc.x + i;
							int neiY = curLoc.y + j;
							Point neiLoc = new Point(neiX, neiY);
							Tile neiTile = resources.getMap().tileAt(neiX, neiY); //getTileType(neiLoc);

							// we only care if we've not seen it yet and we can walk on it
							if (!visited.contains(neiLoc))
							{
								if (!resources.getBadTiles().contains(neiTile))
								{
									// cost of the move
									//int cost = Math.abs(dirI) + Math.abs(dirJ);
//									Point tileLoc = resources.getMap().tileCoords(neiX, neiY);
									
//									System.out.println(neiX + " " + neiY);
									
									double cost = resources.getMap().getCostMask()[neiX][neiY]; // XXX ???
									neighbours.add(new SearchNode(neiLoc, current, costSoFar + cost, goal));
								}
							}
						}
					}
				}
			}
		}

		frontier.addAll(neighbours);
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

	/**
	 * Keep only every "gap"th waypoint
	 * 
	 * @param dense the dense linked list to be pruned
	 * @return a less dense list of waypoints for the AI to follow
	 */
	private LinkedList<Point> sparsifyPath(LinkedList<Point> dense, int gap)
	{
		LinkedList<Point> sparse = new LinkedList<Point>();
		Point destination = dense.getLast();
		int init_size = dense.size();

		for (int position = 0; dense.size() > 1; position += gap)
		{
			for (int i = 0; i < gap && position + i < init_size; i++)
			{
				Point temp = dense.removeFirst();
				if (i == 0)
				{
					sparse.addLast(temp);
				}
			}
		}
		
		sparse.addLast(destination);

		return sparse;
	}
	
	/**
	 * Smooth the path found by A* to make it look more natural
	 * @param jagged path raw from A*
	 * @return a smoothed version of jagged
	 */
	private LinkedList<Point> smoothPath(LinkedList<Point> jagged)
	{
		LinkedList<Point> sparse = sparsifyPath(jagged, 3); // only keep every third waypoint
		
		// TODO
		// if normal is same/nearly the same as the last, we can get rid of the last
		
		Point lastWaypoint = null;
		Vector lastVector = null;
		int i = 0; // index of current waypoint
		
		while(sparse.size() > 1 && i < sparse.size())
		{
			// every time after the first
			if (lastWaypoint != null)
			{
				Vector currentVector = new Vector(lastWaypoint, sparse.get(i));
				if (lastVector != null && currentVector.equalDirection(lastVector))
				{
//					System.out.println(currentVector + " ~= " + lastVector);
					// remove the last waypoint from the list, as it is too similar to the current one
					sparse.remove(i-1);
					// we also need to move i back a step now
					i--;
				}
				else
				{
					System.out.println(currentVector + " != " + lastVector);
				}
				lastVector = currentVector;
			}
			
			lastWaypoint = sparse.get(i);
			i++;
		}
		
		return sparse;
	}

}