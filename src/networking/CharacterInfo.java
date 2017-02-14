package networking;

import java.io.Serializable;

public class CharacterInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5417282971483563559L;
	private int x;
	private int y;
	private int id;
	
	public CharacterInfo(int id, int x, int y) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
