package graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;

import graphics.sprites.SheetDeets;
import graphics.sprites.SpriteSheet;
import resources.Character;
import resources.Collidable;

/**
 * Class to model a character
 * 
 * @author George Kaye
 *
 */

public class CharacterModel extends Observable implements Collidable {

	private Character character;
	private SpriteSheet spriteSheet;
	private ArrayList<BufferedImage> rollingSprites;
	private int rollingFrame;
	private int velX, velY;
	private static final int SPEED = 10;
	private Character.Heading direction;
	private boolean moving;

	/**
	 * Create a new model of a character
	 * 
	 * @param character
	 *            the character
	 */

	public CharacterModel(Character character) {

		super();
		this.character = character;

		this.spriteSheet = SheetDeets.getSpriteSheetFromCharacter(character);

		this.moving = false;
		this.direction = character.getDirection();
		rollingSprites = new ArrayList<BufferedImage>();

		ArrayList<int[][]> sections = spriteSheet.getSections();
		int[][] rollingSpriteLocs = sections.get(0);

		for (int i = 0; i < rollingSpriteLocs.length; i++) {
			BufferedImage sprite = spriteSheet.getSprite(rollingSpriteLocs[i][0], rollingSpriteLocs[i][1]);
			rollingSprites.add(sprite);
			rollingSprites.add(sprite);
		}

		rollingFrame = 0;

	}

	/**
	 * Get the current rolling frame
	 * 
	 * @return the frame
	 */

	public BufferedImage getNextFrame(boolean moving) {

		if (moving) {
			switch (direction) {
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
			if (rollingFrame == 16)
				rollingFrame = 0;

			if (rollingFrame == -1)
				rollingFrame = 15;
		}
		
		return this.rollingSprites.get(rollingFrame);
	}

	/*
	 * Testing methods Should not be used in the final demo
	 */

	/*private void update() {
		velX = 0;
		velY = 0;

		if (character.isDown())
			velY = SPEED;
		if (character.isUp())
			velY = -SPEED;
		if (character.isLeft())
			velX = -SPEED;
		if (character.isRight())
			velX = SPEED;

	}*/

	/**
	 * Move the character (TESTING)
	 */
	
	//public void move(){
	//	setX(getX() + velX);
	//	/setY(getY() + velY);
	//}
	
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
		return this.character.isUp();
	}

	/**
	 * Is a down command being received?
	 * 
	 * @return down?
	 */

	public boolean isDown() {
		return this.character.isDown();
	}

	/**
	 * Is a left command being received?
	 * 
	 * @return left?
	 */

	public boolean isLeft() {
		return this.character.isLeft();
	}

	/**
	 * Is a right command being received?
	 * 
	 * @return right?
	 */

	public boolean isRight() {
		return this.character.isRight();
	}

	/**
	 * Is the character jumping?
	 * 
	 * @return is the character jumping?
	 */

	public boolean isJump() {
		return this.character.isJump();
	}

	/**
	 * Is the character punching?
	 * 
	 * @return is the character punching?
	 */

	public boolean isPunch() {
		return this.character.isPunch();
	}

	/**
	 * Is the character blocking?
	 * 
	 * @return is the character blocking?
	 */

	public boolean isBlock() {
		return this.character.isBlock();
	}

	/**
	 * Set if an up command is being received
	 * 
	 * @param up
	 *            up?
	 */

	public void setUp(boolean up) {
		character.setUp(up);
		setDirection();
	}

	/**
	 * Set if a down command is being received
	 * 
	 * @param down
	 *            down?
	 */

	public void setDown(boolean down) {
		character.setDown(down);
		setDirection();
	}

	/**
	 * Set if a left command is being received
	 * 
	 * @param left
	 *            left?
	 */

	public void setLeft(boolean left) {
		character.setLeft(left);
		setDirection();
	}

	/**
	 * Set if a right command is being received
	 * 
	 * @param right
	 *            right?
	 */

	public void setRight(boolean right) {
		character.setRight(right);
		setDirection();
	}

	/**
	 * Set if a jump command is being received
	 * 
	 * @param jump
	 *            is a jump command being received?
	 */

	public void setJump(boolean jump) {
		this.character.setJump(jump);

	}

