package networking;

import resources.Powerup.Power;

/**
 * This encapsulates the updates to the position of a character which can be
 * sent across the network (unlike the Character class).
 * @author aaquibnaved
 *
 */
public class CharacterInfo {

	private double x;
	private double y;
	private String id;
	private boolean up, right, left, down;
	private boolean isFalling, isDead, isDashing, isBlocking;
	public boolean sendDashing, sendBlocking;
	private int playerNumber;
	private int stamina;
	private boolean hasPowerUp;
	private Power lastPowerUp;
	private int kills, deaths, suicides, lives, score;
	private boolean hasBomb;
	private resources.Character.Class type;
	private String name;
	private int dyingStep;
	private boolean isVisible;
	private boolean isExploding;
	private int timeOfDeath;
	
	public CharacterInfo() {
		
	}
	
	/**
	 * Initialise the CharacterInfo object using only x, y, id and player number.
	 * @param id The Id of the Character
	 * @param d The x position of the Character
	 * @param e The y position of the Character
	 * @param playerNumber The player number
	 */
	public CharacterInfo(String id, double d, double e, int playerNumber, resources.Character.Class type, String name) {
		this.x = d;
		this.y = e;
		this.id = id;
		this.playerNumber = playerNumber;
		this.type = type;
		this.name = name;
	}
	
	/**
	 * Initialise the CharacterInfo object using both x and y coordinates
	 * as well as other variables such as if it is dashing or not.
	 * @param id The ID of the Character
	 * @param d The x position of the Character
	 * @param e The y position of the Character
	 * @param playerNumber The player number
	 * @param isFalling Is the player falling?
	 * @param isDead Is the player dead?
	 * @param isDashing Is the player dashing?
	 * @param isBlocking Is the player blocking?
	 * @param stamina The player stamina
	 */
	public CharacterInfo(String id, double d, double e, int playerNumber, boolean isFalling, boolean isDead, boolean isDashing, boolean isBlocking, int stamina, boolean hasPowerUp, Power lastPowerUp, int kills, int deaths, int suicides, int lives, int score, boolean hasBomb, int dyingStep, boolean isVisible, boolean isExploding, int timeOfDeath) {
		this.x = d;
		this.y = e;
		this.id = id;
		this.playerNumber = playerNumber;
		this.isFalling = isFalling;
		this.isDead = isDead;
		this.isDashing = isDashing;
		this.isBlocking = isBlocking;
		this.stamina = stamina;
		this.hasPowerUp = hasPowerUp;
		this.lastPowerUp = lastPowerUp;
		this.kills = kills;
		this.deaths = deaths;
		this.suicides = suicides;
		this.lives = lives;
		this.score = score;
		this.hasBomb = hasBomb;
		this.dyingStep = dyingStep;
		this.isVisible = isVisible;
		this.isExploding = isExploding;
		this.timeOfDeath = timeOfDeath;
	}
	
	/**
	 * Initialise without using player number or stamina.
	 * @param id
	 * @param up
	 * @param right
	 * @param left
	 * @param down
	 * @param dashing
	 * @param block
	 */
	public CharacterInfo(String id, boolean up, boolean right, boolean left, boolean down, boolean dashing, boolean block) {
		this.id = id;
		this.up = up;
		this.right = right;
		this.left = left;
		this.down = down;
		this.isDashing = dashing;
		this.isBlocking = block;
	}

	/**
	 * Has up been pressed?
	 * @return true if up is pressed, false otherwise.
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * Set is up pressed
	 * @param up If up is pressed
	 */
	public void setUp(boolean up) {
		this.up = up;
	}

	/**
	 * Has right been pressed
	 * @return true if right is pressed, false otherwise.
	 */
	public boolean isRight() {
		return right;
	}

	/**
	 * Set is right pressed
	 * @param right If right is pressed
	 */
	public void setRight(boolean right) {
		this.right = right;
	}

	/**
	 * Is left pressed?
	 * @return true if left is pressed, false otherwise.
	 */
	public boolean isLeft() {
		return left;
	}

	/**
	 * Set is left pressed.
	 * @param left If left is pressed
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}

	/**
	 * Is down pressed?
	 * @return true if down is pressed, false otherwise.
	 */
	public boolean isDown() {
		return down;
	}

	/**
	 * Set if down is pressed
	 * @param down If down is pressed.
	 */
	public void setDown(boolean down) {
		this.down = down;
	}

	/**
	 * Get the ID of the Character
	 * @return The ID of the Character
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the ID of the Character
	 * @param id The ID of the Character
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the x-coordinate of the Character
	 * @return The x-coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set the x-coordinate of the Character
	 * @param x The x-coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get the y-coordinate of the Character
	 * @return The y-coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the y-coordinate of the Character
	 * @param y The y-coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Get the player number (indicates the colour)
	 * @return The player number
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * Set the player number (indicates the colour)
	 * @param playerNumber The Player Number
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	
	/**
	 * Is the Character falling?
	 * @return true if the Character is falling, false otherwise.
	 */
	public boolean isFalling() {
		return isFalling;
	}

