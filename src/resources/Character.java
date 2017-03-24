package resources;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.UUID;

import ai.AITemplate;
import audio.AudioFile;
import graphics.sprites.SheetDetails;
import graphics.sprites.Sprite;
import resources.Powerup.Power;
import ui.UIRes;

/**
 * @author Alex & Luke
 *
 */
public class Character extends Observable implements Collidable_Circle {
	// Changing these will break some character JUnit tests
	private static final double default_mass = 1.0;
	private static final int default_radius = 25;
	private static final double default_max_speed_x = 2.75;
	private static final double default_max_speed_y = 2.75;
	private static final double default_acc = 0.1;
	private static final double default_restitution = 0.7; // 'bounciness'
	private static final double blue_mass_mult = 50;
	private static final double red_speed_mult = 2;
	private static final double spike_rest_mult = 4.0;
	private static final Random r = new Random();

	public enum Heading {
		N, E, S, W, NE, NW, SE, SW, STILL
	};

	// this will have all the Character classes in use.
	public enum Class {
		WIZARD, ARCHER, WARRIOR, MONK, WITCH, HORSE;

		/**
		 * Get a random class
		 * 
		 * @return the random class
		 */

		public static Class getRandomClass() {

			int x = r.nextInt(6);

			switch (x) {
			case 0:
				return Class.WIZARD;
			case 1:
				return Class.ARCHER;
			case 2:
				return Class.WARRIOR;
			case 3:
				return Class.MONK;
			case 4:
				return Class.WITCH;
			case 5:
				return Class.HORSE;
			default:
				return Class.WIZARD;
			}

		}

	}; // add to this as we develop more classes.

	private boolean isAI = false;
	private AITemplate ai;

	// flags for keys pressed.
	// e.g. if up is true, then the player/ai is holding the up button.
	// jump is currently not being used, but is there if we need it.
	// jump punch and/or block may be replaced by a single 'special' flag,
	// which does an action based on the class of the character.
	// Collided flag added to help with collision calculations (depreciated)
	// moveFlags moves ...
	private boolean up = false, right = false, left = false, down = false;
	// state flags
	private boolean falling = false, dead = false, dashing = false, blocking = false;
	private int lives = 4;

	// these are for the physics engine. Restitution is 'bounciness'.
	private double mass = 0.0, inv_mass = 0.0, dx = 0.0, dy = 0.0, maxdx = 0.0, maxdy = 0.0, acc = 0.0,
			restitution = 0.0;

	// these are for the physics engine and the graphics engine.
	// Characters are circles.
	// x and y are the coordinates of the centre of the circle, relative to the
	// top-left of the arena.
	// radius is the radius of the circle, in arbitrary units.
	// direction is the direction that the character's facing
	// (this is entirely for graphics)
	private double x = 0.0, y = 0.0;
	private int radius = 0;
	private Heading direction = Heading.STILL;
	private Class classType = Class.WIZARD;

	// variables imported from CharacterModel
	private BufferedImage characterSheet;
	private BufferedImage dashSheet;
	private ArrayList<BufferedImage> rollingSprites, dashSprites, bigRollingSprites, bigDashSprites;
	private int rollingFrame, directionFrame;
	private int dyingStep = 0;
	private boolean moving;
	private String id;
	private int playerNo; // 0 means cpu
	private boolean visible = true;
	private BufferedImage currentFrame;
	private BufferedImage arrow;
	private BufferedImage bigArrow;
	private BufferedImage arrowMe;
	private BufferedImage bigArrowMe;
	private BufferedImage spikes;
	private BufferedImage bigSpikes;
	private Heading dashDirection = Heading.STILL;

	// So we can control how long a character dashes/blocks for
	private int dashTimer, blockTimer = 0;
	// Stamina recharges until this maximum value
	private int maxStamina = 150;
	// 0 is empty
	private int stamina = maxStamina;
	// Stamina used when dashing/blocking
	private int dashStamina = 90;
	private int blockStamina = 75;

