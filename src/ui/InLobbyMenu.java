package ui;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;

import networking.ConnectionDataModel;

public class InLobbyMenu extends BaseMenu{
	
	public JPanel getInLobbyMenu(ConnectionDataModel cModel, Client client){
		JPanel panel = new JPanel();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		buttonPanel.add(leaveLobbyButton(cModel,client));

		panel.add(buttonPanel);
		return panel;
	}

}
