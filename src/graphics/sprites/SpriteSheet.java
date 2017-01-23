package graphics.sprites;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteSheet {

	private String name;
	private Sprite.SheetType type;
	private BufferedImage spriteSheet;
	private int rows;
	private int cols;
	private int size;
	
	private ArrayList<int[][]> sections;
	 
	public SpriteSheet(Sprite.SheetType type, String name, int rows, int cols, int size, ArrayList<int[][]> sections){
		
		this.type = type;
		this.name = name;
		this.rows = rows;
		this.cols = cols;
		this.size = size;
		
		this.sections = sections;
		
		this.spriteSheet = Sprite.loadSpriteSheet(name, type);
		
	}

	/**
	 * Get a particular sprite from the sprite sheet
	 * @param x the column of the sprite
	 * @param y the row of the sprite
	 * @return the sprite
	 */
	
	public BufferedImage getSprite(int x, int y){
		
		return Sprite.getSprite(spriteSheet, x, y, size);		
	}
	
	/**
	 * Get the sprite sheet
	 * @return the sprite sheet
	 */
	
	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	/**
	 * Get the rows of the sprite sheet
	 * @return the rows
	 */
	
	public int getRows() {
		return rows;
	}

	/**
	 * Get the columns of the sprite sheet
	 * @return the columns
	 */
	
	public int getCols() {
		return cols;
	}

	/**
	 * Get the size of a sprite on the sprite sheet
	 * @return the size
	 */
	
	public int getSize() {
		return size;
	}

	/**
	 * Get the list of different sections of the sprite sheet
	 * @return the sections
	 */
	
	public ArrayList<int[][]> getSections(){
		return sections;
	}
	

}
