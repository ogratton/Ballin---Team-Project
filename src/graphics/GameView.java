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
import resources.Resources;

/**
 * Class where all the rendering happens
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class GameView extends JPanel implements Observer {

	private HashMap<Character, Point> points;
	private BufferedImage mapSprite;
	private double multiplier = 1;

	// for debugging pathfinding
	private boolean debugPaths = false;
	private HashMap<Character, ArrayList<Point>> pointTrail;

	private Resources resources;

	/**
	 * Create a new game view
	 * 
	 * @param characters
	 *            an ArrayList of all character models on the view
	 * @param mapModel
	 *            the model of the map on the view
	 */

	public GameView(Resources resources, boolean debugPaths) {
		super();

		this.resources = resources;
		this.debugPaths = debugPaths;

		makeMap();

		points = new HashMap<Character, Point>();

		pointTrail = new HashMap<Character, ArrayList<Point>>();

		for (Character model : resources.getPlayerList()) {
			points.put(model, new Point((int) model.getX(), (int) model.getY()));

			if (debugPaths) {
				pointTrail.put(model, new ArrayList<Point>());
				pointTrail.get(model).add(new Point((int) model.getX(), (int) model.getY()));
			}
		}

		repaint();
	}

	public void makeMap() {
		mapSprite = Sprite.createMap(resources.getMap());
		repaint();
	}

	/**
	 * Repaints the view
	 */

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		// clear the screen to prepare for the next frame
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		// get screen size, important for retaining 16:9 ratio
		
		double screenWidth = getWidth();
		double screenHeight = getHeight();
		double offset = 0;
		boolean notSixteenNine = false;

		if (screenWidth / screenHeight < (16.0 / 9.0)) {

			offset = 0.5 * (screenHeight - (screenWidth * (9.0 / 16.0)));
			notSixteenNine = true;
		}

		// draw the map sprite (this is the same throughout a game)
		
		g.drawImage(mapSprite, 0, (int) offset, (int) (mapSprite.getWidth() * multiplier),
				(int) (mapSprite.getHeight() * multiplier), this);

		// drawing each of the characters on the board
		
		for (Character character : resources.getPlayerList()) {

			// no point drawing invisible characters
			
			if (character.isVisible()) {

				BufferedImage frame = null;
				
				// compare old and new points

				int oldX = (int) points.get(character).getX();
				int oldY = (int) points.get(character).getY();

				int newX = (int) character.getX();
				int newY = (int) character.getY();

				// get the next frame of the character
				
				frame = character.getNextFrame(oldX, oldY, newX, newY);
				points.put(character, new Point(newX, newY));

				if (debugPaths) {
					pointTrail.get(character).add(new Point(newX, newY));
				}
				
				// determine the size of the player sprite
				
				int sizeX = (int) (frame.getWidth() * multiplier);
				int sizeY = (int) (frame.getHeight() * multiplier);

				// if the character is dying they need to get smaller
				
				int deathModifier = 0;

				if (character.isDead()) {
					int step = character.getDyingStep();

					if (step < 50) {

						deathModifier = (int) (step);
						sizeX -= deathModifier;
						sizeY -= deathModifier;
						character.incDyingStep();
					} else {
						sizeX = 0;
						sizeY = 0;
						character.setVisible(false);
					}

				}

				// determine the actual centre of the character rather than drawing from the origin
				
				int centreX = (int) (newX - character.getRadius());
				int centreY = (int) (newY - character.getRadius());
				
				// fancy mathemagic to get the right position even with death or different screen size
				
				int actualX = (int) ((centreX + deathModifier / 2) * multiplier);
				int actualY = (int) ((centreY + deathModifier / 2) * multiplier);

				if (debugPaths) {
					g.setColor(Color.WHITE);

					ArrayList<Point> charPoints = pointTrail.get(character);

					for (int i = 0; i < charPoints.size() - 1; i++) {
						g.drawLine((int) (charPoints.get(i).getX() * multiplier),
								(int) (charPoints.get(i).getY() * multiplier + offset),
								(int) (charPoints.get(i + 1).getX() * multiplier),
								(int) (charPoints.get(i + 1).getY() * multiplier + offset));
					}
				}

				// draw the player!
				
				g.drawImage(frame, (int) actualX, (int) (actualY + offset), sizeX, sizeY, this);
				
				// draw the arrowhead for the player
				
				BufferedImage arrow = SheetDeets.getArrowFromPlayer(character.getPlayerNumber());
				
				g.drawImage(arrow, (int)(actualX), (int) (actualY + offset - (50 * multiplier) + deathModifier), (int)(sizeX), (int)(sizeY), this);
				
				// if the player is dashing, draw the fire sprite
				
				if (character.isDashing()) {

					int dashX = 0;
					int dashY = 0;

					int dashMult = 30;

					switch (character.getDirection()) {
					case N:
						dashX = newX - character.getRadius();
						dashY = newY - character.getRadius() + dashMult + 15;
						break;
					case NE:
						dashX = newX - character.getRadius() - dashMult;
						dashY = newY - character.getRadius() + dashMult;
						break;
					case E:
						dashX = newX - character.getRadius() - dashMult - 15;
						dashY = newY - character.getRadius();
						break;
					case SE:
						dashX = newX - character.getRadius() - dashMult;
						dashY = newY - character.getRadius() - dashMult;
						break;
					case S:
						dashX = newX - character.getRadius();
						dashY = newY - character.getRadius() - dashMult - 15;
						break;
					case SW:
						dashX = newX - character.getRadius() + dashMult;
						dashY = newY - character.getRadius() - dashMult;
						break;
					case W:
						dashX = newX - character.getRadius() + dashMult + 15;
						dashY = newY - character.getRadius();
						break;
					case NW:
						dashX = newX - character.getRadius() + dashMult;
						dashY = newY - character.getRadius() + dashMult;
						break;
					case STILL:
						break;

					}

					g.drawImage(character.getDashSprite(), (int) (dashX * multiplier),
							(int) ((dashY * multiplier) + offset), (int) (50 * multiplier), (int) (50 * multiplier),
							this);
				}
			}

		}
		
		// draw black bars if the ratio is not 16:9

		if (notSixteenNine) {

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, (int) screenWidth, (int) offset);
			g.fillRect(0, (int) (screenHeight - offset), (int) screenWidth, (int) offset);
		}

		Toolkit.getDefaultToolkit().sync();
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

}
