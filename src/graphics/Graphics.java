package graphics;

import javax.swing.JFrame;

import networking.Updater;
import resources.Resources;

public class Graphics extends Thread {

	Resources resources;
	Updater updater;
	
	public Graphics(Resources resources, Updater updater){
		this.resources = resources;
		this.updater = updater;
	}
	
	public Graphics(Resources resources){
		this.resources = resources;
	}
	
	public void run() {
	
		
		GameComponent comp = new GameComponent(resources, 1200, 675, updater);
	
		comp.setUndecorated(true);
		comp.setSize(1200, 675);
		comp.setLocationRelativeTo(null);
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 								// end program
		comp.setTitle("" + resources.getId());
		comp.setVisible(true);
		
	}

	

}
