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
		add(label);
		
		JLabel label2 = new JLabel(modeGoal);
		UIRes.setCustomFont(label2, 18);
		label2.setAlignmentX(CENTER_ALIGNMENT);
		add(label2);

		String up = KeyEvent.getKeyText(resources.getUp());
		String down = KeyEvent.getKeyText(resources.getDown());
		String left = KeyEvent.getKeyText(resources.getLeft());
		String right =  KeyEvent.getKeyText(resources.getRight());
		String dash = KeyEvent.getKeyText(resources.getDash());
		
		JLabel label4 = new JLabel("Up: " + up + " Left: " + left + " Down: " + down + " Right: " + right + " Dash: " + dash);
		UIRes.setCustomFont(label4, 32);
		label4.setAlignmentX(CENTER_ALIGNMENT);
		add(label4);
		
		JLabel label3 = new JLabel("Press any key to start");
		UIRes.setCustomFont(label3, 32);
		label3.setAlignmentX(CENTER_ALIGNMENT);
		add(label3);
		
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(50, 50, 50, 50)));

	}

}
