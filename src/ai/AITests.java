package ai;

import static org.junit.Assert.assertEquals;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import ai.AITemplate.Behaviour;
import gamemodes.DebugMode;
import resources.Character;
import resources.Map;
import resources.Map.Tile;
import resources.MapCosts;
import resources.Resources;

/**
 * It is very difficult to test something that is based on randomness and visuals
 * Hence these tests may sometimes fail
 * @author Oliver Gratton
 *
 */
public class AITests
{

	// TODO make wrappers for the private methods that need testing

	/*
	 * BasicAI:
	 * 
	 * funeral()
	 * scanForNearestPlayer
	 * ?? moveToNextWaypoint()
	 * normalToNextWaypoint()
	 * getOurLocation()
	 * getCurrentTileCoords()
	 * getTileCoords()
	 * ?? projectedPosition()
	 * isWalkable()
	 * convertWaypoints()
	 * ?? moveTo()
	 * ?? fuzzyEqual()
	 * setBehaviour()
	 * setDestinations()
	 */

	/*
	 * AStarSearch
	 * 
	 * addNeighbours()
	 * sparsifyPath()
	 * smoothPath()
	 */

	private Resources resources;
	private Character cpu;
	private Character controlled;
	private static Point[] destinations = new Point[] { new Point(12, 28), new Point(8, 32), new Point(16, 38), new Point(20, 20) };

	@Before
	public void setUp() throws Exception
	{
		resources = new Resources();
		resources.setMap(new Map(1200, 650, Map.World.SPACE, "asteroid"));
		new MapCosts(resources);
		
		// user-controlled character
		controlled = new Character(Character.Class.HORSE, 1);
		controlled.setX(400);
		controlled.setY(400);
		resources.addPlayerToList(controlled);
		
		// cpu
		cpu = new Character(Character.Class.WIZARD, 0);
		cpu.setX(500);
		cpu.setY(520);
		cpu.setPlayerNumber(0); // so debug stuff is drawn
		resources.addPlayerToList(cpu);

	}

	/**
	 * Uses the FightingAI to test AITemplate functions
	 */
	@Test
	public void AITemplateTest()
	{
		FightingAI ai = new FightingAI(resources, cpu);
		cpu.setAI(ai);
		
		DebugMode mode = new DebugMode(resources);
		mode.start();

		ai.start();
		
		assertEquals(Tile.FLAT, ai.getCurrentTile());
		assertEquals(new Point(20,20), ai.getCurrentTileCoords());
//		System.out.println(ai.distToNearestPlayer());
		
		// wait a bit
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		assertEquals(ai.currentTarget, controlled);
//		assertEquals(ai.scanForNearestPlayer(), controlled);
		
		ai.setBehaviour(Behaviour.POIROT);
		ai.setDestinations(destinations);
		
//		int i = 0;
//		
//		while(i < destinations.length)
//		{
//			Point aiCurr = ai.currentGoal;
//			
//			System.out.println(resources.getMap().tileCoords(aiCurr.x, aiCurr.y));
//			System.out.println(destinations[i]);
//			
//			while(!ai.getCurrentTileCoords().equals(destinations[i]))
//			{
//				try
//				{
//					Thread.sleep(10);
//				}
//				catch (InterruptedException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			i++;
////			assertTrue(ai.getCurrentTileCoords().equals(destinations[i]));
//		}
		
		
		

	
		
	}
}