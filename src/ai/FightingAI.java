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
			else
			{
				if (debug)
					System.out.println(distToNearestPlayer + " considered too far");
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

	// (Javadoc in superclass)
	@Override
	protected void cowardBehaviour() throws InterruptedException
	{
		// this ai is no coward!
		// (never used)

	}

	// (Javadoc in superclass)
	@Override
	protected void aggressiveBehaviour() throws InterruptedException
	{
		// 	if we don't have a target to hunt
		if (waypoints.isEmpty())
		{
			if (debug)
				System.out.println("Searching for nearest player...");
			Character nearestPlayer;
			try
			{
				nearestPlayer = scanForNearestPlayer();
			}
			catch (NullPointerException e)
			{
				// no other players, so probably switch behaviour
				if (debug)
					System.out.println("Nobody around, switching to Roving");
				setBehaviour(Behaviour.ROVING);
				return;
			}

			currentTarget = nearestPlayer;
			currentGoal = getTargetLocation(nearestPlayer);

			// pathfind to them
			Point charPos = getCurrentTileCoords();
			Point newDest = getTargetLocation(nearestPlayer);
			Point newDestTile = getTileCoords(newDest);
			if (debug)
				System.out.println("Now hunting player " + nearestPlayer.getPlayerNumber());
			if (newDestTile != null)
			{
				waypoints = convertWaypoints(aStar.search(charPos, newDestTile));
				resources.setDestList(waypoints);
				resources.setAINextDest(newDest);
			}
			else
			{
				// player has died in the time since we found them
				if (debug)
					System.out.println("target lost, switching to roving");
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

}
