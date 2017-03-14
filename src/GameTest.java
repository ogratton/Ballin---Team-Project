import java.awt.EventQueue;

import javax.swing.JFrame;

import audio.MusicPlayer;
import resources.Resources;

@Deprecated
public class GameTest extends JFrame {

	private static Resources resources;
	
	public GameTest() {
		
		resources = null;
		
		int WIDTH = 1000;
		int HEIGHT = 600;
		add(new Arena	(WIDTH, HEIGHT));
		setSize(WIDTH + 26, HEIGHT + 50);
		setResizable(false);

		setTitle("Game Test");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				GameTest ex = new GameTest();
				ex.setVisible(true);
			}
		});
		
		//new Thread(new MusicPlayer("grandma")).start();
		MusicPlayer mp = new MusicPlayer(resources, "guile", "pokemon");
		Thread mp_thread = new Thread(mp);
		mp_thread.start();
		
		try
		{
			while (true)
			{

				Thread.sleep(2000);
				mp.setGain(-10);

				Thread.sleep(2000);
				mp.setGain(5);
			}
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}