	/**
	 * Set if the Character is falling.
	 * @param isFalling If the Character falling
	 */
	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

	/**
	 * Is the Character dead?
	 * @return true if the Character is dead, false otherwise.
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * Set if the Character is dead.
	 * @param isDead If the Character is dead.
	 */
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	/**
	 * Is the Character dashing?
	 * @return true if the Character is dashing, false otherwise.
	 */
	public boolean isDashing() {
		return isDashing;
	}

	/**
	 * Set if the Character is dashing
	 * @param isDashing If the Character is dashing
	 */
	public void setDashing(boolean isDashing) {
		this.isDashing = isDashing;
	}
	
	/**
	 * Is the Character blocking?
	 * @return true if the character is blocking, false otherwise?
	 */
	public boolean isBlocking() {
		return isBlocking;
	}

	/**
	 * Set if the Character is blocking
	 * @param isBlocking If the Character is blocking
	 */
	public void setBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
	}

	/**
	 * Get the stamina of the Character
	 * @return The stamina
	 */
	public int getStamina() {
		return stamina;
	}

	/**
	 * Set the stamina of the Character
	 * @param stamina The stamina
	 */
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	/**
	 * Does the Character have a power up.
	 * @return true if the character has a power up, false otherwise.
	 */
	public boolean isHasPowerUp() {
		return hasPowerUp;
	}

	/**
	 * Set if the Character has a power up.
	 * @param hasPowerUp If the Character has a power up.
	 */
	public void setHasPowerUp(boolean hasPowerUp) {
		this.hasPowerUp = hasPowerUp;
	}

	/**
	 * Get the last power up this Character had.
	 * @return The last power up.
	 */
	public Power getLastPowerUp() {
		return lastPowerUp;
	}

	/**
	 * Set the last power up this Character had.
	 * @param lastPowerUp The last power up.
	 */
	public void setLastPowerUp(Power lastPowerUp) {
		this.lastPowerUp = lastPowerUp;
	}

	/**
	 * Get the number of kills this character has made.
	 * @return The number of kills.
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * Set the number of kills this character has made.
	 * @param kills The number of kills.
	 */
	public void setKills(int kills) {
		this.kills = kills;
	}

	/**
	 * Get the number of deaths this character has had.
	 * @return The number of deaths.
	 */
	public int getDeaths() {
		return deaths;
	}

	/**
	 * Set the number of deaths this character has had.
	 * @param deaths The number of deaths.
	 */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	/**
	 * Get the number of suicides.
	 * @return The number of suicides.
	 */
	public int getSuicides() {
		return suicides;
	}

	/**
	 * Set the number of suicides this character has had.
	 * @param suicides The number of suicides.
	 */
	public void setSuicides(int suicides) {
		this.suicides = suicides;
	}

	/**
	 * Get the number of lives this character has.
	 * @return The number of lives.
	 */
	public int getLives() {
		return lives;
	}

	/**
	 * Set the number of lives this character has.
	 * @param lives The number of lives
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * Get the score of this character
	 * @return The score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Set the score of this character.
	 * @param score The score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Does this character have the bomb. (In Hot Potato mode)
	 * @return true if the character has a bomb, false otherwise.
	 */
	public boolean isHasBomb() {
		return hasBomb;
	}

	/**
	 * Set if the character has the bomb.
	 * @param hasBomb If the character has the bomb.
	 */
	public void setHasBomb(boolean hasBomb) {
		this.hasBomb = hasBomb;
	}

	/**
	 * Get the Class type of the Character
	 * @return The class type
	 */
	public resources.Character.Class getType() {
		return type;
	}

	/**
	 * Set the class type of the Character
	 * @param type The class type
	 */
	public void setType(resources.Character.Class type) {
		this.type = type;
	}

	/**
	 * Get the name of the character
	 * @return The name of the character
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the character
	 * @param name The name of the character.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the dying step (for the dying animation)
	 * @return The dying step
	 */
	public int getDyingStep() {
		return dyingStep;
	}

	/**
	 * Set the dying step for the Character (for the dying animation)
	 * @param dyingStep The dying step
	 */
	public void setDyingStep(int dyingStep) {
		this.dyingStep = dyingStep;
	}

	/**
	 * Is the Character visible?
	 * @return true if the character is visible, false otherwise.
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Set if the Character is visible
	 * @param isVisible Is the character visible
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * Is the character exploding?
	 * @return true if the character is exploding, false otherwise.
	 */
	public boolean isExploding() {
		return isExploding;
	}

	/**
	 * Set if the character is exploding.
	 * @param isExploding If the character is exploding
	 */
	public void setExploding(boolean isExploding) {
		this.isExploding = isExploding;
	}

	/**
	 * Get the time of death after being killed by the bomb (in Hot Potato)
	 * @return The time of death.
	 */
	public int getTimeOfDeath() {
		return timeOfDeath;
	}

	/**
	 * Set the time of death after being killed by the bomb (in Hot Potato)
	 * @param timeOfDeath The time of death
	 */
	public void setTimeOfDeath(int timeOfDeath) {
		this.timeOfDeath = timeOfDeath;
	}
	
	
}
