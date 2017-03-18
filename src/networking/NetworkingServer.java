package networking;

// Usage:
//        java Server
//
// There is no provision for ending the server gracefully.  It will
// end if (and only if) something exceptional happens.


import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import networking.GameData.Tag;
import resources.Resources;
import resources.Resources.Mode;

import java.io.*;

public class NetworkingServer {

	static Server server;
	static int UDPport = 27970;
	static int TCPport = 27970;
	
	public static void main(String[] args) throws Exception {
		
		//Log.DEBUG();
		
		ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
		 
	    ConcurrentMap<String, ClientInformation> clients = new ConcurrentHashMap<String, ClientInformation>();
	    
	    ConcurrentMap<String, Resources> resourcesMap = new ConcurrentHashMap<String, Resources>();
	    
	    ConcurrentMap<String, Connection> connections = new ConcurrentHashMap<String, Connection>();
		
		server = new Server();
		
		registerClasses(server);
		
		server.bind(UDPport, TCPport);
		
		server.addListener(new ServerListener(sessions, clients, resourcesMap, connections));
		
		server.start();
		
		System.out.println("Server is operational.");
	}
	
	private static void registerClasses(Server server) {
		Kryo kryo = server.getKryo();
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
		kryo.register(resources.Character.Class.class);
	}
}
