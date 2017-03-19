package physics;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import resources.Map;
import resources.Resources;
import resources.Resources.Mode;
import resources.Character;
import resources.Character.Class;
import resources.Character.Heading;

public class PhysicsTests {
	private Physics physics;
	private Resources r;
	private Map map;
	private static final ActionEvent ev = new ActionEvent(new Object(), 0, "");
	
	@Before
	public void setUp() {
		r = new Resources();
		physics = new Physics(r, false);
		r.mode = Mode.LastManStanding;
		map = new Map(
				new Point2D.Double(0,0), // origin
				100, // width
				100, // height
				0.0, // friction (no friction! Hahahahahaaa!)
				"TestMap");
		
		r.setMap(map);
	}

	@After
	public void tearDown() {
		physics = null;
	}

	@Test
	public void testActionPerformed1() {
		final int args = 100;
		double x[] = new double[args];
		double y[] = new double[args];
		double dx[] = new double[args];
		double dy[] = new double[args];
		
		for (int i = 0; i < dy.length; i++) {
			testActionPerformed1(x[i], y[i], dx[i], dy[i]);
		}
	}
	
	private void testActionPerformed1(double x, double y, double dx, double dy) {
		//Test physics.actionPerformed().
		//Test single character movement:
		Character c1 = new Character(1, x, y, 25, Heading.STILL, Class.WARRIOR, 0, "Player 1");
		
		c1.setDx(dx);
		c1.setDy(dy);
		
		physics.actionPerformed(ev);
		
		assertTrue(Double.compare(c1.getX(), x + dx) == 0);
		assertTrue(Double.compare(c1.getY(), y + dy) == 0);
		
	}
	
	@Test
	public void testActionPerformed2() {
		//Test physics.actionPerformed().
		//Test one character movement:
		fail("Not yet implemented");
	}
	
	@Test
	public void testActionPerformed3() {
		//Test physics.actionPerformed().
		//Test single character wall collision:
		fail("Not yet implemented");
	}

}
