package graphics;

import java.awt.Dimension;

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
		// since apparently setUndecorated won't work on CentOS :'(
	
		comp.getContentPane().setPreferredSize(new Dimension(1200, 675));
		comp.pack();
		

		
		comp.setLocationRelativeTo(null);
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 								// end program
		comp.setTitle("" + resources.getId());
		
	}

	

}
