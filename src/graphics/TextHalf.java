package graphics;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Character;
import ui.UIRes;

public class TextHalf extends JPanel {

	private Character character;
	private JLabel name;
	private JLabel score;
	
	public TextHalf(Character character) {
		
		setLayout(new BorderLayout());
		
		this.character = character;
		
		name = new JLabel(character.getName());
		score = new JLabel(character.getScore() + "");
		
		name.setHorizontalAlignment(JLabel.CENTER);
		score.setHorizontalAlignment(JLabel.CENTER);
		
		UIRes.setCustomFont(name, 20);
		UIRes.setCustomFont(score, 20);
		
		add(name, BorderLayout.CENTER);
		add(score, BorderLayout.EAST);
		
	}
	
	public void updateScore(){
		
		score.setText(character.getScore() + "");
		
	}

}
