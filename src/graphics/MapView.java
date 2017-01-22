package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

import javax.swing.JPanel;

public class MapView extends JPanel implements KeyListener {

	private CharacterModel character;
	private boolean up, down, left, right = false;

	public MapView(CharacterModel character) {
		super();
		this.character = character;
		
		repaint();
		setFocusable(true);
		addKeyListener(this);

	}

	public void paintComponent(Graphics g) {

		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		// Graphics2D g2 = (Graphics2D)g;
		g.drawImage(character.getRollingFrame(), (int)character.getX(), (int)character.getY(), this);

	}

	public void update(Observable arg0, Object arg1) {
		repaint();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(e.getKeyCode()){
		case KeyEvent.VK_W:
			up = true;
			break;
		case KeyEvent.VK_A:
			left = true;
			break;
		case KeyEvent.VK_S:
			down = true;
			break;
		case KeyEvent.VK_D:
			right = true;
			break;
		}

		character.update(up, down, left, right);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		}

		character.update(up, down, left, right);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
