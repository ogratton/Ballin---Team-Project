package resources;
import java.awt.Point;

import graphics.sprites.SheetDeets;

public class Wall implements Collidable_Circle {
	public static Wall wall = new Wall(new Point(0,0));
	private double x;
	private double y;

	public Wall(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	@Override
	public double getInvMass() {
		//infinite mass
		return 0;
	}

	@Override
	public double getRestitution() {
		//very bouncy
		return 10;
	}

	@Override
	public double getDx() {
		// no movement
		return 0;
	}

	@Override
	public double getDy() {
		// no movement
		return 0;
	}

	@Override
	public void setDx(double dx) {
		// HAHAHAHAHAAAA!
		
	}

	@Override
	public void setDy(double dx) {
		// MUAHAHAHAHA!
		
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
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public int getRadius() {
		return SheetDeets.TILES_SIZEX / 2;
	}

	@Override
	public void setDead(boolean dead) {		
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void setFalling(boolean falling) {
		
	}

	@Override
	public boolean isFalling() {
		return false;
	}

	@Override
	public CollidableType getType() {
		return CollidableType.Wall;
	}

	@Override
	public void setLastCollidedWith(Character c, int time) {
		
	}

	@Override
	public Character getLastCollidedWith() {
		return null;
	}

	@Override
	public int getLastCollidedTime() {
		return 0;
	}

	@Override
	public void setVisible(boolean visible) {
		
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void setDyingStep(int step) {
		
	}

	@Override
	public int getDyingStep() {
		return 0;
	}
}