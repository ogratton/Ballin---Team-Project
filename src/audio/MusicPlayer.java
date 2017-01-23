package audio;

import java.util.ArrayList;

/**
 * A thread that will loop a playlist of audio files until thread death
 * 
 * Adapted from an online tutorial by:
 * 
 * @author Matthew Rogers
 * http://www.github.com/BossLetsPlays
 */
public class MusicPlayer implements Runnable
{

	private ArrayList<AudioFile> musicFiles;
	private int currentSongIndex;
	private boolean running;

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
			musicFiles.add(new AudioFile("./resources/audio/" + file + ".wav"));
		}

	}

	/**
	 * Play the next song if the current has finished
	 */
	@Override
	public void run()
	{
		running = true;
		AudioFile song = musicFiles.get(currentSongIndex);
		song.play();
		while (running)
		{
			if (!song.isPlaying())
			{
				currentSongIndex++;
				if (currentSongIndex >= musicFiles.size())
				{
					currentSongIndex = 0;
				}
				song = musicFiles.get(currentSongIndex);
				song.play();
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