	// Store this character's score
	private int score = 0;
	// Who this character last collided with
	private Character lastCollidedWith = null;
	// What time this character last collided
	private int lastCollidedTime = -1;

	private CollidableType type = CollidableType.Character;
	private Power lastPowerup;
	private int lastPowerupTime;
	private boolean hasPowerup = false;
	private boolean hasBomb = false; // holding bomb in hot potato
	private boolean exploding = false; // bomb has just exploded
	private int timeOfDeath = -1;
	private int kills = 0;
	private int deaths = 0;
	private int suicides = 0;

	private int teamNumber;

	private int requestId;
	
	private boolean isGameOver;

	private int dashCooldown = 0;

	private String name;

	private AudioFile playerOutSound;
	private AudioFile[] deathSounds;
	private Random rand = new Random();

	/**
	 * @return The current request id.
	 */
	public int getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId
	 *            The new request id.
	 */
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	/**
	 * Default character with default sprite
	 */
	public Character() {
		this(default_mass, 0, 0, default_radius, Heading.STILL, Class.WIZARD, 0, "Player");
	}

	/**
	 * Default character with a given class
	 * 
	 * @param c
	 *            the class
	 */
	public Character(Class c) {
		this(default_mass, 0, 0, SheetDetails.getRadiusFromSprite(c), Heading.STILL, c, 0, "Player");
	}

	/**
	 * Default character with a given class and player number
	 * 
	 * @param c
	 *            the class
	 * @param playerNo
	 *            the player number
	 */
	public Character(Class c, int playerNo) {
		this(default_mass, 0, 0, SheetDetails.getRadiusFromSprite(c), Heading.STILL, c, playerNo, "Player");
	}

	public Character(Class c, int playerNo, String name) {
		this(default_mass, 0, 0, SheetDetails.getRadiusFromSprite(c), Heading.STILL, c, playerNo, name);
	}

	public Character(double mass, double x, double y, int radius, Heading direction, Class classType, int playerNo, String name) {
		this(mass,
				x, // x
				y, // y
				default_max_speed_x * (1 / mass), 
				default_max_speed_y * (1 / mass), 
				default_acc, // acceleration (TODO: calculate this)
				default_restitution, 
				radius, 
				direction, 
				classType, 
				playerNo, 
				name);

		// XXX set temp String for single player
		// overwritten by networking
		this.id = UUID.randomUUID().toString();
	}

	// master constructor. Any other constructors should eventually call this.
	private Character(double mass, double x, double y, double max_speed_x, double max_speed_y,
			double acceleration, double restitution, int radius, Heading direction, Class classType, int playerNo,
			String name) {
		// new Character();

		this.mass = mass;
		if (mass == 0)
			this.inv_mass = 0; // a mass of 0 makes an object infinitely massive
		else
			this.inv_mass = 1.0 / mass;

		this.x = x;
		this.y = y;
		this.maxdx = max_speed_x;
		this.maxdy = max_speed_y;
		this.acc = acceleration;
		this.restitution = restitution; // bounciness
		this.radius = radius;
		this.direction = direction;
		this.classType = classType;
		this.playerNo = playerNo;

		// imported from graphics.
		this.characterSheet = SheetDetails.getSpriteSheetFromCharacter(this);
		this.dashSheet = SheetDetails.getMiscSpritesFromType(SheetDetails.Misc.DASH);
		this.moving = false;

		// sprite ArrayLists
		rollingSprites = new ArrayList<BufferedImage>();
		dashSprites = new ArrayList<BufferedImage>();
		arrow = Sprite.getSprite(SheetDetails.getArrowFromPlayer(playerNo), 0, 0, 50, 50);
		arrowMe = Sprite.getSprite(SheetDetails.getArrowFromPlayer(playerNo), 0, 1, 50, 50);
		spikes = SheetDetails.SPIKES;

		for (int i = 0; i < SheetDetails.CHARACTERS_COLS; i++) {
			BufferedImage sprite = Sprite.getSprite(characterSheet, i, 0, SheetDetails.CHARACTERS_SIZEX,
					SheetDetails.CHARACTERS_SIZEX);
			rollingSprites.add(sprite);
		}

		for (int i = 0; i < SheetDetails.MISC_COLS; i++) {
			BufferedImage sprite = Sprite.getSprite(dashSheet, i, 0, SheetDetails.MISC_SIZEX, SheetDetails.MISC_SIZEY);
			dashSprites.add(sprite);
		}

		this.currentFrame = rollingSprites.get(0);

		rollingFrame = 0;
		directionFrame = 0;
		falling = false;
		dead = false;
		this.name = name;

		if (!Resources.silent) {
			deathSounds = new AudioFile[3];
			deathSounds[0] = UIRes.deathSound1;
			deathSounds[1] = UIRes.deathSound2;
			deathSounds[2] = UIRes.deathSound3;

			playerOutSound = UIRes.playerOut;
		}
	}

