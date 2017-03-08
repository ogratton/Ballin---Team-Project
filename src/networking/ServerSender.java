package networking;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import resources.Resources;

import java.io.*;

// Continuously reads from message queue for a particular client,
// forwarding to the client.

public class ServerSender extends Thread {
  private MessageQueue queue;
  private ObjectOutputStream toClient;
  private ConcurrentMap<UUID, Session> sessions;
  private ConcurrentMap<UUID, ClientInformation> clients;
  private ConcurrentMap<UUID, Resources> resourcesMap;

  /**
   * Sends a message to the client based on what it reads from the blocking queue of the client.
   * @param q The message queue of the client
   * @param c The output stream which the Server Sends sends messages to the client
   * @param t The client table which contains the information about the client on the server.
   */
  public ServerSender(MessageQueue q, ObjectOutputStream c, ConcurrentMap<UUID, Session> sessions, ConcurrentMap<UUID, ClientInformation> clients, ConcurrentMap<UUID, Resources> resourcesMap) {
    queue = q;   
    toClient = c;
    this.sessions = sessions;
    this.clients = clients;
    this.resourcesMap = resourcesMap;
  }
  
  /**
   * Prints the commands to the client
   * Takes messages from the blocking queue after that.
   */
  public void run() {
	  Message msg = queue.take();
	  while (!msg.getCommand().equals(Command.QUIT)) {
		msg = queue.take();
		try {
			toClient.reset();
//			ConcurrentHashMap<UUID, SerializableSession> s = (ConcurrentHashMap<UUID, SerializableSession>)msg.getObject();
//			Set<Entry<UUID, SerializableSession>> set = s.entrySet();
//			Iterator<Entry<UUID, SerializableSession>> it = set.iterator();
//			String name = it.next().getValue().getClass().toString();
			
			System.out.println("Command: " + msg.getCommand() + " Note: " + msg.getNote());
		
			toClient.writeUnshared(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
  }
}
