package networking;

import resources.Powerup.Power;

public class SerializablePowerUp {
	
	private Power p;
	private double x;
	private double y;
	private boolean active;
	
	public SerializablePowerUp(){
		
	}
	
	public SerializablePowerUp(Power p, double x, double y, boolean active) {
		this.p = p;
		this.x = x;
		this.y = y;
		this.active = active;
	}

	public Power getP() {
		return p;
	}

	public void setP(Power p) {
		this.p = p;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
