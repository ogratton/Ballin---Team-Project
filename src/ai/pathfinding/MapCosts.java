package ai.pathfinding;

import java.util.Arrays;

import resources.Map.Tile;
import resources.Resources;

public class MapCosts
{
	/**
	 * Create a 2D array the same size as the map with the costs of moving to
	 * each tile
	 * An abyss tile obviously has the highest cost and the cost decreases
	 * exponentially as we move farther from the edge
	 * 
	 * @param resources we will add the array directly into resources
	 */
	public static void genMapCostsMask(Resources resources)
	{

		Tile[][] tiles = resources.getMap().getTiles();

		int width = tiles.length;
		int height = tiles[0].length;

		// first see how close all the tiles are to the edge
		int[][] proxMask = new int[width][height];
		Arrays.fill(proxMask, -1); // all unexplored to begin with

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{

				//System.out.print(tiles[i][j] + "; ");
				if (proxMask[i][j] == -1)
				{
					findProx(i, j, tiles);
				}
			}

			//System.out.println();
		}

		// go through and evaluate the costs
		double[][] costMask = new double[width][height];

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				costMask[i][j] = costEquation(proxMask[i][j]);
			}
		}
	}

	/**
	 * A nice user-friendly starter for the proper findProx
	 * @param i coord
	 * @param j coord
	 * @param tiles map
	 * @return how close the tile is from an abyss tile
	 */
	private static int findProx(int i, int j, Tile[][] tiles)
	{
		int[] dirs = new int[] { -1, 0, 1 };
		return findProx(i, j, tiles, tiles.length, tiles[0].length, dirs);
	}

	/**
	 * From point i,j spiral outwards until abyss tile is reached
	 * 
	 * @param i
	 * @param j
	 * @param tiles
	 * @param maxI width of the tiles array
	 * @param maxJ height of the tiles array
	 * @return
	 */
	private static int findProx(int i, int j, Tile[][] tiles, int maxI, int maxJ, int[] dirs)
	{
		if (tiles[i][j] == Tile.ABYSS)
		{
			return 0;
		}
		else
		{
			int closestNeighbour = Integer.MAX_VALUE;
			
			for (int n = 0; n < dirs.length; n++)
			{
				for (int m = 0; m < dirs.length; m++)
				{
					// TODO for efficiency, the proximities it caluclates here should be stored too
					// So make all this not static and have a field for proxMask
				}
			}
		}
		
		return -1;
	}

	/**
	 * Function that determines the cost of a tile based on its proximity to an
	 * ABYSS tile
	 * according to the equation y = 100*e^(-x)+1
	 * 
	 * @param x
	 * @return
	 */
	private static double costEquation(int x)
	{
		return 100 * Math.pow(Math.E, -x) + 1;
	}
}
