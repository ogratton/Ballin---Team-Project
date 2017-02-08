package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import resources.Character;
import resources.Map;

public class GameView extends JPanel implements Observer {

	private ArrayList<Character> characters;
	private Map map;
	private HashMap<Character, Point> points;
	private double multiplier = 1;

	private static final int TILE_SIZEX = 74;
	private static final int TILE_SIZEY = 64;
	
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

		points = new HashMap<Character, Point>();

		for (Character model : characters) {
			points.put(model, new Point(cast(model.getX()), cast(model.getY())));
		}

		repaint();
	}

	/**
	 * Repaints the view
	 */

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		double screenWidth = getWidth();
		double screenHeight = getHeight();
		double offset = 0;

		double startX = -(TILE_SIZEX / 2);
		double startY = -(TILE_SIZEY / 2);
		double diffX = TILE_SIZEX + TILE_SIZEX/2;
		double diffY = TILE_SIZEY / 2;
		double gap = Math.sqrt(Math.pow(TILE_SIZEX / 2, 2) - Math.pow(TILE_SIZEY / 2, 2));
		
		if (screenWidth / screenHeight < (16.0 / 9.0)) {

			offset = 0.5 * (screenHeight - (screenWidth * (9.0 / 16.0)));

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, cast(screenWidth), cast(offset));
			g.fillRect(0, cast(screenHeight - offset), cast(screenWidth), cast(offset));
		}
		
		for(int i = 0; i < map.getTiles().size(); i++){
			for(int j = 0; j < map.getTiles().get(i).length; j++){
			
				BufferedImage sprite = map.getTileSprite(j, i);
				
				if(i % 2 == 0){
					
					int xPos = cast(multiplier * (startX + (diffX*j)));
					int yPos = cast(multiplier * (startY + (diffY*i)));
					
					
					
					g.drawImage(sprite, xPos, yPos, cast(TILE_SIZEX * multiplier), cast(TILE_SIZEY * multiplier), this);
					
					
				}
				else
				{
					
					int xPos = cast(multiplier * (startX + (diffX*j) + TILE_SIZEX / 2 + gap));
					int yPos = cast(multiplier * (startY + (diffY*i)));
					
					g.drawImage(sprite, xPos, yPos, cast(TILE_SIZEX * multiplier), cast(TILE_SIZEY * multiplier), this);
					
				}

				
			}
			
		}
		
		for (Character model : characters) {

			BufferedImage frame = null;

			int newX = cast(model.getX());
			int newY = cast(model.getY());

			if (newX != points.get(model).getX() || newY != points.get(model).getY()) {
				frame = model.getNextFrame(true);
				points.put(model, new Point(newX, newY));
			} else {
				frame = model.getNextFrame(false);
			}

			int actualX = cast(newX * multiplier);
			int actualY = cast(newY * multiplier);

			int sizeX = cast(frame.getWidth() * multiplier);
			int sizeY = cast(frame.getHeight() * multiplier);

			g.drawImage(frame, cast(actualX), cast(actualY + offset), sizeX, sizeY, this);

			Toolkit.getDefaultToolkit().sync();

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
	 * Actual effective casting of double to int, taking into account rounding up
	 * @param x
	 * @return
	 */
	
	private int cast(double x){
		if(x - (int) x > 0.5){
			return (int) x + 1;
		} else {
			return (int) x;
		}
	}

}
