package physics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

import javax.swing.Timer;

import resources.Character;
import resources.Collidable;
import resources.Map.Tile;
import resources.Resources;
import resources.Wall;

public class Physics extends Thread implements ActionListener {
	//dashing reduces stamina, speed multiplied by stamina.
	private Timer timer;
	private final int DELAY = 10;
	private Resources resources;
	
	//checks whether the tile is a 'killing' tile.
	private static Function<Tile,Boolean> tileCheck = (tile) -> (tile == null || tile == Tile.ABYSS || tile == Tile.EDGE_ABYSS);

	
	public Physics(Resources resources){
		this.resources = resources;
	}
	
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
		for(Character c : resources.getPlayerList()){
			update(c);
			for (Character d : resources.getPlayerList()) {
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
			for (Wall w : resources.getMap().getWalls()) {
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
			if(c.getDyingStep() >= 200) { //this number is "sizeX * 4". TODO find sizeX and read it.
				//respawn
				c.setDead(false);
				c.setFalling(false);
				c.setDyingStep(0);
				c.setX(500);
				c.setY(200);
				c.setDx(0);
				c.setDy(0);
			}
		}
		
		// find terrain type:
		Tile t = resources.getMap().tileAt(c.getX(),c.getY());
		//check for falling.
		if(tileCheck.apply(t)) {
			c.setFalling(true);
		}
		// Recharge stamina
		c.incrementStamina();
		// If a special button has been pressed, perform the ability if possible
		if (special(c)){
			return;
		}
		if(!c.isFalling()) {
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
				c.setDx(c.getDx() * resources.getMap().getFriction());
			}
			if (!c.isUp() && !c.isDown()) {
				c.setDy(c.getDy() * resources.getMap().getFriction());
			}
		} else {
			//dead if completely off the map.
			boolean dead = true;
			Tile t2 = resources.getMap().tileAt(c.getX() + c.getRadius(), c.getY());
			if(!tileCheck.apply(t2)) { // bottom edge
				dead = false;
				c.setDx(0 - Math.abs(c.getDx()));
			}
			t2 = resources.getMap().tileAt(c.getX() - c.getRadius(), c.getY());
			if(!tileCheck.apply(t2)) { // top edge
				dead = false;
				c.setDx(Math.abs(c.getDx()));
			}			
			t2 = resources.getMap().tileAt(c.getX(), c.getY() + c.getRadius());
			if(!tileCheck.apply(t2)) { // right edge
				dead = false;
				c.setDy(0 - Math.abs(c.getDy()));
			}			
			t2 = resources.getMap().tileAt(c.getX(), c.getY() - c.getRadius());
			if(!tileCheck.apply(t2)) { // left edge
				dead = false;
				c.setDy(Math.abs(c.getDy()));
			}
			if(dead) {
				c.setDead(true);
			}
		}
		move(c);
	}
	
	/**
	 * Returns true if a special is being performed, false otherwise.
	 */
	private boolean special(Character c) {
		int stam = c.getStamina();
		// Just pressed dash button
		if (c.isDashing() && c.getDashTimer() == 0 && !c.isBlocking()) {
			int dashStam = c.getDashStamina();
			if (stam >= dashStam) {
				// If you can dash, drain the amount of stamina
				c.setStamina(stam - dashStam);
			} else {
				// Otherwise give up on dashing
				c.setDashing(false);
			}
		}
		// Just pressed block button
		if (c.isBlocking() && c.getBlockTimer() == 0 && !c.isDashing()) {
			int blockStam = c.getBlockStamina();
			if (stam >= blockStam) {
				// If you can block, drain the amount of stamina
				c.setStamina(stam - blockStam);
			} else {
				// Otherwise give up on blocking
				c.setBlocking(false);
			}
		}
		if (c.isDashing() && !c.isBlocking()) {
			dash(c);
			move(c);
			// Don't allow any other inputs
			return true;
		}
		if (c.isBlocking() && !c.isDashing()) {
			block(c);
			move(c);
			// Don't allow any other inputs
			return true;
		}
		return false;
	}
	
