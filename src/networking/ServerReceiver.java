package networking;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
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
	  Message message = new Message();
	  Message response = null;
	  int sessionId;
	  Session session;
	  List<ClientInformation> clientList;
	  
	  while(message.getCommand() != Command.QUIT) {
		  try {
			  message = (Message)myClient.readUnshared();
			  ClientInformation senderClient;
			  
			  switch(message.getCommand()) {
			  case SESSION:
				  switch(message.getNote()) {
				  case INDEX:
					  senderClient = clients.get(message.getSenderId());
					  response = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), -1, -1, -1, sessions);
					  senderClient.getQueue().offer(response);
					  break;
				  case CREATE:
					  System.out.println("Creating Session.");
					  senderClient = clients.get(message.getSenderId());
					  if(senderClient.getSession() != null) {
						  senderClient.getSession().removeClient(senderClient.getId());
					  }
					  
					  int id = generateID(sessions);
				  	  ConcurrentMap<Integer, ClientInformation> sessionClients = new ConcurrentHashMap<Integer, ClientInformation>();
				  	  sessionClients.put(senderClient.getId(), senderClient);
				  	  session = new Session(id, sessionClients);
					  sessions.put(id, session);
					  senderClient.setSession(session);
					  response = new Message(Command.SESSION, Note.CREATED, senderClient.getId(), -1, session.getId(), -1, sessions);
					  senderClient.getQueue().offer(response);
					  System.out.println("Session created.");
					  break;
				  case JOIN:
					  senderClient = clients.get(message.getSenderId());
					  sessionId = message.getTargetSessionId();
					  
					  if(senderClient.getSession() != null) {
						  senderClient.getSession().removeClient(senderClient.getId());
					  }
					  session = sessions.get(sessionId);
					  session.addClient(senderClient.getId(), senderClient);
					  senderClient.setSession(session);
					  response = new Message(Command.SESSION, Note.JOINED, senderClient.getId(), -1, session.getId(), -1, sessions);
					  //senderClient.getQueue().offer(response);
					  clientList = session.getAllClients();
					  for(int i=0; i<clientList.size(); i++) {
						  clientList.get(i).getQueue().offer(response);
					  }
					  break;
				  case LEAVE:
					  senderClient = clients.get(message.getSenderId());
					  sessionId = message.getTargetSessionId();
					  session = sessions.get(sessionId);
					  sessions.get(sessionId).removeClient(senderClient.getId());
					  senderClient.setSession(null);
					  response = new Message(Command.SESSION, Note.LEFT, senderClient.getId(), -1, -1, -1, sessions);
					  senderClient.getQueue().offer(response);
					  response = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), -1, -1, -1, sessions);
					  clientList = session.getAllClients();
					  for(int i=0; i<clientList.size(); i++) {
						  clientList.get(i).getQueue().offer(response);
					  }
					  break;
				  default:
					  break;
				  }
			  case GAME:
				  switch(message.getNote()) {
				  case START:
					  session = sessions.get(message.getCurrentSessionId());
					  session.setGameInProgress(true);
					  clientList = session.getAllClients();
					  
					  // Send to all clients in the session.
					  for(int i=0; i<clientList.size(); i++) {
						  if(clientList.get(i).getId() != message.getSenderId()) {
							  clientList.get(i).getQueue().offer(message);
						  }
					  }
					  break;
				  case UPDATE:
					  //System.out.println(message.getCurrentSessionId());
					  session = sessions.get(message.getCurrentSessionId());
					  clientList = session.getAllClients();
					  
					  // Send to all clients in the session.
					  for(int i=0; i<clientList.size(); i++) {
						  if(clientList.get(i).getId() != message.getSenderId()) {
							  clientList.get(i).getQueue().offer(message);
						  }
					  }
					  break;
				  default:
					  break;
				  }
			  default:
				  break;
			  }
		  }
		  catch(ClassNotFoundException e) {
			  e.printStackTrace();
		  }
		  catch(IOException e) {
			  e.printStackTrace();
		  }
		  
		  
	  }
      // No point in trying to close sockets. Just give up.
      // We end this thread (we don't do System.exit(1)).
  }
  
  private static boolean any(Integer i, Set<Integer> s) {
	  for (Integer ID: s) {
          if (i.equals(ID)) {
        	  return true;
          }
      }
	  return false;
  }
  
  private static Integer generateID(ConcurrentMap<Integer, Session> sessions) {
	  Integer id = 1000000;
	  while (any(id, sessions.keySet()) == true) {
		  id += 1;
	  }
	  return id;
  }
}
