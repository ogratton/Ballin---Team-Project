package ai.junit;

import java.awt.Point;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import ai.pathfinding.StaticHeuristics;

public class AITests
{

	// TODO
	
	/*
	 *  BasicAI:
	 *  
	 *  	funeral()
	 *  	scanForNearestPlayer
	 *  	?? moveToNextWaypoint()
	 *  	normalToNextWaypoint()
	 *  	getOurLocation()
	 *  	getCurrentTileCoords()
	 *  	getTileCoords()
	 *  	?? projectedPosition()
	 *  	isWalkable()
	 *  	convertWaypoints()
	 *  	?? moveTo()
	 *  	?? fuzzyEqual()
	 *  	setBehaviour()
	 *  	setDestinations()
	 */
	
	/*  AStarSearch
	 * 
	 * 		addNeighbour()
	 * 		sparsifyPath()
	 * 		smoothPath()
	 */
	
	/*
	 *	PointComparator
	 *
	 *		just show two points being compared
	 */
	
	/*
	 * 	SearchNode
	 * 
	 * 		??
	 */
	

	
	/*
	 * 	Vector
	 * 
	 * 		getCentre()
	 * 		getX()
	 * 		getY()
	 * 		pointInside()
	 * 		getYfromX()
	 * 		getXfromY()
	 * 		equalDirection()
	 * 		fuzzyEqual()
	 */
	
	
	@Test
	public void test() {
//		fail("Not yet implemented");
		
		/*
		 *	StaticHeuristics
		 *
		 *		euclidean()
		 *		manhattan()
		 */
		Point a = new Point(0,0);
		Point b = new Point(2,3);
		
		double distAB_ = Math.sqrt(13); 
		double distAB = StaticHeuristics.euclidean(a, b);
		assertEquals(distAB_, distAB);
	}

}