	/**
	 * Set if a punch command is being received
	 * 
	 * @param punch
	 *            is a punch command being received?
	 */

	public void setPunch(boolean punch) {
		this.character.setPunch(punch);
	}

	/**
	 * Set if a block command is being received
	 * 
	 * @param block
	 *            is a block command being received?
	 */

	public void setBlock(boolean block) {
		this.character.setBlock(block);
	}

	/**
	 * Set the direction of the character based on the commands it is currently
	 * receiving
	 */

	private void setDirection() {

		if (character.isUp()) {
			if (character.isLeft()) {
				direction = Character.Heading.NW;
			}

			else if (character.isRight()) {
				direction = Character.Heading.NE;
			}

			else {
				direction = Character.Heading.N;
			}
		} else if (character.isDown()) {
			if (character.isLeft()) {
				direction = Character.Heading.SW;
			}

			else if (character.isRight()) {
				direction = Character.Heading.SE;
			}

			else {
				direction = Character.Heading.S;
			}
		} else if (character.isLeft()) {
			direction = Character.Heading.W;
		} else if (character.isRight()) {
			direction = Character.Heading.E;
		}

		if (getDx() != 0 || getDy() != 0) {
			setMoving(true);
		} else {
			setMoving(false);
		}
		
		setDirection(direction);
		
		//update();
		
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
		return this.character.getX();
	}

	/**
	 * Get the y coordinate of a character
	 * 
	 * @return the y coordinate
	 */

	public double getY() {
		return this.character.getY();
	}

	/**
	 * Has the character collided?
	 * 
	 * @return if the character has collided
	 */

	public boolean isCollided() {
		return this.character.isCollided();
	}

	/**
	 * Get the facing of this character
	 * 
	 * @return the facing
	 */

	public Character.Heading getDirection() {
		return this.character.getDirection();
	}

	/**
	 * Set the x coordinate of the character
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
	 * Set the y coordinate of the character
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
	 * Set if the character has collided
	 * 
	 * @param collided
	 *            if the character has collided
	 */

	public void setCollided(boolean collided) {
		this.character.setCollided(collided);
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the facing of the character
	 * 
	 * @param facing
	 */

	public void setDirection(Character.Heading facing) {
		this.character.setDirection(facing);
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
		return this.character.getMass();
	}

	/**
	 * Get the 'inv mass' of the character
	 * 
	 * @return the inv mass
	 */

	public double getInvMass() {
		return this.character.getInvMass();
	}

	/**
	 * Get the dx of the character
	 * 
	 * @return the dx
	 */

	public double getDx() {
		return this.character.getDx();
	}

	/**
	 * Get the dy of the character
	 * 
	 * @return
	 */

	public double getDy() {
		return this.character.getDy();
	}

	/**
	 * Get the max dx of the character
	 * 
	 * @return the max dx
	 */

	public double getMaxDx() {
		return this.character.getMaxDx();
	}

	/**
	 * Get the max dy of the character
	 * 
	 * @return the max dy
	 */

	public double getMaxDy() {
		return this.character.getMaxDy();
	}

	/**
	 * Get the acceleration of the character
	 * 
	 * @return the acceleration
	 */

	public double getAcc() {
		return this.character.getAcc();
	}

	/**
	 * Get the restitution of the character
	 * 
	 * @return the restitution
	 */

	public double getRestitution() {
		return this.character.getRestitution();
	}

	/**
	 * Get the radius of the character
	 * 
	 * @return the radius
	 */

	public int getRadius() {
		return this.character.getRadius();
	}

	/**
	 * Set the mass of a character
	 * 
	 * @param mass
	 *            the mass
	 */

	public void setMass(double mass) {
		this.character.setMass(mass);
	}

	/**
	 * Set the dx of a character
	 * 
	 * @param dx
	 *            the dx
	 */

	public void setDx(double dx) {
		this.character.setDx(dx);
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
		this.character.setDy(dy);
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
		this.character.setMaxDx(maxDx);
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
		this.character.setMaxDy(maxDy);
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
		this.character.setAcc(acceleration);
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
		this.character.setRestitution(restitution);
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
		this.character.setRadius(radius);
		setChanged();
		notifyObservers();
	}

}
