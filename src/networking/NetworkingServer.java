package networking;

// Usage:
//        java Server
//
// There is no provision for ending the server gracefully.  It will
// end if (and only if) something exceptional happens.

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import networking.GameData.Tag;
import resources.Resources;
import resources.Resources.Mode;

/**
 * The NetworkingServer Class is the main Server code which needs to be running to enable
 * Clients to connect to it.
 * @author aaquibnaved
 *
 */
public class NetworkingServer {

	static Server server;
	static int UDPport = 27970;
	static int TCPport = 27970;
	
	public static void main(String[] args) throws Exception {
		
		//Log.DEBUG();
		
		// Initialise all the different Hash Maps which store the information about each different session and game.
		// Key is the Client ID.
		ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	    ConcurrentMap<String, Resources> resourcesMap = new ConcurrentHashMap<String, Resources>();
	    
	    // Key is the Session ID.
	    ConcurrentMap<String, ClientInformation> clients = new ConcurrentHashMap<String, ClientInformation>();
	    ConcurrentMap<String, Connection> connections = new ConcurrentHashMap<String, Connection>();
		
		server = new Server();
		
		registerClasses(server);
		
		// Bind the Server to the ports which it will send and receive packets through.
		server.bind(UDPport, TCPport);
		
		server.addListener(new ServerListener(sessions, clients, resourcesMap, connections));
		
		server.start();
		
		System.out.println("Server is operational.");
	}
	
	/**
	 * This method registers all the classes we will be sending across the network
	 * with Kryonet's networking system.
	 * 
	 * @param server The Kryonet Server object
	 */
	private static void registerClasses(Server server) {
		Kryo kryo = server.getKryo();
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