	/**
	 * Note: this object has not been passed resources, therefore when played
	 * gain must be set in the play method (i.e. play(resources.getSFXGain())
	 * 
	 * Also note: should never be called when Resources.silent
	 * 
	 * @return a random death sound AudioFile object
	 */
	public AudioFile getRandDeathSound() {
//		System.out.println(lives);

		// XXX I am making the assumption that lives are decremented before this
		// is called
		// If they have lives left
		if (lives != 1) {
			int index = rand.nextInt(deathSounds.length);
			return deathSounds[index];
		} else {
			return playerOutSound;
		}

	}

	/**
	 * @return The current id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The new id.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the current rolling frame.
	 * 
	 * @return The current rolling frame.
	 */
	public BufferedImage getNextFrame(int oldX, int oldY, int newX, int newY, boolean fullscreen) {

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

		if (rollingFrame == 32)
			rollingFrame = 0;

		if (rollingFrame == -1)
			rollingFrame = 31;

		BufferedImage sprite = null;

		if (fullscreen) {
			sprite = this.bigRollingSprites.get(rollingFrame / 4);
		} else {
			sprite = this.rollingSprites.get(rollingFrame / 4);
		}

		this.currentFrame = sprite;
		return sprite;
	}

	/**
	 * Get the dash sprite for a character Assumes direction has already been
	 * set.
	 * 
	 * @return The dash sprite.
	 */
	public BufferedImage getDashSprite(boolean fullscreen, Heading direction) {

		int frame = 0;

		switch (direction) {
		case E:
			frame = 2;
			break;
		case N:
			frame = 0;
			break;
		case NE:
			frame = 1;
			break;
		case NW:
			frame = 7;
			break;
		case S:
			frame = 4;
			break;
		case SE:
			frame = 3;
			break;
		case SW:
			frame = 5;
			break;
		case W:
			frame = 6;
			break;
		case STILL:
		default:
			break;

		}
		if (fullscreen) {
			return this.bigDashSprites.get(frame);
		}

		return this.dashSprites.get(frame);

	}

	/**
	 * Given a change in x and y positions, return the direction a character is
	 * visibly moving in This might be identical to another method? I don't know
	 * 
	 * @param delX
	 *            the change in X
	 * @param delY
	 *            the change in Y
	 * @return The direction the character is moving in.
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
	 * Getters and setters for controls: this is important for determining which
	 * frame of the sprite to use next
	 */

	/**
	 * Is an up command being received?
	 * 
	 * @return True if the player is pressing up.
	 */
	public boolean isUp() {
		return this.up;
	}

	/**
	 * Is a down command being received?
	 * 
	 * @return True if the player is pressing down.
	 */
	public boolean isDown() {
		return this.down;
	}

	/**
	 * Is a left command being received?
	 * 
	 * @return True if the player is pressing left.
	 */

	public boolean isLeft() {
		return this.left;
	}

	/**
	 * Is a right command being received?
	 * 
	 * @return True if the player is pressing right.
	 */

	public boolean isRight() {
		return this.right;
	}

	/**
	 * Set if an up command is being received
	 * 
	 * @param up
	 *            Is the player pressing up?
	 */

