package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import ai.pathfinding.AStarSearch;
import ai.pathfinding.Line;
import ai.pathfinding.Vector;
import resources.Character;
import resources.Map.Tile;
import resources.Resources;

public class VeryBasicAI extends Thread
{

	private Character character;
	private Resources resources;

	private enum Behaviour
	{
		COWARD, // runs from all danger
		AGGRESSIVE, // actively seeks other players

		POIROT, // use A* search to follow a list of points more intelligently
		ROVING, // debug: moves randomly
		PACING, // debug: moves up&down/left&right
		STUBBORN, // debug: tries to stop itself moving anywhere
		POTATO // debug: does literally nothing other than common behaviour
	};

	private Behaviour behaviour = Behaviour.POIROT; // default

	//	private int raycast_length = 10;
	private final double fuzziness = 30;
	//	private final long reaction_time = 5; // can be increased once ray-casting is implemented
	private final long tick = 70; // loop every <tick>ms
	private long prescience = tick * 1; // how many ms ahead we look for our predicted point

	private ArrayList<Tile> bad_tiles;

	private UUID id;

	private AStarSearch aStar;

	private LinkedList<Point> waypoints;

	private Point lastWaypoint; // the last waypoint we followed
	private Vector normalToNextWaypoint;

	private boolean success = true; // we start off a winner (because we need to be motivated to look for new goals) 

	// XXX debug stuff
	// this is setting things up for the debug Detective
	Point[] destinations = new Point[] { new Point(12, 28), new Point(8, 32), new Point(16, 38), new Point(20, 20) };
	int destI = 0; // destination index

	/*
	 * Notes:
	 * 
	 * TODO AI should ray-cast ahead of it and scan that area for danger
	 * For now change direction randomly to avoid danger
	 * (This is Coward behaviour)
	 * 
	 * TODO moveAwayFromEdge is still a bit dodgy 
	 *  
	 * Look at this
	 * https://www.javacodegeeks.com/2014/08/game-ai-an-introduction-to-
	 * behaviour-trees.html
	 * 
	 */

	public VeryBasicAI(Resources resources, Character character)
	{
		this.character = character;
		this.resources = resources;

		// the tiles we don't want to step on
		bad_tiles = resources.getBadTiles();

		waypoints = new LinkedList<Point>();

		id = character.getId();

		aStar = new AStarSearch(resources);

		lastWaypoint = getCurrentTileCoords();
	}

