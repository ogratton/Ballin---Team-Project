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
	
	public CharacterInfo() {
		
	}
	
	/**
	 * Initialise the CharacterInfo object using only x, y, id and player number.
	 * @param id The Id of the Character
	 * @param d The x position of the Character
	 * @param e The y position of the Character
	 * @param playerNumber The player number
	 */
	public CharacterInfo(String id, double d, double e, int playerNumber) {
		this.x = d;
		this.y = e;
		this.id = id;
		this.playerNumber = playerNumber;
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
	public CharacterInfo(String id, double d, double e, int playerNumber, boolean isFalling, boolean isDead, boolean isDashing, boolean isBlocking, int stamina, boolean hasPowerUp, Power lastPowerUp, int kills, int deaths, int suicides, int lives, int score, boolean hasBomb) {
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

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	
	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDashing() {
		return isDashing;
	}

	public void setDashing(boolean isDashing) {
		this.isDashing = isDashing;
	}
	
	public boolean isBlocking() {
		return isBlocking;
	}

	public void setBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public boolean isHasPowerUp() {
		return hasPowerUp;
	}

	public void setHasPowerUp(boolean hasPowerUp) {
		this.hasPowerUp = hasPowerUp;
	}

	public Power getLastPowerUp() {
		return lastPowerUp;
	}

	public void setLastPowerUp(Power lastPowerUp) {
		this.lastPowerUp = lastPowerUp;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getSuicides() {
		return suicides;
	}

	public void setSuicides(int suicides) {
		this.suicides = suicides;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isHasBomb() {
		return hasBomb;
	}

	public void setHasBomb(boolean hasBomb) {
		this.hasBomb = hasBomb;
	}
	
}
