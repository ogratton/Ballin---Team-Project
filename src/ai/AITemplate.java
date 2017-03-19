package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import ai.AITemplate.Behaviour;
import ai.pathfinding.AStarSearch;
import ai.pathfinding.Line;
import ai.pathfinding.StaticHeuristics;
import ai.pathfinding.Vector;
import resources.Character;
import resources.Map.Tile;
import resources.Resources;

/**
 * Abstract AI template
 * 
 * @author Oliver Gratton
 *
 */
public abstract class AITemplate extends Thread
{
	protected Character character;
	protected Resources resources;

	protected enum Behaviour
	{
		COWARD, // runs from all danger
		AGGRESSIVE, // actively seeks other players

		POIROT, // use A* search to follow a list of points more intelligently
		ROVING, // debug: moves randomly
		STUBBORN, // debug: tries to stop itself moving anywhere
		POTATO // debug: does literally nothing other than common behaviour
	};

	protected Behaviour behaviour = Behaviour.ROVING; // default

	//	private int raycast_length = 10;
	protected static final double BRAKING_CONSTANT = 40; // how many ms to brake for. 40-50 seems good
	protected static final double FUZZINESS = 20;
	//	private final long reaction_time = 5; // can be increased once ray-casting is implemented
	protected static final long TICK = 30; // loop every <tick>ms
	protected static long PRESCIENCE = TICK * 1; // how many ms ahead we look for our predicted point

	protected ArrayList<Tile> bad_tiles;

	protected String id;

	protected AStarSearch aStar;

	protected LinkedList<Point> waypoints;

	protected Point lastWaypoint; // the last waypoint we followed
	protected Vector normalToNextWaypoint;

	protected boolean success = true; // we start off a winner (because we need to be motivated to look for new goals) 

	protected ArrayList<Tile> non_edge; // all tiles that are not WALKABLE edge tiles (not EDGE_ABYSS)

	protected Character currentTarget; // for aggressive mode
	protected Point currentGoal; // for aggressive mode

	// XXX debug stuff
	// this is setting things up for the debug Detective
	protected Point[] destinations = new Point[] { new Point(12, 28), new Point(8, 32), new Point(16, 38), new Point(20, 20) };
	protected int destI = 0; // destination index
	protected boolean debug;

	/*
	 * Notes:
	 * 
	 * TODO AI should ray-cast ahead of it and scan that area for danger
	 * For now change direction randomly to avoid danger
	 * (This is Coward behaviour)
	 * 
	 * TODO moveAwayFromEdge is still a bit dodgy
	 * 
	 * 
	 * Look at this
	 * https://www.javacodegeeks.com/2014/08/game-ai-an-introduction-to-
	 * behaviour-trees.html
	 * 
	 */

	public AITemplate(Resources resources, Character character)
	{
		this.character = character;
		this.resources = resources;

		// the tiles we don't want to step on
		bad_tiles = resources.getBadTiles();

		non_edge = new ArrayList<Tile>();
		non_edge.addAll(bad_tiles);
		non_edge.add(Tile.FLAT);

		waypoints = new LinkedList<Point>();

		id = character.getId();

		aStar = new AStarSearch(resources);

		lastWaypoint = getCurrentTileCoords();

		// XXX debug
		debug = character.getPlayerNumber() == 0 ? true : false;
	}