	private void move(Character c) {
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
		if(c.getX() <= w.getX4() && c.getX() >= w.getX1() && c.getY() >= w.getY2() && c.getY() <= w.getY3()) {
			// check if the centre of c is inside w 
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
		//(intersection of two lines)
		//y = mx+c; (y - y1)/(y2 - y1) = (x - x1)/(x2 - x1)
		// rearrange to get: m = (y2 - y1)/(x2 - x1); c = y1 - (x1 * m)
		
		// line between p1 and p2
		double m1 = (p2y - p1y)/(p2x - p1x);
		double c1 = p1y - (p1x * m1);
		
		// line between middle of the wall and middle of the character
		double m2 = (w.getMidy() - c.getY()) / (w.getMidx() - c.getX());
		double c2 = c.getY() - (c.getX() * m2);
		
		// point of intersection:
		double closestX = (c2 - c1) / (m1 - m2);
		double closestY = (m1 * closestX) + c1;
		
		if(closestX <= w.getX4() && closestX >= w.getX1() && closestY >= w.getY2() && closestY <= w.getY3()) {
			// closestX and closestY is a point on the wall.
			double r = c.getRadius() + (Math.sqrt(Math.pow(closestX - w.getMidx(), 2) + Math.pow(closestY - w.getMidy(), 2)));
			r *= r;
			
			double dx = closestX - c.getX();
			double dy = closestY - c.getY();
			double dx2 = Math.pow(dx, 2);
			double dy2 = Math.pow(dy, 2);
			
			if((dx2 + dy2) <= r) { // collision detected! (at last)
				double distance = Math.sqrt(dx2 + dy2);
				cnd.collided = true;
				cnd.collisionDepth = r - distance;
				cnd.collisionNormal.x = dx/distance;
				cnd.collisionNormal.y = dy/distance;
				if(inside) { // if the centre of c is inside w, reverse the normal.
					cnd.collisionNormal.x *= -1;
					cnd.collisionNormal.y *= -1;
				}
			}
		}
		
		return cnd;
	}
	
	private void dash(Character c) {
		/*
		if (c.getDashTimer() == 0) {
			//System.out.println("DASHING");
			//System.out.println("BEFORE: " + c.getDx() + ", " + c.getDy());
		}
		*/
		double maxDxDy = Math.max(Math.abs(c.getDx()), Math.abs(c.getDy()));
		// Can't dash if standing still
		if (maxDxDy == 0) {
			c.setDashing(false);
			// Refund stamina
			c.setStamina(c.getStamina() + c.getDashStamina());
			return;
		}
		double velInc = (2 * c.getMaxDx()) / maxDxDy;
		c.setDx(c.getDx() * velInc);
		c.setDy(c.getDy() * velInc);
		//System.out.println(maxDxDy);
		c.incrementDashTimer();
		
		if (c.getDashTimer() >= 25) {
			//System.out.println("DONE DASHING");
			//System.out.println("AFTER: " + c.getDx() + ", " + c.getDy());
			maxDxDy = Math.max(Math.abs(c.getDx()), Math.abs(c.getDy()));
			velInc = (c.getMaxDx()) / maxDxDy;
			c.setDx(c.getDx() * velInc);
			c.setDy(c.getDy() * velInc);
			//System.out.println("Reducing speed to: " + c.getDx() + ", " + c.getDy());
			c.setDashing(false);
			c.resetDashTimer();
		}
	}
	
	private void block(Character c) {
		// Start blocking - increase mass
		if (c.getBlockTimer() == 0) {
			//System.out.println("BLOCKING");
			//System.out.println("BEFORE: " + c.getDx() + ", " + c.getDy());
			c.setMass(c.getMass() * 10);
		}
		c.incrementBlockTimer();
		// Decrease speed - should instantly stop to avoid abuse of blocking?
		//System.out.println(c.getStamina());
		//System.out.println(c.getDx() + ", " + c.getDy());
		c.setDx(c.getDx() * 0.95 * resources.getMap().getFriction());
		c.setDy(c.getDy() * 0.95 * resources.getMap().getFriction());
		// Stop blocking - revert changes to mass
		if (c.getBlockTimer() >= 25) {
			//System.out.println("DONE BLOCKING");
			//System.out.println("AFTER: " + c.getDx() + ", " + c.getDy());
			c.setBlocking(false);
			c.resetBlockTimer();
			c.setMass(c.getMass() / 10);
		}
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
