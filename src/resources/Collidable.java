package resources;

/**
 * Interface for a collidable object (i.e. things in the game)
 * @author George Kaye
 *
 */

public interface Collidable {
	public double getInvMass();
	public double getRestitution();
	public double getDx();
	public double getDy();
	public void setDx(double dx);
	public void setDy(double dy);
}
