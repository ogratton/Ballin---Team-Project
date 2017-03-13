package ui;

import java.awt.BorderLayout;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import networking.ConnectionDataModel;

public class SessionListMenu extends BaseMenu{
	
	public JPanel getLobbyListPanel(ConnectionDataModel cdmodel, ObjectOutputStream toServer){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		DefaultTableModel tableModel = getSessionTableModel(cdmodel);
		JTable lobbyTable = getSessionTable(tableModel);
		JScrollPane scroll = new JScrollPane(lobbyTable);
		addReturnButton(panel);
		addLobbyListButtons(panel, lobbyTable, cdmodel, toServer);
		panel.add(scroll);
		return panel;
	}

}
