package physics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import graphics.old.CharacterModel;

import resources.Collidable;
import resources.Resources;
import resources.Wall;

/**
 * I've modified the physics class to work with the models I have for graphics
 */

public class PhysicsWithModels extends Thread implements ActionListener {
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
		
		for(CharacterModel c : Resources.models){
			update(c);
			for (CharacterModel d : Resources.models) {
				// check collisions
				if (c != d) {
					CND cnd = detectCollision(c,d);
					if(cnd.collided) {
						collide(c,d,cnd);
						System.out.println("Collision!");
						System.out.println("x1: " + c.getX() + ", y1: " + c.getY());
						System.out.println("x2: " + d.getX() + ", y2: " + d.getY());
					}
				}
			}
			for (Wall w : Resources.map.getWalls()) {
				// check collisions
				CND cnd = detectCollision(c,w);
				if(cnd.collided) {
					collide(c,w,cnd);
				}
				
				// TODO correct positions (prevent clipping)
				
			}
			
		}
//		for (Character c : Resources.players) {
//			c.setCollided(false);
//		}
	}

	/**
	 * calculate speed and location of a character. Assumes no collisions.
	 * @param c
	 */
	private void update(CharacterModel c) {
		// calculate speed, calculate location
		if (c.isLeft() && c.getDx() > -c.getMaxDx()) {
			c.setDx(c.getDx() - c.getAcc());
		}
		if (c.isRight() && c.getDx() < c.getMaxDx()) {
			c.setDx(c.getDx() + c.getAcc());
		}
		if (c.isUp() && c.getDy() > -c.getMaxDy()) {
			c.setDy(c.getDy() - c.getAcc());
		}
		if (c.isDown() && c.getDy() < c.getMaxDy()) {
			c.setDy(c.getDy() + c.getAcc());
		}
		if (!c.isLeft() && !c.isRight()) {
			c.setDx(c.getDx() * Resources.map.getFriction());
		}
		if (!c.isUp() && !c.isDown()) {
			c.setDy(c.getDy() * Resources.map.getFriction());
		}

		double x = c.getX() + c.getDx();
		double y = c.getY() + c.getDy();
		// System.out.println(c.x());
		c.setX(x);
		c.setY(y);

	}

	private void collide(Collidable c, Collidable d, CND cnd) {
		// Calculate relative velocity
		double rvx = c.getDx() - d.getDx();
		double rvy = c.getDy() - d.getDy();
		
		// Calculate relative velocity in terms of the normal direction
		double normalVelocity = rvx*cnd.collisionNormal.x + rvy*cnd.collisionNormal.y;
		 
		// Do not resolve if velocities are pointing away from each other.
		if(normalVelocity > 0)
		  return;
		 
		// Calculate restitution (bounciness)
		double e = Math.min(c.getRestitution(), d.getRestitution());
		 
		// Calculate impulse value
		double j = -(1 + e) * normalVelocity; 
		j /= c.getInvMass() + d.getInvMass();
		
		// Apply impulse
		double impulsex = j * cnd.collisionNormal.x;
		double impulsey = j * cnd.collisionNormal.y;
		
		c.setDx(c.getDx() + (c.getInvMass() * impulsex));
		c.setDy(c.getDy() + (c.getInvMass() * impulsey));
		
		d.setDx(d.getDx() - (d.getInvMass() * impulsex));
		d.setDy(d.getDy() - (d.getInvMass() * impulsey));
		
		/*
		double dx1 = c.getDx();
		double dx2 = d.getDx();
		double dy1 = c.getDy();
		double dy2 = d.getDy();
		double m1 = c.getMass();
		double m2 = d.getMass();

		double newDX1 = ((m1 - m2) / (m1 + m2)) * dx1 + ((2 * m2) / (m1 + m2)) * dx2;
		double newDX2 = ((2 * m1) / (m1 + m2)) * dx1 + ((m2 - m1) / (m1 + m2)) * dx2;

		double newDY1 = ((m1 - m2) / (m1 + m2)) * dy1 + ((2 * m2) / (m1 + m2)) * dy2;
		double newDY2 = ((2 * m1) / (m1 + m2)) * dy1 + ((m2 - m1) / (m1 + m2)) * dy2;

		c.setDx(newDX1);
		c.setDy(newDY1);
		d.setDx(newDX2);
		d.setDy(newDY2);
		
		c.setCollided(true);
		d.setCollided(true);
		*/
	}

//	private double distanceBetween(Character c1, Character c2) {
//		double distance = Math.sqrt(Math.pow((c1.getX() - c2.getX()), 2) + Math.pow((c1.getY() - c2.getY()), 2));
//		return distance;
//	}
	

//	void PositionalCorrection( Object A, Object B )
//	{
//	  const float percent = 0.2 // usually 20% to 80%
//	  const float slop = 0.01 // usually 0.01 to 0.1
//	  Vec2 correction = max( penetration - k_slop, 0.0f ) / (A.inv_mass + B.inv_mass)) * percent * n
//	  A.position -= A.inv_mass * correction
//	  B.position += B.inv_mass * correction
//	}
	
	private CND detectCollision(CharacterModel c, CharacterModel d) {
		CND cnd = new CND();
		int r = c.getRadius() + d.getRadius();
		r *= r; // reduce need for Math.sqrt()
		double dx = c.getX() - d.getX(); // difference in x
		double dy = c.getY() - d.getY(); // difference in y
		double dx2 = Math.pow(dx, 2);
		double dy2 = Math.pow(dy, 2);
		if((dx2 + dy2) <= r && (!c.isCollided() || !d.isCollided())) {
			double distance = Math.sqrt(dx2 + dy2);
			if (distance != 0) {
				cnd.collided = true;
				cnd.collisionDepth = r - distance;
				// normalise collision normal
				cnd.collisionNormal.x = dx/distance;
				cnd.collisionNormal.y = dy/distance;
			}
		}
		return cnd;
	}
	
	private CND detectCollision(CharacterModel c, Wall w) {
		CND cnd = new CND();
		double dx = c.getX() - w.origin().getX(); //difference in x
		double dy = c.getY() - w.origin().getY(); //difference in y
		
		//TODO implement all of this.
		
		return cnd;
	}
	
	/**
	 * @author alexander
	 * stores the collision normal and depth for a collision
	 * (I miss Haskell tuples so much right now).
	 */
	private class CND {
		/**
		 * whether or not a collision has happened
		 */
		public boolean collided = false;
		/**
		 * The normal to the collision, measured in degrees from north.
		 */
		public Vector collisionNormal = new Vector();
		/**
		 * The depth of a collision.
		 */
		public double collisionDepth = 0;
		
	}
	private class Vector {
		public double x = 0;
		public double y = 0;
	}
}
