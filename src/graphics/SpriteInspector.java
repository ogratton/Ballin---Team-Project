package graphics;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import resources.Character;
import resources.Resources;

public class SpriteInspector {

	public static void main(String[] args) {

		Resources resources = new Resources();

		JFrame frame = new JFrame();
		frame.setSize(250, 250);
		frame.add(new Panel(resources));
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

	}

}

@SuppressWarnings("serial")
class Panel extends JPanel implements ActionListener {

	private Resources resources;

	int x = 0;
	int y = 0;

	public Panel(Resources resources) {

		this.resources = resources;

		Character player = new Character(Character.Class.ARCHER, 1);
		resources.addPlayerToList(player);

		addKeyListener(new TAdapter());
		setFocusable(true);

		Timer timer = new Timer(10, this);
		timer.start();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.clearRect(0, 0, 250, 250);
		g.drawImage(resources.getPlayerList().get(0).getNextFrame(0, 0, x, y, false), 85, 85, 50, 50, this);

		Character character = resources.getPlayerList().get(0);

		int dashX = 0;
		int dashY = 0;

		int newX = 85 + character.getRadius();
		int newY = 85 + character.getRadius();

		int dashMult = 30;

		if (resources.getPlayerList().get(0).isDashing()) {
			switch (character.getDirection()) {
			case N:
				dashX = newX - character.getRadius();
				dashY = newY - character.getRadius() + dashMult + 15;
				break;
			case NE:
				dashX = newX - character.getRadius() - dashMult;
				dashY = newY - character.getRadius() + dashMult;
				break;
			case E:
				dashX = newX - character.getRadius() - dashMult - 15;
				dashY = newY - character.getRadius();
				break;
			case SE:
				dashX = newX - character.getRadius() - dashMult;
				dashY = newY - character.getRadius() - dashMult;
				break;
			case S:
				dashX = newX - character.getRadius();
				dashY = newY - character.getRadius() - dashMult - 15;
				break;
			case SW:
				dashX = newX - character.getRadius() + dashMult;
				dashY = newY - character.getRadius() - dashMult;
				break;
			case W:
				dashX = newX - character.getRadius() + dashMult + 15;
				dashY = newY - character.getRadius();
				break;
			case NW:
				dashX = newX - character.getRadius() + dashMult;
				dashY = newY - character.getRadius() + dashMult;
				break;
			case STILL:
				break;

			}

			if(dashX != 0 && dashY != 0)
				g.drawImage(character.getDashSprite(false, character.getDashDirection()), dashX, dashY, 50, 50, this);
		}

	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {
				x = 0;
			}
			if (key == KeyEvent.VK_D) {
				x = 0;
			}
			if (key == KeyEvent.VK_W) {
				y = 0;
			}
			if (key == KeyEvent.VK_S) {
				y = 0;
			}
			if (key == KeyEvent.VK_SHIFT) {
				resources.getPlayerList().get(0).setDashing(false);
			}

			resources.getPlayerList().get(0).setDirection(0, 0, 0, 0);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {

				x = -1;
			}
			if (key == KeyEvent.VK_D) {
				x = 1;
			}
			if (key == KeyEvent.VK_W) {
				y = -1;
			}
			if (key == KeyEvent.VK_S) {
				y = 1;
			}
			if (key == KeyEvent.VK_SHIFT) {
				resources.getPlayerList().get(0).setDashing(true);
			}
			if (key == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}

			resources.getPlayerList().get(0).setDirection(0, 0, x, y);

		}
	}
}