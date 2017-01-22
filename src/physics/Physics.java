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
	private final int DELAY = 16; //~1000ms /60 = 1/60 seconds
	private Map map;
	
	public void run() {
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
		for(int i = 0; i < Resources.players.length; i++){
			update(Resources.players[i]);
		}
	}
	
	private void update(Character c) {
		// calculate speed, calculate location 
		
		if(c.left() && c.dx() > -c.maxdx()) {
			c.dx(c.dx() - c.acc());
		}
		if(c.right() && c.dx() < c.maxdx()) {
			c.dx(c.dx() + c.acc());
		}
		if(c.down() && c.dy() > -c.maxdy()) {
			c.dy(c.dy() - c.acc());
		}
		if(c.up() && c.dy() < c.maxdy()) {
			c.dx(c.dy() + c.acc());
		}
		if(!c.left() && !c.right()) {
			// if problems arise, we can remove the mass bit.
			c.dx(c.dx() * map.friction() * c.mass());
		}
		if(!c.up() && !c.down()) {
			c.dy(c.dy() * map.friction() * c.mass());
		}
		// TODO calculate collisions
		
		c.x(c.x() + c.dx());
		c.y(c.y() + c.dy());
	}
	
	private void collide(Character c1, Character c2) {
		
	}
	
	private void collide(Character c, Wall w)  {
		
	}
}
