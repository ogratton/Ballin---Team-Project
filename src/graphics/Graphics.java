package graphics;

import javax.swing.JFrame;

import networking.Updater;
import resources.Resources;

/**
 * The starting thread for graphics
 * @author George Kaye
 *
 */

public class Graphics extends Thread {

	Resources resources;
	Updater updater;
	boolean debugPaths = false;
	
	/**
	 * Create a new Graphics thread, with resources and an updater
	 * @param resources the resources object
	 * @param updater the updater
	 * @param debugPaths the debug paths flag
	 */
	
	public Graphics(Resources resources, Updater updater, boolean debugPaths){
		this.resources = resources;
		this.updater = updater;
		this.debugPaths = debugPaths;
	}
	
	/**
	 * Create a new Graphics thread, with resources
	 * @param resources the resources object
	 */
	
	public Graphics(Resources resources){
		this.resources = resources;
	}
	
	/**
	 * Run the thread
	 */
	
	public void run() {
		
		GameComponent comp = new GameComponent(resources, 1200, 650, updater, debugPaths);
		
		comp.setLocationRelativeTo(null);
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 							
		comp.setTitle("" + resources.getId());
		
	}

}
