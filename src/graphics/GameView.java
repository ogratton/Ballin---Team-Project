package graphics;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class GameView extends JPanel implements Observer {

	private ArrayList<CharacterModel> characterModels;
	private MapModel mapModel;

	public GameView(ArrayList<CharacterModel> characterModels, MapModel mapModel) {
		super();
		this.characterModels = characterModels;
		this.mapModel = mapModel;
		repaint();
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		for (CharacterModel model : characterModels) {

			BufferedImage frame = model.getNextFrame();

			int x = (int) model.getX();
			int y = (int) model.getY();

			System.out.println("painting at " + x + ", " + y);

			g.drawImage(frame, x, y, this);

			Toolkit.getDefaultToolkit().sync();

		}

	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("observed!");
		repaint();

	}

}
