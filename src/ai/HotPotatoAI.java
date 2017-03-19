package ai;

import java.awt.Point;

import ai.pathfinding.StaticHeuristics;
import resources.Character;
import resources.Resources;

/**
 * TODO
 * An AI to compete in the Hot Potato gamemode
 * They should run away from the player who has the potato
 * Or, if they have it, do standard aggressive mode from Fighting
 * 
 * @author Oliver Gratton
 *
 */
public class HotPotatoAI extends AITemplate
{

	public HotPotatoAI(Resources resources, Character character)
	{
		super(resources, character);
	}

	@Override
	protected void commonBehaviour() throws InterruptedException
	{
		// XXX don't need moveAwayFromEdge as there are always walls in HotPotato, right...?

		if (!waypoints.isEmpty())
		{
			// TODO if we are a certain distance away from the next waypoint, recalculate route
			// this may have to go in the behaviours

			moveToWaypoint();
		}

	}

	@Override
	protected void rovingBehaviour() throws InterruptedException
	{
		// TODO Auto-generated method stub
		// fairly similar to Fighting except for the behaviour changes
		
		if (character.hasBomb())
		{
			setBehaviour(Behaviour.AGGRESSIVE);
			return;
		}
		else
		{
			setBehaviour(Behaviour.COWARD);
			return;
		}

	}

	@Override
	protected void cowardBehaviour() throws InterruptedException
	{
		
		if (!character.hasBomb())
		{
			if (waypoints.isEmpty())
			{
				// try and find the player who has the bomb
				Character bombHolder = scanForBombPlayer();

				// work out our distance to them
				Point charPos = getOurLocation();
				Point charTile = getCurrentTileCoords();
				double distToBomb = StaticHeuristics.euclidean(charPos, getTargetLocation(bombHolder));
				
				// get randPointOnMap
				Point bestPoint = resources.getMap().randPointOnMap();
				double bestDist = StaticHeuristics.euclidean(getTargetLocation(bombHolder), bestPoint);
				
				// while that is closer to the bomb than we are, try again (but only finite times)
				// XXX arbitrary choice alert: numberOfTries
				int numberOfTries = 500;
				for (int i = 0; i < numberOfTries && (bestDist < distToBomb); i++)
				{
					Point samplePoint = resources.getMap().randPointOnMap();
					double sampleDist = StaticHeuristics.euclidean(getTargetLocation(bombHolder), samplePoint);
					if (sampleDist > distToBomb)
					{
						bestPoint = samplePoint;
						bestDist = sampleDist;
					}
				}

				// TODO check if we actually got a better point that current
				if (bestDist > distToBomb)
				{
//					System.out.println("found a better spot");
					// aStar.search to it
					Point newDestTile = getTileCoords(bestPoint);
					// check each of the waypoints is farther
					if (charTile != null && newDestTile != null)
					{
						waypoints = convertWaypoints(aStar.search(charTile, newDestTile));
					}
				}
				{
					System.out.println("no place like home");
				}
				// else we're safer not moving

			}
			else
			{
				// what to check when we have a list of waypoints to follow
				// in order to escape the bombHolder
			}

		}
		else
		{
			setBehaviour(Behaviour.AGGRESSIVE);
			return;
		}

	}

	/**
	 * essentially the same as default except we want to stop being
	 * aggressive if we no longer have the bomb
	 */
	@Override
	protected void aggressiveBehaviour() throws InterruptedException
	{
		if (character.hasBomb())
		{
			defaultAggressiveBehaviour();
		}
		else
		{
			setBehaviour(Behaviour.COWARD);
		}
	}

	/**
	 * Find the player who has the bomb
	 * 
	 * @return
	 * @throws NullPointerException
	 */
	protected Character scanForBombPlayer() throws NullPointerException
	{
		for (Character player : resources.getPlayerList())
		{
			if (player.hasBomb() && !player.getId().equals(id))
			{
				return player;
			}
		}

		// if we've got this far, either no one has the bomb, or we do
		throw new NullPointerException("No one else has the bomb");

	}

}
