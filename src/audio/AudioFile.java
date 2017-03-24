package audio;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import resources.Resources;

/**
 * A single audio file
 * Can be used for sound effects or music
 * 
 * Adapted from an online tutorial by Matthew Rogers:
 * http://www.github.com/BossLetsPlays
 * 
 * @author Oliver Gratton
 * 
 */
public class AudioFile implements LineListener
{
	private File soundFile;
	private String title;
	private AudioInputStream ais;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip clip;
	private FloatControl gainControl;

	/**
	 * Represents the 3 states the clip could be in
	 */
	private enum PlayState
	{
		PLAYING, PAUSED, STOPPED
	};

	private volatile PlayState playState;

	private Resources resources;
	private boolean noResources = false; // true if resources will be null

	/**
	 * Make a new audio file object
	 * 
	 * @param resources common resources object
	 * @param filepath path to file
	 * @param title displayable name of clip
	 */
	public AudioFile(Resources resources, String filepath, String title)
	{
		try
		{
			this.resources = resources;
			soundFile = new File(filepath);
			this.title = title;
			ais = AudioSystem.getAudioInputStream(soundFile);
			format = ais.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.addLineListener(this);
			clip.open(ais);
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
		}
	}

	/**
	 * Special constructor for when resources is not available
	 * 
	 * @param filename path to file
	 * @param title displayable name of clip
	 */
	public AudioFile(String filename, String title)
	{
		noResources = true;
		try
		{
			soundFile = new File(filename);
			this.title = title;
			ais = AudioSystem.getAudioInputStream(soundFile);
			format = ais.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.addLineListener(this);
			clip.open(ais);
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
		}
	}

	/**
	 * @return the title of the audio clip
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Default to playing with normal gain
	 */
	public void play()
	{
		play(0);
	}

	public void stop()
	{
		clip.stop();
	}

	/**
	 * Play the clip with specified gain level
	 * Relative to slider input defined in menu by user
	 * (0 is normal, -ve numbers decrease volume)
	 * 
	 * @param gain gain to apply to clip
	 */
	public void play(float gain)
	{
		if (playState != PlayState.PLAYING)
		{
			if (!noResources)
			{
				int offset = resources.getSFXGain() < 0 ? resources.getSFXGain() : 0; // sfx_gain is the gain set by the player in the settings (always <0)
				gain += offset;
			}
			if (gainControl != null) gainControl.setValue(gain);
			clip.start();
			playState = PlayState.PLAYING;
		}
	}

	/**
	 * @return Is the clip currently playing?
	 */
	public boolean isPlaying()
	{
		return (playState == PlayState.PLAYING);
	}

	/**
	 * @return Is the clip currently paused
	 */
	public boolean isPaused()
	{
		return (playState == PlayState.PAUSED);
	}

	/**
	 * Is the clip currently stopped (i.e. not playing but not paused)
	 * 
	 * @return
	 */
	public boolean isStopped()
	{
		return (playState == PlayState.STOPPED);
	}

	/**
	 * set absolute gain on the fly
	 * 
	 * @param gain +ve number for louder, -ve for lower
	 */
	public void setGain(float gain)
	{
		if (gainControl != null) gainControl.setValue(gain);
	}

	public float getGain()
	{
		if (gainControl != null) 
		{
			return gainControl.getValue();
		}
		else
		{
			return 0;
		}
		
	}

	/**
	 * Pause the clip
	 * 
	 * @return return the current position in the clip
	 */
	public int pause()
	{
		if (playState == PlayState.PLAYING)
		{
			playState = PlayState.PAUSED;
			clip.stop();
			clip.flush();
		}
		return clip.getFramePosition();
	}

	/**
	 * resume where we left
	 */
	public void resume(int pos, float gain)
	{
		// for a wav file at 44100hz, this sets it back a second
		// but seems to actually set it back where it really was
		int clip_length = clip.getFrameLength();
		int res_pos = pos - 44100;

		// accounting for a bug that can only be replicated with near-millisecond precision
		// pausing *ON* the last second of the clip
		// or something
		// replicate by pausing and resuming "grandma" alternately every second
		if (clip_length - res_pos <= 44100)
		{
			clip.stop();
			playState = PlayState.STOPPED;
			return;
		}
		clip.setFramePosition(res_pos);
		int offset = resources.getSFXGain() < 0 ? resources.getSFXGain() : 0; // sfx_gain is the gain set by the player in the settings (always <0)
		if (gainControl != null) gainControl.setValue(gain + offset);
		clip.start();
		playState = PlayState.PLAYING;
	}

	/**
	 * For detecting when the end of the file has been reached
	 */
	@Override
	public void update(LineEvent event)
	{
		if (event.getType() == LineEvent.Type.START)
		{
			// gets called every time the clip starts again
			playState = PlayState.PLAYING;
		}
		else if (event.getType() == LineEvent.Type.STOP)
		//		else if (clip.getMicrosecondLength() - clip.getMicrosecondPosition() <= 100) // test value
		{
			// gets called every time the clip is stopped/ends
			if (playState != PlayState.PAUSED)
			{
				clip.stop();
				clip.flush();
				clip.setFramePosition(0);
				playState = PlayState.STOPPED;
			}
		}
	}
}
