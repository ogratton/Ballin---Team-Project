package graphics;

import java.util.ArrayList;

import javax.swing.JFrame;

import resources.Character;
import resources.Map;

public class Graphics extends Thread {


	private ArrayList<Character> players;
	private Map map;
	
	public Graphics(ArrayList<Character> players, Map map){
		this.players = players;
		this.map = map;
		
	}
	
	public void run() {
	
		
		GameComponent comp = new GameComponent(players, map, 1200, 675);
	
		comp.setUndecorated(true);
		comp.setSize(1200, 675);
		comp.setLocationRelativeTo(null);
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 								// end program
		comp.setTitle("Ballin'");
		comp.setVisible(true);
		
	}

	

}
