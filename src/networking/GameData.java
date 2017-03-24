package networking;

import java.util.ArrayList;
import java.util.List;

import resources.Powerup;

/**
 * Packages a list of characters and other data into an object so that it can be sent across the network.
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
	private ArrayList<SerializablePowerUp> powerUps;
	private int timer;
	
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
	
	/**
	 * Get an ArrayList of serialized power ups.
	 * @return ArrayList of serialized power ups.
	 */
	public ArrayList<SerializablePowerUp> getPowerUps() {
		return powerUps;
	}
	
	/**
	 * Set the list of serialized power ups.
	 * @param powerUps
	 */
	public void setPowerUps(ArrayList<SerializablePowerUp> powerUps) {
		this.powerUps = powerUps;
	}

	/**
	 * Get the timer for deathmatch.
	 * @return The timer
	 */
	public int getTimer() {
		return timer;
	}

	/**
	 * Set the timer for deathmatch.
	 * @param timer The timer for deathmatch.
	 */
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
}
