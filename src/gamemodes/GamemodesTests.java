package gamemodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import resources.Character;
import resources.Map;
import resources.MapCosts;
import resources.Resources;

public class GamemodesTests {
	private Resources resources;
	private ArrayList<Integer> intendedScores;
	private GameModeFFA mode;
	private ArrayList<Character> winners;

	/**
	 * Create a list of players with 8 characters with score from 1 to 8 in a
	 * random order.
	 */
	@Before
	public void setUp() {
		resources = new Resources();
		resources.setMap(new Map(1200, 650, Map.World.SPACE, "asteroid"));
		new MapCosts(resources);
		// Create and add players
		refreshCharacters();
		intendedScores = new ArrayList<>();
		for (int i = 7; i >= 0; i--) {
			intendedScores.add(i);
		}
		winners = new ArrayList<>();
		winners.add(resources.getOrderedScores().get(0));
	}

	private void refreshCharacters() {
		resources.setPlayerList(new ArrayList<Character>());
		Character c;
		for (int i = 0; i < 8; i++) {
			c = new Character(Character.Class.WIZARD, i, "Player " + i);
			c.setScore(i);
			resources.addPlayerToList(c);
		}
		Collections.shuffle(resources.getPlayerList());
	}

	@After
	public void tearDown() {
		resources.setPlayerList(null);
		mode = null;
		resources = null;
	}

	/**
	 * Test if ordered scores are equal to the intended scores. Intended scores
	 * = [7,6,5,4,3,2,1,0]
	 */
	@Test
	public void testActionPerformed1() {
		ArrayList<Integer> scores = new ArrayList<>();
		for (Character c : resources.getOrderedScores()) {
			scores.add(c.getScore());
		}
		assertTrue(scores.equals(intendedScores));
	}

	/**
	 * Test Deathmatch.getWinners() - returns all characters with the highest
	 * score.
	 */
	@Test
	public void testActionPerformed2() {
		mode = new Deathmatch(resources);
		assertTrue(winners.equals(mode.getWinners()));
	}

	/**
	 * Test HotPotato.placeBomb() - zero characters should have a bomb on them
	 * before the function is called, exactly one character should have a bomb
	 * on them afterwards.
	 */
	@Test
	public void testActionPerformed3() {
		mode = new HotPotato(resources);
		assertTrue(numberOfBombs(resources.getPlayerList()) == 0);
		((HotPotato) mode).placeBomb();
		assertTrue(numberOfBombs(resources.getPlayerList()) == 1);
	}

	private int numberOfBombs(ArrayList<Character> cs) {
		int counter = 0;
		for (Character c : cs) {
			if (c.hasBomb()) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Test HotPotato.explodeBomb() - explodes the player with the bomb - should
	 * be one player with bomb before and zero afterwards.
	 */
	@Test
	public void testActionPerformed4() {
		mode = new HotPotato(resources);
		((HotPotato) mode).placeBomb();
		assertTrue(numberOfBombs(resources.getPlayerList()) == 1);
		((HotPotato) mode).explodeBomb();
		assertTrue(numberOfBombs(resources.getPlayerList()) == 0);
	}

	/**
	 * Test playersRemaining() (present in several game modes) - should be 8
	 * remaining at the start and once someone loses all of their lives there
	 * should be 7 remaining.
	 */
	@Test
	public void testActionPerformed5() {
		refreshCharacters();
		for (Character c : resources.getPlayerList()) {
			c.setLives(1);
		}
		mode = new HotPotato(resources);
		assertTrue(((HotPotato) mode).playersRemaining() == 8);
		resources.getPlayerList().get(0).setLives(0);
		assertTrue(((HotPotato) mode).playersRemaining() == 7);
	}

	/**
	 * Test setAllLives() (present in several game modes) - all characters
	 * should have the same number of lives.
	 */
	@Test
	public void testActionPerformed6() {
		mode = new LastManStanding(resources, 5);
		((LastManStanding) mode).setAllLives(3);
		boolean b = true;
		for (Character c : resources.getPlayerList()) {
			if (c.getLives() != 3) {
				b = false;
			}
		}
		assertTrue(b);
	}

	/**
	 * Test LastManStanding.getCombinedLives() - totals the number of lives of
	 * all players.
	 */
	@Test
	public void testActionPerformed7() {
		mode = new LastManStanding(resources, 5);
		((LastManStanding) mode).setAllLives(3);
		assertTrue(((LastManStanding) mode).getCombinedLives() == (3 * 8));
	}

	/**
	 * Test LastManStanding.isGameOver() - the game is over when only 1 player
	 * remains.
	 */
	@Test
	public void testActionPerformed8() {
		mode = new LastManStanding(resources, 5);
		((LastManStanding) mode).setAllLives(1);
		assertTrue(!((LastManStanding) mode).isGameOver());
		((LastManStanding) mode).setAllLives(0);
		resources.getPlayerList().get(0).setLives(1);
		assertTrue(((LastManStanding) mode).isGameOver());
	}

	/**
	 * Test Deathmatch.isGameOver() - the game is over when the timer
	 * (Resources.timer) is 0.
	 */
	@Test
	public void testActionPerformed9() {
		mode = new Deathmatch(resources);
		resources.setTimer(30);
		assertTrue(!mode.isGameOver());
		resources.setTimer(0);
		assertTrue(mode.isGameOver());
	}
}
