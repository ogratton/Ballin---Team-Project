package graphics;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import javax.swing.JFrame;

import resources.Map;
import resources.Character;

public class GraphicsWithoutCharacterModel extends Thread {


	private ArrayList<Character> players;
	private Map map;
	
	public GraphicsWithoutCharacterModel(ArrayList<Character> players, Map map){
		this.players = players;
		this.map = map;
		
	}
	
	public void run() {
	
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		
		GameComponentWithoutCharacterModel comp = new GameComponentWithoutCharacterModel(players, map, width, height);
	
		comp.setUndecorated(true);
		comp.setSize(width, height);
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the close
																// operation to
																// end program
		comp.setTitle("Ballin'");
		comp.setVisible(true);
		
	}

	

}
