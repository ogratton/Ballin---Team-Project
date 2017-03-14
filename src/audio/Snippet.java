package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * A properly looping sound file
 * 
 * @author Oliver Gratton
 *
 */
//@Deprecated
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
				clip.stop();
				break;
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
		
		Thread.sleep(15000); // stops after 15 seconds
		
		snip.interrupt();
		
		System.out.println("stopped");
	}
}