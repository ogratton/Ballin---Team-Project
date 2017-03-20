package networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import networking.GameData.Tag;
import resources.Resources.Mode;
import ui.UIRes;

/**
 * NetworkingClient initialises the Client side for the networking code.
 * It initialises the listener which receive messages from the Server.
 * @author aaquibnaved
 *
 */
public class NetworkingClient extends Thread {

	static int UDPport = 27970;
	static int TCPport = 27970;
	private String ip;
	private String name;
	static boolean messageReceived = false;
	
	/**
	 * Initialises the NetworkingClient thread.
	 * @param ip The IP address of the Server the Client wants to connect to.
	 * @param name The Name of the Client
	 */
	public NetworkingClient(String ip, String name) {
		this.ip = ip;
		this.name = name;
	}
	
	public void run() {
		
		Client client = new Client();
		
		registerClasses(client);
		
		new Thread(client).start();
		
		try {
			client.connect(5000, ip, UDPport, TCPport);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Gets the Connection Data Model from the UI Res object so that the UI has access to it.
		UIRes uires = new UIRes();
	    ConnectionDataModel cModel = uires.cModel;
	    System.out.println(cModel);
		client.addListener(new ClientListener(cModel, client));
		
		// Creates and sends a message to the server
		// indicating to the server that the Client has started and so the server can store the
		// name of the Client.
		Message m = new Message();
	    m.setCommand(Command.MESSAGE);
	    m.setMessage(name);
	    client.sendTCP(m);
		
	    // Keeps the thread going.
		while(!messageReceived) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method registers all the classes we will be sending across the network
	 * with Kryonet's networking system.
	 * 
	 * @param client The Kryonet Client object
	 */
	private static void registerClasses(Client client) {
		  Kryo kryo = client.getKryo();
		  kryo.register(Message.class);
		  kryo.register(Command.class);
		  kryo.register(Note.class);
		  kryo.register(CharacterInfo.class);
		  kryo.register(Session.class);
		  kryo.register(Empty.class);
		  kryo.register(ClientInformation.class);
		  kryo.register(LinkedBlockingQueue.class);
		  kryo.register(java.util.concurrent.ConcurrentHashMap.class);
		  kryo.register(GameData.class);
		  kryo.register(ArrayList.class);
		  kryo.register(Tag.class);
		  kryo.register(resources.Map.World.class);
		  kryo.register(Mode.class);
		  kryo.register(resources.Character.Class.class);
	}
}
