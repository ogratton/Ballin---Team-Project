package audio;

import java.util.ArrayList;

/**
 * A thread that will loop a playlist of audio files until thread death
 * 
 * Adapted from an online tutorial by Matthew Rogers:
 * http://www.github.com/BossLetsPlays
 * 
 * @author Oliver Gratton
 * 
 */
public class MusicPlayer implements Runnable
{

	private ArrayList<AudioFile> musicFiles;
	private int currentSongIndex;
	private boolean running;
	private boolean paused;
	private float gain;

	/**
	 * the gain before muting
	 */
	private float pre_mute_gain;

	/**
	 * this is where the frame position is stored after a pause
	 */
	private int paused_at;

	/**
	 * Initiate the player with a playlist of files
	 * 
	 * @param files the 'songs' to play
	 */
	public MusicPlayer(String... files)
	{
		musicFiles = new ArrayList<AudioFile>();
		for (String file : files)
		{
			musicFiles.add(new AudioFile("./resources/audio/" + file + ".wav", file));
		}

		currentSongIndex = 0;
		running = false;
		gain = 0;

	}

	/**
	 * replace the current playlist with another
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

		ArrayList<AudioFile> tempMusicFiles = new ArrayList<AudioFile>();
		for (String file : files)
		{
			tempMusicFiles.add(new AudioFile("./resources/audio/" + file + ".wav", file));
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
		if (!musicFiles.get(currentSongIndex).isStopped())
		{
			musicFiles.get(currentSongIndex).stop();
		}

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

		System.out.println("Now Playing: " + nowPlaying());
	}

	/**
	 * play the previous song in the playlist
	 */
	public void previousSong()
	{
		// stop the song that is playing, if it has not ended already
		if (!musicFiles.get(currentSongIndex).isStopped())
		{
			musicFiles.get(currentSongIndex).stop();
		}

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

		System.out.println("Now Playing: " + nowPlaying());
	}

	/**
	 * pause the current song and store where we paused it
	 */
	public void pause()
	{
		paused_at = musicFiles.get(currentSongIndex).pause();
		paused = true;
	}

	/**
	 * resume playing where we left off
	 */
	public void resume()
	{
		if (paused)
		{
			running = true;
			paused = false;
			musicFiles.get(currentSongIndex).resume(paused_at);
			paused_at = 0;
			System.out.println("Now Playing: " + nowPlaying());
		}

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
	 * @return urrent absolute gain value
	 */
	public float getGain()
	{
		return gain;
	}

	/**
	 * Play the next song if the current has finished
	 */
	@Override
	public void run()
	{
		running = true;
		musicFiles.get(currentSongIndex).play(gain);
		System.out.println("Now Playing: " + nowPlaying());
		while (running)
		{
			//if (!song.isPlaying() && !song.isPaused())
			if (musicFiles.get(currentSongIndex).isStopped())
			{
				nextSong();
			}

			// not necessary if AudioFile.playing is volatile
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

}
