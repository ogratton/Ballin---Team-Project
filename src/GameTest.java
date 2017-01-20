import java.awt.EventQueue;
import javax.swing.JFrame;

public class GameTest extends JFrame {

	public GameTest() {
		add(new Arena());

		setSize(1000, 600);
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
	}
}