package graphics;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graphics.sprites.SheetDeets;
import resources.Character;
import resources.Map;

public class GameComponent extends JPanel {

	public GameComponent(Character character, Map map){
		
		setLayout(new BorderLayout());
		
		JButton button = new JButton("exit");
		button.addActionListener(e -> System.exit(0));
		add(button, BorderLayout.SOUTH);
		
		CharacterModel model = new CharacterModel(SheetDeets.CHAR_WIZ, character, Character.Class.DEFAULT);
		MapView arena = new MapView(model);	
		
		arena.setFocusable(true);
		
		add(arena, BorderLayout.CENTER);
			
		setVisible(true);
		
	}
	
}
