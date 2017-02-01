package graphics.sprites;

import java.util.ArrayList;
import resources.Character;

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
	public static final int CHAR_WIZ_SIZE = 50;

	public static final int[][] CHAR_WIZ_ROLLING = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 },
			{ 6, 0 }, { 7, 0 } };

	@SuppressWarnings("serial")
	public static final ArrayList<int[][]> CHAR_WIZ_SECTIONS = new ArrayList<int[][]>() {
		{
			add(CHAR_WIZ_ROLLING);
		}
	};

	public static final SpriteSheet CHAR_WIZ = new SpriteSheet(Sprite.SheetType.CHARACTER, CHAR_WIZ_NAME, CHAR_WIZ_ROWS,
			CHAR_WIZ_COLS, CHAR_WIZ_SIZE, CHAR_WIZ_SECTIONS);

	// elf
	public static final String CHAR_ELF_NAME = "elf";

	public static final int CHAR_ELF_COLS = 8;
	public static final int CHAR_ELF_ROWS = 1;
	public static final int CHAR_ELF_SIZE = 50;

	public static final int[][] CHAR_ELF_ROLLING = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 },
			{ 6, 0 }, { 7, 0 } };

	@SuppressWarnings("serial")
	public static final ArrayList<int[][]> CHAR_ELF_SECTIONS = new ArrayList<int[][]>() {
		{
			add(CHAR_WIZ_ROLLING);
		}
	};

	public static final SpriteSheet CHAR_ELF = new SpriteSheet(Sprite.SheetType.CHARACTER, CHAR_ELF_NAME, CHAR_ELF_ROWS,
			CHAR_ELF_COLS, CHAR_ELF_SIZE, CHAR_ELF_SECTIONS);

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
	 * @param c the class
	 * @return the sprite sheet
	 */
	
	public static SpriteSheet getSpriteSheetFromClass(Character.Class c) {
		
		switch (c) {
		case DEFAULT:
			return CHAR_WIZ;
		case WIZARD:
			return CHAR_WIZ;
		case ELF:
			return CHAR_ELF;
		}

		return null;
	}

	/**
	 * Get the radius of a character from their sprite
	 * @param c
	 * @return
	 */
	
	public static int getRadiusFromSprite(Character.Class c) {

		SpriteSheet sheet = getSpriteSheetFromClass(c);
		return (int) (sheet.getSize());
		
	}

}
