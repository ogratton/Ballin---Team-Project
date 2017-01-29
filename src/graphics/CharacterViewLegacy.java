package graphics;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class CharacterViewLegacy extends JPanel implements ActionListener {

	private CharacterModel character;
	private Timer timer;
	private final int DELAY = 30;
	
	public CharacterViewLegacy(CharacterModel character) {
		super();
		this.character = character;

		repaint();
		
		addKeyListener(new TAdapter());
		setFocusable(true);
		timer = new Timer(DELAY, this);
		timer.start();
		
		

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		// Graphics2D g2 = (Graphics2D)g;
		g.drawImage(character.getNextFrame(), (int) character.getX(), (int) character.getY(), this);

		Toolkit.getDefaultToolkit().sync();
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		character.move();
		repaint();

	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {

			character.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			character.keyPressed(e);
		}
	}
}
