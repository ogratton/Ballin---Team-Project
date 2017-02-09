package graphics.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import resources.Character;
import resources.Map;

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
	 * 
	 * @param classType
	 *            the class
	 * @return the name
	 */

	public static String determineSpriteSheetName(Character.Class classType) {

		return SheetDeets.getSpriteSheetFromClass(classType).getName();

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

	public static BufferedImage getSprite(BufferedImage spriteSheet, int x, int y, int sizeX, int sizeY) {
		return spriteSheet.getSubimage(x * sizeX, y * sizeY, sizeX, sizeY);
	}

	/**
	 * Draw one big map image from all the individual tiles
	 */

	public static BufferedImage createMap(Map map) {

		// do some calculate first
		double width = map.getWidth();
		double height = map.getHeight();

		Map.Tile[][] tiles = map.getTiles();
		int tileSize = map.getTileSize();

		// create a new buffer and draw two image into the new image
		BufferedImage newImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		// fill background
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, (int) width, (int) height);
		// draw image
		g2.setColor(oldColor);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {

				g2.drawImage(map.getTileSprite(j, i), null, tileSize * j, tileSize * i);

			}
		}
		
		g2.dispose();
		return newImage;
	}
}
