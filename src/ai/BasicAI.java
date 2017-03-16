package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import ai.pathfinding.AStarSearch;
import ai.pathfinding.Line;
import ai.pathfinding.StaticHeuristics;
import ai.pathfinding.Vector;
import resources.Character;
import resources.Map.Tile;
import resources.Resources;

/**
 * An AI that takes control of a Character
 * and dynamically switches between a series of behaviours
 * 
 * @author Oliver Gratton
 *
 */
public class BasicAI extends Thread
{
	private Character character;
	private Resources resources;

	private enum Behaviour
	{
		COWARD, // runs from all danger
		AGGRESSIVE, // actively seeks other players

		POIROT, // use A* search to follow a list of points more intelligently
		ROVING, // debug: moves randomly
		STUBBORN, // debug: tries to stop itself moving anywhere
		POTATO // debug: does literally nothing other than common behaviour
	};

	private Behaviour behaviour = Behaviour.AGGRESSIVE; // default

	//	private int raycast_length = 10;
	private static final double BRAKING_CONSTANT = 40; // how many ms to brake for. 40-50 seems good
	private static final double FUZZINESS = 20;
	//	private final long reaction_time = 5; // can be increased once ray-casting is implemented
	private static final long TICK = 20; // loop every <tick>ms
	private static long PRESCIENCE = TICK * 1; // how many ms ahead we look for our predicted point

	private ArrayList<Tile> bad_tiles;

	private String id;

	private AStarSearch aStar;

	private LinkedList<Point> waypoints;

	private Point lastWaypoint; // the last waypoint we followed
	private Vector normalToNextWaypoint;

	private boolean success = true; // we start off a winner (because we need to be motivated to look for new goals) 
	
	private ArrayList<Tile> non_edge; // all tiles that are not WALKABLE edge tiles (not EDGE_ABYSS)
	
	private Character currentTarget; // for aggressive mode
	private Point currentGoal;

	// XXX debug stuff
	// this is setting things up for the debug Detective
	Point[] destinations = new Point[] { new Point(12, 28), new Point(8, 32), new Point(16, 38), new Point(20, 20) };
	int destI = 0; // destination index
	private boolean debug;

	/*
	 * Notes:
	 * 
	 * TODO AI should ray-cast ahead of it and scan that area for danger
	 * For now change direction randomly to avoid danger
	 * (This is Coward behaviour)
	 * 
	 * TODO moveAwayFromEdge is still a bit dodgy
	 * 
	 * TODO Coward and Aggressive Behaviours (i.e. interact with other players)
	 * 
	 * TODO dynamically switch between behaviours
	 * 
	 * TODO Smoothing algorithm for waypoints
	 * 
	 * Look at this
	 * https://www.javacodegeeks.com/2014/08/game-ai-an-introduction-to-
	 * behaviour-trees.html
	 * 
	 */

	public BasicAI(Resources resources, Character character)
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

