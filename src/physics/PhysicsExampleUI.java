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
		Character A = new Character(Character.Class.DEFAULT);
		A.setX(535);
		A.setY(600);
		
		Character B = new Character(Character.Class.DEFAULT);
		B.setX(500);
		B.setY(500);
		B.setRadius(30);
		B.setMass(2);
		
		Character C = new Character(Character.Class.DEFAULT);
		C.setX(570); // for clipping test, set to ~540, else 570 is out of range of its twin.
		C.setY(500);
		C.setRadius(30);
		C.setMass(2);
		
		Resources.playerList.add(A);
		Resources.playerList.add(B);
		Resources.playerList.add(C);
		
		Resources.map = new Map(950, 550); // smaller than screen (this will allow for falling off the edge)
		Resources.map.getOrigin().setLocation(50.0, 50.0);
		
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
		for (Character player : Resources.playerList) {
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
				Resources.playerList.get(0).setLeft(false);
			}
			if (key == KeyEvent.VK_D) {
				Resources.playerList.get(0).setRight(false);
			}
			if (key == KeyEvent.VK_W) {
				Resources.playerList.get(0).setUp(false);
			}
			if (key == KeyEvent.VK_S) {
				Resources.playerList.get(0).setDown(false);
			}		
			if (key == KeyEvent.VK_LEFT) {
				Resources.playerList.get(1).setLeft(false);
			}
			if (key == KeyEvent.VK_RIGHT) {
				Resources.playerList.get(1).setRight(false);
			}
			if (key == KeyEvent.VK_UP) {
				Resources.playerList.get(1).setUp(false);
			}
			if (key == KeyEvent.VK_DOWN) {
				Resources.playerList.get(1).setDown(false);
			}
		}
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {
				Resources.playerList.get(0).setLeft(true);
			}
			if (key == KeyEvent.VK_D) {
				Resources.playerList.get(0).setRight(true);
			}
			if (key == KeyEvent.VK_W) {
				Resources.playerList.get(0).setUp(true);
			}
			if (key == KeyEvent.VK_S) {
				Resources.playerList.get(0).setDown(true);
			}		
			if (key == KeyEvent.VK_LEFT) {
				Resources.playerList.get(1).setLeft(true);
			}
			if (key == KeyEvent.VK_RIGHT) {
				Resources.playerList.get(1).setRight(true);
			}
			if (key == KeyEvent.VK_UP) {
				Resources.playerList.get(1).setUp(true);
			}
			if (key == KeyEvent.VK_DOWN) {
				Resources.playerList.get(1).setDown(true);
			}
			if (key == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
				Resources.playerList.get(0).setDashing(true);
			}
			if (key == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
				Resources.playerList.get(1).setDashing(true);
			}
			if (key == KeyEvent.VK_CONTROL && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
				Resources.playerList.get(0).setBlocking(true);
			}
			if (key == KeyEvent.VK_CONTROL && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
				Resources.playerList.get(1).setBlocking(true);
			}
		}
	}
}
