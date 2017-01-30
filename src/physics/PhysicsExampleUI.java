package physics;

import resources.*;
import resources.Character;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Physics playtest. Fairly self explanatory.
 */

public class PhysicsExampleUI extends JFrame {
	public PhysicsExampleUI() {
		int WIDTH = 1000;
		int HEIGHT = 600;
		add(new Panel());
		setSize(WIDTH + 26, HEIGHT + 50);
		setResizable(false);

		setTitle("Game Test");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// init character and map
		Resources.players = new Character[3];
		Resources.players[0] = new Character(Character.Class.DEFAULT);
		Resources.players[0].setX(535);
		Resources.players[0].setY(600);
		//Resources.players[0].setMass(1000); // very fun. Game mode?
		
		Resources.players[1] = new Character(Character.Class.DEFAULT);
		Resources.players[1].setX(500);
		Resources.players[1].setY(500);
		Resources.players[1].setRadius(30);
		Resources.players[1].setMass(2);
		
		Resources.players[2] = new Character(Character.Class.DEFAULT);
		Resources.players[2].setX(570);
		Resources.players[2].setY(500);
		Resources.players[2].setRadius(30);
		Resources.players[2].setMass(2);
		
		Resources.map = new Map(1000, 600);
		
		// create physics thread
		Physics p = new Physics();
		p.start();
		
		// create ui thread
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				PhysicsExampleUI ex = new PhysicsExampleUI();
				ex.setVisible(true);
			}
		});

	}
}

class Panel extends JPanel implements ActionListener {
	private Timer timer;
	private final int DELAY = 16;
	
	public Panel() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setForeground(Color.LIGHT_GRAY);

		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);

		Toolkit.getDefaultToolkit().sync();
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//System.out.println(Resources.players[0].x() + ", " + Resources.players[0].y());
		g2d.setColor(Color.RED);
		for (Character player : Resources.players) {
			g2d.drawOval((int) (player.getX()-player.getRadius()), (int) (player.getY()-player.getRadius()), player.getRadius()*2, player.getRadius()*2);
			g2d.fillOval((int) (player.getX()-player.getRadius()), (int) (player.getY()-player.getRadius()), player.getRadius()*2, player.getRadius()*2);
			g2d.setColor(Color.GREEN);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// tick
		repaint();
	}
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {
				Resources.players[0].setLeft(false);
			}
			if (key == KeyEvent.VK_D) {
				Resources.players[0].setRight(false);
			}
			if (key == KeyEvent.VK_W) {
				Resources.players[0].setUp(false);
			}
			if (key == KeyEvent.VK_S) {
				Resources.players[0].setDown(false);
			}		
			if (key == KeyEvent.VK_LEFT) {
				Resources.players[1].setLeft(false);
			}
			if (key == KeyEvent.VK_RIGHT) {
				Resources.players[1].setRight(false);
			}
			if (key == KeyEvent.VK_UP) {
				Resources.players[1].setUp(false);
			}
			if (key == KeyEvent.VK_DOWN) {
				Resources.players[1].setDown(false);
			}
		}
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {
				Resources.players[0].setLeft(true);
			}
			if (key == KeyEvent.VK_D) {
				Resources.players[0].setRight(true);
			}
			if (key == KeyEvent.VK_W) {
				Resources.players[0].setUp(true);
			}
			if (key == KeyEvent.VK_S) {
				Resources.players[0].setDown(true);
			}	
			if (key == KeyEvent.VK_LEFT) {
				Resources.players[1].setLeft(true);
			}
			if (key == KeyEvent.VK_RIGHT) {
				Resources.players[1].setRight(true);
			}
			if (key == KeyEvent.VK_UP) {
				Resources.players[1].setUp(true);
			}
			if (key == KeyEvent.VK_DOWN) {
				Resources.players[1].setDown(true);
			}
		}
	}
}
