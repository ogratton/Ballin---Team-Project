package networking;

import java.io.Serializable;

public class CharacterInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5417282971483563559L;
	private double x;
	private double y;
	private int id;
	
	public CharacterInfo(int id, double d, double e) {
		this.x = d;
		this.y = e;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
