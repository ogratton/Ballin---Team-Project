package resources;

public class Wall implements Collidable {
	public static final Wall wall = new Wall();

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
	
	
}