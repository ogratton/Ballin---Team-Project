import java.awt.EventQueue;

import javax.swing.JFrame;

import audio.MusicPlayer;

public class GameTest extends JFrame {

	public GameTest() {
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
		new Thread(new MusicPlayer("grandma")).start();
	}
}