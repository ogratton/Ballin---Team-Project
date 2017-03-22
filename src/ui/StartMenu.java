package ui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import gamemodes.PlayGame;
import networking.NetworkingClient;
import resources.Resources;

@SuppressWarnings("serial")
public class StartMenu extends JPanel{
	
	public StartMenu(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	//	setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 5), new EmptyBorder(50, 50, 50, 50)));
		JLabel titleLabel = UIRes.getLabel("Ballin'");
		JLabel usernameLabel = UIRes.getLabel("Welcome, " + UIRes.username + "!");
		JButton usernameButton = getUsername(usernameLabel);
		add(usernameLabel);
		UIRes.addSpace(this, 0, 0.1);
		add(titleLabel);
		UIRes.addSpace(this, 0, 0.1);
		UIRes.getButtonAndIcon(this, getPlaySingleplayerButton());
		UIRes.getButtonAndIcon(this, getPlayMultiplayerButton());
		UIRes.getButtonAndIcon(this, usernameButton);
		//UIRes.optionsPanel.setBackToPanel(this);
		UIRes.getButtonAndIcon(this, new OptionsButton());
		UIRes.getButtonAndIcon(this, new ExitButton());

	}

	public static void main(String[] args) {
		JFrame frame = UIRes.createFrame();
		frame.setVisible(true);	
	}
	
	JButton getPlaySingleplayerButton() {
		JButton startButton = new JButton("Start Singleplayer Game");
		UIRes.customiseButton(startButton, true);
		startButton.addActionListener(e -> {
			UIRes.resources.refresh();
			//PlayGame.start(UIRes.resources);

			if (!Resources.silent) {
				// button sound effect
				UIRes.audioPlayer.play();
				// change the song
				// resources.getMusicPlayer().changePlaylist("paris30");
				// resources.getMusicPlayer().resumeMusic();
			}

		});
		return startButton;
	}
	
	JButton getPlayMultiplayerButton() {
		JButton startButton = new JButton("Start Multiplayer Game");
		UIRes.customiseButton(startButton, true);
		startButton.addActionListener(e -> {
			UIRes.resources.refresh();
			JFrame frame = new JFrame();
			String input = JOptionPane.showInputDialog(frame, "Enter the server name:", "Input server",
					JOptionPane.PLAIN_MESSAGE);
			if (input != null) {
				NetworkingClient client = new NetworkingClient(input, UIRes.username);
				client.start();
			} else {
				frame.dispose();
			}
		});
		return startButton;
	}
	
	JButton getUsername(JLabel label) {
		JButton usernameButton = new JButton("Change username");
		UIRes.customiseButton(usernameButton, true);
		usernameButton.addActionListener(e -> {
			JFrame frame = new JFrame();
			String input = (String) JOptionPane.showInputDialog(frame, "Enter your username:", "Input username",
					JOptionPane.PLAIN_MESSAGE);
			if (input != null) {
				UIRes.username = input;
				label.setText("Welcome, " + UIRes.username + "!");
			} else {
				frame.dispose();
			}
		});
		return usernameButton;
	}

}

