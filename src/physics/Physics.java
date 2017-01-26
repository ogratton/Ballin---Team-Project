package physics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import resources.Character;
import resources.Map;
import resources.Resources;
import resources.Wall;

public class Physics extends Thread implements ActionListener {
	//
	private Timer timer;
	private final int DELAY = 10; // ~1000ms /60 = 1/60 seconds

	@Override
	public void run() {
		System.out.println("start");
		timer = new Timer(DELAY, this);
		timer.start();
	}

	/**
	 * pauses the simulation
	 */
	public void pause() {
		timer.stop();
	}

	/**
	 * unpauses the simulation
	 */
	public void unpause() {
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for(Character c : Resources.players){
			update(c);
			for (Character d : Resources.players) {
				if (c != d && distanceBetween(c, d) <= (c.radius() + d.radius()) && !c.collided() && !d.collided()) {
					collide(c,d);
					System.out.println("Collision!");
					System.out.println("x1: " + c.x() + ", y1: " + c.y());
					System.out.println("x2: " + d.x() + ", y2: " + d.y());
				}
			}
		}
		for (Character c : Resources.players) {
			c.setCollided(false);
		}
	}

	private void update(Character c) {
		// calculate speed, calculate location
		// if(c.left()) System.out.println(c.dx());
		if (c.left() && c.dx() > -c.maxdx()) {
			// System.out.println("l");
			c.dx(c.dx() - c.acc());
		}
		if (c.right() && c.dx() < c.maxdx()) {
			// System.out.println("r" + c.mass());
			c.dx(c.dx() + c.acc());
		}
		if (c.up() && c.dy() > -c.maxdy()) {
			// System.out.println("u");
			c.dy(c.dy() - c.acc());
		}
		if (c.down() && c.dy() < c.maxdy()) {
			// System.out.println("d");
			c.dy(c.dy() + c.acc());
		}
		if (!c.left() && !c.right()) {
			c.dx(c.dx() * Resources.map.friction());
		}
		if (!c.up() && !c.down()) {
			c.dy(c.dy() * Resources.map.friction());
		}
		// TODO calculate collisions

		double x = c.x() + c.dx();
		double y = c.y() + c.dy();
		// System.out.println(c.x());
		c.x(x);
		c.y(y);

	}

	private void collide(Character c1, Character c2) {
		double dx1 = c1.dx();
		double dx2 = c2.dx();
		double dy1 = c1.dy();
		double dy2 = c2.dy();
		double m1 = c1.mass();
		double m2 = c2.mass();

		double newDX1 = ((m1 - m2) / (m1 + m2)) * dx1 + ((2 * m2) / (m1 + m2)) * dx2;
		double newDX2 = ((2 * m1) / (m1 + m2)) * dx1 + ((m2 - m1) / (m1 + m2)) * dx2;

		double newDY1 = ((m1 - m2) / (m1 + m2)) * dy1 + ((2 * m2) / (m1 + m2)) * dy2;
		double newDY2 = ((2 * m1) / (m1 + m2)) * dy1 + ((m2 - m1) / (m1 + m2)) * dy2;

		c1.dx(newDX1);
		c1.dy(newDY1);
		c2.dx(newDX2);
		c2.dy(newDY2);
		
		c1.setCollided(true);
		c2.setCollided(true);
	}

	private void collide(Character c, Wall w) {

	}

	private double distanceBetween(Character c1, Character c2) {
		double distance = Math.sqrt(Math.pow((c1.x() - c2.x()), 2) + Math.pow((c1.y() - c2.y()), 2));
		return distance;
	}
}
