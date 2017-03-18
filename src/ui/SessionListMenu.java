package ui;

import java.awt.BorderLayout;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;

import networking.ConnectionDataModel;
import networking.NetworkingClient;

public class SessionListMenu extends BaseMenu{
	
	public JPanel getLobbyListPanel(ConnectionDataModel cModel, Client client){
		JPanel panel = new JPanel();
		InLobbyMenu lobbyMenu = new InLobbyMenu();
		JPanel lobbyPanel = lobbyMenu.getInLobbyMenu(cModel, client);
		updateSessionsPanel(cModel, client);
		panel.setLayout(new BorderLayout());
		panel.add(addSessionButtons(cModel, client, lobbyPanel), BorderLayout.PAGE_START);
		panel.add(sessionsPanels, BorderLayout.CENTER);

		return panel;
	}

}
