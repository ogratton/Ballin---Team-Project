package resources;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;

import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;

public class Character extends Observable implements Collidable {
	private static final double default_mass = 1.0;
	private static final int default_radius = 25;
	private static final double default_max_speed_x = 3;
	private static final double default_max_speed_y = 3;
	private static final double default_acc = 0.1;
	private static final double default_restitution = 0.7; // 'bounciness'

	public enum Heading {
		N, E, S, W, NE, NW, SE, SW, STILL
	};

	// this will have all the Character classes in use.
	public enum Class {
		DEFAULT, WIZARD, ELF, TEST
	}; // add to this as we develop more classes.

	// flags for keys pressed.
	// e.g. if up is true, then the player/ai is holding the up button.
	// jump is currently not being used, but is there if we need it.
	// jump punch and/or block may be replaced by a single 'special' flag,
	// which does an action based on the class of the character.
	// Collided flag added to help with collision calculations (depreciated)
	private boolean up, right, left, down, jump, punch, block, collided, falling, dead, dashing, blocking = false;

	// these are for the physics engine. Restitution is 'bounciness'.
	private double mass, inv_mass, dx, dy, maxdx, maxdy, acc, restitution = 0.0;

	// these are for the physics engine and the graphics engine.
	// Characters are circles.
	// x and y are the coordinates of the centre of the circle, relative to the
	// top-left of the arena.
	// radius is the radius of the circle, in arbitrary units.
	// direction is the direction that the character's facing
	// (this is entirely for graphics)
	private double x, y = 0.0;
	private int radius = 0;
	private Heading direction = Heading.STILL;
	private Class classType = Class.DEFAULT;

	// variables imported from CharacterModel
	private BufferedImage characterSheet;
	private BufferedImage dashSheet;
	private ArrayList<BufferedImage> rollingSprites, directionSprites, dashSprites;
	private int rollingFrame, directionFrame;
	private int dyingStep = 0;
	private boolean moving;
	private boolean visible = true;
	private BufferedImage currentFrame;

	// So we can control how long a character dashes/blocks for
	private int dashTimer, blockTimer = 0;
	// Stamina recharges until this maximum value
	private int maxStamina = 150;
	// 0 is empty
	private int stamina = maxStamina;
	// Stamina used when dashing/blocking
	private int dashStamina = 150;
	private int blockStamina = 75;

	/**
	 * Default character with default sprite
	 */
	public Character() {
		this(default_mass, 0, 0, default_radius, Heading.STILL, Class.DEFAULT);
	}

	/**
	 * Default character with a given class
	 * 
	 * @param c
	 *            the class
	 */
	public Character(Class c) {
		this(default_mass, 0, 0, SheetDeets.getRadiusFromSprite(c), Heading.STILL, c);
	}

	public Character(double mass, double x, double y, int radius, Heading direction, Class classType) {
		this(false, false, false, false, false, false, false, // control flags
				mass, x, // x
				y, // y
				0.0, // speed_x
				0.0, // speed_y
				default_max_speed_x * (1 / mass), default_max_speed_y * (1 / mass), default_acc, // acceleration
																									// (TODO:
																									// calculate
																									// this)
				default_restitution, radius, direction, classType);
	}