	/**
	 * The main execution loop of the AI
	 * Performs common behaviour and current behaviour once every 'tick'
	 */
	public void run()
	{

		try
		{
			// The newborn AI stops to ponder life, and give me time to bring up the window and pay attention
			Thread.sleep(500);

			while (!character.isDead())
			{

				// common behaviour goes first
				commonBehaviour();

				// XXX debug
				if (debug)
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
				else if (behaviour == Behaviour.COWARD)
				{
					// TODO
					cowardBehaviour();
				}
				else if (behaviour == Behaviour.AGGRESSIVE)
				{
					// TODO
					aggressiveBehaviour();
				}
				else if (behaviour == Behaviour.POTATO)
				{
					// literally nothing
				}
				else
				{
					System.out.println("Behaviour not yet implemented");
				}
				Thread.sleep(TICK);

			}

			funeral();

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
	protected abstract void commonBehaviour() throws InterruptedException;

	/**
	 * XXX Debug only
	 * Perform 1 tick's worth of Poirot
	 * (follow set points by A*, so hopefully isn't a lemming)
	 * 
	 * @throws InterruptedException
	 */
	protected void poirotBehaviour() throws InterruptedException
	{

		if (waypoints.isEmpty())
		{
			if (debug)
			{
				System.out.println();
				System.out.println("made it to destination " + destinations[destI]);
			}

			destI++;
			if (destI >= destinations.length)
			{
				destI = 0; // loop
			}
			
			Point charPos = getCurrentTileCoords();
			//if (debug) System.out.println(charPos);
			waypoints = convertWaypoints(aStar.search(charPos, destinations[destI]));

			// XXX debug
			if (debug)
			{
				resources.setDestList(waypoints);
				resources.setAINextDest(resources.getMap().tileCoordsToMapCoords(destinations[destI].x, destinations[destI].y));

				System.out.println("pathfinding to point " + destinations[destI]);
				//System.out.println("waypoints: " + waypoints);
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
	protected void stubbornBehaviour() throws InterruptedException
	{
		brakeChar();
	}

	/**
	 * Performs 1 tick's worth of Roving behaviour
	 * (moves randomly)
	 * 
	 * @throws InterruptedException
	 */
	protected abstract void rovingBehaviour() throws InterruptedException;

	/**
	 * Performs 1 tick's worth of Coward behaviour
	 * (runs away from all players)
	 * 
	 * @throws InterruptedException
	 */
	protected abstract void cowardBehaviour() throws InterruptedException;

	/**
	 * Performs 1 tick's worth of Aggressive behaviour
	 * (runs towards all players)
	 * 
	 * @throws InterruptedException
	 */
	protected abstract void aggressiveBehaviour() throws InterruptedException;

	/**
	 * Cleans up the AI after it dies
	 */
	protected void funeral()
	{
		setAllMovementFalse();
		waypoints.clear();
		lastWaypoint = resources.getMap().randPointOnMap(); // safer than null

		// XXX debug
		if (debug)
			resources.setProjectedPos(null);

	}

	// BELOW ARE A LOAD OF HELPER FUNCTIONS

	/**
	 * Seek out the nearest player and dash once we are near them
	 * If they have moved since we planned our route, recalculate
	 * 
	 * @throws InterruptedException
	 */
	protected void defaultAggressiveBehaviour() throws InterruptedException
	{
		// if we don't have a target to hunt
		if (waypoints.isEmpty())
		{
			Character nearestPlayer;
			try
			{
				nearestPlayer = scanForNearestPlayer();
			}
			catch (NullPointerException e)
			{
				// no other players, so probably switch behaviour
				setBehaviour(Behaviour.ROVING);
				return;
			}

			currentTarget = nearestPlayer;
			currentGoal = getTargetLocation(nearestPlayer);

			// pathfind to them
			Point charPos = getCurrentTileCoords();
			Point newDest = getTargetLocation(nearestPlayer);
			Point newDestTile = getTileCoords(newDest);
			if (newDestTile != null && charPos != null)
			{
				waypoints = convertWaypoints(aStar.search(charPos, newDestTile));
				resources.setDestList(waypoints);
				resources.setAINextDest(newDest);
			}
			else
			{
				// player has died in the time since we found them
				setBehaviour(Behaviour.ROVING);
//				return;
			}

		}
		else
		{
			try
			{
				// dash when we are close to the target
				if (StaticHeuristics.euclidean(getOurLocation(), getTargetLocation(currentTarget)) < 60) // XXX 60 is experimental threshold
				{
					character.setDashing(true);
				}
				// if the player has moved considerably since we targeted them
				else if (StaticHeuristics.euclidean(currentGoal, getTargetLocation(currentTarget)) > 70) // XXX 70 is experimental threshold
				{
					// force recalculation next tick by clearing our waypoints
					waypoints.clear();
				}
			}
			catch (NullPointerException e)
			{
				// this may happen the first time
				// it's fine
			}
		}
	}

	/**
	 * @return The type of tile the AI is standing on
	 */
	protected Tile getCurrentTile()
	{
		return resources.getMap().tileAt(character.getX(), character.getY());
	}

	/**
	 * Is a tile a (walkable) edge tile?
	 * 
	 * @param tile
	 * @return true or false
	 */
	protected boolean isEdge(Tile tile)
	{
		return !non_edge.contains(tile);
	}

	/**
	 * TODO this will need to be adapted for teams
	 * 
	 * @return The closest player that is not us
	 * @throws NullPointerException if there are no players
	 */
	protected Character scanForNearestPlayer() throws NullPointerException
	{
		Character nearestPlayer = null;
		double SLD_to_nearestPlayer = Double.MAX_VALUE;
		for (Character player : resources.getPlayerList())
		{
			String playerID = player.getId();
			// don't hunt ourselves
			if (!playerID.equals(id))
			{
				Point playerLoc = getTargetLocation(player);
				Point ourLoc = getOurLocation();
				double distanceToPlayer = StaticHeuristics.euclidean(ourLoc, playerLoc);
				if (distanceToPlayer < SLD_to_nearestPlayer)
				{
					nearestPlayer = player;
					SLD_to_nearestPlayer = distanceToPlayer;
				}

			}
		}

		if (nearestPlayer == null)
		{
			throw new NullPointerException("No other players around :(");
		}

		return nearestPlayer;
	}

	/**
	 * @return the distance to the nearest other player
	 */
	protected double distToNearestPlayer()
	{
		Character nearestPlayer = scanForNearestPlayer();
		return StaticHeuristics.euclidean(getOurLocation(), getTargetLocation(nearestPlayer));
	}

	/**
	 * Move to the next waypoint in our list
	 * 
	 * @throws InterruptedException
	 */
	protected void moveToWaypoint() throws InterruptedException
	{
		//if (debug) resources.setAINextDest(waypoints.peek()); // XXX debug

		success = moveTo(waypoints.peek());
		if (success)
		{
			success = false;
			if (waypoints.size() > 0)
			{
				//System.out.println("made it to a waypoint!");
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
	protected Vector normalToNextWaypoint()
	{
		if (waypoints.size() > 0)
		{
			// work out vector from lastWaypoint to p
			Point a = lastWaypoint;
			Point b = waypoints.peek();
			Vector ab_vec = new Vector(a, b);
			// work out the normal to that (pointing right if original is up)
			Vector ab_norm = ab_vec.normal(b);

			//XXX debug
			if (debug)
			{
				//System.out.println(ab_norm);
				Point onNormal1 = new Point((int) (ab_norm.getCentre().getX() + 100 * ab_norm.getX()),
						(int) (ab_norm.getCentre().getY() + 100 * ab_norm.getY()));
				Point onNormal2 = new Point((int) (ab_norm.getCentre().getX() - 100 * ab_norm.getX()),
						(int) (ab_norm.getCentre().getY() - 100 * ab_norm.getY()));
				Line normal = new Line(onNormal1, onNormal2);
				resources.setNormal(normal);
			}

			return ab_norm;
		}
		else
		{
			return null; // TODO seems iffy
		}

	}

	/**
	 * @return the AI's location in character coords (not tiles)
	 */
	protected Point getOurLocation()
	{
		return new Point((int) character.getX(), (int) character.getY());
	}

	/**
	 * @param c character target
	 * @return the location of the target character
	 */
	protected Point getTargetLocation(Character c)
	{
		return new Point((int) c.getX(), (int) c.getY());
	}

	/**
	 * @return the tile index of the AI's current location
	 */
	protected Point getCurrentTileCoords()
	{
		return resources.getMap().tileCoords(character.getX(), character.getY());
	}

	/**
	 * @param p a point in the character coord system
	 * @return the tile index of a point p
	 */
	protected Point getTileCoords(Point p)
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
		int x = (int) (character.getX() + PRESCIENCE * character.getDx());
		int y = (int) (character.getY() + PRESCIENCE * character.getDy());

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
	protected Tile projectedTile()
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
	protected boolean isWalkable(Tile tile)
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
	protected LinkedList<Point> convertWaypoints(LinkedList<Point> waypoints)
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

	protected void moveAwayFromEdge() throws InterruptedException
	{
		Point currentTileIndex = getCurrentTileCoords();
		//setAllMovementFalse();
		if (currentTileIndex != null)
		{
			int column = (int) currentTileIndex.getX();
			int row = (int) currentTileIndex.getY();
			// get the surrounding tiles 
			// n.b. These values weren't the expected ones (tile_down should be row+1 by my reckoning) 	
			// They must not use the same +&- conventions as dy and dx 	
			// Or I'm being silly 		
			// But either way, this works: 		
			int tilesAway = 1;

			Tile tile_down = resources.getMap().tileAt(column - tilesAway, row);
			Tile tile_up = resources.getMap().tileAt(column + tilesAway, row);
			Tile tile_right = resources.getMap().tileAt(column, row - tilesAway);
			Tile tile_left = resources.getMap().tileAt(column, row + tilesAway);
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
	}

	/**
	 * If we have overshot the waypoint then we can go onto the next one
	 * 
	 * @return true if overshoot detected (and dealt with)
	 */
	protected boolean detectOvershoot()
	{
		// get where we are
		Point curLoc = getOurLocation();

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
					//System.out.println("Overshot! Skipping ahead");
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
	protected boolean moveTo(Point p) throws InterruptedException
	{
		if (detectOvershoot())
			return false;

		if (fuzzyEqual(character.getX(), p.x) && fuzzyEqual(character.getY(), p.y))
		{
			brakeChar();
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
	protected boolean fuzzyEqual(double coord1, double coord2)
	{
		return (Math.abs(coord1 - coord2) <= FUZZINESS);
	}

	/**
	 * 'Detach' all keys
	 */
	protected void setAllMovementFalse()
	{
		character.setUp(false);
		character.setDown(false);
		character.setLeft(false);
		character.setRight(false);
	}

	/**
	 * Given how fast we are going, for how long should we brake?
	 * This is a very experimental equation and should be tinkered with
	 * 
	 * @param velocity component of dx and dy
	 * @return the number of milliseconds to spend braking
	 */
	protected long brakingTime(double velocity)
	{
		long bt = (long) (BRAKING_CONSTANT * velocity);
		//		System.out.println(bt);
		return bt;
	}

	/**
	 * Try and slow the character down
	 * 
	 * @throws InterruptedException
	 */
	protected void brakeChar() throws InterruptedException
	{
		// 'release' all keys
		setAllMovementFalse();

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
	}

	/**
	 * XXX debugging only
	 * Sets what type of AI to be
	 * 
	 * @param behaviour
	 */
	public void setBehaviour(String behaviour)
	{
		switch (behaviour.toLowerCase().trim())
		{
			case ("aggressive"):
				setBehaviour(Behaviour.AGGRESSIVE);
				break;
			case ("coward"):
				setBehaviour(Behaviour.COWARD);
				break;
			case ("poirot"):
				setBehaviour(Behaviour.POIROT);
				break;
			case ("potato"):
				setBehaviour(Behaviour.POTATO);
				break;
			case ("stubborn"):
				setBehaviour(Behaviour.STUBBORN);
				break;
			case ("roving"):
				setBehaviour(Behaviour.ROVING);
				break;
			default:
				setBehaviour(Behaviour.ROVING);
				break;
		}

	}

	/**
	 * 
	 * @param behaviour
	 */
	protected void setBehaviour(Behaviour behaviour)
	{
		this.behaviour = behaviour;
	}

	/**
	 * XXX debugging only
	 * Set the waypoints to a given list
	 * 
	 * @param wp
	 */
	public void setDestinations(Point[] destinations)
	{
		this.destinations = destinations;
	}

}