	public void setUp(boolean up) {
		this.up = up;
		setDirection();
	}

	/**
	 * Set if a down command is being received
	 * 
	 * @param down
	 *            Is the player pressing down?
	 */

	public void setDown(boolean down) {
		this.down = down;
		setDirection();
	}

	/**
	 * Set if a left command is being received
	 * 
	 * @param left
	 *            Is the player pressing left?
	 */

	public void setLeft(boolean left) {
		this.left = left;
		setDirection();
	}

	/**
	 * Set if a right command is being received
	 * 
	 * @param right
	 *            Is the player pressing right?
	 */

	public void setRight(boolean right) {
		this.right = right;
		setDirection();
	}

	/**
	 * Set the direction of the character based on the commands it is currently
	 * receiving. (public for JUnit)
	 */
	public void setDirection() {

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

	/**
	 * Get the direction that the character is accelerating towards. (e.g.
	 * holding up and right = NE)
	 */
	/**
	 * Get the direction that the character is accelerating towards (e.g.
	 * holding up and right = NE).
	 * 
	 * @return The direction the character is accelerating towards.
	 */
	public Heading getMovingDirection() {

		if (isUp()) {
			if (isLeft()) {
				return Heading.NW;
			}

			else if (isRight()) {
				return Heading.NE;
			}

			else {
				return Heading.N;
			}
		} else if (isDown()) {
			if (isLeft()) {
				return Heading.SW;
			}

			else if (isRight()) {
				return Heading.SE;
			}

			else {
				return Heading.S;
			}
		} else if (isLeft()) {
			return Heading.W;
		} else if (isRight()) {
			return Heading.E;
		}
		return Heading.STILL;
	}

	/*
	 * Moving getters and setters: used for knowing when to generate the next
	 * frame of the sprite
	 */

	/**
	 * Set if the character is moving.
	 * 
	 * @param moving
	 *            Is the character moving?
	 */
	public void setMoving(boolean moving) {

		this.moving = moving;
	}

	/**
	 * Get if the character is moving
	 * 
	 * @return Is the character moving?
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
	 * Get the facing of this character
	 * 
	 * @return The direction the character is moving.
	 */
	public Character.Heading getDirection() {
		return this.direction;
	}

	/**
	 * Decrements the lives counter.
	 */
	public void decrementLives() {
		lives--;
	}

	/**
	 * Sets number of lives.
	 * 
	 * @param lives
	 *            The new number of lives.
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * get the number of lives for this character.
	 * 
	 * @return The current number of lives.
	 */
	public int getLives() {
		return lives;
	}

	/**
	 * Set the x coordinate of the character
	 * 
	 * @param x
	 *            The new x coordinate.
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
	 *            The new y coordinate.
	 */

	public void setY(double y) {
		this.y = y;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the x coordinate of the character
	 * 
	 * @param x
	 *            The new x coordinate.
	 */

	public void setXWithoutNotifying(double x) {
		this.x = x;
	}

	/**
	 * Set the y coordinate of the character
	 * 
	 * @param y
	 *            The new y coordinate.
	 */

	public void setYWithoutNotifying(double y) {
		this.y = y;
	}

	/**
	 * Set the facing of the character
	 * 
	 * @param direction
	 *            The new direction.
	 */
	public void setDirection(Character.Heading direction) {
		this.direction = direction;
	}

	/**
	 * Calculate the direction the character is moving.
	 * 
	 * @param oldX
	 * @param oldY
	 * @param newX
	 * @param newY
	 */
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
	 * @return The character's mass.
	 */
	public double getMass() {
		return this.mass;
	}

	/**
	 * Get the 'inv mass' of the character
	 * 
	 * @return The inverse mass of the character.
	 */
	public double getInvMass() {
		return this.inv_mass;
	}

	/**
	 * Get the dx of the character
	 * 
	 * @return The current x velocity.
	 */
	public double getDx() {
		return this.dx;
	}

	/**
	 * Get the dy of the character
	 * 
	 * @return The current y velocity.
	 */
	public double getDy() {
		return this.dy;
	}

	/**
	 * Get the max dx of the character
	 * 
	 * @return The maximum x velocity.
	 */
	public double getMaxDx() {
		return this.maxdx;
	}

	/**
	 * Get the max dy of the character
	 * 
	 * @return The maximum y velocity.
	 */
	public double getMaxDy() {
		return this.maxdy;
	}

	/**
	 * Get the acceleration of the character
	 * 
	 * @return The current acceleration.
	 */
	public double getAcc() {
		return this.acc;
	}

	/**
	 * Get the restitution of the character
	 * 
	 * @return The character's restitution.
	 */
	public double getRestitution() {
		return this.restitution;
	}

	/**
	 * Get the radius of the character
	 * 
	 * @return The character's radius.
	 */
	public int getRadius() {
		return this.radius;
	}

	/**
	 * Find out if character is set to fall.
	 * 
	 * @return True if the character is falling.
	 */
	public boolean isFalling() {
		return falling;
	}

	/**
	 * find out if character is dead. Dead characters don't update. (for now)
	 * 
	 * @return True if the character is dead.
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Set the mass of a character
	 * 
	 * @param mass
	 *            The new mass.
	 */
	public void setMass(double mass) {
		this.mass = mass;
		this.inv_mass = 1 / mass;
	}

	/**
	 * Set the dx of a character
	 * 
	 * @param dx
	 *            The new x velocity.
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
	 *            The new y velocity.
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
	 *            The new maximum x velocity.
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
	 *            The new maximum y velocity.
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
	 *            The new acceleration.
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
	 *            The new restitution.
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
	 *            The new radius.
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		setChanged();
		notifyObservers();
	}

	// setters/getters for control flags
	/**
	 * Deprecated? Set all of the control flags at once.
	 * 
	 * @param up
	 * @param down
	 * @param left
	 * @param right
	 * @param jump
	 * @param punch
	 * @param block
	 */
	public void setControls(boolean up, boolean down, boolean left, boolean right, boolean blocking, boolean dashing) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.blocking = blocking;
		this.dashing = dashing;
		setChanged();
		notifyObservers();
	}

	/**
	 * Get the class type of this character.
	 * 
	 * @return The class type of this character.
	 */
	public Class getClassType() {
		return this.classType;
	}

	/**
	 * Set the class type of this character.
	 * 
	 * @param c
	 *            The new class type for this character.
	 */
	public void setClassType(Class c) {
		this.classType = c;
	}

	/**
	 * Change whether or not the character is falling.
	 * 
	 * @param falling
	 *            Is the character falling?
	 */
	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	/**
	 * Change whether or not the character is dead.
	 * 
	 * @param dead
	 *            Is the character dead?
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * Change whether or not the character is dashing.
	 * 
	 * @param dashing
	 *            Is the character dashing?
	 */
	public void setDashing(boolean dashing) {
		this.dashing = dashing;
	}

	/**
	 * Returns true if the character is dashing.
	 * 
	 * @return Is the character dashing?
	 */
	public boolean isDashing() {
		return this.dashing;
	}

	/**
	 * Set the direction the character is dashing.
	 * 
	 * @param direction
	 *            The direction the character is dashing.
	 */
	public void setDashDirection(Heading direction) {
		this.dashDirection = direction;
	}

	/**
	 * Get the direction the character is dashing.
	 * 
	 * @return The direction the character is dashing.
	 */
	public Heading getDashDirection() {
		return this.dashDirection;
	}

	/**
	 * Returns the current dash timer for this character. The dash timer stores
	 * how long (number of ticks) the character has been dashing for.
	 * 
	 * @return The current dash timer.
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
	 * Resets the dash timer to 0.
	 */
	public void resetDashTimer() {
		this.dashTimer = 0;
	}

	/**
	 * Change whether or not the character is blocking.
	 * 
	 * @param blocking
	 *            Is the character blocking?
	 */
	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	/**
	 * Returns true if the character is blocking.
	 * 
	 * @return Is the character blocking?
	 */
	public boolean isBlocking() {
		return this.blocking;
	}

	/**
	 * Returns the current blocking timer for this character. The block timer
	 * stores how long (number of ticks) the character has been blocking for.
	 * 
	 * @return The current block timer.
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
	 * @return The current stamina.
	 */
	public int getStamina() {
		return stamina;
	}

	/**
	 * Set the current stamina value.
	 * 
	 * @param stamina
	 *            The new stamina value.
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
	 * Sets the maximum stamina value.
	 * 
	 * @param maxStamina
	 *            The new maximum stamina value.
	 */
	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}

