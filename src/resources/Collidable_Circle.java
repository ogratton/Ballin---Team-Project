package resources;

public interface Collidable_Circle extends Collidable {
	public double getX();
	public double getY();
	public int getRadius();
	public void setDead(boolean dead);
	public boolean isDead();
	public void setX(double x);
	public void setY(double y);
	public void setFalling(boolean falling);
	public boolean isFalling();
}
