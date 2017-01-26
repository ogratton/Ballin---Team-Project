package graphics;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import graphics.sprites.SheetDeets;
import resources.Character;
import resources.Map;

public class GameComponent extends JPanel {

	private CharacterModel model;
	
	public GameComponent(Character character, Map map){
		
		setLayout(new BorderLayout());
		
		JButton button = new JButton("exit");
		button.addActionListener(e -> System.exit(0));
		add(button, BorderLayout.SOUTH);
		
		model = new CharacterModel(SheetDeets.CHAR_WIZ, character, Character.Class.DEFAULT);
		//MapView arena = new MapView(model);	
		
		PlayerView player = new PlayerView(model);
		model.addObserver(player);
		
		JButton button1 = new JButton("left");
		button1.addActionListener(e -> demo());
		
		add(button1, BorderLayout.NORTH);
		add(player, BorderLayout.CENTER);
			
		setVisible(true);
		
	}
	
	public void demo(){
		
		
		
	}
	
}
