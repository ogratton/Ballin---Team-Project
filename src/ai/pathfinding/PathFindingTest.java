package ai.pathfinding;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeSet;

import resources.Map;
import resources.MapReader;
import resources.Resources;

public class PathFindingTest
{
	public static void main(String[] args)
	{
		Resources resources = new Resources();
		
		// make the map the default just in case the following fails
		Map.Tile[][] tiles = null;	
		MapReader mr = new MapReader();	
		try
		{
			tiles = mr.readMap("./resources/maps/map1.csv");
			System.out.println("Map Loaded");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
			
		}
		
		resources.setMap(new Map(1200, 675, tiles, Map.World.CAVE));
		
		
		// TEST SEARCH
		AStarSearch aStar = new AStarSearch(resources);	
		Point start = new Point(600,300); // 450, 500
		Point goal = new Point(450,510); // 800, 450
		
//		System.out.println(resources.getMap().tileAt(start.getX(), start.getY()));
		
		LinkedList<Point> ll = aStar.search(start, goal);
		System.out.println(ll);
		
		
	}
}