	/**
	 * Returns the maximum stamina value.
	 * 
	 * @return The current maximum stamina value.
	 */
	public int getMaxStamina() {
		return maxStamina;
	}

	/**
	 * Gets the amount of stamina that is used when dashing.
	 * 
	 * @return The current dash stamina.
	 */
	public int getDashStamina() {
		return dashStamina;
	}

	/**
	 * Sets a new value for the amount of stamina drained when dashing.
	 * 
	 * @param dashStamina
	 *            The new dash stamina.
	 */
	public void setDashStamina(int dashStamina) {
		this.dashStamina = dashStamina;
	}

	/**
	 * Gets the amount of stamina that is used when blocking.
	 * 
	 * @return The current block stamina.
	 */
	public int getBlockStamina() {
		return blockStamina;
	}

	/**
	 * Sets a new value for the amount of stamina drained when blocking.
	 * 
	 * @param blockStamina
	 *            The new block stamina.
	 */
	public void setBlockStamina(int blockStamina) {
		this.blockStamina = blockStamina;
	}

	/**
	 * Set the dying step
	 * 
	 * @param step
	 *            The new dying step.
	 */
	public void setDyingStep(int step) {
		this.dyingStep = step;
	}

	/**
	 * Get the dying step
	 * 
	 * @return The current dying step.
	 */

