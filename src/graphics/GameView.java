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

@SuppressWarnings("serial")
public class GameView extends JPanel implements Observer {

	private HashMap<Character, Point> points;
	private BufferedImage mapSprite;
	private double multiplier = 1;

	private Resources resources;

	/**
	 * Create a new game view
	 * 
	 * @param characters
	 *            an ArrayList of all character models on the view
	 * @param mapModel
	 *            the model of the map on the view
	 */

	public GameView(Resources resources) {
		super();

		this.resources = resources;

		makeMap();

		points = new HashMap<Character, Point>();

		for (Character model : resources.getPlayerList()) {
			points.put(model, new Point((int) model.getX(), (int) model.getY()));
		}

		repaint();
	}
	
	public void makeMap(){
		mapSprite = Sprite.createMap(resources.getMap());
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

		for (Character character : resources.getPlayerList()) {

			if (character.isVisible()) {

				BufferedImage frame = null;

				int oldX = (int) points.get(character).getX();
				int oldY = (int) points.get(character).getY();

				int newX = (int) character.getX();
				int newY = (int) character.getY();
				
				frame = character.getNextFrame(oldX, oldY, newX, newY);
				points.put(character, new Point(newX, newY));

				int sizeX = (int) (frame.getWidth() * multiplier);
				int sizeY = (int) (frame.getHeight() * multiplier);

				int deathModifier = 0;

				if (character.isDead()) {
					int step = character.getDyingStep();

					if (step < sizeX * 4) {

						deathModifier = (int) (step * 0.25);
						sizeX -= deathModifier;
						sizeY -= deathModifier;
						character.incDyingStep();
					} else {
						sizeX = 0;
						sizeY = 0;
						character.setVisible(false);
					}

				}

				int centreX = (int) (newX - character.getRadius());
				int centreY = (int) (newY - character.getRadius());
				
				int actualX = (int) ((centreX + deathModifier / 2) * multiplier);
				int actualY = (int) ((centreY + deathModifier / 2) * multiplier);

				g.drawImage(frame, (int)actualX, (int)(actualY + offset), sizeX, sizeY, this);

				if (character.isDashing()) {

					int dashX = 0;
					int dashY = 0;

					int dashMult = 30;

					switch (character.getDirection()) {
					case N:
						dashX = newX - character.getRadius();
						dashY = newY - character.getRadius() + dashMult;
						break;
					case NE:
						dashX = newX - character.getRadius() - dashMult;
						dashY = newY - character.getRadius() + dashMult;
						break;
					case E:
						dashX = newX - character.getRadius() - dashMult;
						dashY = newY - character.getRadius();
						break;
					case SE:
						dashX = newX - character.getRadius() - dashMult;
						dashY = newY - character.getRadius() - dashMult;
						break;
					case S:
						dashX = newX - character.getRadius();
						dashY = newY - character.getRadius() - dashMult;
						break;
					case SW:
						dashX = newX - character.getRadius() + dashMult;
						dashY = newY - character.getRadius() - dashMult;
						break;
					case W:
						dashX = newX - character.getRadius() + dashMult;
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
