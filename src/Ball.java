import java.awt.event.KeyEvent;

public class Ball {

	private int dx;
	private int dy;
	private int x;
	private int y;
	private final int speed;

	public Ball() {
		this.x = 40;
		this.y = 60;
		this.speed = 2;
	}

	public void move() {
		x += dx;
		y += dy;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			dx = -speed;
		}
		if (key == KeyEvent.VK_RIGHT) {
			dx = speed;
		}
		if (key == KeyEvent.VK_UP) {
			dy = -speed;
		}
		if (key == KeyEvent.VK_DOWN) {
			dy = speed;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT && dx < 0) {
			dx = 0;
		}
		if (key == KeyEvent.VK_RIGHT && dx > 0) {
			dx = 0;
		}
		if (key == KeyEvent.VK_UP && dy < 0) {
			dy = 0;
		}
		if (key == KeyEvent.VK_DOWN && dy > 0) {
			dy = 0;
		}
	}
}