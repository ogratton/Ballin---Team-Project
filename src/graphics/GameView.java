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
	private double sizeMultiplier = 1.92;
	private double positionMultiplier = 1;
	
	/**
	 * Create a new game view
	 * 
	 * @param characterModels
	 *            an ArrayList of all character models on the view
	 * @param mapModel
	 *            the model of the map on the view
	 */

	public GameView(ArrayList<CharacterModel> characterModels, MapModel mapModel) {
		super();
		this.characterModels = characterModels;
		this.mapModel = mapModel;

		points = new HashMap<CharacterModel, Point>();

		for (CharacterModel model : characterModels) {
			points.put(model, new Point((int) model.getX(), (int) model.getY()));
		}

		repaint();
	}

	/**
	 * Repaints the view
	 */

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		for (CharacterModel model : characterModels) {

			BufferedImage frame = null;

			int newX = (int) model.getX();
			int newY = (int) model.getY();

			if (newX != points.get(model).getX() || newY != points.get(model).getY()) {
				frame = model.getNextFrame(true);
				points.put(model, new Point(newX, newY));
			} else {
				frame = model.getNextFrame(false);
			}
			
			int actualX = (int)(newX * positionMultiplier);
			int actualY = (int)(newY * positionMultiplier);
			
			int sizeX = (int)(frame.getWidth() * sizeMultiplier);
			int sizeY = (int)(frame.getHeight() * sizeMultiplier);
			
			g.drawImage(frame, actualX, actualY, sizeX, sizeY, this);

			Toolkit.getDefaultToolkit().sync();

		}

	}

	/**
	 * Sets the scaling multiplier
	 * @param mult the multiplier
	 */
	
	public void setMultiplier(double mult){
		this.sizeMultiplier = mult;
		this.positionMultiplier = mult / 1.92;
		repaint();
	}
	
	/**
	 * Basically just calls repaint()
	 * Should be called using notifyObservers()
	 * whenever a player moves, or if the map changes in any way (if changing
	 * maps are a thing)
	 */

	@Override
	public void update(Observable o, Object arg) {
		repaint();

	}

}
