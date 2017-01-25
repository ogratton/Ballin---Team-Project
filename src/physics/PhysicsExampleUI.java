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

/*
 * Literally just the GameTest.java thing converted to run using Character instead of Ball
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
		Resources.players = new Character[1];
		Resources.players[0] = Character.character(Character.Class.DEFAULT);
		Resources.players[0].x(40);
		Resources.players[0].y(60);
		Resources.map = new Map(1000, 600);
		// create physics thread
		Physics p = new Physics();
		p.start();
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
		g2d.drawOval((int) (Resources.players[0].x()), (int) (Resources.players[0].y()), 20, 20);
		g2d.fillOval((int) (Resources.players[0].x()), (int) (Resources.players[0].y()), 20, 20);
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

		}
	}
}
