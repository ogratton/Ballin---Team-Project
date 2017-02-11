package physics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import resources.Character;
import resources.Collidable;
import resources.Map.Tile;
import resources.Resources;
import resources.Wall;

public class Physics extends Thread implements ActionListener {
	// dashing reduces stamina, speed multiplied by stamina.
	private Timer timer;
	private final int DELAY = 10;

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
		for(Character c : Resources.playerList){
			update(c);
			for (Character d : Resources.playerList) {
				// check collisions
				if (c != d) {
					CND cnd = detectCollision(c,d);
					if(cnd.collided) {
						collide(c,d,cnd);
						//System.out.println("Collision!");
						//System.out.println("x1: " + c.getX() + ", y1: " + c.getY());
						//System.out.println("x2: " + d.getX() + ", y2: " + d.getY());
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
				//positionalCorrection(cnd);
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
	private void update(Character c) {
		// if dead, don't do anything (yet):
		if(c.isDead()) {
			return;
		}
		// find terrain type:
		Tile t = Resources.map.tileAt(c.getX(),c.getY());
		//check for falling.
		if(t == null || t == Tile.ABYSS) c.setFalling(true);
		if(c.isFalling()){
			Tile t2 = Resources.map.tileAt(c.getX(), c.getY() - c.getRadius());
			if(!(t2 == null || t2 == Tile.ABYSS)) { // at top of map
				c.setY(c.getY() + (c.getRadius()/10));
			}
			t2 = Resources.map.tileAt(c.getX() - c.getRadius(), c.getY());
			if(!(t2 == null || t2 == Tile.ABYSS)) { // at right of map
				c.setX(c.getX() + (c.getRadius()/10));
			}
			t2 = Resources.map.tileAt(c.getX() + c.getRadius(), c.getY());
			if(!(t2 == null || t2 == Tile.ABYSS)) { // at left of map
				c.setX(c.getX() - (c.getRadius()/10));
			}
			t2 = Resources.map.tileAt(c.getX(), c.getY() + c.getRadius());
			if(!(t2 == null || t2 == Tile.ABYSS)) { // at bottom of map
				c.setY(c.getY() - (c.getRadius()/10));
			}
			//c.setRadius(c.getRadius() - 1);
			//if(c.getRadius() > 3) c.setDead(true);
			return; // falling people don't move.
		}
		// calculate speed
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
		//apply friction
		if (!c.isLeft() && !c.isRight()) {
			c.setDx(c.getDx() * Resources.map.getFriction());
		}
		if (!c.isUp() && !c.isDown()) {
			c.setDy(c.getDy() * Resources.map.getFriction());
		}
		
		//calculate location
		double x = c.getX() + c.getDx();
		double y = c.getY() + c.getDy();
		
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
		 
		// Calculate impulse scalar
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
//	{ // copied from an example; needs adapting.
//	  const float percent = 0.2 // usually 20% to 80%
//	  const float slop = 0.01 // usually 0.01 to 0.1
//	  Vec2 correction = max( penetration - k_slop, 0.0f ) / (A.inv_mass + B.inv_mass)) * percent * n
//	  A.position -= A.inv_mass * correction
//	  B.position += B.inv_mass * correction
//	}
	
	private CND detectCollision(Character c, Character d) {
		CND cnd = new CND();
		int r = c.getRadius() + d.getRadius();
		r *= r; // reduce need for Math.sqrt()
		double dx = c.getX() - d.getX(); // difference in x
		double dy = c.getY() - d.getY(); // difference in y
		double dx2 = Math.pow(dx, 2);
		double dy2 = Math.pow(dy, 2);
		if((dx2 + dy2) <= r) {
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
	
	private CND detectCollision(Character c, Wall w) {
		CND cnd = new CND();
		double p1x, p1y;
		double p2x, p2y;
		// check whether c is inside w:
		boolean inside = false;
		if(inside) // TODO check if c is inside w 
		{
			inside = true;
		}
		// calculate which points define the line that intersects the 
		// line joining the middle of c to the middle of w
		if(c.getX() > w.getMidx()) { // p4 is the farthest to the right
			p1x = w.getX4();
			p1y = w.getY4();
		} else { // p1 is the farthest to the left
			p1x = w.getX1();
			p1y = w.getY1();
		}
		if(c.getY() > w.getMidy()) { // p3 is the lowest point
			p2x = w.getX3();
			p2y = w.getY3();
		} else { // p2 is the highest point
			p2x = w.getX2();
			p2y = w.getY2();
		}
		//find closest point on w to c:
		
		
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
		 * The normal to the collision.
		 */
		public Vector collisionNormal = new Vector();
		/**
		 * The depth of a collision.
		 */
		public double collisionDepth = 0; // used for position correction - make sure you aren't sinking into a wall, etc.
		
	}
	private class Vector {
		public double x = 0;
		public double y = 0;
	}
}
