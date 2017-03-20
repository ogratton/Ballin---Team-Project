package resources;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Random;

import graphics.sprites.SheetDeets;

public class Powerup extends Observable implements Collidable_Circle {

	private int radius = 21;
	private double x, y = 0;
	private CollidableType type = CollidableType.Powerup;
	private BufferedImage sprite = null;

	// Size? Affect other characters? Fireballs?
	public enum Power {
		Speed, Mass
	};
	
	private Power power = null;
	private Character lastCollidedWith = null;
	private boolean falling = false;
	private boolean dead = false;
	private double dy = 0;
	private double dx = 0;
	private int lastCollidedTime = -1;
	private boolean visible;
	private int dyingStep;
	private boolean active = true;

	/**
	 * Create a new powerup containing a random power.
	 */
	public Powerup() {
		Random rand = new Random();
		int p = rand.nextInt(Power.values().length);
		power = Power.values()[p];
		sprite = SheetDeets.getPowerUpSpriteFromType(power);
	}

	@Override
	public double getInvMass() {
		return 0;
	}

	@Override
	public double getRestitution() {
		return 0;
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
	
	/**
	 * @return Which power does this powerup hold?
	 */
	public Power getPower() {
		return power;
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

	/**
	 * @param sprite The new sprite of this powerup.
	 */
	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	/**
	 * @return The sprite of this powerup.
	 */
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
	public void setDyingStep(int step) {
		dyingStep = step;
	}

	@Override
	public int getDyingStep() {
		return dyingStep;
	}
	
	/**
	 * @return Is this powerup active?
	 */
	public boolean isActive(){
		return active;
	}
	
	/**
	 * @param active Is this powerup active?
	 */
	public void setActive(boolean active){
		this.active = active;
	}
}
