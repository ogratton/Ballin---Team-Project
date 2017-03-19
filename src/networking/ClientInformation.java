package networking;

import java.util.UUID;

import graphics.sprites.Sprite;

/**
 * This contains all the information about a particular client.
 * @author aaquibnaved
 *
 */
public class ClientInformation {
	
	private String id;
	private String name;
	private String sessionId;
	private boolean ready;
	private resources.Character.Class characterClass;
	private int playerNumber;

	public ClientInformation() {
		
	}
	
	/**
	 * Initialises using only a name. ID is generated and converted to a String.
	 * @param name Name of the Client.
	 */
	public ClientInformation(String name) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.ready = false;
	}
	
	/**
	 * Initialises using a preset ID.
	 * @param id The ID of the client
	 * @param name The Name of the client
	 */
	public ClientInformation(String id, String name) {
		this.id = id;
		this.name = name;
		this.ready = false;
	}
	
	/**
	 * Initialises using the ID, the name and type of character class which the Client has chosen to me.
	 * @param id The ID of the client
	 * @param name The name of the client
	 * @param characterClass The character class of the client
	 */
	public ClientInformation(String id, String name, resources.Character.Class characterClass) {
		this.id = id;
		this.name = name;
		this.ready = false;
		this.characterClass = characterClass;
	}

	/**
	 * Get the ID of the client
	 * @return The ID of the client
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set the ID of the client
	 * @param id The ID of the client
	 */ 
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get the name of the client
	 * @return The name of the client
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Is the client ready for the game to start?
	 * @return true if the client is ready, false otherwise
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Set whether the client is ready to start or not.
	 * @param b whether the client is ready or not
	 */
	public void setReady(boolean b) {
		this.ready = b;
	}

	/**
	 * Get the session ID which the client has currently joined
	 * This returns null when the client is not part of a session.
	 * @return The Session ID
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Stores the session ID which the client belongs to.
	 * @param sessionId The session ID
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * Get the character class which this client has chosen.
	 * @return
	 */
	public resources.Character.Class getCharacterClass() {
		return characterClass;
	}

	/**
	 * Set the character class of the client.
	 * @param characterClass
	 */
	public void setCharacterClass(resources.Character.Class characterClass) {
		this.characterClass = characterClass;
	}
	
	/**
	 * Get the number assigned to the player (this is used for the player colour)
	 * @return The player number
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * Set the player number (this is used for the player colour)
	 * @param playerNumber The player number
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
}