	public int getDyingStep() {
		return this.dyingStep;
	}

	/**
	 * Increment the dying step by 1.
	 */
	public void incDyingStep() {
		this.dyingStep++;
	}

	/**
	 * Is the player visible?
	 * 
	 * @return Is the player visible?
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Set the player's visibility
	 * 
	 * @param visible
	 *            The new state of visibility.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return The score of this character
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param n
	 *            The amount to increment this character's score.
	 */
	public void incrementScore(int n) {
		score += n;
	}

	/**
	 * Stores the last character that this character collided with and when they
	 * collided.
	 * 
	 * @param c
	 *            The character that was collided with.
	 * @param time
	 *            The time at which they collided (resources.globalTimer).
	 */
	public void setLastCollidedWith(Character c, int time) {
		this.lastCollidedWith = c;
		this.lastCollidedTime = time;
	}

	/**
	 * @return Who this character last collided with.
	 */
	public Character getLastCollidedWith() {
		return lastCollidedWith;
	}

	/**
	 * @return When this character last collided.
	 */
	public int getLastCollidedTime() {
		return lastCollidedTime;
	}

	/**
	 * @return The current player number.
	 */
	public int getPlayerNumber() {
		return this.playerNo;
	}

	/**
	 * @param playerNumber
	 *            The new player number.
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNo = playerNumber;
	}

	@Override
	public CollidableType getType() {
		return type;
	}

	/**
	 * Apply a powerup object to this character. The time is used to remove the
	 * powerup after a certain length of time.
	 * 
	 * @param p
	 *            The powerup to apply.
	 * @param time
	 *            The current time.
	 */
	public void applyPowerup(Powerup p, int time) {
		if (hasPowerup) {
			revertPowerup();
		}
		Power pow = p.getPower();
		lastPowerup = pow;
		lastPowerupTime = time;
		switch (pow) {
		case Speed:
			setMaxDx(maxdx * red_speed_mult);
			setMaxDy(maxdy * red_speed_mult);
			// setAcc(acc * 2);
			break;
		case Mass:
			setMass(mass * blue_mass_mult);
			setMaxDx(maxdx / 2);
			setMaxDy(maxdy / 2);
			setAcc(acc / 2);
			break;
		case Spike:
			setRestitution(restitution * spike_rest_mult);
			setMaxDx(maxdx / 10);
			setMaxDy(maxdy / 10);
			setMass(mass * 10);
			break;
		}
		hasPowerup = true;
	}

