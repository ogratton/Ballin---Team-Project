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
	private boolean verbose = false;
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
		r.clearPlayerList();
	}

	@Test
	public void testActionPerformed1() {
		//Test physics.actionPerformed().
		//Test single character movement:
		// starts character in the middle of the map, then moves them in each direction for twenty steps.
		Character c1 = new Character(1, 50, 50, 25, Heading.STILL, Class.WARRIOR, 0, "Player 1");
		r.addPlayerToList(c1);
		double acc = c1.getAcc();
		double maxdx = c1.getMaxDx(), maxdy = c1.getMaxDy();
		double dx = c1.getDx(), dy = c1.getDy();
		double x = c1.getX(), y = c1.getY();
		for(Character.Heading h : new Character.Heading[]{Character.Heading.N, Character.Heading.NW, Character.Heading.NE, Character.Heading.W, Character.Heading.E, Character.Heading.S, Character.Heading.SW, Character.Heading.SE, Character.Heading.STILL}) {
			switch(h) {
			case N:
				c1.setControls(true, false, false, false, false, false);
				break;
			case E:
				c1.setControls(false, false, false, true, false, false);
				break;
			case NE:
				c1.setControls(true, false, false, true, false, false);
				break;
			case NW:
				c1.setControls(true, false, true, false, false, false);
				break;
			case S:
				c1.setControls(false, true, false, false, false, false);
				break;
			case SE:
				c1.setControls(false, true, false, true, false, false);
				break;
			case STILL:
				c1.setControls(false, false, false, false, false, false);
				break;
			case SW:
				c1.setControls(false, true, true, false, false, false);
				break;
			case W:
				c1.setControls(false, false, true, false, false, false);
				break;
			}
			dx = 0.0;
			dy = 0.0;
			c1.setDx(0);
			c1.setDy(0);
			c1.setX(50.0);
			c1.setY(50.0);
			for(int step = 1; step <= 20; step++) {
				//20 steps
				x = c1.getX();
				y = c1.getY();
				if( c1.isLeft()  ) dx = Math.max(-maxdx, dx - acc);
				if( c1.isRight() ) dx = Math.min( maxdx, dx + acc);
				if( c1.isUp()    ) dy = Math.max(-maxdy, dy - acc);
				if( c1.isDown()  ) dy = Math.min( maxdy, dy + acc);
				
				physics.actionPerformed(ev);
				if(verbose) System.out.println(h + " step " + step + ": dx " + dx + ", dy " + dy + ", coords: (" + x + "," + y + ")");
				assertTrue(Double.compare(c1.getX(), x + dx) == 0);
				assertTrue(Double.compare(c1.getY(), y + dy) == 0);
			}
		}
		
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
