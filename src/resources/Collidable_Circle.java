package resources;

/**
 * Interface for a collidable circle (most objects in the game)
 * @author Alex & Luke
 *
 */

public interface Collidable_Circle extends Collidable {
	public enum CollidableType {
		Character, Puck, Powerup, Wall
	};
	public void setX(double x);
	public void setY(double y);
	public double getX();
	public double getY();
	
	public int getRadius();
	public void setDead(boolean dead);
	public boolean isDead();
	public void setFalling(boolean falling);
	public boolean isFalling();
	public CollidableType getType();
	public void setLastCollidedWith(Character c, int time);
	public Character getLastCollidedWith();
	public int getLastCollidedTime();

	public void setVisible(boolean visible);
	public boolean isVisible();
	public void setDyingStep(int step);
	public int getDyingStep();

}
