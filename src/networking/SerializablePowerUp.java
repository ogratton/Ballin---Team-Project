package networking;

import resources.Powerup.Power;

/**
 * The serializable version of the Powerup class (which we can send over the network)
 * @author axn598
 *
 */
public class SerializablePowerUp {
	
	private Power p;
	private double x;
	private double y;
	private boolean active;
	
	public SerializablePowerUp(){
		
	}
	
	/**
	 * Constructs the Serializable power up
	 * @param p The power up enum
	 * @param x The x-coordinate of the power up
	 * @param y The y-coordinate of the power up
	 * @param active If the power up is active
	 */
	public SerializablePowerUp(Power p, double x, double y, boolean active) {
		this.p = p;
		this.x = x;
		this.y = y;
		this.active = active;
	}

	/**
	 * Get the power enum
	 * @return The power
	 */
	public Power getP() {
		return p;
	}

	/**
	 * Set the power enum
	 * @param p The power
	 */
	public void setP(Power p) {
		this.p = p;
	}

	/**
	 * Get the x-coordinate
	 * @return The x-coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set the x-coordinate
	 * @param x The x-coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get the y-coordinate
	 * @return The y-coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the y-coordinate
	 * @param y The y-coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Is the power up active?
	 * @return true if the power up is active, false otherwise.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set if the power up is active.
	 * @param active If the power up is active.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
