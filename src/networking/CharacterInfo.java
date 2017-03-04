package networking;

import java.io.Serializable;
import java.util.UUID;

public class CharacterInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5417282971483563559L;
	private double x;
	private double y;
	private UUID id;
	private boolean up, right, left, down, jump, punch, block;
	private boolean isFalling, isDead, isDashing, isBlock;
	private int playerNumber;
	
	public CharacterInfo(UUID id, double d, double e, int playerNumber) {
		this.x = d;
		this.y = e;
		this.id = id;
		this.playerNumber = playerNumber;
	}
	
	public CharacterInfo(UUID id, double d, double e, int playerNumber, boolean isFalling, boolean isDead, boolean isDashing, boolean isBlock) {
		this.x = d;
		this.y = e;
		this.id = id;
		this.playerNumber = playerNumber;
		this.isFalling = isFalling;
		this.isDead = isDead;
		this.isDashing = isDashing;
		this.isBlock = isBlock;
	}

	public CharacterInfo(UUID id, boolean up, boolean right, boolean left, boolean down, boolean jump, boolean punch, boolean block) {
		this.id = id;
		this.up = up;
		this.right = right;
		this.left = left;
		this.down = down;
		this.jump = jump;
		this.punch = punch;
		this.block = block;
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

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public boolean isPunch() {
		return punch;
	}

	public void setPunch(boolean punch) {
		this.punch = punch;
	}

	public boolean isBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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
		return isBlock;
	}

	public void setBlocking(boolean isBlock) {
		this.isBlock = isBlock;
	}
	
}
