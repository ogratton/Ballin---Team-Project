package networking;

import java.net.*;
import java.util.Map;
import java.io.*;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

public class ServerReceiver extends Thread {
  private Integer myClientsID;
  private BufferedReader myClient;
  private Map<Integer, Session> sessions;
  private Map<Integer, ClientInformation> clients;

  /**
   * Creates a Server Receiver to receive messages from a client. It interprets these messages and acts accordingly.
   * @param id The ID of the client for which this Server Receiver is for.
   * @param c The reader object which reads the messages from the client.
   * @param t The client table containing the information about the client on the server.
   */
  public ServerReceiver(Integer id, BufferedReader c, Map<Integer, Session> sessions, Map<Integer, ClientInformation> clients) {
    myClientsID = id;
    myClient = c;
    this.sessions = sessions;
    this.clients = clients;
  }

  public void run() {
    try {
    	String commandType = "";
    	String myClientName = clients.get(myClientsID).getName(); 
    }
    catch (IOException e) {
      System.err.println("Something went wrong with the client " 
                       + clients.get(myClientsID).getName() + " " + e.getMessage()); 
      // No point in trying to close sockets. Just give up.
      // We end this thread (we don't do System.exit(1)).
    }
  }
}
