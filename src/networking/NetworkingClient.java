package networking;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import networking.GameData.Tag;
import resources.Resources.Mode;

public class NetworkingClient {

	//private Client client;
	static int UDPport = 27970;
	static int TCPport = 27970;
	static String ip = "localhost";
	static boolean messageReceived = false;
	
	public static void main(String[] args) throws Exception {
		
		String name = "Aaquib";
		
		Client client = new Client();
		
		registerClasses(client);
		
		new Thread(client).start();
		
		client.connect(5000, ip, UDPport, TCPport);
		
		ConnectionData conn = new ConnectionData();
	    ConnectionDataModel cModel = new ConnectionDataModel(conn);
		
		client.addListener(new ClientListener(cModel, client));
		
		System.out.print("Client is now waiting for a packet.");
		
		Message m = new Message();
	    m.setCommand(Command.MESSAGE);
	    m.setMessage(name);
	    client.sendTCP(m);
		
		while(!messageReceived) {
			Thread.sleep(1000);
		}
	}
	
	private static void registerClasses(Client client) {
		  Kryo kryo = client.getKryo();
		  kryo.register(Message.class);
		  kryo.register(Command.class);
		  kryo.register(Note.class);
		  kryo.register(CharacterInfo.class);
		  kryo.register(Session.class);
		  kryo.register(Empty.class);
		  kryo.register(ClientInformation.class);
		  kryo.register(MessageQueue.class);
		  kryo.register(LinkedBlockingQueue.class);
		  kryo.register(java.util.concurrent.ConcurrentHashMap.class);
		  kryo.register(GameData.class);
		  kryo.register(ArrayList.class);
		  kryo.register(Tag.class);
		  kryo.register(resources.Map.World.class);
		  kryo.register(Mode.class);
	}
}
