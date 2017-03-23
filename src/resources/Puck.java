package resources;

import java.awt.image.BufferedImage;
import java.util.Observable;

import graphics.sprites.SheetDeets;
// Deprecated?
public class Puck extends Observable implements Collidable_Circle {
	int radius = 4;
	double x,y,dx,dy = 0;
	double inv_mass = 1;
	double restitution = 10;
	boolean falling,dead = false;
	private CollidableType type = CollidableType.Puck;
	private BufferedImage sprite = SheetDeets.getMiscSpritesFromType(SheetDeets.Misc.PUCK);
	private Character lastCollidedWith = null;
	private int lastCollidedTime = -1;
	private boolean visible;
	private int dyingStep;
	
	public Puck() {
		new Puck(0,0);
	}
	
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
	public void setDy(double dy) {
		this.dy = dy;
	}

	@Override
	public void setX(double x) {
		this.x = x;
		setChanged();
		notifyObservers();
	}

	@Override
	public void setY(double y) {
		this.y = y;
		setChanged();
		notifyObservers();
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
		setChanged();
		notifyObservers();
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void setFalling(boolean falling) {
		this.falling = falling;
	}
	@Override
	public boolean isFalling() {
		return falling;
	}

	@Override
	public CollidableType getType() {
		return type;
	}

	@Override
	public void setLastCollidedWith(Character c, int time) {
		this.lastCollidedWith = c;
		this.lastCollidedTime = time;
	}
	
	@Override
	public Character getLastCollidedWith() {
		return lastCollidedWith;
	}
	
	@Override
	public int getLastCollidedTime() {
		return lastCollidedTime;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;		
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setDyingStep(int i) {
		this.dyingStep = i;
	}

	@Override
	public int getDyingStep() {
		return dyingStep;
	}
	
}
