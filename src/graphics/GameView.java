package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;
import resources.Character;
import resources.Resources;

/**
 * Class where all the rendering happens
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class GameView extends JPanel implements Observer {

	private HashMap<Character, Point> points;
	private double ordinaryPlayerSize = 50;
	private double fullScreenPlayerSize;
	private double ordinaryMapHeight = 675;
	private double fullScreenMapHeight;
	private double ordinaryMapWidth = 1200;
	private double fullScreenMapWidth;
	private double fullScreenWindowWidth;
	private double fullScreenWindowHeight;
	private double currentPlayerSize;
	private double currentMapHeight;
	private double currentMapWidth;
	private double currentWindowHeight;
	private double currentWindowWidth;
	private double multiplier;
	private double currentMultiplier = 1;
	private boolean notSixteenNine;
	private boolean fullscreen = false;
	private double offset;
	private double currentOffset = 0;
	private BufferedImage mapSprite;
	private BufferedImage bigMapSprite;
	private BufferedImage currentMapSprite;

	// for debugging pathfinding
	private boolean debugPaths = false;
	private HashMap<Character, ArrayList<Point>> pointTrail;
	private LinkedList<Point> destList;
	private LinkedList<Point> fullDestList;

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
		destList = resources.getDestList();
		fullDestList = resources.getDestList();

		setUpSizes();

		for (Character player : resources.getPlayerList()) {
			points.put(player, new Point((int) player.getX(), (int) player.getY()));

			player.makeSizeSprites(multiplier);

			if (debugPaths) {
				pointTrail.put(player, new ArrayList<Point>());
				pointTrail.get(player).add(new Point((int) player.getX(), (int) player.getY()));
			}
		}

	}

	public void makeMap() {
		mapSprite = Sprite.createMap(resources.getMap());
	}

	private void setUpSizes() {

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		fullScreenWindowWidth = gd.getDisplayMode().getWidth();
		fullScreenWindowHeight = gd.getDisplayMode().getHeight();

		if (fullScreenWindowWidth / fullScreenWindowHeight < (16.0 / 9.0)) {

			offset = (int) (0.5 * (fullScreenWindowHeight - (fullScreenWindowWidth * (9.0 / 16.0))));
			notSixteenNine = true;
		}

		multiplier = fullScreenWindowWidth / ordinaryMapWidth;

		fullScreenMapWidth = ordinaryMapWidth * multiplier;
		fullScreenMapHeight = ordinaryMapHeight * multiplier;

		fullScreenPlayerSize = ordinaryPlayerSize * multiplier;

		currentMapWidth = ordinaryMapWidth;
		currentMapHeight = ordinaryMapHeight;
		currentPlayerSize = ordinaryPlayerSize;

		bigMapSprite = mapSprite;

		int w = mapSprite.getWidth();
		int h = mapSprite.getHeight();
		bigMapSprite = new BufferedImage((int) fullScreenMapWidth, (int) fullScreenMapWidth, mapSprite.getType());
		Graphics2D g = bigMapSprite.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(mapSprite, 0, 0, (int) fullScreenMapWidth, (int) fullScreenMapHeight, 0, 0, w, h, null);
		g.dispose();

		currentMapSprite = mapSprite;

	}

	/**
	 * Repaints the view
	 */

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		// clear the screen to prepare for the next frame

		g.clearRect(0, 0, (int) currentWindowWidth, (int) currentWindowHeight);

		// draw the map sprite (this is the same throughout a game)

		g.drawImage(currentMapSprite, 0, (int) currentOffset, this);

		/*
		 * destList = resources.getDestList();
		 * 
		 * for (Point p : destList) { if (!fullDestList.contains(p)) {
		 * fullDestList.add(p); } }
		 * 
		 * if (debugPaths) { g.setColor(Color.RED);
		 * 
		 * for (int i = 0; i < fullDestList.size() - 1; i++) {
		 * 
		 * g.drawLine((int) fullDestList.get(i).getX(), (int)
		 * fullDestList.get(i).getY(), (int) fullDestList.get(i + 1).getX(),
		 * (int) fullDestList.get(i + 1).getY());
		 * 
		 * } }
		 */

		// drawing each of the characters on the board

		for (Character character : resources.getPlayerList()) {

			// no point drawing invisible characters

			if (character.isVisible()) {

				// sometimes the sizes will differ between players (e.g. death)
				double adjustedPlayerSize = currentPlayerSize;

				BufferedImage frame = null;

				// compare old and new points

				int oldX = (int) points.get(character).getX();
				int oldY = (int) points.get(character).getY();

				int newX = (int) character.getX();
				int newY = (int) character.getY();

				// get the next frame of the character

				frame = character.getNextFrame(oldX, oldY, newX, newY, fullscreen);
				points.put(character, new Point(newX, newY));

				if (debugPaths) {
					pointTrail.get(character).add(new Point(newX, newY));
				}

				// if the character is dying they need to get smaller

				int deathModifier = 0;

				if (character.isDead()) {
					int step = character.getDyingStep();

					if (step < 50) {

						deathModifier = (int) (step);
						adjustedPlayerSize -= deathModifier;
						character.incDyingStep();
					} else {
						adjustedPlayerSize = 0;
						adjustedPlayerSize = 0;
						character.setVisible(false);
					}

				}

				// determine the actual centre of the character rather than
				// drawing from the origin

				int centreX = (int) (newX - character.getRadius());
				int centreY = (int) (newY - character.getRadius());

				// fancy mathemagic to get the right position even with death or
				// different screen size

				int actualX = (int) ((centreX + deathModifier / 2) * currentMultiplier);
				int actualY = (int) ((centreY + deathModifier / 2) * currentMultiplier);

				if (debugPaths) {
					g.setColor(Color.WHITE);

					ArrayList<Point> charPoints = pointTrail.get(character);

					for (int i = 0; i < charPoints.size() - 1; i++) {
						g.drawLine((int) (charPoints.get(i).getX() * currentMultiplier),
								(int) (charPoints.get(i).getY() * currentMultiplier + currentOffset),
								(int) (charPoints.get(i + 1).getX() * currentMultiplier),
								(int) (charPoints.get(i + 1).getY() * currentMultiplier + currentOffset));
					}
				}

				// draw the player!

				g.drawImage(frame, actualX, (int) (actualY + currentOffset), (int) adjustedPlayerSize,
						(int) adjustedPlayerSize, this);

				// draw the arrowhead for the player

				BufferedImage arrow = SheetDeets.getArrowFromPlayer(character.getPlayerNumber());

				g.drawImage(arrow, (int) (actualX),
						(int) (actualY + currentOffset - (50 * currentMultiplier) + deathModifier),
						(int) adjustedPlayerSize, (int) adjustedPlayerSize, this);

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

					g.drawImage(character.getDashSprite(fullscreen), (int) (dashX * currentMultiplier),
							(int) ((dashY * currentMultiplier) + currentOffset), (int) adjustedPlayerSize,
							(int) adjustedPlayerSize, this);
				}
			}

		}

		// draw black bars if the ratio is not 16:9

		if (notSixteenNine && fullscreen) {

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, (int) currentWindowWidth, (int) currentOffset);
			g.fillRect(0, (int) (currentWindowHeight - currentOffset), (int) currentWindowWidth, (int) currentOffset);
		}

		Toolkit.getDefaultToolkit().sync();
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
	 * Set the correct sizes for sprites based on whether the game is fullscreen
	 * or not
	 * 
	 * @param fullscreen
	 *            whether the game is fullscreen
	 */

	public void setFullScreen(boolean fullscreen) {

		if (fullscreen) {
			currentMapHeight = fullScreenMapHeight;
			currentMapWidth = fullScreenMapWidth;
			currentPlayerSize = fullScreenPlayerSize;
			currentWindowHeight = fullScreenWindowHeight;
			currentWindowWidth = fullScreenWindowWidth;
			currentOffset = offset;
			currentMultiplier = multiplier;
			currentMapSprite = bigMapSprite;
			this.fullscreen = true;
		} else {
			currentMapHeight = ordinaryMapHeight;
			currentMapWidth = ordinaryMapWidth;
			currentPlayerSize = ordinaryPlayerSize;
			currentWindowHeight = ordinaryMapHeight;
			currentWindowWidth = ordinaryMapWidth;
			currentOffset = 0;
			currentMultiplier = 1;
			currentMapSprite = mapSprite;
			this.fullscreen = false;
		}
	}

}
