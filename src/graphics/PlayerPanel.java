package graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import resources.Character;
import resources.Resources;

public class PlayerPanel extends JPanel {

	private Resources resources;
	private ArrayList<PlayerInfo> boxes;

	public PlayerPanel(Resources resources) {

		super();
		this.resources = resources;

		setLayout(new GridLayout(2, 1));
		
		ArrayList<Character> characters = resources.gamemode.getOrderedScores();
		boxes = new ArrayList<>();
		
		int i = 0;
		
		for(Character c : characters){
			
			PlayerInfo box = new PlayerInfo(c);
			boxes.add(box);
			add(box, i/4, i%4);
			
		}
		
		setPreferredSize(new Dimension(1920, 100));
		
	}
	
	public void setOrder(){
		
		ArrayList<Character> characters = resources.gamemode.getOrderedScores();
		
		for(int i  = 0; i < characters.size(); i++){
			
			boxes.get(characters.size() - 1 - i).setCharacter(characters.get(i));
		}
		
	}

}
