package physics;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Timer;

import ai.BasicAI;
import audio.AudioFile;
import graphics.sprites.SheetDeets;
import resources.Character;
import resources.Character.Heading;
import resources.Collidable;
import resources.Collidable_Circle;
import resources.Map;
import resources.Map.Tile;
import resources.Map.World;
import resources.Resources.Mode;
import resources.NetworkMove;
import resources.Powerup;
import resources.Puck;
import resources.Resources;
import resources.Wall;

public class Physics extends Thread implements ActionListener {
	//dashing reduces stamina, speed multiplied by stamina.
	private Timer timer;
	private final int DELAY = 10;
	private Resources resources;
	
	private AudioFile boing;
	private AudioFile death;
	
	private boolean client = false;
	
	public Physics(Resources resources, boolean client){
		this.resources = resources;
		this.client = client;
		if(!Resources.silent) {
			boing = new AudioFile(resources, "resources/audio/boing.wav", "Boing");
			death = new AudioFile(resources, "resources/audio/death.wav", "Death");
		}
	}
	
	@Override
	public void run() {
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
		resources.incrementGlobalTimer();
		
		// if hockey, move puck.
		if(resources.mode == Mode.Hockey) update(resources.getPuck());
		
		for(Character c : resources.getPlayerList()){
//			if (c.getPlayerNumber() == 1) {
//				System.out.println("hp: " + c.getHealth());
//			}
			update(c);
			for (Character d : resources.getPlayerList()) {
				// check collisions
				if (c != d && !c.isDead() && !d.isDead()) {
					CND cnd = detectCollision(c,d);
					if(cnd.collided) {
						collide(c,d,cnd);
						// If playing hot potato, pass bomb if you have it
						if (c.hasBomb() && resources.mode == Mode.HotPotato) {
							c.hasBomb(false);
							d.hasBomb(true);
//							System.out.println("Player " + c.getPlayerNumber() + " has passed the bomb to player " 
//									+ d.getPlayerNumber() + "!");
						}
					}
				}
			}
			// Check collisions with powerups
			for (Powerup p : resources.getPowerupList()) {
				CND cnd = detectCollision(c,p);
				if (cnd.collided && p.isActive()) {
					// Grant power to character, remove powerup
					c.applyPowerup(p, resources.getGlobalTimer());
					resources.removePowerup(p);
				}
			}
			if (resources.mode == Mode.Hockey) {
				Puck p = resources.getPuck();
				CND cnd = detectCollision(c,p);
				if(cnd.collided) {
					collide(c,p,cnd);
				}
			}
			
			// for networking
			if(client) {
				NetworkMove m = new NetworkMove();
				m.x = c.getX();
				m.y = c.getY();
				m.t = new Date();
				m.isBlocking = c.isBlocking();
				m.isDashing = c.isDashing();
				m.isDead = c.isDead();
				m.isFalling = c.isFalling();
				m.id = resources.getId();
				
				if(resources.getClientMoves().size() < 1) {
					resources.getClientMoves().add(m);
				}
				else {
					if(!(resources.getClientMoves().peekLast().x == m.x && resources.getClientMoves().peekLast().y == m.y)) {
						resources.getClientMoves().add(m);
					}
				}
			}
		}
	}

