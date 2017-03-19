package graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import gamemodes.GameModeFFA;
import resources.Resources;
import ui.UIRes;

@SuppressWarnings("serial")
public class SplashScreen extends JPanel {

	public SplashScreen(Resources resources, GameView view) {

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
			modeGoal = "Avoid the bomb and pass it to your foes before it goes off!";
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

		JLabel label3 = new JLabel("Press any key to start");
		UIRes.setCustomFont(label3, 32);
		label3.setAlignmentX(CENTER_ALIGNMENT);
		add(label3);
		
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(50, 50, 50, 50)));

	}

}
