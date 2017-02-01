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

import resources.Character;

public class GameViewWithoutCharacterModel extends JPanel implements Observer {

	private ArrayList<Character> characters;
	private MapModel mapModel;
	private HashMap<Character, Point> points;
	private double multiplier = 1;
	
	/**
	 * Create a new game view
	 * 
	 * @param characters
	 *            an ArrayList of all character models on the view
	 * @param mapModel
	 *            the model of the map on the view
	 */

	public GameViewWithoutCharacterModel(ArrayList<Character> characters, MapModel mapModel) {
		super();
		this.characters = characters;
		this.mapModel = mapModel;

		points = new HashMap<Character, Point>();

		for (Character model : characters) {
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

		for (Character model : characters) {

			BufferedImage frame = null;

			int newX = (int) model.getX();
			int newY = (int) model.getY();

			if (newX != points.get(model).getX() || newY != points.get(model).getY()) {
				frame = model.getNextFrame(true);
				points.put(model, new Point(newX, newY));
			} else {
				frame = model.getNextFrame(false);
			}
			
			int actualX = (int)(newX * multiplier);
			int actualY = (int)(newY * multiplier);
			
			int sizeX = (int)(frame.getWidth() * multiplier);
			int sizeY = (int)(frame.getHeight() * multiplier);
			
			g.drawImage(frame, actualX, actualY, 2 * sizeX, 2 * sizeY, this);

			Toolkit.getDefaultToolkit().sync();

		}

	}

	/**
	 * Sets the scaling multiplier
	 * @param mult the multiplier
	 */
	
	public void setMultiplier(double mult){
		this.multiplier = mult;
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
