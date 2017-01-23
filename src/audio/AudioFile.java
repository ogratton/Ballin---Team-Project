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

/**
 * A single audio file
 * 
 * Adapted from an online tutorial by:
 * 
 * @author Matthew Rogers
 * http://www.github.com/BossLetsPlays
 */
public class AudioFile implements LineListener
{
	private File soundFile;
	private AudioInputStream ais;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip clip;
	private FloatControl gainControl;
	private volatile boolean playing;

	/**
	 * Make a new audio file object
	 * 
	 * @param filename fully qualified filename/path of the audio file for this
	 * object
	 */
	public AudioFile(String filename)
	{
		try
		{
			soundFile = new File(filename);
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
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Default to playing with normal gain
	 */
	public void play()
	{
		play(0);
	}

	/**
	 * Play the clip with specified gain level
	 * (0 is normal, -ve numbers decrease volume)
	 * 
	 * @param gain gain to apply to clip
	 */
	public void play(float gain)
	{
		if (!playing)
		{
			gainControl.setValue(gain);
			clip.start();
			playing = true;
		}
	}

	/**
	 * @return Is the clip currently playing?
	 */
	public boolean isPlaying()
	{
		return playing;
	}

	/**
	 * For detecting when the end of the file has been reached
	 */
	@Override
	public void update(LineEvent event)
	{
		if (event.getType() == LineEvent.Type.START)
		{
			playing = true;
		}
		else if (event.getType() == LineEvent.Type.STOP)
		{
			clip.stop();
			clip.flush();
			clip.setFramePosition(0);
			playing = false;
		}
	}
}
