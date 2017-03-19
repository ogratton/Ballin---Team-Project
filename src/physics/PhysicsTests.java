package physics;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import resources.Map;
import resources.Resources;
import resources.Resources.Mode;

public class PhysicsTests {
	Physics physics = null;
	@Before
	public void setUp() throws Exception {
		Resources r= new Resources();
		physics = new Physics(r, false);
		r.mode = Mode.LastManStanding;
		r.setMap(new Map(new Point2D.Double(0,0), 100, 100, 0.02, "TestMap"));
	}

	@After
	public void tearDown() throws Exception {
		physics = null;
	}

	@Test
	public void testActionPerformed1() {
		//Test physics.actionPerformed().
		//Test single character movement:
		fail("Not yet implemented");
	}
	
	@Test
	public void testActionPerformed2() {
		//Test physics.actionPerformed().
		//Test two character collision:
		fail("Not yet implemented");
	}
	
	@Test
	public void testActionPerformed3() {
		//Test physics.actionPerformed().
		//Test single character wall collision:
		fail("Not yet implemented");
	}

}