	/**
	 * The main execution loop of the AI
	 * How will this work with behaviours?
	 * Maybe behaviours will all be in a big switch statement in here.
	 * That's messy but with the structure we have it might be best
	 */
	public void run()
	{

		try
		{
			// The newborn AI stops to ponder life, and give me time to bring up the window and pay attention
			Thread.sleep(1000);

			while (!character.isDead())
			{
				// common behaviour should go first

				// TODO this should be the predicted future tile, not the one we are actually on
				// otherwise it's OP
				commonBehaviour();

				resources.setProjectedPos(projectedPosition());

				if (behaviour == Behaviour.POIROT)
				{
					poirotBehaviour();
				}
				else if (behaviour == Behaviour.STUBBORN)
				{
					stubbornBehaviour();
				}
				else if (behaviour == Behaviour.ROVING)
				{
					rovingBehaviour();
				}
				else if (behaviour == Behaviour.POTATO)
				{
					// literally nothing
				}
				else
				{
					System.out.println("Behaviour not yet implemented");
				}
				Thread.sleep(tick);
			}

			setAllMovementFalse();
			// XXX debug
			resources.setProjectedPos(null);

		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Performs 1 tick's worth of common behaviour
	 * (move away from edge, move to next waypoint, etc)
	 * 
	 * @throws InterruptedException
	 */
	private void commonBehaviour() throws InterruptedException
	{
		//		while (projectedTile() != Tile.FLAT && !character.isDead()) // if we are expected to be heading for a dangerous tile
		if (projectedTile() != Tile.FLAT)
		{
			//Thread.sleep(reaction_time);
			moveAwayFromEdge();
//			System.out.println("Near an edge!");
		}
		if (!waypoints.isEmpty())
		{
			// TODO if we are a certain distance away from the next waypoint, recalculate route
			// this may have to go in the behaviours
			
			moveToWaypoint();
		}
	}

	/**
	 * XXX Debug only
	 * Perform 1 tick's worth of Poirot
	 * (follow set points by A*, so hopefully isn't a lemming)
	 * 
	 * @throws InterruptedException
	 */
	private void poirotBehaviour() throws InterruptedException
	{

		if (waypoints.isEmpty())
		{
			//System.out.println();
//			System.out.println("made it to destination " + destinations[destI]);
			destI++;
			try
			{
				Point charPos = getCurrentTileCoords();
				//System.out.println(charPos);
				waypoints = convertWaypoints(aStar.search(charPos, destinations[destI]));

				resources.setDestList(waypoints); // XXX debug
				//resources.setAINextDest(resources.getMap().tileCoordsToMapCoords(destinations[destI].x, destinations[destI].y)); // XXX debug

				//System.out.println("pathfinding to point " + destinations[destI]);
				//System.out.println("waypoints: " + waypoints);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				destI = 0; // loop
			}
		}
	}

	/**
	 * XXX Debug only
	 * Performs 1 tick's worth of Stubborn behaviour
	 * (brakes)
	 * 
	 * @throws InterruptedException
	 */
	private void stubbornBehaviour() throws InterruptedException
	{
		brakeChar();
	}

	/**
	 * Performs 1 tick's worth of Roving behaviour
	 * (moves randomly)
	 * 
	 * @throws InterruptedException
	 */
	private void rovingBehaviour() throws InterruptedException
	{
		if (waypoints.isEmpty())
		{

			Point charPos = getCurrentTileCoords();
			Point newDest = getTileCoords(resources.getMap().randPointOnMap());
			//System.out.println("going to try to pathfind to " + newDest);
			while (waypoints.isEmpty())
			{
				// keep trying to get a new dest until we get a valid path
				// just in case point given is dodgy
				waypoints = convertWaypoints(aStar.search(charPos, newDest));
			}
			//System.out.println("New destination: " + newDest);		

			//System.out.println(waypoints);
			resources.setDestList(waypoints);

		}
	}

	/**
	 * Move to the next waypoint in our list
	 * 
	 * @throws InterruptedException
	 */
	private void moveToWaypoint() throws InterruptedException
	{
		resources.setAINextDest(waypoints.peek()); // XXX debug

		success = moveTo(waypoints.peek());
		if (success)
		{
			success = false;
			if (waypoints.size() > 0)
			{
//				System.out.println("made it to a waypoint!");
				lastWaypoint = waypoints.removeFirst();
				normalToNextWaypoint = normalToNextWaypoint();
				brakeChar();
			}

		}
	}

	/**
	 * Works out the normal to the next point based on the vector from the
	 * previous
	 * 
	 */
	private Vector normalToNextWaypoint()
	{
		if (waypoints.size() > 0)
		{
			// TODO test

			// work out vector from lastWaypoint to p
			Point a = lastWaypoint;
			Point b = waypoints.peek();
			Vector ab_vec = new Vector(a, b);
			// work out the normal to that (pointing right if original is up)
			Vector ab_norm = ab_vec.normal(b);

			//XXX debug
//			System.out.println(ab_norm);
			Point onNormal1 = new Point((int) (ab_norm.getCentre().getX() + 100 * ab_norm.getX()),
					(int) (ab_norm.getCentre().getY() + 100 * ab_norm.getY()));
			Point onNormal2 = new Point((int) (ab_norm.getCentre().getX() - 100 * ab_norm.getX()),
					(int) (ab_norm.getCentre().getY() - 100 * ab_norm.getY()));
			Line normal = new Line(onNormal1, onNormal2);
			resources.setNormal(normal);

			return ab_norm;
		}
		else
		{
			return null; // TODO seems iffy
		}

	}

	/**
	 * @return The type of tile the AI is standing on
	 */
	private Tile getCurrentTile()
	{
		return resources.getMap().tileAt(character.getX(), character.getY());
	}

	private Point getCurrentTileCoords()
	{
		return resources.getMap().tileCoords(character.getX(), character.getY());
	}

	private Point getTileCoords(Point p)
	{
		return resources.getMap().tileCoords(p.getX(), p.getY());
	}

	/**
	 * Uses current dy & dx to estimate position in "prescience" ms
	 * 
	 * @return
	 */
	private Point projectedPosition()
	{
		int x = (int) (character.getX() + prescience * character.getDx());
		int y = (int) (character.getY() + prescience * character.getDy());

		int maxX = (int) resources.getMap().getWidth() - 1; // -1 in case of rounding
		int maxY = (int) resources.getMap().getHeight() - 1;

		// clamp prediction to the map
		if (x < 0)
			x = 0;
		if (x > maxX)
			x = maxX;

		if (y < 0)
			y = 0;
		if (y > maxY)
			y = maxY;

		return new Point(x, y);
	}

	/**
	 * What tile is the AI predicted to be on in "prescience" ms
	 * 
	 * @return
	 */
	private Tile projectedTile()
	{
		Point proj = projectedPosition();
		return resources.getMap().tileAt(proj.getX(), proj.getY());
	}

	/**
	 * Can we walk/roll on this sort of tile?
	 * 
	 * @param tile
	 * @return true or false
	 */
	private boolean isWalkable(Tile tile)
	{
		return bad_tiles.contains(tile);
	}

	/**
	 * Convert a list of waypoints of tiles to use the coord system
	 * 
	 * @param waypoints a list of waypoints that uses the same coords as the
	 * character
	 * @return
	 */
	private LinkedList<Point> convertWaypoints(LinkedList<Point> waypoints)
	{
		LinkedList<Point> newWays = new LinkedList<Point>();
		for (int i = 0; i < waypoints.size(); i++)
		{
			Point old = waypoints.get(i);
			Point converted = resources.getMap().tileCoordsToMapCoords(old.x, old.y);
			newWays.add(i, converted);
		}

		return newWays;
	}

	/**
	 * 
	 * TODO this should use the new costMask
	 * 
	 * Move perpendicularly away from the imminent abyss
	 * TODO how would we cope if were surrounded on two/four sides?
	 * - shouldn't happen, hopefully
	 * do we care about diagonals?
	 * 
	 * @param currentTileIndex the index of the current tile
	 */

	private void moveAwayFromEdge() throws InterruptedException
	{
		Point currentTileIndex = getCurrentTileCoords();
		//		setAllMovementFalse();
		int column = (int) currentTileIndex.getX();
		int row = (int) currentTileIndex.getY();
		// get the surrounding tiles 
		// n.b. These values weren't the expected ones (tile_down should be row+1 by my reckoning) 	
		// They must not use the same +&- conventions as dy and dx 	
		// Or I'm being silly 		
		// But either way, this works: 		
		Tile tile_down = resources.getMap().tileAt(column - 2, row);
		Tile tile_up = resources.getMap().tileAt(column + 2, row);
		Tile tile_right = resources.getMap().tileAt(column, row - 2);
		Tile tile_left = resources.getMap().tileAt(column, row + 2);
		if (!isWalkable(tile_left))
		{
			character.setRight(true);
		}
		if (!isWalkable(tile_right))
		{
			character.setLeft(true);
		}
		if (!isWalkable(tile_up))
		{
			character.setDown(true);
		}
		if (!isWalkable(tile_down))
		{
			character.setUp(true);
		}
		Thread.sleep(10);
		setAllMovementFalse();
	}

	/**
	 * If we have overshot the waypoint then we can go onto the next one
	 * 
	 * @return true if overshoot detected (and dealt with)
	 */
	private boolean detectOvershoot()
	{
		// get where we are
		Point curLoc = new Point((int) character.getX(), (int) character.getY());

		// if we have a normal to be looking at
		if (normalToNextWaypoint != null)
		{
			// and we are beyond that normal
			if (!normalToNextWaypoint.pointInside(curLoc))
			{
				// skip to next waypoint
				// we've got to have one waypoint to remove and one to become the new head
				if (waypoints.size() >= 2)
				{
//					System.out.println("Overshot! Skipping ahead"); // TODO how come this is triggered more than a white girl on tumblr
					lastWaypoint = waypoints.removeFirst();
					normalToNextWaypoint = normalToNextWaypoint();
//					System.out.println("Now the next waypoint is " + waypoints.peek());
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Dumbly move as-the-(drunken-)crow-flies to coords.
	 * <br>
	 * n.b: This uses coords not tiles
	 * 
	 * @param x coord
	 * @param y coord
	 * @return are we nearly there yet?
	 * @throws InterruptedException
	 */
	private boolean moveTo(Point p) throws InterruptedException
	{
		// TODO fix this horrific bug with overshoots triggering too much:

		if (detectOvershoot())
			return false;

		if (fuzzyEqual(character.getX(), p.x) && fuzzyEqual(character.getY(), p.y))
		{
//			brakeChar();
			return true;
		}
		if (fuzzyEqual(character.getX(), p.x))
		{
			brakeChar();
		}
		if (fuzzyEqual(character.getY(), p.y))
		{
			brakeChar();
		}
		if (character.getX() < p.x)
		{
			character.setLeft(false);
			character.setRight(true);
		}
		if (character.getY() < p.y)
		{
			character.setUp(false);
			character.setDown(true);
		}
		if (character.getX() > p.x)
		{
			character.setRight(false);
			character.setLeft(true);
		}
		if (character.getY() > p.y)
		{
			character.setDown(false);
			character.setUp(true);
		}
		return false;
	}

	/**
	 * 
	 * Are two coords equal give or take fuzziness?
	 * 
	 * @param coord1
	 * @param coord2
	 * @return true if the two values are close enough
	 */
	private boolean fuzzyEqual(double coord1, double coord2)
	{
		return (Math.abs(coord1 - coord2) <= fuzziness);
	}

	/**
	 * 'Detach' all keys
	 */
	private void setAllMovementFalse()
	{
		character.setUp(false);
		character.setDown(false);
		character.setLeft(false);
		character.setRight(false);
	}

	/**
	 * Given how fast we are going, for how long should we brake?
	 * TODO This is a very experimental equation and should be tinkered with
	 * 
	 * @param velocity component of dx and dy
	 * @return the number of milliseconds to spend braking
	 */
	private long brakingTime(double velocity)
	{
		long bt = (long) (40 * velocity);
		//		System.out.println(bt);
		return bt;
	}

	/**
	 * Try and slow the character down
	 * 
	 * @throws InterruptedException
	 */
	private void brakeChar() throws InterruptedException
	{
		// 'release' all keys
		setAllMovementFalse();
		character.setBlock(true);

		double dX = character.getDx();
		double dY = character.getDy();

		// work out velocity
		double vel = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

		// from speed work out how long to stop for
		long time = brakingTime(vel); // do it for this many milliseconds

		// work out which direction
		double proportionDx = Math.abs(dX / dY);
		double ratioDx = 0.5; // how much of the delay should go to countering dX

		// work out relevant ratios (0 and infinity caught later)
		if (proportionDx < 1)
		{
			// more UP/DOWN than LEFT/RIGHT
			ratioDx = 1 - proportionDx;
		}
		else if (proportionDx >= 1)
		{
			// more LEFT/RIGHT than UP/DOWN
			ratioDx = 1 / proportionDx;
		}

		// tap in the opposite direction for a proportional amount of time in each direction
		if (Math.signum(dX) == -1)
		{
			if (Math.signum(dY) == -1)
			{
				// UP-LEFT
				long delayX = (long) (ratioDx * time);
				character.setRight(true);
				Thread.sleep(delayX);
				character.setRight(false);
				character.setDown(true);
				Thread.sleep(time - delayX);
				character.setDown(false);

			}
			else if (Math.signum(dY) == 1)
			{
				// DOWN-LEFT
				long delayX = (long) (ratioDx * time);
				character.setRight(true);
				Thread.sleep(delayX);
				character.setRight(false);
				character.setUp(true);
				Thread.sleep(time - delayX);
				character.setUp(false);
			}
			else
			{
				// LEFT
				character.setRight(true);
				Thread.sleep(time);
				character.setRight(false);
				// don't need to do up/down
			}
		}
		else if (Math.signum(dX) == 1)
		{
			if (Math.signum(dY) == -1)
			{
				// UP-RIGHT
				long delayX = (long) (ratioDx * time);
				character.setLeft(true);
				Thread.sleep(delayX);
				character.setLeft(false);
				character.setDown(true);
				Thread.sleep(time - delayX);
				character.setDown(false);
			}
			else if (Math.signum(dY) == 1)
			{
				// DOWN-RIGHT
				long delayX = (long) (ratioDx * time);
				character.setLeft(true);
				Thread.sleep(delayX);
				character.setLeft(false);
				character.setUp(true);
				Thread.sleep(time - delayX);
				character.setUp(false);
			}
			else
			{
				// RIGHT
				character.setLeft(true);
				Thread.sleep(time);
				character.setLeft(false);
				// don't need to do up/down
			}
		}
		else
		{
			if (Math.signum(dY) == -1)
			{
				// UP
				// don't need to do left/right
				character.setDown(true);
				Thread.sleep(time);
				character.setDown(false);
			}
			else if (Math.signum(dY) == 1)
			{
				// DOWN
				// don't need to do left/right
				character.setUp(true);
				Thread.sleep(time);
				character.setUp(false);
			}
			else
			{
				// already stopped... whoops
				return;
			}
		}

		character.setBlock(false);

	}

}
