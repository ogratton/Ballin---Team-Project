package ai.pathfinding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import resources.Resources;

public class PathFindingJUnit
{
	
	private Resources resources;
	
	@Before
	public void setUp() throws Exception
	{
		resources = new Resources();
	}
	
//	@Test
//	public void aStarTest()
//	{
//		AStarSearch aStar = new AStarSearch(resources);
//		// TODO
//	}
		
	/**
	 * 
	 * Tests the following functions in StaticHeuristics
	 *
	 * euclidean()
	 * manhattan()
	 * 
	 */
	@Test
	public void staticHeuristicsTest()
	{

		// setup
		Point a = new Point(0, 0);
		Point b = new Point(2, 3);

		// euclidean()
		double eucAB_ = Math.sqrt(13);
		double eucAB = StaticHeuristics.euclidean(a, b);
		assertEquals(eucAB_, eucAB, 0);

		// manhattan()
		double manAB_ = 5;
		double manAB = StaticHeuristics.manhattan(a, b);
		assertEquals(manAB_, manAB, 0);
	}

	/**
	 * 
	 * Tests the following functions in Vector
	 *
	 * getCentre()
	 * getX()
	 * getY()
	 * pointInside()
	 * equalDirection()
	 * 
	 */
	@Test
	public void vectorTest()
	{
		// setup
		Point o = new Point(500, 500);

		Vector v0 = new Vector(0.4, -0.6, o);
		Vector v0n = v0.normal(o);

		Vector v1 = new Vector(-1, 0, o);
		Vector v1n = v1.normal(o);

		// getCentre()
		assertEquals(v0.getCentre(), o);
		assertEquals(v1.getCentre(), o);

		// getX()
		assertEquals(v0.getX(), 0.4, 0);
		assertEquals(v1.getX(), -1, 0);

		// getY()
		assertEquals(v0.getY(), -0.6, 0);
		assertEquals(v1.getY(), 0, 0);

		// pointInside()
		boolean[] tests = new boolean[] { v0n.pointInside(new Point(500, 600)), v0n.pointInside(new Point(440, 480)),
				!v0n.pointInside(new Point(500, 400)), !v0n.pointInside(new Point(600, 400)),

				v1n.pointInside(new Point(600, 600)), v1n.pointInside(new Point(600, 400)), !v1n.pointInside(new Point(400, 400)),
				!v1n.pointInside(new Point(400, 600)) };

		for (int i = 0; i < tests.length; i++)
		{
			assertTrue(tests[i]);
		}

		// equalDirection
		assertTrue(v0.equalDirection(new Vector(0.4, -0.6, o))); // exactly alike
		assertTrue(v0.equalDirection(new Vector(0.5, -0.5, o))); // nearly alike
	}

	/**
	 * Tests the points are sorted correctly in sorted Collections
	 * (sorts by x then y ascending)
	 */
	@Test
	public void pointComparatorTest()
	{
		TreeSet<Point> testSet = new TreeSet<Point>(new PointComparator());
		Point p1 = new Point(0, 100);
		Point p2 = new Point(200, 300);
		Point p3 = new Point(50, 500);
		testSet.add(p1);
		testSet.add(p2);
		testSet.add(p3);

		assertTrue(testSet.higher(new Point(-1, -1)).equals(p1)); // test item considered least
		assertTrue(testSet.lower(new Point(1000, 1000)).equals(p2)); // test item considered greatest
	}

	/**
	 * Tests the following functions in Vector
	 * 
	 * distanceTravelled()
	 * distanceToGo()
	 * getLocation()
	 * getParent()
	 * isEmpty()
	 */
	@Test
	public void searchNodeTest()
	{

		// setup
		SearchNode emptySearchNode = new SearchNode();

		Point a = new Point(10, 10);
		Point b = new Point(100, 100);
		SearchNode testSearchNode = new SearchNode(a, emptySearchNode, 20.0, b);

		// distanceTravelled()
		assertEquals(testSearchNode.distanceTravelled(), 20.0, 0);

		// distanceToGo()
		double distToGo = StaticHeuristics.euclidean(a, b);
		assertEquals(testSearchNode.distanceToGo(), distToGo, 0);

		// getLocation()
		assertEquals(testSearchNode.getLocation(), a);

		// getParent()
		assertEquals(testSearchNode.getParent(), emptySearchNode);

		// isEmpty()
		assertTrue(emptySearchNode.isEmpty());
		assertTrue(!testSearchNode.isEmpty());
	}
}
