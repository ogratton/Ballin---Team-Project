package ai.pathfinding;

import java.awt.Point;
import java.io.IOException;

import javax.swing.SwingUtilities;

import ai.VeryBasicAI;
import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Map;
import resources.MapReader;
import resources.Resources;

/**
 * Launches a game window with just one AI and debug mode on
 * 
 * @author Oliver Gratton
 *
 */
public class PathFindingTest
{
	private static Point startTile = new Point(12, 28);
	//	private static Point[] destinations = new Point[] { new Point(12, 28), new Point(8, 32), new Point(16, 38), new Point(20, 20) };
	private static Point[] destinations = new Point[] { new Point(6, 20), new Point(10, 14), new Point(9, 4), new Point(20, 44) }; // TODO to test moveAwayFrom Edge
//	private static Point[] destinations = new Point[] { new Point(6, 20), new Point(10, 20), new Point(9, 20), new Point(20, 20) }; // TODO to test smoothing

	public static void main(String[] args)
	{

		/* SETTING UP THE MAP AND RESOURCES */

		Resources resources = new Resources();
		// make the map the default just in case the following fails
		Map.Tile[][] tiles = null;
		MapReader mr = new MapReader(resources);
		try
		{
			tiles = mr.readMap("./resources/maps/map0.csv");
			System.out.println("Map Loaded");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();

		}
		resources.setMap(new Map(1200, 675, tiles, Map.World.LAVA, "Map1"));

		/* SETTING UP THE AI PLAYER */

		Point startCoords = new Point(resources.getMap().tileCoordsToMapCoords(startTile.x, startTile.y));
		Character player = new Character(Character.Class.WIZARD, 0);
		player.setX(startCoords.getX());
		player.setY(startCoords.getY());
		player.setPlayerNumber(0);
		resources.addPlayerToList(player);

		VeryBasicAI ai = new VeryBasicAI(resources, player);
		ai.setBehaviour("poirot"); // so we can feed it our own waypoints
		ai.setDestinations(destinations);

		/* STARTING THINGS UP */
		
		Physics p = new Physics(resources, true);
		p.start();
		ai.start();

		SwingUtilities.invokeLater(new Graphics(resources, null, true));

	}
}
