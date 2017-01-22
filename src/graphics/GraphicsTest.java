package graphics;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import resources.Character;
import resources.Map;

public class GraphicsTest {

	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		frame.setSize((int)width, (int)height);
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		Character player = new Character();
		Map map = new Map(null, 0,0, 0, 0, null, null);
		
		GameComponent comp = new GameComponent(player, map);
		comp.setVisible(true);
		
		frame.add(comp);
		
	}
	
}
