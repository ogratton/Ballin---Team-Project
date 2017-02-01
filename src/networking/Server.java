package networking;

// Usage:
//        java Server
//
// There is no provision for ending the server gracefully.  It will
// end if (and only if) something exceptional happens.


import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.io.*;

public class Server {

  public static void main(String [] args) {

	  if (args.length != 1) {
	      System.err.println("Usage: java Server <port number>");
	      System.exit(1); // Give up.
	    }  
	
    // This will be shared by the server threads:
    ConcurrentMap<Integer, Session> sessions = new ConcurrentHashMap<Integer, Session>();
    
    // Initialise the client information table.
    ConcurrentMap<Integer, ClientInformation> clients = new ConcurrentHashMap<Integer, ClientInformation>();
    
    String port = args[0];

    // Open a server socket:
    ServerSocket serverSocket = null;

    // We must try because it may fail with a checked exception:
    try {
      serverSocket = new ServerSocket(Integer.parseInt(port));
    } 
    catch (IOException e) {
      System.err.println("Couldn't listen on port " + Port.number);
      System.exit(1); // Give up.
    }

    // Good. We succeeded. But we must try again for the same reason:
    try { 
      // We loop for ever, as servers usually do:

      while (true) {
        // Listen to the socket, accepting connections from new clients:
        Socket socket = serverSocket.accept();

        // This is so that we can use readLine():
        ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

        // We ask the client what its name is:
        Message clientName = null;
		try {
			clientName = (Message)fromClient.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // For debugging:
        System.out.println(clientName.getMessage() + " connected");

        // Edited Code
        Integer id = generateID(clients);
        
        clients.put(id, new ClientInformation(id, clientName.getMessage()));

        // We create and start a new thread to read from the client:
        (new ServerReceiver(fromClient, sessions, clients)).start();

        // We create and start a new thread to write to the client:
        ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
        (new ServerSender(clients.get(id).getQueue(), toClient, sessions, clients)).start();
      }
    } 
    catch (IOException e) {
      // Lazy approach:
      System.err.println("IO error " + e.getMessage());
      // A more sophisticated approach could try to establish a new
      // connection. But this is beyond this simple exercise.
    }
  }
  
  private static boolean any(Integer i, Set<Integer> s) {
	  for (Integer ID: s) {
          if (i.equals(ID)) {
        	  return true;
          }
      }
	  return false;
  }
  
  private static Integer generateID(Map<Integer, ClientInformation> clients) {
	  Integer id = 1000000;
	  while (any(id, clients.keySet()) == true) {
		  id += 1;
	  }
	  return id;
  }
}
