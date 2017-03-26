package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import graphics.GameComponent;
import graphics.LayeredPane;
import graphics.sprites.Sprite;
import networking.ConnectionData;
import networking.ConnectionDataModel;
import resources.Map;

/**
 * Class for the In-Game Menu panel.
 * 
 * @author Andreea Diana Dinca
 *
 */
@SuppressWarnings("serial")
public class InGameMenu extends JPanel {

	/**
	 * Constructor of the In-Game Menu panel.
	 * 
	 * @param frame
	 * 		the frame the game runs on
	 * @param optionsPanel
	 * 		the options panel
	 * @param width
	 * 		the width of this panel
	 * @param height
	 * 		the height of this panel
	 */
	public InGameMenu(JFrame frame, JPanel optionsPanel, int width, int height) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel map = new JLabel(new ImageIcon(Sprite.createMap(new Map(width, height, ""))));
		JPanel panel2 = new JPanel();
		BoxLayout box = new BoxLayout(panel2, BoxLayout.Y_AXIS);
		BackButton backToStartMenuButton = new BackButton(UIRes.startPanel, "Back to Main Menu");
		backToStartMenuButton.addActionListener(e -> {
			UIRes.optionsPanel.setBackToPanel(UIRes.startPanel);
			frame.dispose();
			UIRes.resources.setSong("grandma");
		});
		map.setLayout(new BorderLayout());
		panel2.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 5), new EmptyBorder(50, 50, 50, 50)));
		panel2.setLayout(box);
		panel2.setOpaque(false);
		UIRes.getButtonAndIcon(panel2, getResumeToGameButton());
		UIRes.getButtonAndIcon(panel2, backToMainMenuButton(frame));
		UIRes.getButtonAndIcon(panel2, getOptionsMenu(optionsPanel));
		UIRes.getButtonAndIcon(panel2, new ExitButton());
		map.add(panel2, BorderLayout.CENTER);
		add(map);

	}

	/**
	 * Creates a button that takes the user back to the game.
	 * 
	 * @return
	 * 		the button
	 */
	JButton getResumeToGameButton() {
		JButton button = new JButton("Resume");
		button.addActionListener(e -> {
			GameComponent.layers.setLayer(this, new Integer(10));
			LayeredPane.menuShowing = !LayeredPane.menuShowing;
		});

		UIRes.customiseButton(button, true);
		return button;
	}

	/**
	 * Creates a button that closes the game and returns the user to the Start Menu.
	 * 
	 * @param frame
	 * 		the frame the game is running on
	 * @return
	 * 		the button
	 */
	JButton backToMainMenuButton(JFrame frame) {
		JButton button = new JButton("Back to Main Menu");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			UIRes.resources.setFinished(true);
			UIRes.switchPanel(UIRes.startPanel);
			System.out.println("Is connected: " + (UIRes.cModel.getConnection() != null));
			if (UIRes.cModel.getConnection() != null) {
				UIRes.cModel.getConnection().close();
				UIRes.cModel.setSessionId(null);
				UIRes.cModel.setGameInProgress(false);
				UIRes.cModel.setCharacters(new ConcurrentHashMap<String, resources.Character>());
				UIRes.cModel = new ConnectionDataModel(new ConnectionData());
				UIRes.fullReset();
				System.out.println("Disconnecting from server");
			} else {
				System.out.println("TRYING TO END GAME");
				UIRes.resources.gamemode.setEndGame(true);
			}

			UIRes.optionsPanel.remove(UIRes.optionsPanel.getComponentCount() - 1);
			UIRes.getButtonAndIcon(UIRes.optionsPanel, new BackButton(UIRes.startPanel, "Back"));

			frame.dispose();
		});

		return button;
	}

	/**
	 * Creates a button that takes the user to the Options Menu.
	 * 
	 * @param panel
	 * 		the options panel
	 * @return
	 * 		the button
	 */
	JButton getOptionsMenu(JPanel panel) {
		JButton button = new JButton("Options");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			GameComponent.layers.setLayer(panel, new Integer(40));

			UIRes.optionsPanel.remove(UIRes.optionsPanel.getComponentCount() - 1);
			UIRes.getButtonAndIcon(UIRes.optionsPanel, getBackToInGameMenuButton());
		});
		return button;
	}

	/**
	 * Creates a button that takes the user back to the in game menu from the options panel.
	 * 
	 * @return
	 * 		the button
	 */
	JButton getBackToInGameMenuButton() {
		JButton button = new JButton("Back");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			GameComponent.layers.setLayer(LayeredPane.panel2, new Integer(5));
		});

		return button;
	}

}
