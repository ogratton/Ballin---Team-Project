package graphics;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Character;
import resources.Map;

public class GameComponent extends JPanel {

	public GameComponent(Character character, Map map){
		
		JLabel label = new JLabel("hello");
		label.setHorizontalAlignment(JLabel.CENTER);
		setLayout(new BorderLayout());
		
		add(label, BorderLayout.CENTER);
		
		JButton button = new JButton("exit");
		button.addActionListener(e -> System.exit(0));
		add(button, BorderLayout.SOUTH);
		
	}
	
}
