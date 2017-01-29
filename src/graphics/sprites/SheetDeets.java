package graphics.sprites;

import java.util.ArrayList;
import resources.Character;

public class SheetDeets {
	
	/*
	 * ArrayLists for spritesheet sections should be defined as follows:
	 * 1) rolling sprites
	 * 2+) other, as of yet undefined sections
	 */
	
	// CHARACTER CLASS SHEETS
	
	// wizard
	public static final String CHAR_WIZ_NAME = "wizard";
	
	public static final int CHAR_WIZ_COLS = 8;
	public static final int CHAR_WIZ_ROWS = 1;
	public static final int CHAR_WIZ_SIZE = 50;
	
	public static final int[][] CHAR_WIZ_ROLLING = {{0,0},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0}};

	@SuppressWarnings("serial")
	public static ArrayList<int[][]> sections = new ArrayList<int[][]>() {{add(CHAR_WIZ_ROLLING);}};
	
	public static final SpriteSheet CHAR_WIZ = new SpriteSheet(Sprite.SheetType.CHARACTER, CHAR_WIZ_NAME, CHAR_WIZ_ROWS, CHAR_WIZ_COLS, CHAR_WIZ_SIZE, sections);
	
	public static SpriteSheet getClassSpriteSheet(Character character){
		
		Character.Class classType = character.getClassType();
		
		switch(classType){
		case DEFAULT:
			return CHAR_WIZ;
		}
		
		return null;
		
	}
	
	
}
