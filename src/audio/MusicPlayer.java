package audio;

import java.util.ArrayList;
import java.util.Hashtable;

import resources.FilePaths;
import resources.Resources;

/**
 * A thread that will loop a playlist of audio files until thread death
 * 
 * Adapted from an online tutorial by Matthew Rogers:
 * http://www.github.com/BossLetsPlays
 * 
 * @author Oliver Gratton
 * 
 */
public class MusicPlayer extends Thread
{

	private ArrayList<AudioFile> musicFiles;
	private volatile int currentSongIndex;
	private volatile boolean running;
	private volatile boolean paused;
	private float gain;

	/**
	 * the gain before muting
	 */
	private float pre_mute_gain;

	/**
	 * this is where the frame position is stored after a pause
	 */
	private int paused_at;
	
	private Hashtable<String, AudioFile> trackDict;

	/**
	 * Initiate the player with a playlist of files
	 * These files will be all the files it can play in the object's life,
	 * so choose well
	 * 
	 * @param files the 'songs' to play
	 */
	public MusicPlayer(Resources resources, String... files)
	{		
		musicFiles = new ArrayList<AudioFile>();
		trackDict = new Hashtable<String, AudioFile>();
		
		for (String file : files)
		{
			AudioFile af = new AudioFile(resources, FilePaths.music + file + ".wav", file);
			musicFiles.add(af);
			trackDict.put(file, af);
		}

		currentSongIndex = 0;
		running = false;
		gain = 0;

	}
	
	/**
	 * Play the next song if the current has finished
	 */
	@Override
	public void run()
	{
		running = true;
		musicFiles.get(currentSongIndex).play(gain);
//		System.out.println("Now Playing: " + nowPlaying());
		while (true)
		{
			
			if (running)
			{
//				System.out.println("music is running");
				if (musicFiles.get(currentSongIndex).isStopped())
				{
					nextSong();
				}
			}

			// not necessary if AudioFile.playState is volatile
			// or there's other stuff going on to keep pc busy
			// but good to have
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * stop music and replace the current playlist with another
	 * this will need to be followed by a resume call
	 * 
	 * @param files filenames (minus wav extension) of songs
	 */
	public void changePlaylist(String... files)
	{
		musicFiles.get(currentSongIndex).stop();
		// clean reset
		paused_at = 0;
		currentSongIndex = 0;
		running = false;
		paused = true;

		ArrayList<AudioFile> tempMusicFiles = new ArrayList<AudioFile>();
		for (String file : files)
		{
			if (trackDict.get(file) != null)
			{
				tempMusicFiles.add(trackDict.get(file));
			}
			else
			{
				System.err.println("Unrecognised track: " + file);
			}
			
		}

		musicFiles = tempMusicFiles;
	}

	/**
	 * @return the names of the songs in the current playlist
	 */
	public ArrayList<String> viewPlaylist()
	{
		ArrayList<String> titles = new ArrayList<String>();
		for (int i = 0; i < musicFiles.size(); i++)
		{
			titles.add(musicFiles.get(i).getTitle());
		}
		return titles;
	}

	/**
	 * @return the title of the current
	 */
	public String nowPlaying()
	{
		return musicFiles.get(currentSongIndex).getTitle();
	}

	/**
	 * play the next song in the playlist
	 */
	public void nextSong()
	{
		// stop the song that is playing, if it has not ended already
//		if (!musicFiles.get(currentSongIndex).isStopped())
//		{
			musicFiles.get(currentSongIndex).stop();
//		}

		// remove any previous pause data
		paused_at = 0;

		// wrap index around (playlist loops)
		currentSongIndex++;
		if (currentSongIndex >= musicFiles.size())
		{
			currentSongIndex = 0;
		}

		// play the new song
		musicFiles.get(currentSongIndex).play(gain);

//		System.out.println("Now Playing: " + nowPlaying());
	}

	/**
	 * play the previous song in the playlist
	 */
	public void previousSong()
	{
		// stop the song that is playing, if it has not ended already
//		if (!musicFiles.get(currentSongIndex).isStopped())
//		{
			musicFiles.get(currentSongIndex).stop();
//		}

		// remove any previous pause data
		paused_at = 0;

		// wrap index around (playlist loops)
		currentSongIndex--;
		if (currentSongIndex < 0)
		{
			currentSongIndex = musicFiles.size() - 1;
		}

		// play the new song
		musicFiles.get(currentSongIndex).play(gain);

//		System.out.println("Now Playing: " + nowPlaying());
	}

	/**
	 * pause the current song and store where we paused it
	 */
	public void pauseMusic()
	{
		paused_at = musicFiles.get(currentSongIndex).pause();
		paused = true;
	}

	/**
	 * resume playing where we left off
	 */
	public void resumeMusic()
	{
		if (paused)
		{
			running = true;
			paused = false;
			musicFiles.get(currentSongIndex).resume(paused_at, gain);
			paused_at = 0;
//			System.out.println("Now Playing: " + nowPlaying());
		}
	}
	
	/**
	 * Pause if playing, play if paused
	 */
	public void togglePlaying()
	{
		if(paused)
		{
			resumeMusic();
		}
		else
		{
			pauseMusic();
		}
	}
	
	/**
	 * Is the music paused?
	 * @return
	 */
	public boolean isPaused()
	{
		return paused;
	}
	
	/**
	 * Is the music player running
	 * (Potentially deprecated variable)
	 * @return
	 */
	public boolean isRunning()
	{
		return running;
	}

	/**
	 * mute the music
	 */
	public void mute()
	{
		pre_mute_gain = gain;
		gain = -100000;
		setGain(gain);
	}

	/**
	 * unmute the music and return it to its previous volume
	 */
	public void unmute()
	{
		gain = pre_mute_gain;
		setGain(gain);
	}

	/**
	 * set the new gain now and for future clips
	 * 
	 * @param gain new gain
	 */
	public void setGain(float gain)
	{
		this.gain = gain;
		musicFiles.get(currentSongIndex).setGain(gain);
	}

	/**
	 * @return current absolute gain value
	 */
	public float getGain()
	{
		return gain;
	}

}
