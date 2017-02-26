package graphics;

import javax.swing.JFrame;

import networking.Updater;
import resources.Resources;

public class Graphics extends Thread {

	Resources resources;
	Updater updater;
	boolean debugPaths = false;
	
	public Graphics(Resources resources, Updater updater, boolean debugPaths){
		this.resources = resources;
		this.updater = updater;
		this.debugPaths = debugPaths;
	}
	
	public Graphics(Resources resources){
		this.resources = resources;
	}
	
	public void run() {
	
		
		GameComponent comp = new GameComponent(resources, 1200, 675, updater, debugPaths);
	
		comp.setUndecorated(false);
		comp.setSize(1200, 725);
		comp.setLocationRelativeTo(null);
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 								// end program
		comp.setTitle("" + resources.getId());
		comp.setVisible(true);
		
	}

	

}