	// master constructor. Any other constructors should eventually call this.
	private Character(boolean up, boolean right, boolean left, boolean down, boolean jump, boolean punch, boolean block,
			double mass, double x, double y, double speed_x, double speed_y, double max_speed_x, double max_speed_y,
			double acceleration, double restitution, int radius, Heading direction, Class classType) {
		// new Character();
		this.up = up;
		this.right = right;
		this.left = left;
		this.down = down;
		this.jump = jump;
		this.punch = punch;
		this.block = block;

		this.mass = mass;
		if (mass == 0)
			this.inv_mass = 0; // a mass of 0 makes an object infinitely massive
		else
			this.inv_mass = 1.0 / mass;

		this.x = x;
		this.y = y;
		this.dx = speed_x;
		this.dy = speed_y;
		this.maxdx = max_speed_x;
		this.maxdy = max_speed_y;
		this.acc = acceleration;
		this.restitution = restitution; // bounciness
		this.radius = radius;
		this.direction = direction;
		this.classType = classType;

		// imported from graphics.
		this.characterSheet = SheetDeets.getSpriteSheetFromCharacter(this);
		this.dashSheet = SheetDeets.getMiscSpritesFromType(SheetDeets.Misc.DASH);
		this.moving = false;

		// sprite ArrayLists
		rollingSprites = new ArrayList<BufferedImage>();
		directionSprites = new ArrayList<BufferedImage>();
		dashSprites = new ArrayList<BufferedImage>();

		for (int i = 0; i < SheetDeets.CHARACTERS_COLS; i++) {
			BufferedImage sprite = Sprite.getSprite(characterSheet, i, 0, SheetDeets.CHARACTERS_SIZEX,
					SheetDeets.CHARACTERS_SIZEX);
			rollingSprites.add(sprite);
			directionSprites.add(sprite);
		}

		for (int i = 0; i < SheetDeets.MISC_COLS; i++) {
			BufferedImage sprite = Sprite.getSprite(dashSheet, i, 0, SheetDeets.MISC_SIZEX, SheetDeets.MISC_SIZEY);
			dashSprites.add(sprite);
		}

		this.currentFrame = rollingSprites.get(0);

		rollingFrame = 0;
		directionFrame = 0;
		falling = false;
		dead = false;
	}

	/**
	 * Get the current rolling frame
	 * 
	 * @return the frame
	 */

	public BufferedImage getNextFrame(int oldX, int oldY, int newX, int newY) {

		int delX = newX - oldX;
		int delY = newY - oldY;

		if (isDead()) {
			delY = 5;
			delX = 5;
		}

		if (delX == 0 && delY == 0) {
			return currentFrame;
		}

		Heading h = getVisibleDirection(delX, delY);

		switch (h) {
		case N:
			directionFrame = 0;
			rollingFrame--;
			break;
		case NE:
			directionFrame = 1;
			rollingFrame++;
			break;
		case E:
			directionFrame = 2;
			rollingFrame++;
			break;
		case SE:
			directionFrame = 3;
			rollingFrame++;
			break;
		case S:
			directionFrame = 4;
			rollingFrame++;
			break;
		case SW:
			directionFrame = 5;
			rollingFrame--;
			break;
		case W:
			directionFrame = 6;
			rollingFrame--;
			break;
		case NW:
			directionFrame = 7;
			rollingFrame--;
			break;
		case STILL:
			break;
		}

		switch (classType) {
		case TEST:

			BufferedImage sprite = this.directionSprites.get(directionFrame);
			this.currentFrame = sprite;
			return sprite;

		case ELF:
		case WIZARD:
		case DEFAULT:

			if (rollingFrame == 32)
				rollingFrame = 0;

			if (rollingFrame == -1)
				rollingFrame = 31;
		}

		BufferedImage sprite = this.rollingSprites.get(rollingFrame / 4);
		this.currentFrame = sprite;
		return sprite;
	}

	/**
	 * Get the dash sprite for a character Assumes direction has already been
	 * set
	 * 
	 * @return the dash sprite
	 */

	public BufferedImage getDashSprite() {

		return this.dashSprites.get(directionFrame);

	}

	/**
	 * Given a change in x and y positions, return the direction a character is
	 * visibly moving in This might be identical to another method? I don't know
	 * 
	 * @param delX
	 *            the change in X
	 * @param delY
	 *            the change in Y
	 * @return the direction the character is moving in
	 */

	public Heading getVisibleDirection(int delX, int delY) {

		if (delX > 0) {

			if (delY > 0) {
				direction = Heading.SE;
			} else if (delY < 0) {
				direction = Heading.NE;
			} else {
				direction = Heading.E;
			}
		} else {
			if (delX < 0) {

				if (delY > 0) {
					direction = Heading.SW;
				} else if (delY < 0) {
					direction = Heading.NW;
				} else {
					direction = Heading.W;
				}
			} else

			if (delY > 0) {
				direction = Heading.S;
			} else if (delY < 0) {
				direction = Heading.N;
			}
		}
		return direction;
	}

	/*
	 * Getters and setters for controls: this is mportant for determining which
	 * frame of the sprite to use next
	 */

	/**
	 * Is an up command being received?
	 * 
	 * @return up?
	 */

	public boolean isUp() {
		return this.up;
	}

	/**
	 * Is a down command being received?
	 * 
	 * @return down?
	 */

