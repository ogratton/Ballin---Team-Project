package networking;

import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.io.*;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

public class ServerReceiver extends Thread {
  private ObjectInputStream myClient;
  private ConcurrentMap<Integer, Session> sessions;
  private ConcurrentMap<Integer, ClientInformation> clients;

  /**
   * Creates a Server Receiver to receive messages from a client. It interprets these messages and acts accordingly.
   * @param id The ID of the client for which this Server Receiver is for.
   * @param c The reader object which reads the messages from the client.
   * @param t The client table containing the information about the client on the server.
   */
  public ServerReceiver(ObjectInputStream c, ConcurrentMap<Integer, Session> sessions, ConcurrentMap<Integer, ClientInformation> clients) {
    myClient = c;
    this.sessions = sessions;
    this.clients = clients;
  }

  public void run() {
	  Command command = null;
	  Message message = new Message();
	  
	  while(command != Command.QUIT) {
		  try {
			  message = (Message)myClient.readObject();
		  } 
		  catch (ClassNotFoundException e) {
			  e.printStackTrace();
		  } 
		  catch (IOException e) {
			  e.printStackTrace();
		  }
		  
		  ClientInformation client = clients.get(message.getTargetId());
		  
		  switch(message.getCommand()) {
		  case SESSION:
			  switch(message.getMessage()) {
			  case("getSessions"):
				  Message response = new Message(Command.SESSION, "allSessions", client.getId(), client.getId(), sessions);
				  client.getQueue().offer(response);
			  }
		  default:
			  break;
		  }
	  }
      // No point in trying to close sockets. Just give up.
      // We end this thread (we don't do System.exit(1)).
  }
}