	/**
	 * calculate speed and location of a character. Assumes no collisions.
	 * @param c
	 */
	@SuppressWarnings("deprecation")
	private void update(Character c) {
		// if dead, don't do anything (yet):
		if(c.isDead() && c.getLives() != 0) {
			if(c.getDyingStep() >= 50) { //the last dyingStep is 50
				c.decrementLives();
				if(!client) resources.getMap().spawn(c);
				if (c.isAI()) {
					BasicAI ai = new BasicAI(resources, c);
					c.setAI(ai);
					ai.start();
					
				}
			}
		}
		
		// find terrain type:
		Tile t = resources.getMap().tileAt(c.getX(), c.getY());
		//check for falling.
		if (Map.tileCheck(t)) {
			if (resources.getMap().getWorldType() == World.LAVA) {
				c.setBurning(true);
				if (c.getHealth() <= 0) {
					c.setFalling(true);
				}
			} else {
				c.setFalling(true);
			}
		} else {
			c.setBurning(false);
		}
		if (c.getBurning() == true) {
			c.decrementHealth();
		}
		// Powerup timer, remove powerup after 5 secs
		if (c.hasPowerup() && resources.getGlobalTimer() - c.getLastPowerupTime() >= 500) {
			c.revertPowerup();
		}
		// Recharge stamina
		c.incrementStamina();
		// If a special button has been pressed, perform the ability if possible
		if (special(c) && !c.isFalling()){
			return;
		}
		if(!c.isFalling()) { //moving
			// check for wall collisions:
			calculateWallCollisions(c);
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
			double f = resources.getMap().getFriction();
			if (!c.isLeft() && !c.isRight()) {
				if (c.getDx() < -f ) {
					c.setDx(c.getDx() + f);
				} else if (c.getDx() > f) {
					c.setDx(c.getDx() - f);
				} else {
					c.setDx(0);
				}
			}
			if (!c.isUp() && !c.isDown()) {
				if (c.getDy() < -f ) {
					c.setDy(c.getDy() + f);
				} else if (c.getDy() > f) {
					c.setDy(c.getDy() - f);
				} else {
					c.setDy(0);
				}
			}
		} else { //falling
			if(dead(c) && !c.isDead()) {
				c.setDead(true);
				c.setTimeOfDeath(resources.getGlobalTimer());
				// XXX lovely sound effect
				if(!Resources.silent) death.play();
				// Calculate score changes
				System.out.println("Player " + c.getPlayerNumber() + " died!");
				Character lastCollidedWith = c.getLastCollidedWith();
				// If c has collided with someone else in the last 5 seconds
				if (lastCollidedWith != null && resources.getGlobalTimer() - c.getLastCollidedTime() <= 500) {
					// give 1 point to whoever they collided with
					lastCollidedWith.incrementScore(1);
					c.incrementScore(-1);
					System.out.println("Credit goes to player " + lastCollidedWith.getPlayerNumber() + "! +1 point");
					lastCollidedWith.incrementKills();
					c.incrementDeaths();

					
				} else {
					// take 2 points away from c
					System.out.println("Player " + c.getPlayerNumber() + " killed themself... -2 points");
					c.incrementScore(-2);
					c.incrementSuicides();
				}
				c.setLastCollidedWith(null, 0);
				if (c.hasPowerup()) {
					c.revertPowerup();
				}
				
				resources.setScoreChanged(true);
			}
		}
		//System.out.println("Got here");
		move(c);
	}
	
	/**
	 * Calculate speed and location of collidable circles.
	 * @param c
	 */
	private void update(Collidable_Circle c) {
		// find terrain type:
		Tile t = resources.getMap().tileAt(c.getX(),c.getY());
		//check for falling.
		if(Map.tileCheck(t)) {
			c.setFalling(true);
			if(!c.isDead() && dead(c)) {
				//if (c.getType() == CollidableType.Puck) {...}
				// Goal is scored
				Character lastCollidedWith = c.getLastCollidedWith();
				System.out.println("Player " + lastCollidedWith.getPlayerNumber() + " scored!");
				// Find which goal was scored in
				// If own goal, -2 points to player, otherwise +1 point
				// +1 point to team who scored
			}
		}
		move(c);
	}
	
