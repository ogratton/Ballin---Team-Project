package ai.pathfinding;

import java.util.Collection;
import java.util.TreeSet;

import resources.Map.Tile;
import resources.Resources;

public class MapCosts
{
	private Resources resources;

	Tile[][] tileMap;
	int[][] proxMask;
	double[][] costMask;
	boolean[][] unreachable;

	int width, height;
	int biggestDimension;

	public MapCosts(Resources resources, Tile[][] tilemap, boolean[][] unreachable) {
		this.resources = resources;
		
		tileMap = tilemap;
		width = tileMap.length;
		height = tileMap[0].length;
		biggestDimension = height < width ? width : height;
		this.unreachable = unreachable;

		genMapCostsMask();

//		printProxMask();
//		System.out.println();
//		printCostMask(); // debug
		
		resources.getMap().setProxMask(proxMask);
		resources.getMap().setCostMask(costMask);
	}

	@SuppressWarnings("unused")
	private void printProxMask()
	{
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				System.out.print(proxMask[i][j] + "; ");
			}
			System.out.println();
		}
	}
	
	@SuppressWarnings("unused")
	private void printCostMask()
	{
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				System.out.print(costMask[i][j] + "; ");
			}
			System.out.println();
		}
	}

	/**
	 * Create a 2D array the same size as the map with the costs of moving to
	 * each tile
	 * An abyss tile obviously has the highest cost and the cost decreases
	 * exponentially as we move farther from the edge
	 * 
	 * @param resources we will add the array directly into resources
	 */
	private void genMapCostsMask()
	{

		// first see how close all the tiles are to the edge		
		genProxMask();

		// then convert those proximities to costs
		evaluateProxMask();

	}

	/**
	 * Fill the proximity mask array by calculating how far away
	 * the nearest abyss tile is
	 */
	private void genProxMask()
	{
		proxMask = new int[width][height];
//		Arrays.fill(proxMask, Integer.MAX_VALUE); // all unexplored to begin with

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				if (!unreachable[i][j] && proxMask[i][j] == 0)
				{
					findProx(i, j);
				}
			}
		}
		printProxMask();
	}

	/**
	 * Search from given point to the closest abyss tile
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	private int findProx(int i, int j)
	{
		// If we're on a bad tile to start with...
		if (resources.getBadTiles().contains(tileMap[i][j]))
		{
			proxMask[i][j] = 0;
			return 0;
		}
		for (int level = 1; level < biggestDimension; level++)
		{
			
			TreeSet<Tile> neighbours = new TreeSet<Tile>();

			// for every surrounding tile <level> tiles away
			for (int n = -level; n <= level; n++)
			{
				// check it's a legal coordinate
				if ((i + n) < width && (i + n) >= 0)
				{
					for (int m = -level; m <= level ; m++)
					{
						// check it's a legal coordinate 
						if (j + m < height && (j + m) >= 0)
						{
							// don't evaluate ourselves
							if (!(n == 0 && m == 0))
							{
								neighbours.add(tileMap[i + n][j + m]);
							}
						}
					}
				}
			}
			
			if (containsAny(neighbours, resources.getBadTiles()))
			{
				proxMask[i][j] = level;
				return level;
			}
		}
		
		return Integer.MAX_VALUE; // if we never found a bad tile then this tile should be cheap to walk on
	}
	
	/**
	 * @return true if A contains any of the elements of B
	 */
	private <E> boolean containsAny(Collection<E> A, Collection<E> B)
	{
		for (Object object : B)
		{
			if(A.contains(object))
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Fill the costMask with the cost value based on the proximity to an edge
	 */
	private void evaluateProxMask()
	{
		costMask = new double[width][height];

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				costMask[i][j] = costEquation(proxMask[i][j]);
			}
		}
	}

	/**
	 * Function that determines the cost of a tile based on its proximity to an
	 * ABYSS tile
	 * according to the equation y = 100*e^(-x)+1
	 * 
	 * @param x
	 * @return
	 */
	private double costEquation(int x)
	{
		return 100 * Math.pow(Math.E, -x); // XXX (arbitrary choice!) 100 * Math.pow(Math.E, -x) + 1
	}
}
