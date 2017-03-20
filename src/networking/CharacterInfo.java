package networking;

public class CharacterInfo {

	private double x;
	private double y;
	private String id;
	private boolean up, right, left, down, jump, punch;
	private boolean isFalling, isDead, isDashing, isBlocking;
	public boolean sendDashing, sendBlocking;
	private int playerNumber;
	private int requestId;
	private int stamina;
	
	public CharacterInfo() {
		
	}
	
	public CharacterInfo(String id, double d, double e, int playerNumber) {
		this.x = d;
		this.y = e;
		this.id = id;
		this.playerNumber = playerNumber;
	}
	
	public CharacterInfo(String id, double d, double e, int playerNumber, boolean isFalling, boolean isDead, boolean isDashing, boolean isBlocking, int requestId, int stamina) {
		this.x = d;
		this.y = e;
		this.id = id;
		this.playerNumber = playerNumber;
		this.isFalling = isFalling;
		this.isDead = isDead;
		this.isDashing = isDashing;
		this.isBlocking = isBlocking;
		this.requestId = requestId;
		this.stamina = stamina;
	}

	public CharacterInfo(String id, boolean up, boolean right, boolean left, boolean down, boolean dashing, boolean punch, boolean block, int requestId) {
		this.id = id;
		this.up = up;
		this.right = right;
		this.left = left;
		this.down = down;
		this.isDashing = dashing;
		this.punch = punch;
		this.isBlocking = block;
		this.requestId = requestId;
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

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	
	
	
}
