package physics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import resources.Character;
import resources.Map;
import resources.Resources;
import resources.Wall;

public class Physics extends Thread implements ActionListener {
	//
	private Timer timer;
	private final int DELAY = 10; //~1000ms /60 = 1/60 seconds
	
	@Override
	public void run() {
		System.out.println("start");
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	/**
	 * pauses the simulation
	 */
	public void pause() {
		timer.stop();
	}
	
	/**
	 * unpauses the simulation
	 */
	public void unpause() {
		timer.start();
	}

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// loop
		for(Character c: Resources.players){
			update(c);
		}
	}
	
	private void update(Character c) {
		// calculate speed, calculate location 
		//if(c.left()) System.out.println(c.dx());
		if(c.left() && c.dx() > -c.maxdx()) {
			System.out.println("l");
			c.dx(c.dx() - c.acc());
		}
		if(c.right() && c.dx() < c.maxdx()) {
			System.out.println("r"+c.mass());
			c.dx(c.dx() + c.acc());
		}
		if(c.up() && c.dy() > -c.maxdy()) {
			System.out.println("u");
			c.dy(c.dy() - c.acc());
		}
		if(c.down() && c.dy() < c.maxdy()) {
			System.out.println("d");
			c.dy(c.dy() + c.acc());
		}
		if(!c.left() && !c.right()) {
			c.dx(c.dx() * Resources.map.friction());
		}
		if(!c.up() && !c.down()) {
			c.dy(c.dy() * Resources.map.friction());
		}
		// TODO calculate collisions
		
		double x = c.x() + c.dx();
		double y = c.y() + c.dy();
		System.out.println(c.x());
		c.x(x);
		c.y(y);
		
	}
	
	private void collide(Character c1, Character c2) {
		
	}
	
	private void collide(Character c, Wall w)  {
		
	}
}
