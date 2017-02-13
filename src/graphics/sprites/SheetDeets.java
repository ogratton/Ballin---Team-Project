package graphics.sprites;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import resources.Character;
import resources.Map;

public class SheetDeets {

	/*
	 * ArrayLists for spritesheet sections should be defined as follows: 1)
	 * rolling sprites 2+) other, as of yet undefined sections
	 */

	// CHARACTER CLASS SHEETS

	// wizard
	public static final String CHAR_WIZ_NAME = "wizard";

	public static final int CHAR_WIZ_COLS = 8;
	public static final int CHAR_WIZ_ROWS = 1;
	public static final int CHAR_WIZ_SIZEX = 50;
	public static final int CHAR_WIZ_SIZEY = 50;
	
	public static final int[][] CHAR_WIZ_ROLLING = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 },
			{ 6, 0 }, { 7, 0 } };

	@SuppressWarnings("serial")
	public static final ArrayList<int[][]> CHAR_WIZ_SECTIONS = new ArrayList<int[][]>() {
		{
			add(CHAR_WIZ_ROLLING);
			add(new int[8][2]);
		}
	};

	public static final SpriteSheet CHAR_WIZ = new SpriteSheet(Sprite.SheetType.CHARACTER, CHAR_WIZ_NAME, CHAR_WIZ_ROWS,
			CHAR_WIZ_COLS, CHAR_WIZ_SIZEX, CHAR_WIZ_SIZEY, CHAR_WIZ_SECTIONS);

	// elf
	public static final String CHAR_ELF_NAME = "elf";

	public static final int CHAR_ELF_COLS = 8;
	public static final int CHAR_ELF_ROWS = 1;
	public static final int CHAR_ELF_SIZEX = 50;
	public static final int CHAR_ELF_SIZEY = 50;
	
	public static final int[][] CHAR_ELF_ROLLING = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 },
			{ 6, 0 }, { 7, 0 } };

	@SuppressWarnings("serial")
	public static final ArrayList<int[][]> CHAR_ELF_SECTIONS = new ArrayList<int[][]>() {
		{
			add(CHAR_WIZ_ROLLING);
			add(new int[8][2]);
		}
	};

	public static final SpriteSheet CHAR_ELF = new SpriteSheet(Sprite.SheetType.CHARACTER, CHAR_ELF_NAME, CHAR_ELF_ROWS,
			CHAR_ELF_COLS, CHAR_ELF_SIZEX, CHAR_ELF_SIZEY, CHAR_ELF_SECTIONS);

	// test (with directions)
	public static final String CHAR_TEST_NAME = "test";

	public static final int CHAR_TEST_COLS = 8;
	public static final int CHAR_TEST_ROWS = 1;
	public static final int CHAR_TEST_SIZEX = 50;
	public static final int CHAR_TEST_SIZEY = 50;
	
	public static final int[][] CHAR_TEST_DIRS = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 }, { 6, 0 },
			{ 7, 0 } };

	@SuppressWarnings("serial")
	public static final ArrayList<int[][]> CHAR_TEST_SECTIONS = new ArrayList<int[][]>() {
		{
			add(new int[8][2]); // rolling
			add(CHAR_TEST_DIRS); // directions (wip)
		}
	};

	public static final SpriteSheet CHAR_TEST = new SpriteSheet(Sprite.SheetType.CHARACTER, CHAR_TEST_NAME,
			CHAR_TEST_ROWS, CHAR_TEST_COLS, CHAR_TEST_SIZEX, CHAR_TEST_SIZEY, CHAR_TEST_SECTIONS);

	// TILE SPRITE SHEETS

	/* TILE CONVENTION:
	 * Row 0:
	 * 0: Abyss
	 * 1: Flat
	 * 2: Edge N
	 * 3: Edge E
	 * 4: Edge S
	 * 5: Edge W
	 * Row 1:
	 * 0: Edge NE
	 * 1: Edge SE
	 * 2: Edge SW
	 * 3: Edge NW
	 * 4: Edge NS
	 * 5: Edge EW
	 * Row 2:
	 * 0: Edge NES
	 * 1: Edge ESW
	 * 2: Edge SWN
	 * 3: Edge WNE
	 * 4: Edge NESW
	 * 5: Edge Abyss
	 */
	
	// test hex

	public static final String TILES_NAME = "tiles";

	public static final int TILES_COLS = 6;
	public static final int TILES_ROWS = 3;
	public static final int TILES_SIZEX = 25;
	public static final int TILES_SIZEY = 25;

	public static final SpriteSheet TILES = new SpriteSheet(Sprite.SheetType.TILE, TILES_NAME, TILES_ROWS,
			TILES_COLS, TILES_SIZEX, TILES_SIZEY, null);

	// speed lines
	public static final SpriteSheet MISC = new SpriteSheet(Sprite.SheetType.MISC, "misc", 1, 1, 50, 50, null);
	
	/**
	 * Return the correct sprite sheet for a given character
	 * 
	 * @param character
	 *            the character
	 * @return the sprite sheet
	 */

	public static SpriteSheet getSpriteSheetFromCharacter(Character character) {

		Character.Class c = character.getClassType();

		return getSpriteSheetFromClass(c);

	}

	/**
	 * Return the correct sprite sheet for a given class
	 * 
	 * @param c
	 *            the class
	 * @return the sprite sheet
	 */

	public static SpriteSheet getSpriteSheetFromClass(Character.Class c) {

		switch (c) {
		case DEFAULT:
			return CHAR_TEST;
		case WIZARD:
			return CHAR_WIZ;
		case ELF:
			return CHAR_ELF;
		case TEST:
			return CHAR_TEST;
		}

		return null;
	}
	
	/**
	 * Get the sprite sheet for a given world type
	 * @param w the world type
	 * @return the sprite sheet
	 */
	
	public static BufferedImage getTileSetFromWorld(Map.World w){
		
		int x = 0;
		
		switch(w){
		case CAVE:
			x = 0;
			break;
		case LAVA:
			x = 0;
			break;
		case ICE:
			x = 0;
			break;
		case DESERT:
			x = 0;
			break;
		}
		
		return Sprite.getSprite(TILES.getSpriteSheet(), 0, TILES_SIZEY * x, TILES_SIZEX * 6 , TILES_SIZEY * 3);
	}

	/**
	 * Get the radius of a character from their sprite
	 * 
	 * @param c
	 * @return
	 */

	public static int getRadiusFromSprite(Character.Class c) {

		SpriteSheet sheet = getSpriteSheetFromClass(c);
		return (int) (sheet.getSizeX() / 2);

	}

}
