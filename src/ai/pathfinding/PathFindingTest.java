package ai.pathfinding;

import java.awt.Point;

import ai.FightingAI;
import ai.HotPotatoAI;
import gamemodes.DebugMode;
import gamemodes.HotPotato;
import resources.Character;
import resources.Map;
import resources.Resources;

/**
 * Launches a game window with just one AI and debug mode on
 * (Very messy code, please don't look too closely)
 * 
 * @author Oliver Gratton
 *
 */
public class PathFindingTest
{
	private static Point startTile = new Point(12, 25);
	//	private static Point[] destinations = new Point[] { new Point(12, 28), new Point(8, 32), new Point(16, 38), new Point(20, 20) };
	private static Point[] destinations = new Point[] { new Point(6, 20), new Point(10, 14), new Point(9, 4), new Point(20, 43) }; // TODO to test moveAwayFrom Edge
	//	private static Point[] destinations = new Point[] { new Point(6, 20), new Point(10, 20), new Point(9, 20), new Point(20, 20) }; // TODO to test smoothing

	private static boolean followSetPoints = false;
	private static boolean hotPotato = true;

	private static void testPoirot(Character player, FightingAI ai)
	{
		ai.setBehaviour("poirot"); // so we can feed it our own waypoints
		ai.setDestinations(destinations);
		player.setAI(ai);
	}

	private static void testOther(Character player, FightingAI ai, String behaviour)
	{
		ai.setBehaviour(behaviour);
		player.setAI(ai);
	}

	public static void main(String[] args)
	{

		/* SETTING UP THE MAP AND RESOURCES */

		Resources resources = new Resources();

		String mapName = followSetPoints ? "map0" : "asteroid";
		mapName = hotPotato ? "potato" : mapName;

		resources.setMap(new Map(1200, 650, Map.World.CAVE, mapName));
		new MapCosts(resources);

		/* SETTING UP THE PLAYERS */

		Point startCoords = new Point(resources.getMap().tileCoordsToMapCoords(startTile.x, startTile.y));

		Character controlled = new Character(Character.Class.HORSE, 1);
		controlled.setX(400);
		controlled.setY(400);
		resources.addPlayerToList(controlled);

		Character player = new Character(Character.Class.WIZARD, 0);
		player.setX(startCoords.getX());
		player.setY(startCoords.getY());
		player.setPlayerNumber(0); // so debug stuff is drawn
		resources.addPlayerToList(player);

		if (!hotPotato)
		{
			FightingAI ai = new FightingAI(resources, player);
			if (followSetPoints)
			{
				testPoirot(player, ai);
			}
			else
			{
				testOther(player, ai, "coward");
			}

			/* STARTING THINGS UP */

			DebugMode mode = new DebugMode(resources);
			mode.start();

			ai.start();
		}
		else
		{
			HotPotatoAI ai = new HotPotatoAI(resources, player);
			HotPotato mode = new HotPotato(resources);
			mode.start();
			ai.start();
		}

	}
}
