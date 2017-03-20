package ai;

import java.awt.Point;

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
				// XXX this is a curious return statement as in general the game works
				// better without it, except for the small issue of its absence sometimes
				// causing the AI to sit and pathfind forever
				// Probably best to leave it in...
				return;
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
	 * Just go back to roving
	 */
	@Override
	protected void cowardBehaviour() throws InterruptedException
	{
		setBehaviour(Behaviour.ROVING);
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
