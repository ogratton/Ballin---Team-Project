package audio;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Resources;

/**
 * A little app to test the functionality of MusicPlayer
 * 
 * @author Oliver Gratton
 *
 */
@SuppressWarnings("serial")
public class MusicTester extends JPanel
{
	private static JLabel label;
	
	private static MusicPlayer mp;


	public MusicTester()
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
		mp.start();
	}

	public void paintComponent(Graphics g)
	{

		g.clearRect(0, 0, this.getWidth(), this.getHeight()); // XXX this covers up the weird label bug
		label.setText(mp.nowPlaying());

	}

	public static void main(String[] args)
	{

		Resources res = new Resources();

		String[] titles = new String[] { "frog", "grandma", "swing", "thirty", "ultrastorm", "ultrastorm30" };
		
		mp = new MusicPlayer(res, titles);

		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(255, 200);
		frame.add(new MusicTester(), BorderLayout.CENTER);
		label = new JLabel();
		label.setHorizontalAlignment(JLabel.CENTER);
		frame.add(label, BorderLayout.SOUTH);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private class TAdapter extends KeyAdapter
	{
		@Override
		public void keyReleased(KeyEvent e)
		{

		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ESCAPE)
			{

				System.exit(0);
			}

			if (key == KeyEvent.VK_SPACE)
			{
				mp.togglePlaying();
			}

			if (key == KeyEvent.VK_RIGHT)
			{
				mp.nextSong();
			}
			if (key == KeyEvent.VK_LEFT)
			{
				mp.previousSong();
			}

			repaint();
		}
	}

}
