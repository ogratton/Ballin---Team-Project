package networking;

import java.io.ObjectOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;

public class ClientGUI extends Thread {
	private ConnectionDataModel model;
	private String name;
	private ObjectOutputStream toServer;
	
	/**
	 * Creates a GUI for the client to use.
	 * @param model The Client Data Model which contains all the information about the client
	 * @param name The name of the client who is using this GUI.
	 * @param toServer The output stream to the server to which the GUI prints to.
	 */
	public ClientGUI(ConnectionDataModel model, String name, ObjectOutputStream toServer) {
		super();
		this.model = model;
		this.name = name;
		this.toServer = toServer;
	}
	
	/**
	 * Initializes the frame.
	 */
	public void run() {
		
		JFrame frame = new JFrame(name);
		frame.setSize(600, 400);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ConnectionDataComponent comp = new ConnectionDataComponent(model, toServer);	
		frame.add(comp);
				
		frame.setVisible(true);
	}
}
