package ui;

import java.awt.BorderLayout;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;

import networking.ConnectionDataModel;
import networking.NetworkingClient;

public class SessionListMenu extends BaseMenu{
	
	public JPanel getLobbyListPanel(ConnectionDataModel cdmodel, Client client){
		updateSessionsPanel(cdmodel);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(createSessionButton(cdmodel, client), BorderLayout.PAGE_START);
		panel.add(joinSessionButton(inLobbyPanel), BorderLayout.EAST);
		panel.add(sessionsPanels, BorderLayout.CENTER);
//		DefaultTableModel tableModel = getSessionTableModel(cdmodel);
//		JTable lobbyTable = getSessionTable(tableModel);
//		JScrollPane scroll = new JScrollPane(lobbyTable);
//		addReturnButton(panel);
//		addLobbyListButtons(panel, lobbyTable, cdmodel, toServer);
//		panel.add(scroll);
		return panel;
	}

}
