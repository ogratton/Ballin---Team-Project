package resources;

import java.util.Random;

public class Powerup implements Collidable_Circle {

	private int radius = 5;
	private double x, y = 0;
	private CollidableType type = CollidableType.Powerup;

	// Size? Affect other characters? Fireballs?
	public enum Power {
		Speed, Mass
	};
	
	private Power power;

	public Powerup() {
		Random rand = new Random();
		int p = rand.nextInt(Power.values().length);
		power = Power.values()[p];
		System.out.println("POWER: " + power);
	}

	@Override
	public double getInvMass() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRestitution() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDx() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDx(double dx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDy(double dx) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
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
	public void setFalling(boolean falling) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isFalling() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CollidableType getType() {
		return type;
	}
	
	public Power getPower() {
		return power;
	}
}
