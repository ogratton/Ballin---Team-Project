package networking;

import java.util.List;

/**
 * Packages a list of characters into an object so that it can be sent across the network.
 * @author aaquibnaved
 *
 */
public class GameData {
	
	/**
	 * Enum to indicate whether the Game Data object was sent at the initialisation
	 * of the game or during the game.
	 * @author aaquibnaved
	 *
	 */
	public enum Tag {
		START, POSITION
	}
	
	private Tag tag;
	private List<CharacterInfo> characters;
	private CharacterInfo info;
	
	public GameData() {
		
	}
	
	/**
	 * Constructor for when you need to send multiple character position updates
	 * across the network.
	 * @param characters The list of CharacterInfo objects
	 */
	public GameData(List<CharacterInfo> characters) {
		this.characters = characters;
	}
	
	/**
	 * Constructor for when you only want to send one character position update.
	 * @param info A single CharacterInfo object.
	 */
	public GameData(CharacterInfo info) {
		this.info = info;
	}
	
	/**
	 * Get the list of character information objects.
	 * @return A list of character information objects.
	 */
	public List<CharacterInfo> getCharactersList() {
		return characters;
	}
	
	/**
	 * Get a single CharacterInfo object.
	 * @return
	 */
	public CharacterInfo getInfo() {
		return this.info;
	}
	
	/**
	 * Get the tag associated with this particular Game Data object.
	 * @return
	 */
	public Tag getTag() {
		return tag;
	}
	
}
