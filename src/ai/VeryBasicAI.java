package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import ai.pathfinding.AStarSearch;
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

		CLOUSEAU, // debug: Follows a list of points dumbly
		POIROT, // use A* search to follow a list of points more intelligently
		AIMLESS, // debug: moves randomly
		PACING, // debug: moves up&down/left&right
		STUBBORN // debug: tries to stop itself moving anywhere
	};

	private Behaviour behaviour = Behaviour.POIROT; // default

	//	private int raycast_length = 10;
	private final double fuzziness = 30;
	private final long reaction_time = 5; // can be increase once ray-casting is implemented

	private final long tick = 70; // loop every <tick>ms

	private ArrayList<Tile> bad_tiles;
	private ArrayList<Tile> non_edge; // all tiles that are not WALKABLE edge tiles (not EDGE_ABYSS)

	private Random r;
	private int tempID;

	private AStarSearch aStar;

	/*
	 * Notes:
	 * 
	 * TODO AI should ray-cast ahead of it and scan that area for danger
	 * For now change direction randomly to avoid danger
	 * (This is Coward behaviour)
	 * 
	 * TODO For moveAwayFromEdge use the ray-cast as the centre, not the player
	 * centre
	 * This will make it look more human, hopefully
	 * 
	 * TODO Test pathfinding
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

		non_edge = new ArrayList<Tile>();
		non_edge.addAll(bad_tiles);
		non_edge.add(Tile.FLAT);

		r = new Random();
		
		tempID = r.nextInt(500);

		aStar = new AStarSearch(resources);
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

			// this is setting things up for the debug Detective
			boolean success = false; // we all start off life as failures
			Point[] destinations = new Point[] { new Point(700, 300), new Point(800, 200), new Point(950, 400), new Point(500, 500) }; // { new Point(500, 500)};
			int i = 0;

			Point charStartPos = new Point((int) character.getX(), (int) character.getY());
//			System.out.println("Started search from " + charStartPos + " to " + destinations[i]);
			LinkedList<Point> waypoints = aStar.search(charStartPos, destinations[i]);
			//System.out.println("Worked out waypoints to "+ destinations[i]);
			//System.out.println(waypoints);

			while (!character.isDead())
			{
				// common behaviour should go first

				// TODO this should be the predicted future tile, not the one we are actually on
				// otherwise it's OP
				while (isEdge(getCurrentTile()))
				{
					Thread.sleep(reaction_time);
					moveAwayFromEdge(getCurrentTileCoords());
					//					System.out.println("Near an edge!");
				}
				if (behaviour == Behaviour.CLOUSEAU)
				{
					/*
					 * CLOUSEAU BEHAVIOUR:
					 * Uses 'as-the-crow-flies' pathfinding, causing it to
					 * probably fall into holes
					 */

					//System.out.println("Target: " + destinations[i]);

					if (i < destinations.length)
					{
						Thread.sleep(10);
						success = moveTo(destinations[i].getX(), destinations[i].getY());
						if (success)
						{
							//System.out.println("Checkpoint reached! " + character.getX() + ", " + character.getY());
							//System.out.println(resources.getMap().tileCoords(character.getX(), character.getY()));
							i++;
							success = false; // not strictly necessary as it will reset next loop anyway, but hey-ho
							if (i < destinations.length)
							{
								//System.out.println("Target: " + destinations[i]);
							}
						}
					}
				}
				else if (behaviour == Behaviour.POIROT)
				{
					/*
					 * POIROT BEHAVIOUR:
					 * Uses A* pathfinding, so hopefully isn't a lemming
					 */
					//System.out.println("Poirot tick. Waypoints length: " + waypoints.size());

					if (i < destinations.length)
					{
						if (!waypoints.isEmpty())
						{
							success = moveTo(waypoints.removeFirst());
							if (success)
							{
								success = false;
								//System.out.println("Ze little grey cells, zey have led me to my goal!");

							}
						}
						else
						{
							System.out.println(tempID + " made it to destination " + i);
							brakeChar();
							i++;
							try {
								waypoints = aStar.search(charStartPos, destinations[i]);
								System.out.println(tempID + " pathfinding to point " + i);
							} catch (ArrayIndexOutOfBoundsException e) {
								i = 0; // loop
							}
						}

					}
				}
				else if (behaviour == Behaviour.STUBBORN)
				{
					brakeChar();
				}
				else if (behaviour == Behaviour.AIMLESS)
				{
					// Move in random directions
					// But preferably not off the edge

					// TODO
				}
				else
				{
					//System.out.println("Behaviour not yet implemented");
				}
				Thread.sleep(tick);
			}

			setAllMovementFalse();

		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
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

	/**
	 * @return The type of tile that we are heading for
	 */
	@SuppressWarnings("unused")
	private Tile getFacingTile()
	{
		// TODO I need to predict the location of the AI in x amount of time/distance

		return Tile.FLAT;
	}

	/**
	 * Can we walk/roll on the tile at these coords?
	 * 
	 * @param x
	 * @param y
	 * @return true or false
	 */
	@SuppressWarnings("unused")
	private boolean isWalkable(double x, double y)
	{
		Tile subject = resources.getMap().tileAt(x, y);
		return bad_tiles.contains(subject);
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
	 * Move perpendicularly away from the imminent abyss
	 * TODO how would we cope if were surrounded on two/four sides?
	 * - shouldn't happen, hopefully
	 * TODO do we care about diagonals?
	 * 
	 * @param currentTileIndex the index of the current tile
	 */
	private void moveAwayFromEdge(Point currentTileIndex) throws InterruptedException
	{

		//		setAllMovementFalse();

		// TODO I don't get why I have to cast these, but maybe it's a Point thing
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

	// TODO are there players in the way?
	//private boolean isClear

	private boolean moveTo(Point p) throws InterruptedException
	{
		return this.moveTo(p.getX(), p.getY());
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
	private boolean moveTo(double x, double y) throws InterruptedException
	{
		if (fuzzyEqual(character.getX(), x) && fuzzyEqual(character.getY(), y))
		{
			brakeChar();
			return true;
		}
		if (fuzzyEqual(character.getX(), x))
		{
			brakeChar();
		}
		if (fuzzyEqual(character.getY(), y))
		{
			brakeChar();
		}
		if (character.getX() < x)
		{
			character.setLeft(false);
			character.setRight(true);
		}
		if (character.getY() < y)
		{
			character.setUp(false);
			character.setDown(true);
		}
		if (character.getX() > x)
		{
			character.setRight(false);
			character.setLeft(true);
		}
		if (character.getY() > y)
		{
			character.setDown(false);
			character.setUp(true);
		}

		return false;
	}

	/**
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
