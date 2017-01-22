package graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;

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
	private int rollingFrame;
	private boolean up, down, left, right;
	private int velX, velY, posX, posY;
	private static final int SPEED = 3;

	public static enum Direction {
		U, D, L, R
	};

	/**
	 * Create a new model of a character
	 * 
	 * @param character
	 *            the character
	 */

	public CharacterModel(SpriteSheet spriteSheet, Character character, Character.Class classType) {

		super();
		this.character = character;

		this.spriteSheet = spriteSheet;
		rollingSprites = new ArrayList<BufferedImage>();

		ArrayList<int[][]> sections = spriteSheet.getSections();
		int[][] rollingSpriteLocs = sections.get(0);

		for (int i = 0; i < rollingSpriteLocs.length; i++) {
			rollingSprites.add(spriteSheet.getSprite(rollingSpriteLocs[i][0], rollingSpriteLocs[i][1]));
		}

		rollingFrame = 0;

	}

	/**
	 * Get the x coordinate of a player
	 * 
	 * @return the x coordinate
	 */

	public double getX() {
		return this.character.x();
	}

	/**
	 * Get the y coordinate of a player
	 * 
	 * @return the y coordinate
	 */

	public double getY() {
		return this.character.y();
	}

	/**
	 * Set the x coordinate of a player
	 * 
	 * @param x
	 *            the x coordinate
	 */

	public void setX(double x) {
		this.character.x(x);
	}

	/**
	 * Set the y coordinate of a player
	 * 
	 * @param y
	 *            the y coordinate
	 */

	public void setY(double y) {
		this.character.y(y);
	}

	/**
	 * Get the current rolling frame
	 * 
	 * @return the frame
	 */

	public BufferedImage getRollingFrame() {

		if (rollingFrame != 7) {
			rollingFrame++;
		} else {
			rollingFrame = 0;
		}
		return this.rollingSprites.get(rollingFrame);
	}

	/**
	 * Move the character
	 */

	public void move() {
		posX += velX;
		posY += velY;
	}

	/**
	 * Update the character's position
	 * 
	 * @param up
	 *            up?
	 * @param down
	 *            down?
	 * @param left
	 *            left?
	 * @param right
	 *            right?
	 */

	public void update(boolean up, boolean down, boolean left, boolean right) {
		velX = 0;
		velY = 0;

		if (up)
			velY = -SPEED;
		if (down)
			velY = SPEED;
		if (left)
			velX = -SPEED;
		if (right)
			velX = SPEED;

	}

}
