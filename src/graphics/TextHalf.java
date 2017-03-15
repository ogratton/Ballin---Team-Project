package graphics;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import resources.Character;
import ui.UIRes;

/**
 * The text half of a panel detailing a player
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class TextHalf extends JPanel {

	private Character character;
	private JLabel name;
	private JLabel score;
	
	/**
	 * Create a new text half
	 * @param character the character being detailed
	 */
	
	public TextHalf(Character character) {
		
		setLayout(new BorderLayout());
		
		this.character = character;
		
		name = new JLabel(character.getName());
		score = new JLabel(character.getScore() + "");
		
		name.setHorizontalAlignment(JLabel.LEFT);
		score.setHorizontalAlignment(JLabel.CENTER);
		
		score.setBorder(new EmptyBorder(0, 0, 0, 25));
		
		UIRes.setCustomFont(name, 20);
		UIRes.setCustomFont(score, 20);
		
		add(name, BorderLayout.CENTER);
		add(score, BorderLayout.EAST);
		
	}
	
	/**
	 * Update the score on the panel
	 */
	
	public void updateScore(){
		
		score.setText(character.getScore() + "");
		
	}

}