	public boolean isDown() {
		return this.down;
	}

	/**
	 * Is a left command being received?
	 * 
	 * @return left?
	 */

	public boolean isLeft() {
		return this.left;
	}

	/**
	 * Is a right command being received?
	 * 
	 * @return right?
	 */

	public boolean isRight() {
		return this.right;
	}

	/**
	 * Is the character jumping?
	 * 
	 * @return is the character jumping?
	 */

	public boolean isJump() {
		return this.jump;
	}

	/**
	 * Is the character punching?
	 * 
	 * @return is the character punching?
	 */

	public boolean isPunch() {
		return this.punch;
	}

	/**
	 * Is the character blocking?
	 * 
	 * @return is the character blocking?
	 */

	public boolean isBlock() {
		return this.block;
	}

	/**
	 * Set if an up command is being received
	 * 
	 * @param up
	 *            up?
	 */

	public void setUp(boolean up) {
		this.up = up;
		setDirection();
	}

	/**
	 * Set if a down command is being received
	 * 
	 * @param down
	 *            down?
	 */

	public void setDown(boolean down) {
		this.down = down;
		setDirection();
	}

	/**
	 * Set if a left command is being received
	 * 
	 * @param left
	 *            left?
	 */

	public void setLeft(boolean left) {
		this.left = left;
		setDirection();
	}

	/**
	 * Set if a right command is being received
	 * 
	 * @param right
	 *            right?
	 */

	public void setRight(boolean right) {
		this.right = right;
		setDirection();
	}

	/**
	 * Set if a jump command is being received
	 * 
	 * @param jump
	 *            is a jump command being received?
	 */

	public void setJump(boolean jump) {
		this.jump = jump;

	}

	/**
	 * Set if a punch command is being received
	 * 
	 * @param punch
	 *            is a punch command being received?
	 */

	public void setPunch(boolean punch) {
		this.punch = punch;
	}

	/**
	 * Set if a block command is being received
	 * 
	 * @param block
	 *            is a block command being received?
	 */

	public void setBlock(boolean block) {
		this.block = block;
	}

	/**
	 * Set the direction of the character based on the commands it is currently
	 * receiving
	 */

	private void setDirection() {

		if (isUp()) {
			if (isLeft()) {
				direction = Heading.NW;
			}

			else if (isRight()) {
				direction = Heading.NE;
			}

			else {
				direction = Heading.N;
			}
		} else if (isDown()) {
			if (isLeft()) {
				direction = Heading.SW;
			}

			else if (isRight()) {
				direction = Heading.SE;
			}

			else {
				direction = Heading.S;
			}
		} else if (isLeft()) {
			direction = Character.Heading.W;
		} else if (isRight()) {
			direction = Character.Heading.E;
		}

		if (getDx() != 0 || getDy() != 0) {
			setMoving(true);
		} else {
			setMoving(false);
		}

		setChanged();
		notifyObservers();
	}

	/*
	 * Moving getters and setters: used for knowing when to generate the next
	 * frame of the sprite
	 */

	/**
	 * Set if the character is moving
	 * 
	 * @param moving
	 *            moving?
	 */

	public void setMoving(boolean moving) {

		this.moving = moving;
	}

	/**
	 * Get if the character is moving
	 * 
	 * @return moving?
	 */

	public boolean isMoving() {
		return this.moving;
	}

	/*
	 * Setters and getters for position: these are important for knowing where
	 * to draw the character
	 */

	/**
	 * Get the x coordinate of a character
	 * 
	 * @return the x coordinate
	 */

	public double getX() {
		return this.x;
	}

	/**
	 * Get the y coordinate of a character
	 * 
	 * @return the y coordinate
	 */

	public double getY() {
		return this.y;
	}

	/**
	 * Has the character collided?
	 * 
	 * @return if the character has collided
	 */

	public boolean isCollided() {
		return this.collided;
	}

	/**
	 * Get the facing of this character
	 * 
	 * @return the facing
	 */

	public Character.Heading getDirection() {
		return this.direction;
	}

	/**
	 * Set the x coordinate of the character
	 * 
	 * @param x
	 *            the x coordinate
	 */

