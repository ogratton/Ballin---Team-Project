package resources;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import resources.Character.Heading;
import resources.Powerup.Power;

public class CharacterTests {
	Character c;

	@Before
	public void setUp() {
		c = new Character();
	}

	@After
	public void tearDown() {
		c = null;
	}

	/**
	 * Test getVisibleDirection
	 */
	@Test
	public void testActionPerformed1() {
		int dx = 0;
		int dy = 0;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.STILL);

		dx = 5;
		dy = 0;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.E);

		dx = -5;
		dy = 0;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.W);

		dx = 0;
		dy = 5;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.S);

		dx = 0;
		dy = -5;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.N);

		dx = 5;
		dy = -5;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.NE);

		dx = -5;
		dy = -5;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.NW);

		dx = 5;
		dy = 5;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.SE);

		dx = -5;
		dy = 5;
		c.setDx(dx);
		c.setDy(dy);
		assertTrue(c.getVisibleDirection(dx, dy) == Heading.SW);
	}

	/**
	 * Test setDirection
	 */
	@Test
	public void testActionPerformed2() {
		c.setControls(false, false, false, false, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.STILL);

		c.setControls(true, false, false, false, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.N);

		c.setControls(false, true, false, false, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.S);

		c.setControls(false, false, true, false, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.W);

		c.setControls(false, false, false, true, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.E);

		c.setControls(true, false, true, false, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.NW);

		c.setControls(true, false, false, true, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.NE);

		c.setControls(false, true, true, false, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.SW);

		c.setControls(false, true, false, true, false, false);
		c.setDirection();
		assertTrue(c.getDirection() == Heading.SE);
	}

	/**
	 * Test getMovingDirection
	 */
	@Test
	public void testActionPerformed3() {
		c.setControls(false, false, false, false, false, false);
		assertTrue(c.getMovingDirection() == Heading.STILL);

		c.setControls(true, false, false, false, false, false);
		c.setDirection();
		assertTrue(c.getMovingDirection() == Heading.N);

		c.setControls(false, true, false, false, false, false);
		c.setDirection();
		assertTrue(c.getMovingDirection() == Heading.S);

		c.setControls(false, false, true, false, false, false);
		c.setDirection();
		assertTrue(c.getMovingDirection() == Heading.W);

		c.setControls(false, false, false, true, false, false);
		c.setDirection();
		assertTrue(c.getMovingDirection() == Heading.E);

		c.setControls(true, false, true, false, false, false);
		c.setDirection();
		assertTrue(c.getMovingDirection() == Heading.NW);

		c.setControls(true, false, false, true, false, false);
		c.setDirection();
		assertTrue(c.getMovingDirection() == Heading.NE);

		c.setControls(false, true, true, false, false, false);
		c.setDirection();
		assertTrue(c.getMovingDirection() == Heading.SW);

		c.setControls(false, true, false, true, false, false);
		c.setDirection();
		assertTrue(c.getMovingDirection() == Heading.SE);
	}

	/**
	 * Test powerups - applyPowerup, revertPowerup and
	 * getLastPowerup/getLastPowerupTime
	 */
	@Test
	public void testActionPerformed4() {
		assertTrue(c.getMaxDx() == 2.75);
		Powerup p = new Powerup(Power.Speed);
		c.applyPowerup(p, 1);
		assertTrue(c.getMaxDx() == (2.75 * 2));
		assertTrue(c.getLastPowerup() == Power.Speed);
		assertTrue(c.getLastPowerupTime() == 1);
		c.revertPowerup();
		assertTrue(c.getMaxDx() == 2.75);
		p = new Powerup(Power.Mass);
		c.applyPowerup(p, 2);
		assertTrue(c.getMaxDx() == (2.75 / 2));
		assertTrue(c.getLastPowerup() == Power.Mass);
		assertTrue(c.getLastPowerupTime() == 2);
		c.revertPowerup();
		assertTrue(c.getMaxDx() == 2.75);
		p = new Powerup(Power.Spike);
		c.applyPowerup(p, 3);
		assertTrue(c.getMaxDx() == (2.75 / 10));
		assertTrue(c.getLastPowerup() == Power.Spike);
		assertTrue(c.getLastPowerupTime() == 3);
		c.revertPowerup();
		assertTrue(c.getMaxDx() == 2.75);
	}
}
