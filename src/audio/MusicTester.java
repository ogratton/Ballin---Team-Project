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
	
	private static Resources resources;
//	private static final int VOL_MAX = 100;
//	private static final int VOL_MIN = 0;
//	public static final int width = 300;
//	public static final int height = 300;
//	private static final Dimension buttonSize = new Dimension((int) (width * 0.8), (int) (height * 0.1));
//	private static final double sliderRatio = 0.25;
//	
//	private JSlider getMusicSlider() {
//		JSlider musicSlider = new JSlider(JSlider.VERTICAL, VOL_MIN, VOL_MAX, VOL_MAX);
//		customiseSlider(musicSlider);
//		if (!Resources.silent)
//		{
//			musicSlider.addChangeListener(e -> {
//			int volume = musicSlider.getValue();
//			if (volume == 0)
//				resources.getMusicPlayer().mute();
//			else
//				resources.getMusicPlayer().setGain((float) ((VOL_MAX - volume) * (-0.33)));
//		});
//		}
//		
//		return musicSlider;
//	}
//	
//	private void customiseComponent(JComponent comp, Dimension size, double ratio) {
//		comp.setMaximumSize(size);
//		alignToCenter(comp);
//		comp.setOpaque(false);
//	}
//	
//	private void alignToCenter(JComponent comp) {
//		comp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
//		comp.setAlignmentY(JComponent.CENTER_ALIGNMENT);
//	}
//	
//	private void customiseSlider(JSlider slider) {
//		customiseComponent(slider, buttonSize, sliderRatio);
//		slider.setMajorTickSpacing(20);
//		slider.setMinorTickSpacing(10);
//		slider.setPaintTicks(true);
//		slider.setPaintLabels(true);
//	}

	// TODO
	// should be a little window that lets you:
	// see current song
	// previous song, pause, next song
	// volume control
	// browse all songs
	
	// would need to make an observer to update track name when next comes on

	public MusicTester()
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
		resources.getMusicPlayer().start();
	}

	public void paintComponent(Graphics g)
	{

		g.clearRect(0, 0, this.getWidth(), this.getHeight()); // XXX this covers up the weird label bug
		label.setText(resources.getMusicPlayer().nowPlaying());

	}

	public static void main(String[] args)
	{

		Resources res = new Resources();

		// TODO get all when put music in its own folder
		//		String root = "./resources/audio/";
		//		File folder = new File(root);
		//		File[] listOfFiles = folder.listFiles();
		String[] titles = new String[] { "frog", "grandma", "guile", "pokemon", "swing", "thirty" };

		resources = res;
		
		res.setMusicPlayer(new MusicPlayer(res, titles));

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
				resources.getMusicPlayer().togglePlaying();
			}

			if (key == KeyEvent.VK_RIGHT)
			{
				resources.getMusicPlayer().nextSong();
			}
			if (key == KeyEvent.VK_LEFT)
			{
				resources.getMusicPlayer().previousSong();
			}

			repaint();
		}
	}

}
