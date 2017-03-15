package networking;

import java.util.List;

public class GameData {
	
	public enum Tag {
		START, POSITION
	}
	private Tag tag;
	private List<CharacterInfo> characters;
	private CharacterInfo info;
	
	public GameData() {
		
	}
	
	public GameData(List<CharacterInfo> characters) {
		this.characters = characters;
	}
	
	public GameData(CharacterInfo info) {
		this.info = info;
	}
	
	public List<CharacterInfo> getCharactersList() {
		return characters;
	}
	
	public CharacterInfo getInfo() {
		return this.info;
	}
	
	public Tag getTag() {
		return tag;
	}
	
}
