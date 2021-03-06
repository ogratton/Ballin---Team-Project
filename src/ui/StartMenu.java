package ui;

import java.awt.Choice;

import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import audio.MusicHandler;
import audio.MusicPlayer;
import gamemodes.PlayGame;
import graphics.sprites.Sprite;
import networking.NetworkingClient;
import networking.NetworkingServer;
import resources.Map;
import resources.MapMetaData;
import resources.Resources;
import resources.Resources.Mode;

/**
 * Class for the Start Menu panel.
 * 
 * @author Andreea Diana Dinca
 *
 */
@SuppressWarnings("serial")
public class StartMenu extends JPanel {

	private String ip;
	
	private MusicPlayer musicPlayer;
	private MusicHandler musicHandler;
	
	/**
	 * Constructor of the Start Menu panel.
	 */
	public StartMenu() {
		
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel titleLabel = UIRes.getLabel("Ballin'");
		JLabel usernameLabel = UIRes.getLabel("Welcome, " + UIRes.username + "!");
		JButton usernameButton = getUsername(usernameLabel);
		add(usernameLabel);
		UIRes.addSpace(this, 0, 0.1);
		add(titleLabel);
		UIRes.addSpace(this, 0, 0.1);
		UIRes.getButtonAndIcon(this, startServerButton());
		UIRes.getButtonAndIcon(this, getPlaySingleplayerButton());
		UIRes.getButtonAndIcon(this, getPlayMultiplayerButton());
		UIRes.getButtonAndIcon(this, usernameButton);
		UIRes.getButtonAndIcon(this, new OptionsButton());
		UIRes.getButtonAndIcon(this, new ExitButton());
		
		if (!Resources.silent)
		{
			musicPlayer = new MusicPlayer(UIRes.resources, "grandma", "swing", "thirty", "ultrastorm", "ultrastorm30", "frog", "rage", "rage30");
			musicPlayer.changePlaylist("grandma");
			musicPlayer.start();
			// passed so the volume sliders can work
			UIRes.musicPlayer = musicPlayer;
			
			// start up the music handler that will
			musicHandler = new MusicHandler(musicPlayer, UIRes.resources);
			musicHandler.start();
		}

	}

