package audio;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import resources.Resources;

/**
 * Audio does not lend itself to JUnit very well
 * I have done my best
 * They all play over each other and it's horrible
 * 
 * @author Oliver Gratton
 *
 */
public class AudioJUnit
{

	MusicPlayer mp;
	Resources res;
	
	private static final long sec = 1000;

	@Before
	public void setUp()
	{
		res = new Resources();
		mp = new MusicPlayer(res, "grandma", "frog", "ultrastorm");
		assertFalse(mp.isAlive());
		assertFalse(mp.isRunning());
		mp.start();
	}

	/**
	 * Test the music player comes 'alive' after starting
	 */
	@Test
	public void testThreadAlive()
	{
		System.out.println("testing if the thread is alive");
		assertTrue(mp.isAlive());
	}

	/**
	 * Tests nextSong() method
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testTrackSkipping() throws InterruptedException
	{
		System.out.println("testing track skipping");
		// testing skipping tracks
		assertEquals("grandma", mp.nowPlaying());
		Thread.sleep(sec);
		mp.nextSong();
		assertEquals("frog", mp.nowPlaying());
		Thread.sleep(sec);
	}

	/**
	 * Test if volume is changed correctly
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testGain() throws InterruptedException
	{
		System.out.println("testing gain changing");
		assertEquals(0.0, mp.getGain(), 0);
		mp.setGain(-10);
		Thread.sleep(sec);
		assertEquals(-10.0, mp.getGain(), 0);
		mp.setGain(0);
	}

	/**
	 * Test the pausing of the music
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testPause() throws InterruptedException
	{
		System.out.println("testing pausing");
		mp.pauseMusic();
		assertTrue(mp.isPaused());
		Thread.sleep(sec);
		
		mp.resumeMusic();
		assertFalse(mp.isPaused());
		Thread.sleep(sec);
		
		mp.togglePlaying();
		assertTrue(mp.isPaused());
		Thread.sleep(sec);
		
		mp.togglePlaying();
		assertFalse(mp.isPaused());
		Thread.sleep(sec);
	}
	
	/**
	 * Tests muting
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMute() throws InterruptedException
	{
		mp.setGain(0);
		Thread.sleep(sec);
		
		System.out.println("testing muting");
		mp.mute();
		assertEquals(-100000, mp.getGain(), 0);
		Thread.sleep(sec);
		
		System.out.println("testing unmuting");
		mp.unmute();
		assertEquals(0, mp.getGain(), 0);
		Thread.sleep(sec);
		
	}

}
