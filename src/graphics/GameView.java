package graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class GameView extends JPanel implements Observer {

	private ArrayList<CharacterModel> characterModels;
	private MapModel mapModel;
	private HashMap<CharacterModel, Point> points;

	public GameView(ArrayList<CharacterModel> characterModels, MapModel mapModel) {
		super();
		this.characterModels = characterModels;
		this.mapModel = mapModel;
		
		points = new HashMap<CharacterModel, Point>();
		
		for(CharacterModel model : characterModels){
			points.put(model, new Point((int)model.getX(), (int)model.getY()));
		}
		
		repaint();
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		for (CharacterModel model : characterModels) {

			BufferedImage frame = null;
			
			int newX = (int)model.getX();
			int newY = (int)model.getY();
			
			if (newX != points.get(model).getX() || newY != points.get(model).getY()) {
				frame = model.getNextFrame(true);
				points.put(model, new Point(newX, newY));
			} else {
				frame = model.getNextFrame(false);
			}


			g.drawImage(frame, newX, newY, this);

			Toolkit.getDefaultToolkit().sync();

		}

	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();

	}

}