	/**
	 * Revert the effects of the last powerup applied to this character.
	 */
	public void revertPowerup() {
		switch (lastPowerup) {
		case Speed:
			setMaxDx(maxdx / red_speed_mult);
			setMaxDy(maxdy / red_speed_mult);
			// setAcc(acc / 2);
			break;
		case Mass:
			setMass(mass / blue_mass_mult);
			setMaxDx(maxdx * 2);
			setMaxDy(maxdy * 2);
			setAcc(acc * 2);
			break;
		case Spike:
			setRestitution(restitution / spike_rest_mult);
			setMaxDx(maxdx * 10);
			setMaxDy(maxdy * 10);
			setMass(mass / 10);
			break;
		}
		hasPowerup = false;
	}

	/**
	 * @return The object of the last powerup applied to this character.
	 */
	public Power getLastPowerup() {
		return lastPowerup;
	}

	/**
	 * @param power
	 *            The last power up applied to this character.
	 */
	public void setLastPowerup(Power power) {
		lastPowerup = power;
	}

	/**
	 * @return The time that this character last received a powerup.
	 */
	public int getLastPowerupTime() {
		return lastPowerupTime;
	}

	/**
	 * @param b
	 *            Does this character have a powerup?
	 */
	public void hasPowerup(boolean b) {
		hasPowerup = b;
	}

	/**
	 * @return Does this character have a powerup?
	 */
	public boolean hasPowerup() {
		return hasPowerup;
	}

	/**
	 * Deprecated?
	 * 
	 * @return The team number that this character is on (1 or 2).
	 */
	public int getTeamNumber() {
		return teamNumber;
	}

