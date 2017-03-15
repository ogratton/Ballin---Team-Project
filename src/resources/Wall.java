package resources;

import java.awt.Point;

import graphics.sprites.SheetDeets;

public class Wall implements Collidable_Circle {
	private double x;
	private double y;

	public Wall(Point coords) {
		x = coords.x;
		y = coords.y;
	}
	
	@Override
	public final double getInvMass() {
		//infinite mass
		return 0;
	}

	@Override
	public final double getRestitution() {
		//very bouncy
		return 10;
	}

	@Override
	public final double getDx() {
		// no movement
		return 0;
	}

	@Override
	public final double getDy() {
		// no movement
		return 0;
	}

	@Override
	public final void setDx(double dx) {
		// HAHAHAHAHAAAA!
		
	}

	@Override
	public final void setDy(double dx) {
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
		return y;
	}

	@Override
	public final int getRadius() {
		return SheetDeets.TILES_SIZEX;
	}

	@Override
	public final void setDead(boolean dead) {
		
	}

	@Override
	public final boolean isDead() {
		return false;
	}

	@Override
	public final void setFalling(boolean falling) {
		
	}

	@Override
	public final boolean isFalling() {
		return false;
	}

	@Override
	public final CollidableType getType() {
		return CollidableType.Wall;
	}

	@Override
	public final void setLastCollidedWith(Character c, int time) {
		
	}

	@Override
	public final Character getLastCollidedWith() {
		return null;
	}

	@Override
	public final int getLastCollidedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public final void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final boolean isVisible() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public final void setDyingStep(int step) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final int getDyingStep() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}