package graphics.sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class with functions pertaining to the retrieval and loading of sprites from a given sprite sheet
 * @author George Kaye
 *
 */

public class Sprite {

	// enum for the different types of sprite
	public static enum SheetType{CHARACTER, TILE};
	
	// how big each sprite is (square)
	private static final int TILE_SIZE = 50;

	/**
	 * Load a sprite from a sprite sheet on the system
	 * @param spriteName the name of the sprite
	 * @param spriteType the type of the sprite
	 * @return a BufferedImage of the sprite sheet
	 */
	
	public static BufferedImage loadSprite(String spriteName, SheetType spriteType) {

		BufferedImage sprite = null;
		String sheetType = "";
		
		switch(spriteType){
		case CHARACTER:
			sheetType = "characters/";
			break;
		case TILE:
			sheetType = "tiles/";
			break;
		}
		
		String address = "graphics/sprites/" + sheetType + spriteName + "/sheet.png";
		
		try {
			
			sprite = ImageIO.read(new File(address));
		} catch (IOException e) {
			System.err.println("sprite sheet not found!");
			System.exit(1);
		}

		return sprite;
	}

	/**
	 * Get an individual sprite from a sprite sheet
	 * @param spriteSheet the sprite sheet
	 * @param x the column of the sprite
	 * @param y the row of the sprite
	 * @return a BufferedImage of the sprite
	 */
	
	public static BufferedImage getSprite(BufferedImage spriteSheet, int x, int y) {
		return spriteSheet.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}

}
