package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.esotericsoftware.kryonet.Client;

import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;
import graphics.sprites.Sprite.SheetType;
import resources.Character;
import networking.ClientInformation;
import networking.Command;
import networking.ConnectionDataModel;
import networking.Message;
import networking.Note;
import networking.Session;

@SuppressWarnings("serial")
public class InLobbyMenu extends JPanel implements Observer {

	private Session session;
	private ConnectionDataModel cModel;
	private SessionListMenu sessionList;
	private int spriteIndex = 0;
	
	public InLobbyMenu(Session session, Client client, ConnectionDataModel cModel, SessionListMenu sessionList){


		this.session = session;
		this.cModel = cModel;
		this.sessionList = sessionList;

		cModel.addObserver(this);
		setOpaque(false);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		setLayout(new BorderLayout());
		add(Box.createHorizontalStrut(10));
		UIRes.getButtonAndIcon(buttonPanel, leaveLobbyButton(client));
		add(Box.createHorizontalStrut(10));
		add(buttonPanel, BorderLayout.PAGE_START);
		updateInLobbyPanel();
		add(UIRes.playersPanel, BorderLayout.CENTER);
	}

	JButton leaveLobbyButton(Client client) {
		JButton button = new JButton("Leave Lobby");
		button.setOpaque(false);
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {

			Message leaveMessage = new Message(Command.SESSION, Note.LEAVE, cModel.getMyId(), "", cModel.getSessionId(),
					cModel.getHighlightedSessionId());
			try {
				cModel.getConnection().sendTCP(leaveMessage);
				cModel.setReady(false);
				updateInLobbyPanel();
				UIRes.switchPanel(sessionList);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		return button;
	}

	void updateInLobbyPanel() {
		Session session;
		UIRes.playersPanel.removeAll();
		if (cModel.getSessionId() != null && (!cModel.getSessionId().equals(""))) {
			session = cModel.getSession(cModel.getSessionId());
			for (int i = 0; i < session.getAllClients().size(); i++) {
				addPlayerToLobby(session.getAllClients().get(i), i + 1);
			}
		} else {
			session = getSession();
		}
		UIRes.playersPanel.repaint();
		UIRes.playersPanel.revalidate();

	}

	void addPlayerToLobby(ClientInformation client, int index) {
		UIRes.playersPanel.setOpaque(false);
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension((int) (UIRes.width * 0.95), (int) (UIRes.height * 0.12)));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		String name = client.getName();

		/*if (client.getName().length() > 7) {
			name = client.getName().substring(0, 8);
		}

		if (client.getName().length() < 7) {
			int spaces = 7 - client.getName().length();

			name = client.getName();

			for (int i = 0; i < spaces; i++) {
				name += " ";
			}
		}*/

		JLabel playerLabel = UIRes.getLabel(name);

		playerLabel.setPreferredSize(new Dimension(327, 100));
		
		JComboBox<ImageIcon> characterClass = new JComboBox<ImageIcon>();
		characterClass.setSize(new Dimension(50,50));
		for (int i = 0; i < UIRes.numberIcons; i++) {
			BufferedImage icon = Sprite.getSprite(Sprite.loadSpriteSheet(SheetType.CHARACTER), 0, i,
					SheetDeets.CHARACTERS_SIZEX, SheetDeets.CHARACTERS_SIZEY);
			characterClass.addItem(new ImageIcon(icon));
		}
		

		Color colour = UIRes.resources.getPlayerColor(index);
		panel.setBorder(new CompoundBorder(new LineBorder(colour, 15), new EmptyBorder(10, 10, 10, 10)));

		JButton readyCheck = new JButton("Ready");
		UIRes.customiseButton(readyCheck, false);
		readyCheck.setForeground(Color.RED);
		
		if (this.cModel.getMyId().compareTo(client.getId()) != 0) {
			characterClass.setEnabled(false);

		} else {
			readyCheck.addActionListener(e -> {
				
				if(client.isReady()) {
					client.setReady(false);
				} else {
					client.setReady(true);
				}
				if (client.isReady()) {
					readyCheck.setForeground(Color.GREEN);
					client.setCharacterClass(getCharacter(characterClass.getSelectedIndex()));
					spriteIndex = characterClass.getSelectedIndex();
					System.out.println(client.getCharacterClass().name());
					client.setPlayerNumber(index);
					System.out.println(client.getPlayerNumber());
					if (cModel.getSession(cModel.getSessionId()).getAllClients().size() > 0) {
						if (!cModel.isGameInProgress()) {
							Message message = new Message(Command.GAME, Note.START, cModel.getMyId(), null,
									cModel.getSessionId(), null, client);
							try {
								cModel.getConnection().sendTCP(message);
								cModel.setReady(true);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}

				} else {
					readyCheck.setForeground(Color.RED);
					if (cModel.getSession(cModel.getSessionId()).getAllClients().size() > 0) {
						if (!cModel.isGameInProgress()) {
							Message message = new Message(Command.GAME, Note.STOP, cModel.getMyId(), null,
									cModel.getSessionId(), null);
							try {
								cModel.getConnection().sendTCP(message);
								cModel.setReady(false);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				System.out.println(client.isReady());
			});
		}

		if (client.isReady()) {
			readyCheck.setForeground(Color.GREEN);
			for(int i = 0; i < UIRes.numberIcons; i++){
				if(client.getCharacterClass().toString().equals(getCharacter(i).toString()))
					characterClass.setSelectedIndex(i);
			}
		}
		else
			readyCheck.setForeground(Color.RED);

		panel.add(Box.createHorizontalGlue());
		panel.add(playerLabel);
		panel.add(Box.createHorizontalGlue());
		panel.add(characterClass);
		panel.add(Box.createHorizontalGlue());
		panel.add(readyCheck);
		panel.add(Box.createHorizontalGlue());
		UIRes.playersPanel.add(panel);

	}

	void setSession(Session session) {
		this.session = session;
	}

	Session getSession() {
		return this.session;
	}

	Character.Class getCharacter(int index) {
		switch (index) {
		case 0:
			return Character.Class.WIZARD;
		case 1:
			return Character.Class.ARCHER;
		case 2:
			return Character.Class.WARRIOR;
		case 3:
			return Character.Class.MONK;
		case 4:
			return Character.Class.WITCH;
		case 5:
			return Character.Class.HORSE;
		default:
			return Character.Class.WIZARD;
		}
	}

	int getCharacterIndex(Character.Class character) {
		switch (character) {
		case WIZARD:
			return 0;
		case ARCHER:
			return 1;
		case WARRIOR:
			return 2;
		case MONK:
			return 3;
		case WITCH:
			return 4;
		case HORSE:
			return 5;
		default:
			return 0;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		updateInLobbyPanel();
		repaint();
		validate();
	}

}
