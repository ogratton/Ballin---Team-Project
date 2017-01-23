package graphics;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.sprites.SpriteSheet;
import resources.Character;

/**
 * Class to model a character
 * 
 * @author George Kaye
 *
 */

public class CharacterModel {

	private Character character;
	private SpriteSheet spriteSheet;
	private ArrayList<BufferedImage> rollingSprites;
	private int rollingFrame;
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

	public CharacterModel(SpriteSheet spriteSheet, Character character, Character.Class classType) {

		super();
		this.character = character;

		this.moving = false;

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

	public boolean isMoving() {
		return this.moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	/**
	 * Get the current rolling frame
	 * 
	 * @return the frame
	 */

	public BufferedImage getRollingFrame() {

		if(isMoving()){
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
		character.x(character.x() + velX);
		character.y(character.y() + velY);
	}

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
	}

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
		
		if(velX != 0 || velY !=0){
			setMoving(true);
		}else{
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
