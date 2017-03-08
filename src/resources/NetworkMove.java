package resources;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class NetworkMove implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 939146193915119548L;
	public UUID id;
	public Date t;
	public double x,y;
	public boolean isFalling, isDead, isDashing, isBlocking;
	
	public String toString() {
		String string = "";
		string += "ID: " + this.id;
		string += ", Timestamp: " + this.t;
		string += ", X: " + this.x + ", Y: " + this.y;
		string += ", isFalling: " + this.isFalling + ", isDead: " + this.isDead + ", isDashing: " + this.isDashing + " isBlocking: " + this.isBlocking;
		return string; 
	}
}
