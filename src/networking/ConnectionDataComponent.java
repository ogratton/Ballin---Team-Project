package networking;
import java.awt.BorderLayout;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;

public class ConnectionDataComponent extends JPanel {
	private ConnectionDataModel model;
	
	/**
	 * Creates a component which holds all the different controllers and views in this GUI.
	 * Adds the relevant panels as observers to the Client Data Model.
	 * @param model The model which the GUI controllers and views are observing.
	 * @param toServer The output stream to the server to which the buttons in the GUI are sending their commands to.
	 */
	public ConnectionDataComponent(ConnectionDataModel model, ObjectOutputStream toServer)
	{
		super();
		this.model = model;
		SessionButtons sessionButtons = new SessionButtons(model, toServer);
		ReceivedMessageBox receivedMessages = new ReceivedMessageBox(model);
		MessageBox messageBox = new MessageBox(model, toServer);
		ListView lists = new ListView(model);
		SessionView sessionView = new SessionView(model);
		ControlButtons controls = new ControlButtons(model, toServer);
		
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messagePanel.add(receivedMessages, BorderLayout.NORTH);
		messagePanel.add(messageBox, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(sessionView, BorderLayout.WEST);
		bottomPanel.add(messagePanel, BorderLayout.CENTER);
		bottomPanel.add(sessionButtons, BorderLayout.EAST);
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		listPanel.add(lists, BorderLayout.CENTER);
		
		model.addObserver(sessionButtons);
		model.addObserver(messageBox);
		model.addObserver(lists);
		model.addObserver(sessionView);
		model.addObserver(controls);
		
		setLayout(new BorderLayout());
		
		add(bottomPanel, BorderLayout.SOUTH);
		add(listPanel, BorderLayout.CENTER);
		add(controls, BorderLayout.NORTH);
	}
}
