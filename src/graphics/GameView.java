package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;
import resources.Character;
import resources.Map;

@SuppressWarnings("serial")
public class GameView extends JPanel implements Observer {

	private ArrayList<Character> characters;
	private Map map;
	private HashMap<Character, Point> points;
	private BufferedImage mapSprite;
	private double multiplier = 1;

	/**
	 * Create a new game view
	 * 
	 * @param characters
	 *            an ArrayList of all character models on the view
	 * @param mapModel
	 *            the model of the map on the view
	 */

	public GameView(ArrayList<Character> characters, Map map) {
		super();
		this.characters = characters;
		this.map = map;

		mapSprite = Sprite.createMap(map);

		points = new HashMap<Character, Point>();

		for (Character model : characters) {
			points.put(model, new Point((int)model.getX(), (int)model.getY()));
		}

		repaint();
	}

	/**
	 * Repaints the view
	 */

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		double screenWidth = getWidth();
		double screenHeight = getHeight();
		double offset = 0;
		boolean notSixteenNine = false;

		if (screenWidth / screenHeight < (16.0 / 9.0)) {

			offset = 0.5 * (screenHeight - (screenWidth * (9.0 / 16.0)));
			notSixteenNine = true;
		}

		g.drawImage(mapSprite, 0, (int) offset, (int) (mapSprite.getWidth() * multiplier),
				(int) (mapSprite.getHeight() * multiplier), this);

		for (Character model : characters) {

			BufferedImage frame = null;

			int oldX = (int)points.get(model).getX();
			int oldY = (int)points.get(model).getY();
			
			int newX = (int)model.getX();
			int newY = (int)model.getY();

			frame = model.getNextFrame(oldX, oldY, newX, newY);
			points.put(model, new Point(newX, newY));

			int actualX = (int)(newX * multiplier);
			int actualY = (int)(newY * multiplier);

			int sizeX = (int)(frame.getWidth() * multiplier);
			int sizeY = (int)(frame.getHeight() * multiplier);

			g.drawImage(frame, cast(actualX), cast(actualY + offset), sizeX, sizeY, this);

			if(model.isDashing()){
				
				int dashX = 0;
				int dashY = 0;
				
				switch(model.getDirection()){
				case N:
					dashX = newX;
					dashY = newY + 50;
					break;
				case NE:
					dashX = newX - 50;
					dashY = newY + 50;
					break;
				case E:
					dashX = newX - 50;
					dashY = newY;
					break;
				case SE:
					dashX = newX - 50;
					dashY = newY - 50;
					break;
				case S:
					dashX = newX;
					dashY = newY - 50;
					break;
				case SW:
					dashX = newX - 50;
					dashY = newY - 50;
					break;
				case W:
					dashX = newX + 50;
					dashY = newY;
					break;
				case NW:
					dashX = newX + 50;
					dashY = newY + 50;
					break;
				case STILL:
					break;
					
				}
				
				g.drawImage(Sprite.getSprite(SheetDeets.MISC.getSpriteSheet(), 0, 0, 50, 50), (int)((dashX * multiplier) + offset), (int)((dashY * multiplier) + offset), (int)(50 * multiplier), (int)(50 * multiplier), this);
			}
			
			Toolkit.getDefaultToolkit().sync();

		}

		if (notSixteenNine) {

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, (int) screenWidth, (int) offset);
			g.fillRect(0, (int) (screenHeight - offset), (int) screenWidth, (int) offset);
		}
	}

	/**
	 * Sets the scaling multiplier
	 * 
	 * @param mult
	 *            the multiplier
	 */

	public void setMultiplier(double mult) {
		this.multiplier = mult;
		repaint();
	}

	/**
	 * Basically just calls repaint() Should be called using notifyObservers()
	 * whenever a player moves, or if the map changes in any way (if changing
	 * maps are a thing)
	 */

	@Override
	public void update(Observable o, Object arg) {
		repaint();

	}

	/**
	 * Actual effective casting of double to int, taking into account rounding
	 * up
	 * 
	 * @param x
	 * @return
	 */

	private int cast(double x) {
		if (x - (int) x > 0.5) {
			return (int) x + 1;
		} else {
			return (int) x;
		}
	}

}