	/**
	 * Deprecated?
	 * 
	 * @param teamNumber
	 *            The new team number.
	 */
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}

	/**
	 * Resizes a sprite based on a multiplier
	 * 
	 * @param image
	 *            the original sprite
	 * @param multiplier
	 *            the multiplier
	 * @return the new sprite
	 */
	private BufferedImage resize(BufferedImage image, double multiplier) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage big = new BufferedImage((int) (w * multiplier), (int) (h * multiplier), image.getType());
		Graphics2D g = big.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, (int) (w * multiplier), (int) (h * multiplier), 0, 0, w, h, null);
		g.dispose();

		return big;
	}

	/**
	 * Makes appropriate sprites based on a multiplier
	 * 
	 * @param multiplier
	 *            the multiplier
	 */
	public void makeSizeSprites(double multiplier) {

		bigRollingSprites = new ArrayList<BufferedImage>();
		bigDashSprites = new ArrayList<BufferedImage>();

		for (BufferedImage image : rollingSprites) {

			bigRollingSprites.add(resize(image, multiplier));

		}

		for (BufferedImage image : dashSprites) {

			bigDashSprites.add(resize(image, multiplier));

		}

		bigArrow = resize(arrow, multiplier);
		bigArrowMe = resize(arrowMe, multiplier);

		bigSpikes = resize(spikes, multiplier);

		currentFrame = bigRollingSprites.get(0);

	}

	/**
	 * Get this character's arrow sprite
	 * 
	 * @param fullscreen
	 *            whether the game is fullscreen
	 * @param isPlayer
	 *            if the character is the actual player's
	 * @return the arrow sprite
	 */
	public BufferedImage getArrow(boolean fullscreen, boolean isPlayer) {
		if (fullscreen) {
			if (isPlayer) {
				return bigArrowMe;
			}
			return bigArrow;
		}

		if (isPlayer) {
			return arrowMe;
		}
		return arrow;
	}

	/**
	 * Get the spikes sprite for this character
	 * 
	 * @param fullscreen
	 *            whether the game is fullscreen
	 * @return the spikes sprite
	 */
	public BufferedImage getSpikes(boolean fullscreen) {
		if (fullscreen) {
			return bigSpikes;
		}

		return spikes;
	}

	/**
	 * @return Does the character have a bomb?
	 */
	public boolean hasBomb() {
		return hasBomb;
	}

	/**
	 * @param hasBomb
	 *            Does the character have a bomb?
	 */
	public void hasBomb(boolean hasBomb) {
		this.hasBomb = hasBomb;
	}

	/**
	 * @param exploding
	 *            Is the character exploding?
	 */
	public void setExploding(boolean exploding) {
		this.exploding = exploding;
		if (exploding) {
			dead = true;
			hasBomb = false;
		}
	}

	/**
	 * @return Is the character exploding?
	 */
	public boolean isExploding() {
		return this.exploding;
	}

	/**
	 * @return When did this character last die?
	 */
	public int getTimeOfDeath() {
		return timeOfDeath;
	}

	/**
	 * @param timeOfDeath
	 *            The new time of death.
	 */
	public void setTimeOfDeath(int timeOfDeath) {
		this.timeOfDeath = timeOfDeath;
	}

	/**
	 * Performs a check on whether a character can dash before changing the
	 * dashing status.
	 */
	public void requestDashing() {
		setDashing((stamina >= dashStamina));
	}

	/**
	 * @return Is this character an AI?
	 */
	public boolean isAI() {
		return isAI;
	}

	/**
	 * @param isAI
	 *            Is this character an AI?
	 */
	public void setAI(boolean isAI) {
		this.isAI = isAI;
	}

	/**
	 * @return The AI this character is being controlled by.
	 */
	public AITemplate getAI() {
		return ai;
	}

	/**
	 * @param ai
	 *            The new AI to control this character.
	 */
	public void setAI(AITemplate ai) {
		setAI(true);
		this.ai = ai;
	}

	/**
	 * ???
	 * 
	 * @return
	 */
	public BufferedImage getCurrentFrame() {
		return this.currentFrame;
	}

	/**
	 * ???
	 * 
	 * @return
	 */
	public BufferedImage getFirstFrame() {
		return this.rollingSprites.get(0);
	}

	/**
	 * @return The name of the player controlling this character.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return The number of kills achieved by this character.
	 */
	public int getKills() {
		return this.kills;
	}

	/**
	 * @return The number of times this character has died.
	 */
	public int getDeaths() {
		return this.deaths;
	}

	/**
	 * @return The number of times this character has died on their own.
	 */
	public int getSuicides() {
		return this.suicides;
	}

	/**
	 * Increment number of kills by 1.
	 */
	public void incrementKills() {
		this.kills++;
	}

	/**
	 * Increment number of suicides by 1.
	 */
	public void incrementSuicides() {
		this.suicides++;
	}

	/**
	 * Increment number of deaths by 1.
	 */
	public void incrementDeaths() {
		this.deaths++;
	}

	/**
	 * @return The cooldown remaining before this character can dash again.
	 */
	public int getDashCooldown() {
		return dashCooldown;
	}

	/**
	 * Increment the dash cooldown by 1.
	 */
	public void incrementDashCooldown() {
		this.dashCooldown++;
	}

	/**
	 * @param n
	 *            Set the dash cooldown to a specified value.
	 */
	public void setDashCooldown(int n) {
		dashCooldown = n;
	}

	/**
	 * @param score
	 *            Set this character's score to the specified value.
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @param kills
	 *            Set this character's kills to the specified value.
	 */
	public void setKills(int kills) {
		this.kills = kills;
	}

	/**
	 * @param deaths
	 *            Set this character's deaths to the specified value.
	 */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	/**
	 * @param suicides
	 *            Set this character's suicides to the specified value.
	 */
	public void setSuicides(int suicides) {
		this.suicides = suicides;
	}

	/**
	 * @param name
	 *            Set this character's name to the specified string.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	
}
