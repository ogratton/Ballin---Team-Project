package ui;

import java.awt.BorderLayout;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

import networking.ConnectionDataModel;

public class SessionListMenu extends BaseMenu{
	
//	public JPanel getLobbyListPanel(ConnectionDataModel cdmodel, ObjectOutputStream toServer){
	public JPanel getLobbyListPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(createSessionButton(panel), BorderLayout.PAGE_START);
		panel.add(joinSessionButton(inLobbyPanel), BorderLayout.EAST);
		panel.add(sessionsPanel, BorderLayout.CENTER);
//		DefaultTableModel tableModel = getSessionTableModel(cdmodel);
//		JTable lobbyTable = getSessionTable(tableModel);
//		JScrollPane scroll = new JScrollPane(lobbyTable);
//		addReturnButton(panel);
//		addLobbyListButtons(panel, lobbyTable, cdmodel, toServer);
//		panel.add(scroll);
		return panel;
	}

}
