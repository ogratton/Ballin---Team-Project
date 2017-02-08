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
      System.err.println("Couldn't listen on port " + port);
      System.exit(1); // Give up.
    }
    
    System.out.println("trying to connect.");

    // Good. We succeeded. But we must try again for the same reason:
    try { 
      // We loop for ever, as servers usually do:

      while (true) {
    	System.out.println("still trying.");
        // Listen to the socket, accepting connections from new clients:
        Socket socket = serverSocket.accept();
        
        System.out.println("server connected");

        // We create and start a new thread to write to the client:
        ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());	
        
        System.out.println("Got");
        
        // This is so that we can use readLine():
        ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
        
        System.out.println("Got Here");

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
        
        Message idMessage = new Message();
        idMessage.setCommand(Command.SEND_ID);
        idMessage.setSenderId(id);
        idMessage.setMessage(clientName.getMessage());
        toClient.writeObject(idMessage);

        // We create and start a new thread to read from the client:
        (new ServerReceiver(fromClient, sessions, clients)).start();
        
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