	private boolean dead(Collidable_Circle c) {
		if(!c.isDead()) {//if stationary, give speed:
			if(Math.abs(c.getDx()) < 0.00001) {
				c.setDx(0.00001);
			}
			if(Math.abs(c.getDy()) < 0.00001) {
				c.setDy(0.00001);
			}
			//if too slow, speed up:
			if(Math.abs(c.getDx()) < 0.5) {
				c.setDx(c.getDx() * 1.1);
			}
			if(Math.abs(c.getDy()) < 0.5) {
				c.setDy(c.getDy() * 1.1);
			}
		}
		//dead if completely off the map.
		boolean dead = true;
		Tile t2 = resources.getMap().tileAt(c.getX() + c.getRadius(), c.getY());
		if(!Map.tileCheck(t2)) { // right edge
			dead = false;
			c.setDx(0 - Math.abs(c.getDx()));
		}
		t2 = resources.getMap().tileAt(c.getX() - c.getRadius(), c.getY());
		if(!Map.tileCheck(t2)) { // left edge
			dead = false;
			c.setDx(Math.abs(c.getDx()));
		}			
		t2 = resources.getMap().tileAt(c.getX(), c.getY() + c.getRadius());
		if(!Map.tileCheck(t2)) { // bottom edge
			dead = false;
			c.setDy(0 - Math.abs(c.getDy()));
		}			
		t2 = resources.getMap().tileAt(c.getX(), c.getY() - c.getRadius());
		if(!Map.tileCheck(t2)) { // top edge
			dead = false;
			c.setDy(Math.abs(c.getDy()));
		}
		return dead;
	}
	
	private void calculateWallCollisions(Collidable_Circle c) {
		// Checks walls, if collided then collides.
		CND cnd = null;
		Heading corner = Heading.STILL; // No corners
		Tile t2 = resources.getMap().tileAt(c.getX() + c.getRadius(), c.getY() - c.getRadius());
		if(t2 == Tile.WALL) {//wall is north east of char (bottom-left corner of the wall)
			Point wallCoords = resources.getMap().tileCoords(c.getX() + c.getRadius(), c.getY() - c.getRadius());
			cnd = detectCollision(c, wallCoords);
			if(cnd.collided) {
				collide(c, Wall.wall, cnd);
			}
		}
		t2 = resources.getMap().tileAt(c.getX() - c.getRadius(), c.getY() - c.getRadius());
		if(t2 == Tile.WALL) {///wall is north west of char (bottom-right corner of the wall)
			Point wallCoords = resources.getMap().tileCoords(c.getX() - c.getRadius(), c.getY() - c.getRadius());
			cnd = detectCollision(c, wallCoords);
			if(cnd.collided) {
				collide(c, Wall.wall, cnd);
			}
		}
		t2 = resources.getMap().tileAt(c.getX() + c.getRadius(), c.getY() + c.getRadius());
		if(t2 == Tile.WALL) {///wall is south east of char (top-left corner of the wall)
			Point wallCoords = resources.getMap().tileCoords(c.getX() + c.getRadius(), c.getY() + c.getRadius());
			cnd = detectCollision(c, wallCoords);
			if(cnd.collided) {
				collide(c, Wall.wall, cnd);
			}
		}
		t2 = resources.getMap().tileAt(c.getX() - c.getRadius(), c.getY() + c.getRadius());
		if(t2 == Tile.WALL) {///wall is south west of char (top-right corner of the wall)
			Point wallCoords = resources.getMap().tileCoords(c.getX() - c.getRadius(), c.getY() + c.getRadius());
			cnd = detectCollision(c, wallCoords);
			if(cnd.collided) {
				collide(c, Wall.wall, cnd);
			}
		}
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
	
	private void move(Collidable_Circle c) {
		//calculate location
		double x = c.getX() + c.getDx();
		double y = c.getY() + c.getDy();
		
		c.setX(x);
		c.setY(y);
	}

	/**
	 * General collision of two collidable object.
	 * @param c
	 * @param d
	 * @param cnd
	 */
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
		
		// XXX play SFX
		if(!Resources.silent) boing.play();
		
	}
	
	/**
	 * Collide two characters.
	 * @param c
	 * @param d
	 * @param cnd
	 */
	private void collide(Character c, Character d, CND cnd) {
		collide((Collidable)c,(Collidable)d, cnd);
		// For scores (would be nice to somehow do something based on whether
		// the collidable c/d is a collidable or a character)
		int time = resources.getGlobalTimer();
		c.setLastCollidedWith(d, time);
		d.setLastCollidedWith(c, time);
	}

//	void PositionalCorrection( Object A, Object B )
//	{ // copied from an example; needs adapting.
//	  const float percent = 0.2 // usually 20% to 80%
//	  const float slop = 0.01 // usually 0.01 to 0.1
//	  Vec2 correction = max( penetration - k_slop, 0.0f ) / (A.inv_mass + B.inv_mass)) * percent * n
//	  A.position -= A.inv_mass * correction
//	  B.position += B.inv_mass * correction
//	}
	
