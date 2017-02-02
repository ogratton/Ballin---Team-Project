import java.awt.event.KeyEvent;

import audio.AudioFile;

public class Ball {

	private double dx;
	private double dy;
	private double x;
	private double y;
	private final double acc = 0.1;
	private final double maxSpeed = 4;
	private final double friction = 0.99;
	private boolean accLeft;
	private boolean accRight;
	private boolean accUp;
	private boolean accDown;
	private int WIDTH;
	private int HEIGHT;
	
	private final String audioPath = "./resources/audio/";
	
	private AudioFile dong = new AudioFile(audioPath + "dong.wav", "dong sfx");
	private AudioFile ding = new AudioFile(audioPath + "ding.wav", "ding sfx");

	public Ball(int WIDTH, int HEIGHT) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.x = 40;
		this.y = 60;
		this.accLeft = false;
		this.accRight = false;
		this.accUp = false;
		this.accDown = false;
	}

	public void move() {
		if (accLeft && dx > -maxSpeed) {
			dx -= acc;
		}
		if (accRight && dx < maxSpeed) {
			dx += acc;
		}
		if (accUp && dy > -maxSpeed) {
			dy -= acc;
		}
		if (accDown && dy < maxSpeed) {
			dy += acc;
		}
		if (!accLeft && !accRight) {
			dx *= friction;
		}
		if (!accUp && !accDown) {
			dy *= friction;
		}
		checkWallCollision();
		x += dx;
		y += dy;
	}

	public int getX() {
		return (int)x;
	}

	public int getY() {
		return (int)y;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_A) {
			this.accLeft = true;
		}
		if (key == KeyEvent.VK_D) {
			this.accRight = true;
		}
		if (key == KeyEvent.VK_W) {
			this.accUp = true;
		}
		if (key == KeyEvent.VK_S) {
			this.accDown = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_A) {
			this.accLeft = false;
		}
		if (key == KeyEvent.VK_D) {
			this.accRight = false;
		}
		if (key == KeyEvent.VK_W) {
			this.accUp = false;
		}
		if (key == KeyEvent.VK_S) {
			this.accDown = false;
		}
	}
	
	private void checkWallCollision() {
		if (x < 0 || x > WIDTH) {
			dx = -dx;
			dong.play();
		}
		if (y < 0 || y > HEIGHT) {
			dy = -dy;
			ding.play();
		}
	}
}