package graphics.sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import resources.Character;

/**
 * Class with functions pertaining to the retrieval and loading of sprites from
 * a given sprite sheet
 * 
 * @author George Kaye
 *
 */

public class Sprite {

	// enum for the different types of sprite
	public static enum SheetType {
		CHARACTER, TILE
	};

	/**
	 * Load a sprite from a sprite sheet on the system
	 * 
	 * @param spriteName
	 *            the name of the sprite
	 * @param spriteType
	 *            the type of the sprite
	 * @return a BufferedImage of the sprite sheet
	 */

	public static BufferedImage loadSpriteSheet(String spriteName, SheetType spriteType) {

		BufferedImage sprite = null;
		String sheetType = "";

		switch (spriteType) {
		case CHARACTER:
			sheetType = "characters/";
			break;
		case TILE:
			sheetType = "tiles/";
			break;
		}

		String address = "./resources/sprites/" + sheetType + spriteName + "/sheet.png";

		try {
			sprite = ImageIO.read(new File(address));
		} catch (IOException e) {
			System.err.println("sprite sheet not found!");
			System.exit(1);
		}

		return sprite;
	}

	/**
	 * Determine the name of a sprite sheet for a class
	 * @param classType the class
	 * @return the name
	 */
	
	public static String determineSpriteSheetName(Character.Class classType) {

		switch (classType) {
		case DEFAULT:
			return "wizard";
		case ELF:
			return "elf";
		case WIZARD:
			return "wizard";
		}
		
		return "";

	}

	/**
	 * Get an individual sprite from a sprite sheet
	 * 
	 * @param spriteSheet
	 *            the sprite sheet
	 * @param x
	 *            the column of the sprite
	 * @param y
	 *            the row of the sprite
	 * @param size
	 *            the size of the sprite
	 * @return a BufferedImage of the sprite
	 */

	public static BufferedImage getSprite(BufferedImage spriteSheet, int x, int y, int size) {
		return spriteSheet.getSubimage(x * size, y * size, size, size);
	}

}
