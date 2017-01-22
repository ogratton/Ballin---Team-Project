package networking;

import java.util.Map;
import java.io.*;

// Continuously reads from message queue for a particular client,
// forwarding to the client.

public class ServerSender extends Thread {
  private MessageQueue queue;
  private PrintStream client;
  private Map<Integer, Session> sessions;
  private Map<Integer, ClientInformation> clients;

  /**
   * Sends a message to the client based on what it reads from the blocking queue of the client.
   * @param q The message queue of the client
   * @param c The output stream which the Server Sends sends messages to the client
   * @param t The client table which contains the information about the client on the server.
   */
  public ServerSender(MessageQueue q, PrintStream c, Map<Integer, Session> sessions, Map<Integer, ClientInformation> clients) {
    queue = q;   
    client = c;
    this.sessions = sessions;
    this.clients = clients;
  }
  
  /**
   * Prints the commands to the client
   * Takes messages from the blocking queue after that.
   */
  public void run() {
	  Message msg = queue.take();
	  while (!msg.getType().equals("quit")) {
		
		msg = queue.take();
	  }
  }
}
