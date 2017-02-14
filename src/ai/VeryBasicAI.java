package ai;

import java.awt.Point;
import java.util.ArrayList;

import resources.Character;
import resources.Map.Tile;
import resources.Resources;

public class VeryBasicAI extends Thread
{

	private Character character;
	private Resources resources;

	//	private int raycast_length = 10;
	private final double fuzziness = 20;

	private ArrayList<Tile> bad_tiles;

	/*
	 * Notes:
	 * 
	 * TODO AI should ray-cast ahead of it and scan that area for danger
	 * For now change direction randomly to avoid danger
	 * (This is Coward behaviour)
	 * 
	 * TODO If it is about to fall off (i.e. on/heading for danger tile) it
	 * should try and move
	 * perpendicularly away from the danger
	 * 
	 * TODO Pathfinding (don't run into holes)
	 * 
	 * TODO Behaviours (Coward, Bolshy, Gallivant, etc.)
	 * 
	 * TODO Stopping (should be a case of 'tapping' in the opposite direction)
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
		bad_tiles = new ArrayList<Tile>();
		bad_tiles.add(Tile.ABYSS);
		bad_tiles.add(Tile.EDGE_ABYSS);
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

			boolean success = false;
			Point[] xys = new Point[] { new Point(700, 300), new Point(800, 200), new Point(950, 400), new Point(500, 500) };
			int i = 0;

			// The newborn AI stops to ponder life, and give me time to bring up the window and pay attention
			Thread.sleep(1000);

			System.out.println("Target: " + xys[i]);

			while (i < xys.length && !character.isDead())
			{
				Thread.sleep(10);
				success = moveTo(xys[i].getX(), xys[i].getY());
				if (success)
				{
					System.out.println("Checkpoint reached! " + character.getX() + ", " + character.getY());
					i++;
					success = false; // not strictly necessary as it will reset next loop anyway, but hey-ho
					if (i < xys.length)
						System.out.println("Target: " + xys[i]);
				}
			}

			String message = character.isDead() ? "Dead X(" : "Finished!";
			System.out.println(message);
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

	/**
	 * @return The type of tile that we are heading for
	 */
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
	private boolean isWalkable(double x, double y)
	{
		Tile subject = resources.getMap().tileAt(x, y);
		return bad_tiles.contains(subject);
	}

	// are there players in the way?
	//private boolean isClear

	/**
	 * Dumbly move as-the-(drunken-)crow-flies to coords
	 * 
	 * @param x coord
	 * @param y coord
	 * @return are we nearly there yet?
	 */
	private boolean moveTo(double x, double y)
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
	 * @return
	 */
	private boolean fuzzyEqual(double coord1, double coord2)
	{
		return (Math.abs(coord1 - coord2) <= fuzziness);
	}

	/**
	 * Try and slow the character down
	 */
	private void brakeChar()
	{
		// TODO move backwards slightly to simulate braking
		character.setUp(false);
		character.setDown(false);
		character.setLeft(false);
		character.setRight(false);

	}

}