			while (!character.isDead()) // TODO change to true
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
	private void commonBehaviour() throws InterruptedException
	{
		//		if (projectedTile() != Tile.FLAT)
		if (isEdge(getCurrentTile()))
		{
			//Thread.sleep(reaction_time);
			moveAwayFromEdge();
			//System.out.println("Near an edge!");
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
			if (debug)
			{
				System.out.println();
				System.out.println("made it to destination " + destinations[destI]);
			}

			destI++;
			if (destI < destinations.length)
			{
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
			else
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
			
			// TODO make behaviour change switches here for efficiency
			
			// if there is a player near, seek them out instead
			// XXX hard-coded experimental value used!
			double distToNearestPlayer = distToNearestPlayer();
			if (distToNearestPlayer < 1000)
			{
				setBehaviour(Behaviour.AGGRESSIVE);
			}
			else
			{
				if (debug) System.out.println(distToNearestPlayer + " considered too far");
			}
			

			Point charPos = getCurrentTileCoords();
			Point newDest = resources.getMap().randPointOnMap();
			Point newDestTile = getTileCoords(newDest);
			
			currentGoal = newDest;
			while (waypoints.isEmpty())
			{
				// keep trying to get a new dest until we get a valid path
				// just in case point given is dodgy
				waypoints = convertWaypoints(aStar.search(charPos, newDestTile));
			}

			if (debug)
			{
				resources.setDestList(waypoints);
				resources.setAINextDest(newDest);
			}

		}
	}

	/**
	 * TODO
	 * Performs 1 tick's worth of Coward behaviour
	 * (runs away from all players)
	 * 
	 * @throws InterruptedException
	 */
	private void cowardBehaviour() throws InterruptedException
	{
		// TODO
	}

	/**
	 * TODO
	 * Performs 1 tick's worth of Aggressive behaviour
	 * (runs towards all players)
	 * 
	 * @throws InterruptedException
	 */
	private void aggressiveBehaviour() throws InterruptedException
	{
		// TODO AI doesn't know what to do when it reaches the player so keeps pathfinding to them
		// causing lag. Try dashing when they are close but I have a feeling it won't be good

		// 	if we don't have a target to hunt
		if (waypoints.isEmpty())
		{
			if (debug) System.out.println("Searching for nearest player...");
			Character nearestPlayer;
			try
			{
				nearestPlayer = scanForNearestPlayer();
			}
			catch (NullPointerException e)
			{
				// no other players, so probably switch behaviour
				if (debug) System.out.println("Nobody around, switching to Roving");
				setBehaviour(Behaviour.ROVING);
				return;
			}
			
			currentTarget = nearestPlayer;
			currentGoal = getTargetLocation(nearestPlayer);

			// pathfind to them
			Point charPos = getCurrentTileCoords();
			Point newDest = getTargetLocation(nearestPlayer);
			Point newDestTile = getTileCoords(newDest);
			if (debug) System.out.println("Now hunting player " + nearestPlayer.getPlayerNumber());
			if (newDestTile != null)
			{
				waypoints = convertWaypoints(aStar.search(charPos, newDestTile));
				resources.setDestList(waypoints);
				resources.setAINextDest(newDest);
			}
			else
			{
				// player has died in the time since we found them
				if (debug) System.out.println("target lost, switching to roving");
				setBehaviour(Behaviour.ROVING);
			}

		}
		else
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

	}

	/**
	 * Cleans up the AI after it dies
	 */
	private void funeral()
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
	 * @return The type of tile the AI is standing on 
	 */
	private Tile getCurrentTile()
	{
		return resources.getMap().tileAt(character.getX(), character.getY());
	}
	
	/**
	 * Is a tile a (walkable) edge tile?
	 * 
	 * @param tile
	 * @return true or false
	 */
	private boolean isEdge(Tile tile)
	{
		return !non_edge.contains(tile);
	}

	/**
	 * TODO this will need to be adapted for teams
	 * 
	 * @return The closest player that is not us
	 * @throws NullPointerException if there are no players
	 */
	private Character scanForNearestPlayer() throws NullPointerException
	{
		Character nearestPlayer = null;
		double SLD_to_nearestPlayer = Double.MAX_VALUE;
		for (Character player : resources.getPlayerList())
		{
			String playerID = player.getId();
			// don't hunt ourselves
			if (!id.equals(playerID))
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
	private double distToNearestPlayer()
	{
		Character nearestPlayer = scanForNearestPlayer();
		return StaticHeuristics.euclidean(getOurLocation(), getTargetLocation(nearestPlayer)); 
	}

	/**
	 * Move to the next waypoint in our list
	 * 
	 * @throws InterruptedException
	 */
	private void moveToWaypoint() throws InterruptedException
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
	private Vector normalToNextWaypoint()
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
	private Point getOurLocation()
	{
		return new Point((int) character.getX(), (int) character.getY());
	}
	
	/**
	 * @param c character target
	 * @return the location of the target character
	 */
	private Point getTargetLocation(Character c)
	{
		return new Point((int) c.getX(), (int) c.getY());
	}

	/**
	 * @return the tile index of the AI's current location
	 */
	private Point getCurrentTileCoords()
	{
		return resources.getMap().tileCoords(character.getX(), character.getY());
	}

	/**
	 * @param p a point in the character coord system
	 * @return the tile index of a point p
	 */
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
	private boolean detectOvershoot()
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
					//System.out.println("Overshot! Skipping ahead"); // TODO how come this is triggered more than a white girl on tumblr
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
	private boolean fuzzyEqual(double coord1, double coord2)
	{
		return (Math.abs(coord1 - coord2) <= FUZZINESS);
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
		long bt = (long) (BRAKING_CONSTANT * velocity);
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
	private void setBehaviour(Behaviour behaviour)
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