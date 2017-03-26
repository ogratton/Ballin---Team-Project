package ui;

import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import networking.ConnectionData;
import networking.ConnectionDataModel;

/**
 * Class for the back button.
 * 
 * @author Andreea Diana Dinca
 *
 */
@SuppressWarnings("serial")
public class BackButton extends JButton {

	/**
	 * Constructor for the Back Button class.
	 * 
	 * @param panel
	 * 		the panel the back button switches to
	 * @param text
	 * 		the text of the button
	 */
	public BackButton(JPanel panel, String text) {
		setText(text);
		UIRes.customiseButton(this, true);
		addActionListener(e -> {
			UIRes.switchPanel(panel);
			System.out.println(UIRes.cModel.getConnection() == null);
			if (UIRes.cModel.getConnection() != null) {
				UIRes.cModel.getConnection().close();
				UIRes.cModel.setSessionId(null);
				UIRes.cModel.setGameInProgress(false);
				UIRes.cModel.setCharacters(new ConcurrentHashMap<String, resources.Character>());
				UIRes.cModel = new ConnectionDataModel(new ConnectionData());
				UIRes.fullReset();
			}
		});
	}

}
