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
		// keep trying to get a new dest until we get a valid path
		// if AStar returns null we'll try again next tick anyway
		if (waypoints.isEmpty())
		{

			// TODO make behaviour change switches here for efficiency

			// if there is a player near, seek them out instead
			// XXX hard-coded experimental value used!
			double distToNearestPlayer = distToNearestPlayer();
			if (distToNearestPlayer < 1000)
			{
				setBehaviour(Behaviour.AGGRESSIVE);
				//return;
				// XXX Even though we should return here, it seems to work better if we don't
				// So I shall leave it
				// This is why in pathfinding test it does loads of random points after the player is killed
			}

			Point charPos = getCurrentTileCoords();
			Point newDest = resources.getMap().randPointOnMap();
			Point newDestTile = getTileCoords(newDest);
			currentGoal = newDest;
			
			if (charPos != null && newDestTile != null)
			{
				waypoints = convertWaypoints(aStar.search(charPos, newDestTile));
			}

			if (debug)
			{
				resources.setDestList(waypoints);
				resources.setAINextDest(currentGoal);
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
