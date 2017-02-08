package graphics.sprites;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteSheet {

	private String name;
	private Sprite.SheetType type;
	private BufferedImage spriteSheet;
	private int rows;
	private int cols;
	private int sizeX;
	private int sizeY;

	private ArrayList<int[][]> sections;

	public SpriteSheet(Sprite.SheetType type, String name, int rows, int cols, int sizeX, int sizeY, ArrayList<int[][]> sections) {

		this.type = type;
		this.name = name;
		this.rows = rows;
		this.cols = cols;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		switch (type) {
			case CHARACTER:
				this.sections = sections;
				break;
			case TILE:
				break;
		}

		this.spriteSheet = Sprite.loadSpriteSheet(name, type);

	}

	/**
	 * Get a particular sprite from the sprite sheet
	 * 
	 * @param x
	 *            the column of the sprite
	 * @param y
	 *            the row of the sprite
	 * @return the sprite
	 */

	public BufferedImage getSprite(int x, int y) {

		return Sprite.getSprite(spriteSheet, x, y, sizeX, sizeY);
	}

	/**
	 * Get the name of this sprite sheet
	 * 
	 * @return the name
	 */

	public String getName() {
		return this.name;
	}

	/**
	 * Get the sprite sheet
	 * 
	 * @return the sprite sheet
	 */

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	/**
	 * Get the rows of the sprite sheet
	 * 
	 * @return the rows
	 */

	public int getRows() {
		return rows;
	}

	/**
	 * Get the columns of the sprite sheet
	 * 
	 * @return the columns
	 */

	public int getCols() {
		return cols;
	}

	/**
	 * Get the x size of a sprite on the sprite sheet
	 * 
	 * @return the x size
	 */

	public int getSizeX() {
		return sizeX;
	}
	
	/**
	 * Get the y size of a sprite on the sprite sheet
	 * 
	 * @return the y size
	 */

	public int getSizeY() {
		return sizeY;
	}

	/**
	 * Get the list of different sections of the sprite sheet
	 * 
	 * @return the sections
	 */

	public ArrayList<int[][]> getSections() {
		return sections;
	}

}
