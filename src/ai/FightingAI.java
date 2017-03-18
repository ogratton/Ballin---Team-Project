package ai;

import java.awt.Point;

import ai.pathfinding.StaticHeuristics;
import resources.Character;
import resources.Resources;

/**
 * Flavour of AI that chiefly runs straight at its target
 * To be used in Deathmatch and LastManStanding
 * 
 * @author Oliver Gratton
 *
 */
public class FightingAI extends AITemplate
{

	public FightingAI(Resources resources, Character character)
	{
		super(resources, character);
	}

	// (Javadoc in superclass)
	@Override
	protected void commonBehaviour() throws InterruptedException
	{
		//if (projectedTile() != Tile.FLAT)
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

	// (Javadoc in superclass)
	@Override
	protected void rovingBehaviour() throws InterruptedException
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

			Point charPos = getCurrentTileCoords();
			Point newDest = resources.getMap().randPointOnMap();
			Point newDestTile = getTileCoords(newDest);

			currentGoal = newDest;
			while (waypoints.isEmpty())
			{
				// keep trying to get a new dest until we get a valid path
				// just in case point given is dodgy
				if (charPos != null && newDestTile != null)
				{
					waypoints = convertWaypoints(aStar.search(charPos, newDestTile));
				}
			}

			if (debug)
			{
				resources.setDestList(waypoints);
				resources.setAINextDest(newDest);
			}

		}
	}

	/**
	 * This AI has no need for this behaviour
	 * Left blank
	 */
	@Override
	protected void cowardBehaviour() throws InterruptedException
	{
	}

	/**
	 * Just use default method
	 */
	@Override
	protected void aggressiveBehaviour() throws InterruptedException
	{
		defaultAggressiveBehaviour();
	}
}
