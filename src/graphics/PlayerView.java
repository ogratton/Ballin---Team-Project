package graphics;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class PlayerView extends JPanel implements Observer {

	private CharacterModel model;

	public PlayerView(CharacterModel model) {
		super();
		this.model = model;
		repaint();
	}

	public void paintComponent(Graphics g) {

		System.out.println();
		
		super.paintComponent(g);
		
		BufferedImage frame = model.getNextFrame();
		
		int x = (int) model.getX();
		int y = (int) model.getY();
		
		System.out.println("painting at " + x + ", " + y);
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(frame, x, y, this);

		Toolkit.getDefaultToolkit().sync();

	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("observed!");
		repaint();

	}

}
