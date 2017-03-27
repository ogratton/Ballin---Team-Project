package graphics;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import resources.Resources;
import ui.UIRes;

/**
 * A class to hold the splash screen that appears before the game starts
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class SplashScreen extends JPanel {

	private JLabel label3;
	
	/**
	 * Makes a new splash screen
	 * @param resources the resources object
	 */
	
	public SplashScreen(Resources resources) {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Resources.Mode mode = resources.mode;
		String modeName = resources.mode.toString();
		String modeGoal = "";

		switch (mode) {
		case Deathmatch:
			modeGoal = "Knock as many foes off the edge as possible in thirty seconds!";
			break;
		case LastManStanding:
			modeGoal = "Try to survive for as long as possible with five lives!";
			break;
		case HotPotato:
			modeGoal = "Avoid the bomb and pass it to your foes before it blows!";
			break;
		default:
			break;
		}

		JLabel label = new JLabel(modeName);
		UIRes.setCustomFont(label, 64);
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(label);
		
		JLabel label2 = new JLabel(modeGoal);
		UIRes.setCustomFont(label2, 18);
		label2.setAlignmentX(CENTER_ALIGNMENT);
		label2.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(label2);

		String up = KeyEvent.getKeyText(resources.getUp());
		String down = KeyEvent.getKeyText(resources.getDown());
		String left = KeyEvent.getKeyText(resources.getLeft());
		String right =  KeyEvent.getKeyText(resources.getRight());
		String dash = KeyEvent.getKeyText(resources.getDash());

		JLabel label5 = new JLabel("Movement");
		UIRes.setCustomFont(label5, 32);
		label5.setAlignmentX(CENTER_ALIGNMENT);
		label5.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(label5);
		
		JLabel label4 = new JLabel("Up: " + up + " Left: " + left + " Down: " + down + " Right: " + right);
		UIRes.setCustomFont(label4, 32);
		label4.setAlignmentX(CENTER_ALIGNMENT);
		label4.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(label4);
		
		JLabel label6 = new JLabel("Dash: " + dash);
		UIRes.setCustomFont(label6, 32);
		label6.setAlignmentX(CENTER_ALIGNMENT);
		label6.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(label6);
		
		label3 = new JLabel(3 + "");
		UIRes.setCustomFont(label3,128);
		label3.setAlignmentX(CENTER_ALIGNMENT);
		label3.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(label3);
		
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(50, 50, 50, 50)));
		
	}
	
	/**
	 * Set the countdown
	 * @param i the countdown
	 */
	
	public void setCountdown(int i){
		label3.setText(i + "");
	}
	
	

}
