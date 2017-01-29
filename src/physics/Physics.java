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
				if (c != d && distanceBetween(c, d) <= (c.getRadius() + d.getRadius()) && !c.isCollided() && !d.isCollided()) {
					collide(c,d);
					System.out.println("Collision!");
					System.out.println("x1: " + c.getX() + ", y1: " + c.getY());
					System.out.println("x2: " + d.getX() + ", y2: " + d.getY());
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
		if (c.isLeft() && c.getDx() > -c.getMaxDx()) {
			// System.out.println("l");
			c.setDx(c.getDx() - c.getAcc());
		}
		if (c.isRight() && c.getDx() < c.getMaxDx()) {
			// System.out.println("r" + c.mass());
			c.setDx(c.getDx() + c.getAcc());
		}
		if (c.isUp() && c.getDy() > -c.getMaxDy()) {
			// System.out.println("u");
			c.setDy(c.getDy() - c.getAcc());
		}
		if (c.isDown() && c.getDy() < c.getMaxDy()) {
			// System.out.println("d");
			c.setDy(c.getDy() + c.getAcc());
		}
		if (!c.isLeft() && !c.isRight()) {
			c.setDx(c.getDx() * Resources.map.getFriction());
		}
		if (!c.isUp() && !c.isDown()) {
			c.setDy(c.getDy() * Resources.map.getFriction());
		}
		// TODO calculate collisions

		double x = c.getX() + c.getDx();
		double y = c.getY() + c.getDy();
		// System.out.println(c.x());
		c.setX(x);
		c.setY(y);

	}

	private void collide(Character c1, Character c2) {
		double dx1 = c1.getDx();
		double dx2 = c2.getDx();
		double dy1 = c1.getDy();
		double dy2 = c2.getDy();
		double m1 = c1.getMass();
		double m2 = c2.getMass();

		double newDX1 = ((m1 - m2) / (m1 + m2)) * dx1 + ((2 * m2) / (m1 + m2)) * dx2;
		double newDX2 = ((2 * m1) / (m1 + m2)) * dx1 + ((m2 - m1) / (m1 + m2)) * dx2;

		double newDY1 = ((m1 - m2) / (m1 + m2)) * dy1 + ((2 * m2) / (m1 + m2)) * dy2;
		double newDY2 = ((2 * m1) / (m1 + m2)) * dy1 + ((m2 - m1) / (m1 + m2)) * dy2;

		c1.setDx(newDX1);
		c1.setDy(newDY1);
		c2.setDx(newDX2);
		c2.setDy(newDY2);
		
		c1.setCollided(true);
		c2.setCollided(true);
	}

	private void collide(Character c, Wall w) {

	}

	private double distanceBetween(Character c1, Character c2) {
		double distance = Math.sqrt(Math.pow((c1.getX() - c2.getX()), 2) + Math.pow((c1.getY() - c2.getY()), 2));
		return distance;
	}
}