	private CND detectCollision(Collidable_Circle c, Collidable_Circle d) {
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

	/**
	 * detects collisions between character and wall
	 * @param c
	 * @param topLeft coords of top-left corner of wall
	 * @return collision variable.
	 */
	private CND detectCollision(Collidable_Circle c, Point tileCoords) {
		// tileCoords is the coordinate of the tile in the tile coordinate system
		
		CND cnd = detectCollision(c, new Wall(resources.getMap().tileCoordsToMapCoords(tileCoords.x, tileCoords.y)));
		
		return cnd;
	}
	
	private Point lineSegmentIntersection(Point p1, Point p2, Point q1, Point q2) {
		Point intersection = new Point(p1);
		int dx1 = p2.x - p1.x;
		int dy1 = p2.y - p1.y;
		int dx2 = q2.x - q1.x;
		int dy2 = q2.y - q1.y;
		double s = (-dy1 * (p1.x - q1.x) + dx1 * (p1.y - q1.y)) / (-dx2 * dy1 + dx1 * dy2);
		double t = ( dx2 * (p1.y - q1.y) - dy2 * (p1.x - q1.x)) / (-dx2 * dy1 + dx1 * dy2);
		if(s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			intersection.translate((int)(t*dx1), (int)(t*dy1));
		}
		return intersection;
	}
	
	private void dash(Character c) {
		c.setBlocking(false);
		// Dash in the direction the player is trying to move
		if (c.getDashTimer() == 0) {
			double maxSpeed = 2 * c.getMaxDx();
			//System.out.println("Direction dashing: " + c.getMovingDirection());
			switch(c.getMovingDirection()) {
			case N:
				c.setDx(0);
				c.setDy(-maxSpeed);
				break;
			case NE:
				c.setDx(maxSpeed);
				c.setDy(-maxSpeed);
				break;
			case E:
				c.setDx(maxSpeed);
				c.setDy(0);
				break;
			case SE:
				c.setDx(maxSpeed);
				c.setDy(maxSpeed);
				break;
			case S:
				c.setDx(0);
				c.setDy(maxSpeed);
				break;
			case SW:
				c.setDx(-maxSpeed);
				c.setDy(maxSpeed);
				break;
			case W:
				c.setDx(-maxSpeed);
				c.setDy(0);
				break;
			case NW:
				c.setDx(-maxSpeed);
				c.setDy(-maxSpeed);
				break;
			case STILL:
				// Dash in direction moving, if you're moving
				double maxDxDy = Math.max(Math.abs(c.getDx()), Math.abs(c.getDy()));
				// Can't dash if standing still
				if (maxDxDy == 0) {
					c.setDashing(false);
					c.resetDashTimer();
					// Refund stamina
					c.setStamina(c.getStamina() + c.getDashStamina());
					return;
				}
				double velInc = (2 * c.getMaxDx()) / maxDxDy;
				c.setDx(c.getDx() * velInc);
				c.setDy(c.getDy() * velInc);
			}
		}
		c.incrementDashTimer();
		if (c.getDashTimer() >= 25) {
			c.setDx(c.getDx() / 2);
			c.setDy(c.getDy() / 2);
			c.setDashing(false);
			c.resetDashTimer();
		}
	}
	
	private void block(Character c) {
		c.setDashing(false);
		// Start blocking - increase mass
		if (c.getBlockTimer() == 0) {
			c.setMass(c.getMass() * 10);
		}
		c.incrementBlockTimer();
		// Decrease speed - should instantly stop to avoid abuse of blocking?
		c.setDx(c.getDx() * 0.9);
		c.setDy(c.getDy() * 0.9);
		// Stop blocking - revert changes to mass
		if (c.getBlockTimer() >= 25) {
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
