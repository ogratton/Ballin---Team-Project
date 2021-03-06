package graphics.sprites;

import java.awt.image.BufferedImage;

import resources.Character;
import resources.Map;
import resources.Powerup;

/**
 * Class containing details of the various sprite sheets for the game, with
 * methods to extract correct sprites
 * 
 * @author George Kaye
 *
 */

public class SheetDetails {

	// CHARACTER SPRITE SHEET

	public static final BufferedImage CHARACTERS = Sprite.loadSpriteSheet(Sprite.SheetType.CHARACTER);

	public static final int CHARACTERS_COLS = 8;
	public static final int CHARACTERS_ROWS = 4;
	public static final int CHARACTERS_SIZEX = 50;
	public static final int CHARACTERS_SIZEY = 50;

	/**
	 * Return the correct sprite sheet for a given character
	 * 
	 * @param character
	 *            the character
	 * @return the sprite sheet
	 */

	public static BufferedImage getSpriteSheetFromCharacter(Character character) {

		Character.Class c = character.getClassType();

		return getSpriteSetFromClass(c);

	}

	/**
	 * Get the set of sprites for a given class
	 * 
	 * @param c
	 *            the class
	 * @return the set of sprites
	 */

	public static BufferedImage getSpriteSetFromClass(Character.Class c) {

		int x = 0;

		switch (c) {
		case WIZARD:
			x = 0;
			break;
		case ARCHER:
			x = 1;
			break;
		case WARRIOR:
			x = 2;
			break;
		case MONK:
			x = 3;
			break;
		case WITCH:
			x = 4;
			break;
		case HORSE:
			x = 5;
			break;
		}

		return Sprite.getSprite(CHARACTERS, 0, x, CHARACTERS_SIZEX * 8, CHARACTERS_SIZEY);

	}

	/**
	 * Get the radius of a character from their sprite
	 * 
	 * @param c
	 * @return
	 */

	public static int getRadiusFromSprite(Character.Class c) {

		return 25;

	}

	// TILE SPRITE SHEETS

	/*
	 * TILE CONVENTION: Row 0: 0: Abyss 1: Flat 2: Edge N 3: Edge E 4: Edge S 5:
	 * Edge W Row 1: 0: Edge NE 1: Edge SE 2: Edge SW 3: Edge NW 4: Edge NS 5:
	 * Edge EW Row 2: 0: Edge NES 1: Edge ESW 2: Edge SWN 3: Edge WNE 4: Edge
	 * NESW 5: Edge Abyss
	 */

	public static final BufferedImage TILES = Sprite.loadSpriteSheet(Sprite.SheetType.TILE);

	public static final int TILES_COLS = 6;
	public static final int TILES_ROWS = 6;
	public static final int TILES_SIZEX = 25;
	public static final int TILES_SIZEY = 25;

	/**
	 * Get the sprite sheet for a given world type
	 * 
	 * @param w
	 *            the world type
	 * @return the sprite sheet
	 */

	public static BufferedImage getTileSetFromWorld(Map.World w) {

		int x = 0;

		switch (w) {
		case CAVE:
			x = 0;
			break;
		case LAVA:
			x = 2;
			break;
		case ICE:
			x = 4;
			break;
		case DESERT:
			x = 5;
			break;
		case SPACE:
			x = 1;
			break;
		case CAKE:
			x = 3;
			break;
		}

		return Sprite.getSprite(TILES, 0, x, TILES_SIZEX * 6, TILES_SIZEY * 4);
	}

	// MISCELLANEOUS TILES

	public enum Misc {
		DASH, PUCK, POWERUP, EXPLOSION;
	};

	public static final BufferedImage MISC = Sprite.loadSpriteSheet(Sprite.SheetType.MISC);

	public static final int MISC_COLS = 8;
	public static final int MISC_ROWS = 1;
	public static final int MISC_SIZEX = 50;
	public static final int MISC_SIZEY = 50;

	/**
	 * Get the relevant sprite from the type
	 * 
	 * @param m
	 *            the type of sprite
	 * @return the sprite set
	 */

	public static BufferedImage getMiscSpritesFromType(Misc m) {

		int y = 0;
		int x = 0;
		int numX = 0;
		int numY = 0;

		switch (m) {
		case DASH:
			y = 0;
			numX = 8;
			numY = 1;
			break;
		case POWERUP:
			y = 1;
			x = 0;
			numX = 8;
			numY = 1;
			break;
		case PUCK:
			break;
		case EXPLOSION:
			y = 3;
			x = 0;
			numX = 1;
			numY = 1;
		default:
			break;

		}

		return Sprite.getSprite(MISC, x, y, 50 * numX, 50 * numY);

	}

	/**
	 * Get the correct powerup sprite from the powerup type
	 * 
	 * @param p
	 *            the powerup type
	 * @return the powerup sprite
	 */

	public static BufferedImage getPowerUpSpriteFromType(Powerup.Power p) {

		BufferedImage set = getMiscSpritesFromType(Misc.POWERUP);

		switch (p) {
		case Speed:
			return Sprite.getSprite(set, 1, 0, MISC_SIZEX, MISC_SIZEY);
		case Mass:
			return Sprite.getSprite(set, 2, 0, MISC_SIZEX, MISC_SIZEY);
		case Spike:
			return Sprite.getSprite(set, 3, 0, MISC_SIZEX, MISC_SIZEY);
		default:
			return Sprite.getSprite(set, 0, 0, MISC_SIZEX, MISC_SIZEY);
		}

	}

	/**
	 * Get the bomb sprite
	 * 
	 * @return the bomb sprite
	 */

	public static BufferedImage getBombSprite() {
		BufferedImage set = getMiscSpritesFromType(Misc.POWERUP);

		return Sprite.getSprite(set, 0, 0, MISC_SIZEX, MISC_SIZEY);

	}

	// ARROWHEAD TILES

	public static final BufferedImage ARROWS = Sprite.loadSpriteSheet(Sprite.SheetType.ARROWS);

	public static final int ARROWS_COLS = 9;
	public static final int ARROWS_ROWS = 2;
	public static final int ARROWS_SIZEX = 50;
	public static final int ARROWS_SIZEY = 50;

	/**
	 * Get the correct coloured arrow for a player
	 * 
	 * @param no
	 *            the player number
	 * @return the arrow
	 */

	public static BufferedImage getArrowFromPlayer(int no) {

		if (no > 8) {
			no = 0;
		}

		return Sprite.getSprite(ARROWS, no, 0, ARROWS_SIZEX, ARROWS_SIZEY * 2);
	}

	// SPIKES TILE

	public static final BufferedImage SPIKES = Sprite.loadSpriteSheet(Sprite.SheetType.SPIKES);

	public static final int SPIKES_COLS = 1;
	public static final int SPIKES_ROWS = 1;
	public static final int SPIKES_SIZEX = 66;
	public static final int SPIKES_SIZEY = 66;

}
