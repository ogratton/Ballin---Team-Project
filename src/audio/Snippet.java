package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * BASIC
 * A properly looping sound file
 * Could all be done in a main method if you wanted
 * without threads but still using Thread.sleep()
 * 
 * @author Oliver Gratton
 *
 */
@Deprecated
public class Snippet extends Thread
{
	Clip clip;
	String title;
	
	
	public Snippet(String file)
	{
		try
		{
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("./resources/audio/" + file + ".wav"));
			clip = AudioSystem.getClip();
			clip.open(inputStream);
			title = file;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		// looping as long as this thread is alive
		while (true)
		{
			clip.start();
			try
			{
				Thread.sleep(clip.getMicrosecondLength() / 1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			} 
			clip.setFramePosition(0);
			System.out.println("reset");
		}
	}
	
	public static void main(String[] args) throws Exception
	{

		String file = "grandma";
		Snippet snip = new Snippet(file);
		snip.start();
	}
}