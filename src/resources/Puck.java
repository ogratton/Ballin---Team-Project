package resources;

import graphics.sprites.SheetDeets;

public class Puck implements Collidable_Circle {
	int radius = 5;
	double x,y,dx,dy = 0;
	double inv_mass = 1;
	double restitution = 10;
	boolean falling,dead = false;
	private CollidableType type = CollidableType.Puck;
	private BufferedImage sprite = SheetDeets.getMiscSpritesFromType(SheetDeets.PUCK);
	
	public Puck(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public double getInvMass() {
		return inv_mass;
	}

	@Override
	public double getRestitution() {
		return restitution;
	}

	@Override
	public double getDx() {
		return dx;
	}

	@Override
	public double getDy() {
		return dy;
	}

	@Override
	public void setDx(double dx) {
		this.dx = dx;
	}

	@Override
	public void setDy(double dx) {
		this.dy = dy;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public int getRadius() {
		return radius;
	}

	@Override
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void setFalling(boolean falling) {
		this.falling = falling;
	}
	public boolean isFalling() {
		return falling;
	}

	@Override
	public CollidableType getType() {
		return type;
	}
	
}
