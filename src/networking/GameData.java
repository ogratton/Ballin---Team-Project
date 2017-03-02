package networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameData implements Serializable {
	
	public enum Tag {
		START, POSITION
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2012501718012423753L;
	private Tag tag;
	private List<CharacterInfo> characters;
	private CharacterInfo info;
	private UUID id;
	
	public GameData(List<CharacterInfo> characters) {
		this.characters = characters;
		this.id = UUID.randomUUID();
	}
	
	public GameData(CharacterInfo info) {
		this.info = info;
		this.id = UUID.randomUUID();
	}
	
	public List<CharacterInfo> getCharactersList() {
		return characters;
	}
	
	public UUID getId() {
		return id;
	}
	
	public CharacterInfo getInfo() {
		return this.info;
	}
	
	public Tag getTag() {
		return tag;
	}
	
}
