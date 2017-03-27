package gamemodes;

import java.util.ArrayList;

import graphics.Graphics;
import physics.Physics;
import resources.Character;
import resources.Resources;
import resources.Resources.Mode;

/**
 * Gamemode so I can test my pathfinding in peace
 * 
 * @author Oliver Gratton
 *
 */
public class DebugMode extends Thread implements GameModeFFA
{

	private Resources resources;
	@SuppressWarnings("unused")
	private boolean endGame = false;

	public DebugMode(Resources resources)
	{
		this.resources = resources;
		resources.mode = Mode.Debug;
		resources.gamemode = this;
	}

	/**
	 * No loop or anything, just start the physics and graphics and let it run
	 */
	public void run()
	{
		// Start game
		Physics p = new Physics(resources, false);
		p.start();
		//SwingUtilities.invokeLater(new Graphics(resources, null, false));

		Graphics g = new Graphics(resources, null, true); // run with debug mode on
		g.start();
	}

	/**
	 * @return just the first player in the list
	 */
	@Override
	public ArrayList<Character> getWinners()
	{
		return resources.getPlayerList();
	}

	/**
	 * debug never ends
	 * @return always false
	 */
	@Override
	public boolean isGameOver()
	{
		return false;
	}

	/**
	 * Useless for this gamemode
	 */
	@Override
	public void resetLives()
	{
		setAllLives(-1);
	}

	/**
	 * Set the number of lives for all players to the specified value
	 * 
	 * @param n
	 * Number of lives
	 */
	private void setAllLives(int n)
	{
		for (Character c : resources.getPlayerList())
		{
			c.setLives(n);
		}
	}

	@Override
	public void randomRespawn()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setEndGame(boolean b) {
		endGame = b;
	}
}
