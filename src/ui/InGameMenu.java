package ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import graphics.sprites.Sprite;
import resources.Map;

public class InGameMenu extends BaseMenu {

	public JPanel getInGameMenuPanel(int width, int height){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel map = new JLabel(new ImageIcon(Sprite.createMap(new Map(width, height, ""))));
		JPanel panel2 = new JPanel();
		BoxLayout box = new BoxLayout(panel2, BoxLayout.Y_AXIS);
		map.setLayout(new BorderLayout());
		panel2.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 5), new EmptyBorder(50, 50, 50, 50)));
		panel2.setLayout(box);
		panel2.setOpaque(false);
		addResumeToGameButton(panel2, panel);
		addOptionsButton(panel2);
		addExitButton(panel2);
		map.add(panel2, BorderLayout.CENTER);
		panel.add(map);
		return panel;
	}
	
}
