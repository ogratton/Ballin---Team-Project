package graphics;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;

import graphics.sprites.SheetDeets;
import graphics.sprites.SpriteSheet;
import resources.Character;

/**
 * Class to model a character
 * 
 * @author George Kaye
 *
 */

public class CharacterModel extends Observable {

	private Character character;
	private SpriteSheet spriteSheet;
	private ArrayList<BufferedImage> rollingSprites;
	private int rollingFrame, frameDelay;
	private boolean up, down, left, right = false;
	private int velX, velY;
	private static final int SPEED = 10;
	private Direction dir = Direction.E;
	private boolean moving;

	public static enum Direction {
		N, NE, E, SE, S, SW, W, NW
	};

	/**
	 * Create a new model of a character
	 * 
	 * @param character
	 *            the character
	 */

	public CharacterModel(Character character) {

		super();
		this.character = character;

		spriteSheet = SheetDeets.getClassSpriteSheet(character);

		this.moving = false;
		rollingSprites = new ArrayList<BufferedImage>();

		ArrayList<int[][]> sections = spriteSheet.getSections();
		int[][] rollingSpriteLocs = sections.get(0);

		for (int i = 0; i < rollingSpriteLocs.length; i++) {
			rollingSprites.add(spriteSheet.getSprite(rollingSpriteLocs[i][0], rollingSpriteLocs[i][1]));
		}

		rollingFrame = 0;
		frameDelay = 0;

	}

	/**
	 * Get the x coordinate of a player
	 * 
	 * @return the x coordinate
	 */

	public double getX() {
		return this.character.getX();
	}

	/**
	 * Get the y coordinate of a player
	 * 
	 * @return the y coordinate
	 */

	public double getY() {
		return this.character.getY();
	}

	/**
	 * Set the x coordinate of a player
	 * 
	 * @param x
	 *            the x coordinate
	 */

	public void setX(double x) {
		this.character.setX(x);
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the y coordinate of a player
	 * 
	 * @param y
	 *            the y coordinate
	 */

	public void setY(double y) {

		this.character.setY(y);
		setChanged();
		notifyObservers();
	}

	/**
	 * Get if the character is moving
	 * 
	 * @return moving?
	 */

	public boolean isMoving() {
		return this.moving;
	}

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
	 * Get the current rolling frame
	 * 
	 * @return the frame
	 */

	public BufferedImage getNextFrame(boolean moving) {

		if (moving) {
			switch (dir) {
			case W:
			case NW:
			case SW:
			case N:
				rollingFrame--;
				break;
			case E:
			case NE:
			case SE:
			case S:
				rollingFrame++;
				break;
			}
			if (rollingFrame == 8)
				rollingFrame = 0;

			if (rollingFrame == -1)
				rollingFrame = 7;
		}
		return this.rollingSprites.get(rollingFrame);
	}

	/**
	 * Move the character
	 */

	public void move() {

		setX(getX() + velX);
		setY(getY() + velY);
	}

	/**
	 * Is an up command being received?
	 * 
	 * @return up?
	 */

	public boolean isUp() {
		return up;
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
	 * Is a down command being received?
	 * 
	 * @return down?
	 */

	public boolean isDown() {
		return down;
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
	 * Is a left command being received?
	 * 
	 * @return left?
	 */

	public boolean isLeft() {
		return left;
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
	 * Is a right command being received?
	 * 
	 * @return right?
	 */

	public boolean isRight() {
		return right;
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
	 * Set the direction of the character based on the commands it is currently
	 * receiving
	 */

	private void setDirection() {

		if (up) {
			if (left) {
				dir = Direction.NW;
			}

			else if (right) {
				dir = Direction.NE;
			}

			else {
				dir = Direction.N;
			}
		} else if (down) {
			if (left) {
				dir = Direction.SW;
			}

			else if (right) {
				dir = Direction.SE;
			}

			else {
				dir = Direction.S;
			}
		} else if (left) {
			dir = Direction.W;
		} else if (right) {
			dir = Direction.E;
		}

		update();
	}

	/*
	 * Testing methods
	 * Should not be used in the final demo
	 */

	private void update() {
		velX = 0;
		velY = 0;

		if (down)
			velY = SPEED;
		if (up)
			velY = -SPEED;
		if (left)
			velX = -SPEED;
		if (right)
			velX = SPEED;

		if (velX != 0 || velY != 0) {
			setMoving(true);
		} else {
			setMoving(false);
		}

	}

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {

		case KeyEvent.VK_A:
			left = true;
			break;
		case KeyEvent.VK_D:
			right = true;
			break;
		case KeyEvent.VK_S:
			down = true;
			break;
		case KeyEvent.VK_W:
			up = true;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;

		}

		setDirection();

		update();

	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		}

		update();

	}

}
