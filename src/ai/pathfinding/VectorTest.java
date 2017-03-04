package ai.pathfinding;

import java.awt.Point;

public class VectorTest
{
	/**
	 * TODO convert this to JUnit tests
	 */
	public static void main(String[] args)
	{
		Point o = new Point(500,500);
		
		
		Vector v0 = new Vector(0.4, -0.6, o);	
		Vector v0n = v0.normal(o);
		System.out.println(v0n);
		
		Vector v1 = new Vector(-1, 0, o);
		Vector v1n = v1.normal(o);
		System.out.println(v1n);
		
		boolean[] tests = new boolean[]{
				v0n.pointInside(new Point(500, 600)),
				v0n.pointInside(new Point(440, 480)),
				!v0n.pointInside(new Point(500, 400)),
				!v0n.pointInside(new Point(600,400)),
				
				v1n.pointInside(new Point(600, 600)),
				v1n.pointInside(new Point(600, 400)),
				!v1n.pointInside(new Point(400, 400)),
				!v1n.pointInside(new Point(400, 600))
				};

		boolean clean = true;
		
		for (int i = 0; i < tests.length; i++)
		{
			if (!tests[i])
			{
				System.out.println("Failed test "+(i+1)+"");
				clean = false;
			}
		}

		if (clean) System.out.println("All test passed");

	}
}
