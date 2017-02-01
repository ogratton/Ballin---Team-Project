package networking;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.io.*;

// Continuously reads from message queue for a particular client,
// forwarding to the client.

public class ServerSender extends Thread {
  private MessageQueue queue;
  private ObjectOutputStream toClient;
  private ConcurrentMap<Integer, Session> sessions;
  private ConcurrentMap<Integer, ClientInformation> clients;

  /**
   * Sends a message to the client based on what it reads from the blocking queue of the client.
   * @param q The message queue of the client
   * @param c The output stream which the Server Sends sends messages to the client
   * @param t The client table which contains the information about the client on the server.
   */
  public ServerSender(MessageQueue q, ObjectOutputStream c, ConcurrentMap<Integer, Session> sessions, ConcurrentMap<Integer, ClientInformation> clients) {
    queue = q;   
    toClient = c;
    this.sessions = sessions;
    this.clients = clients;
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
			toClient.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
  }
}
