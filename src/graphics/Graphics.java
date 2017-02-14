package graphics;

import javax.swing.JFrame;

import resources.Resources;

public class Graphics extends Thread {

	Resources resources;
	
	public Graphics(Resources resources){
		this.resources = resources;
	}
	
	public void run() {
	
		
		GameComponent comp = new GameComponent(resources, 1200, 675);
	
		comp.setUndecorated(true);
		comp.setSize(1200, 675);
		comp.setLocationRelativeTo(null);
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 								// end program
		comp.setTitle("Ballin'");
		comp.setVisible(true);
		
	}

	

}