	/**
	 * Creates a button that starts the server when clicked on. If the server is already running this will pop up a message telling the user their IP address.
	 * 
	 * @return 
	 * 		the button
	 */
	JButton startServerButton() {
		JButton startServer = new JButton("Start Server");
		UIRes.customiseButton(startServer, true);
		startServer.addActionListener(e -> {
			try {
				NetworkingServer.main(null);
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame,
						"You have successfully started the server on your machine! This is your ip address: " + ip);
			} catch (Exception e1) {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame,
						"This is your ip address: " + ip);
			}
		});
		return startServer;
	}

	/**
	 * Creates a button that starts the game wizard which lets the user choose a game mode, map and tile set for the game.
	 * 
	 * @return 
	 * 		the button
	 */
	JButton getPlaySingleplayerButton() {
		JButton startButton = new JButton("Start Singleplayer Game");
		UIRes.customiseButton(startButton, true);
		startButton.addActionListener(e -> {

			MapMetaData mmd = new MapMetaData();

			JFrame mapFrame = new JFrame();
			JFrame tileFrame = new JFrame();
			JFrame gameModeFrame = new JFrame();
			
			Mode gameMode = null;
			String mapName = null;
			Map.World tileSet = null;

			JLabel gameModeLabel = new JLabel("Game mode: ");
			UIRes.customiseLabel(gameModeLabel);

			Choice gameModeChoice = new Choice();
			ArrayList<String> gameModeList = new ArrayList<String>();
			gameModeList = mmd.gamemodeNames;
			for (int i = 0; i < gameModeList.size(); i++) {
				if (!gameModeList.get(i).equals("Hockey") && !gameModeList.get(i).equals("Debug"))
					gameModeChoice.add(gameModeList.get(i));
			}

			Object[] gameModeInfo = { gameModeLabel, gameModeChoice };

			int gameModePane = JOptionPane.showConfirmDialog(gameModeFrame, gameModeInfo, "Select the game mode: ",
					JOptionPane.OK_CANCEL_OPTION);

			if (gameModePane == JOptionPane.OK_OPTION) {

				gameMode = mmd.correspondingMode(gameModeChoice.getSelectedItem());

				JLabel mapLabel = new JLabel("Map: ");
				UIRes.customiseLabel(mapLabel);

				JComboBox<ImageIcon> mapChoice = new JComboBox<ImageIcon>();
				mapChoice.setPreferredSize(new Dimension(150, 100));

				HashSet<String> mapNames = null;
				try {
					mapNames = MapMetaData.getTable().get(gameMode);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				for (String map : mapNames) {
					ImageIcon icon = new ImageIcon(Sprite.createMap(new Map(1200, 650, Map.World.DESERT, map)));
					Image image = icon.getImage();
					Image mapIcon = image.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
					mapChoice.addItem(new ImageIcon(mapIcon));
				}

				Object[] mapInfo = { mapLabel, mapChoice };

				int mapPane = JOptionPane.showConfirmDialog(mapFrame, mapInfo, "Select the map: ",
						JOptionPane.OK_CANCEL_OPTION);

				if (mapPane == JOptionPane.OK_OPTION) {
					Iterator<String> iterator = mapNames.iterator();
					for (int i = 0; i < mapChoice.getSelectedIndex(); i++) {
						iterator.next();
					}
					mapName = iterator.next();

					JLabel tileLabel = new JLabel("Tile: ");
					UIRes.customiseLabel(tileLabel);

					JComboBox<ImageIcon> tileChoice = new JComboBox<ImageIcon>();
					for (Map.World tile : Map.World.values()) {
						ImageIcon icon = new ImageIcon(Sprite.createMap(new Map(1200, 650, tile, mapName)));
						Image image = icon.getImage();
						Image tileIcon = image.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
						tileChoice.addItem(new ImageIcon(tileIcon));
					}

					Object[] tileInfo = { tileLabel, tileChoice };

					int tilePane = JOptionPane.showConfirmDialog(tileFrame, tileInfo, "Select the tiles: ",
							JOptionPane.OK_CANCEL_OPTION);

					if (tilePane == JOptionPane.OK_OPTION) {
						tileSet = Map.World.values()[tileChoice.getSelectedIndex()];

					} else {
						tileFrame.dispose();
					}

				} else
					mapFrame.dispose();
			} else
				gameModeFrame.dispose();
			
			if (mapName != null && gameMode != null && tileSet != null) {
				UIRes.resources.refresh();
				PlayGame.start(UIRes.resources, mapName, gameMode, tileSet);
				
				if (!Resources.silent) {
					musicHandler.setResources(UIRes.resources);
					musicPlayer.pauseMusic();	
				}
			}

		});
		return startButton;
	}

	/**
	 * Creates a button that asks the user to type in the server IP they would like to connect to.
	 * 
	 * @return 
	 * 		the button
	 */
	JButton getPlayMultiplayerButton() {
		JButton startButton = new JButton("Start Multiplayer Game");
		UIRes.customiseButton(startButton, true);
		startButton.addActionListener(e -> {
			UIRes.resources.refresh();
			JFrame frame = new JFrame();
			String input = JOptionPane.showInputDialog(frame, "Enter the server address:", "Input server",
					JOptionPane.PLAIN_MESSAGE);
			if (input != null) {
				try{
					NetworkingClient client = new NetworkingClient(input, UIRes.username);
					client.start();					
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(new JFrame(), "The server address was incorrect. Please try again!");
				}
			} else {
				frame.dispose();
			}
		});
		return startButton;
	}

	/**
	 * Creates a button that allows the user to change their username. Updates the label that the button is connected to.
	 * @param label 
	 * 		the label the button is connected to
	 * @return 
	 * 		the button
	 */
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
