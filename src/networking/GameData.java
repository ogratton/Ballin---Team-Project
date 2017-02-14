package networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2012501718012423753L;
	private List<CharacterInfo> characters;
	
	public GameData(List<CharacterInfo> characters) {
		this.characters = characters;
	}
	
	public List<CharacterInfo> getCharactersList() {
		return characters;
	}
	
}
