package graphics.sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {

	private static BufferedImage spriteSheet = loadSprite("wiz");
	private static final int TILE_SIZE = 50;

	public static BufferedImage loadSprite(String file) {

		BufferedImage sprite = null;

		try {
			sprite = ImageIO.read(new File("resources/sprites/" + file + ".png"));
		} catch (IOException e) {
			System.err.println("sprite sheet not found!");
			System.exit(1);
		}

		return sprite;
	}

	public static BufferedImage getSprite(int x, int y) {
		return spriteSheet.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}

}