	public void setX(double x) {
		this.x = x;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the y coordinate of the character
	 * 
	 * @param y
	 *            the y coordinate
	 */

	public void setY(double y) {

		this.y = y;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set if the character has collided
	 * 
	 * @param collided
	 *            if the character has collided
	 */

	public void setCollided(boolean collided) {
		this.collided = collided;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the facing of the character
	 * 
	 * @param direction
	 */

	public void setDirection(Character.Heading direction) {
		this.direction = direction;
	}

	public void setDirection(int oldX, int oldY, int newX, int newY) {

		int delx = oldX - newX;
		int dely = oldY - newY;

		if (delx > 0) {
			if (dely > 0) {
				direction = Heading.SE;
			} else if (dely < 0) {
				direction = Heading.NE;
			} else {
				direction = Heading.E;
			}
		} else if (delx < 0) {
			if (dely > 0) {
				direction = Heading.SW;
			} else if (dely < 0) {
				direction = Heading.NW;
			} else {
				direction = Heading.W;
			}
		} else if (dely > 0) {
			direction = Heading.S;
		} else if (dely < 0) {
			direction = Heading.N;
		} else {
			direction = Heading.STILL;
		}

	}

	/*
	 * Setters and getters for character physics: may be important later on
	 */

	/**
	 * Get the mass of the character
	 * 
	 * @return the mass
	 */

	public double getMass() {
		return this.mass;
	}

	/**
	 * Get the 'inv mass' of the character
	 * 
	 * @return the inv mass
	 */

	public double getInvMass() {
		return this.inv_mass;
	}

	/**
	 * Get the dx of the character
	 * 
	 * @return the dx
	 */

	public double getDx() {
		return this.dx;
	}

	/**
	 * Get the dy of the character
	 * 
	 * @return
	 */

	public double getDy() {
		return this.dy;
	}

	/**
	 * Get the max dx of the character
	 * 
	 * @return the max dx
	 */

	public double getMaxDx() {
		return this.maxdx;
	}

	/**
	 * Get the max dy of the character
	 * 
	 * @return the max dy
	 */

	public double getMaxDy() {
		return this.maxdy;
	}

	/**
	 * Get the acceleration of the character
	 * 
	 * @return the acceleration
	 */

	public double getAcc() {
		return this.acc;
	}

	/**
	 * Get the restitution of the character
	 * 
	 * @return the restitution
	 */

	public double getRestitution() {
		return this.restitution;
	}

	/**
	 * Get the radius of the character
	 * 
	 * @return the radius
	 */

	public int getRadius() {
		return this.radius;
	}

	/**
	 * Find out if character is set to fall.
	 * 
	 * @return falling
	 */
	public boolean isFalling() {
		return falling;
	}

	/**
	 * find out if character is dead. Dead characters don't update. (for now)
	 * 
	 * @return
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Set the mass of a character
	 * 
	 * @param mass
	 *            the mass
	 */

	public void setMass(double mass) {
		this.mass = mass;
		this.inv_mass = 1 / mass;
	}

	/**
	 * Set the dx of a character
	 * 
	 * @param dx
	 *            the dx
	 */

	public void setDx(double dx) {
		this.dx = dx;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the dy of a character
	 * 
	 * @param dy
	 *            the dy
	 */

	public void setDy(double dy) {
		this.dy = dy;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the max dx of a character
	 * 
	 * @param maxDx
	 *            the max dx
	 */

	public void setMaxDx(double maxDx) {
		this.maxdx = maxDx;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the max dy of a character
	 * 
	 * @param maxDy
	 *            the max dy
	 */

	public void setMaxDy(double maxDy) {
		this.maxdy = maxDy;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the acceleration of a character
	 * 
	 * @param acceleration
	 *            the acceleration
	 */

	public void setAcc(double acceleration) {
		this.acc = acceleration;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the restitution of a character
	 * 
	 * @param restitution
	 *            the restitution
	 */

	public void setRestitution(double restitution) {
		this.restitution = restitution;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the radius of a character
	 * 
	 * @param radius
	 *            the radius
	 */

	public void setRadius(int radius) {
		this.radius = radius;
		setChanged();
		notifyObservers();
	}

	// setters/getters for control flags
	/**
	 * Set all of the control flags at once.
	 * 
	 * @param up
	 * @param down
	 * @param left
	 * @param right
	 * @param jump
	 * @param punch
	 * @param block
	 */
	public void setControls(boolean up, boolean down, boolean left, boolean right, boolean jump, boolean punch,
			boolean block) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.jump = jump;
		this.punch = punch;
		this.block = block;
		setChanged();
		notifyObservers();
	}

	/**
	 * Get the class type of this character.
	 * 
	 * @return
	 */
	public Class getClassType() {
		return this.classType;
	}

	/**
	 * Change whether or not the character is falling.
	 * 
	 * @param falling
	 */
	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	/**
	 * Change whether or not the character is dead.
	 * 
	 * @param dead
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * Change whether or not the character is dashing.
	 * 
	 * @param dashing
	 */
	public void setDashing(boolean dashing) {
		this.dashing = dashing;
	}

	/**
	 * Returns true if the character is dashing.
	 * 
	 * @return dashing
	 */
	public boolean isDashing() {
		return this.dashing;
	}

	/**
	 * Returns the current dash timer for this character.
	 * 
	 * @return dashTimer
	 */
	public int getDashTimer() {
		return dashTimer;
	}

	/**
	 * Increments the dash timer by 1.
	 */
	public void incrementDashTimer() {
		this.dashTimer += 1;
	}

	/**
	 * Resets the dash timer to 0;
	 */
	public void resetDashTimer() {
		this.dashTimer = 0;
	}

	/**
	 * Change whether or not the character is blocking.
	 * 
	 * @param blocking
	 */
	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	/**
	 * Returns true if the character is blocking.
	 * 
	 * @return blocking
	 */
	public boolean isBlocking() {
		return this.blocking;
	}

	/**
	 * Returns the current blocking timer for this character.
	 * 
	 * @return blockTimer
	 */
	public int getBlockTimer() {
		return blockTimer;
	}

	/**
	 * Increments the block timer by 1.
	 */
	public void incrementBlockTimer() {
		this.blockTimer += 1;
	}

	/**
	 * Resets the block timer to 0.
	 */
	public void resetBlockTimer() {
		this.blockTimer = 0;
	}

	/**
	 * Returns the current stamina for this character.
	 * 
	 * @return stamina
	 */
	public int getStamina() {
		return stamina;
	}

	/**
	 * Set the current stamina value.
	 */
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	/**
	 * Increments the character's stamina by 1 if it is less that the maximum
	 * stamina. Used to recharge stamina.
	 */
	public void incrementStamina() {
		if (this.stamina < this.maxStamina) {
			this.stamina += 1;
		}
	}

	/**
	 * Resets the stamina to maximum value.
	 */
	public void resetStamina() {
		this.stamina = maxStamina;
	}

	/**
	 * Sets the maximum stamina value. Can use for giving different classes
	 * different max stams.
	 */
	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}

	/**
	 * Returns the maximum stamina value.
	 * 
	 * @return maxStamina
	 */
	public int getMaxStamina() {
		return maxStamina;
	}

	/**
	 * Gets the amount of stamina that is used when dashing.
	 * 
	 * @return dashStamina
	 */
	public int getDashStamina() {
		return dashStamina;
	}

	/**
	 * Sets a new value for the amount of stamina drained when dashing.
	 */
	public void setDashStamina(int dashStamina) {
		this.dashStamina = dashStamina;
	}

	/**
	 * Gets the amount of stamina that is used when blocking.
	 * 
	 * @return blockStamina
	 */
	public int getBlockStamina() {
		return blockStamina;
	}

	/**
	 * Sets a new value for the amount of stamina drained when blocking.
	 */
	public void setBlockStamina(int blockStamina) {
		this.blockStamina = blockStamina;
	}

	/**
	 * Set the dying step
	 * 
	 * @param step
	 *            the dying step
	 */

	public void setDyingStep(int step) {
		this.dyingStep = step;
	}

	/**
	 * Get the dying step
	 * 
	 * @return the dying step
	 */

	public int getDyingStep() {
		return this.dyingStep;
	}

	/**
	 * Increment the dying step
	 */

	public void incDyingStep() {
		this.dyingStep++;
	}

	/**
	 * Is the player visible?
	 * 
	 * @return is the player visible?
	 */

	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Set the player's visibility
	 * 
	 * @param visible
	 *            the state of visibility
	 */

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
