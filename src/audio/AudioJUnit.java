package audio;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import resources.Resources;

/**
 * TODO Check this when I actually have speakers/headphones to hand
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
		assertTrue(mp.isAlive());
	}

	/**
	 * TODO these are a bit broken, it seems Test nextSong() and previousSong()
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testTrackSkipping() throws InterruptedException
	{

		// testing skipping tracks
		assertEquals("grandma", mp.nowPlaying());
		Thread.sleep(sec);
		mp.nextSong();
		assertEquals("frog", mp.nowPlaying());
//		// Thread.sleep(sec);
//		mp.previousSong();
//		System.out.println(mp.nowPlaying());
//		assertEquals("grandma", mp.nowPlaying());
//		// Thread.sleep(sec);

	}

	/**
	 * Test if volume is changed correctly
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testGain() throws InterruptedException
	{
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
		
		mp.mute();
		assertEquals(-100000, mp.getGain(), 0);
		Thread.sleep(sec);
		
		mp.unmute();
		assertEquals(0, mp.getGain(), 0);
		Thread.sleep(sec);
		
	}

}
