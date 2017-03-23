package audio;

import resources.Resources;

/**
 * Tells a music player when to change song
 * 
 * @author Oliver Gratton
 *
 */
public class MusicHandler extends Thread
{
	private MusicPlayer musicPlayer;
	private Resources resources;
	
	public MusicHandler(MusicPlayer musicPlayer, Resources resources)
	{
		this.musicPlayer = musicPlayer;
		this.resources = resources;
	}
	
	public void run()
	{
		while (true) {
			// needs to look at resources and see when gamemodes are finished
			String currentSong = musicPlayer.nowPlaying();
			String proposedSong = resources.getSong();
			if (!currentSong.equals(proposedSong)) {
				changeSong(proposedSong);
			} 
			
			try 
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
			}
		}
	}
	
	
	
	
	
	/**
	 * Set the playlist to this single song
	 * @param songName
	 */
	public void changeSong(String songName)
	{
//		musicPlayer.pauseMusic(); // TODO necessary?
		musicPlayer.changePlaylist(songName);
		musicPlayer.resumeMusic();
	}
	
	/**
	 * Change the songs playing
	 * (Supports playlists but should use single songs for this really)
	 * @param songNames array of titles of tracks
	 */
	public void changeSongs(String[] songNames)
	{
		musicPlayer.changePlaylist(songNames);
	}
	
	/**
	 * The resources objects change from game to game
	 * So this is how we update it
	 * @param res new resources object
	 */
	public void setResources(Resources res)
	{
		resources = res;
	}

}
