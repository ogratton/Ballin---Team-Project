package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
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
 * 
 * @author Diana Dinca
 *
 */
@SuppressWarnings("serial")
public class InGameMenu extends JPanel {

	public InGameMenu(JFrame frame, JPanel optionsPanel, int width, int height) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel map = new JLabel(new ImageIcon(Sprite.createMap(new Map(width, height, ""))));
		JPanel panel2 = new JPanel();
		BoxLayout box = new BoxLayout(panel2, BoxLayout.Y_AXIS);
		BackButton backToStartMenuButton = new BackButton(UIRes.startPanel, "Back to Main Menu");
		backToStartMenuButton.addActionListener(e -> {
			UIRes.optionsPanel.setBackToPanel(UIRes.startPanel);
			frame.dispose();
		});
		map.setLayout(new BorderLayout());
		panel2.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 5), new EmptyBorder(50, 50, 50, 50)));
		panel2.setLayout(box);
		panel2.setOpaque(false);
		UIRes.getButtonAndIcon(panel2, getResumeToGameButton(this));
		UIRes.getButtonAndIcon(panel2, backToMainMenuButton(frame));
		UIRes.getButtonAndIcon(panel2, getOptionsMenu(optionsPanel));
		UIRes.getButtonAndIcon(panel2, new ExitButton());
		map.add(panel2, BorderLayout.CENTER);
		add(map);

	}
	
	JButton getResumeToGameButton(JPanel panel) {
		JButton button = new JButton("Resume");
		button.addActionListener(e -> {
			GameComponent.layers.setLayer(panel, new Integer(10));
			LayeredPane.menuShowing = !LayeredPane.menuShowing;
		});
		
		UIRes.customiseButton(button, true);
		return button;
	}
	
	JButton backToMainMenuButton(JFrame frame){
		JButton button = new JButton("Back to Main Menu");
		UIRes.customiseButton(button, true);
		button.addActionListener(e ->{;
			UIRes.switchPanel(UIRes.startPanel);
			System.out.println("Is connected: " + (UIRes.cModel.getConnection() != null));
			if(UIRes.cModel.getConnection() != null){
				UIRes.cModel.getConnection().close();
				UIRes.cModel.setSessionId(null);
				UIRes.cModel.setGameInProgress(false);
				UIRes.cModel.setCharacters(new ConcurrentHashMap<String, resources.Character>());
				UIRes.cModel = new ConnectionDataModel(new ConnectionData());
				UIRes.fullReset();
				System.out.println("Disconnecting from server");
			}
			
			UIRes.optionsPanel.remove(UIRes.optionsPanel.getComponentCount() - 1);
			UIRes.getButtonAndIcon(UIRes.optionsPanel, new BackButton(UIRes.startPanel, "Back"));
			
			frame.dispose();
		});
		
		return button;
	}
	
	JButton getOptionsMenu(JPanel panel){
		JButton button = new JButton("Options");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			GameComponent.layers.setLayer(panel, new Integer(40));
			
			UIRes.optionsPanel.remove(UIRes.optionsPanel.getComponentCount() - 1);
			UIRes.getButtonAndIcon(UIRes.optionsPanel, getBackToInGameMenuButton());
		});
		return button;
	}
	
	JButton getBackToInGameMenuButton(){
		JButton button = new JButton("Back");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			GameComponent.layers.setLayer(LayeredPane.panel2, new Integer(5));
		});
		
		return button;
	}

}
