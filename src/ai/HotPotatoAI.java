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

	}

	@Override
	protected void cowardBehaviour() throws InterruptedException
	{
		// TODO Auto-generated method stub
		if (!character.hasBomb())
		{
			if (waypoints.isEmpty())
			{
				// try and find the player who has the bomb
				Character bombHolder = scanForBombPlayer();
				// move away from them
				// TODO
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
