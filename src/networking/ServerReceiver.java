package networking;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import resources.MapReader;
import resources.Resources;

import java.io.*;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

public class ServerReceiver extends Thread {
  private ObjectInputStream myClient;
  private ConcurrentMap<UUID, Session> sessions;
  private ConcurrentMap<UUID, ClientInformation> clients;
  private ConcurrentMap<UUID, Resources> resourcesMap;

  /**
   * Creates a Server Receiver to receive messages from a client. It interprets these messages and acts accordingly.
   * @param id The ID of the client for which this Server Receiver is for.
   * @param c The reader object which reads the messages from the client.
   * @param t The client table containing the information about the client on the server.
   */
  public ServerReceiver(ObjectInputStream c, ConcurrentMap<UUID, Session> sessions, ConcurrentMap<UUID, ClientInformation> clients, ConcurrentMap<UUID, Resources> resourcesMap) {
    myClient = c;
    this.sessions = sessions;
    this.clients = clients;
    this.resourcesMap = resourcesMap;
  }

  public void run() {
	  Message message = new Message();
	  Message response = null;
	  UUID sessionId;
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
					  //ConcurrentMap<UUID, SerializableSession> serialized = serialize(this.sessions);
					  response = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
					  //System.out.println("Sessions Inside While Loop: " + serialize(this.sessions).size());
					  senderClient.getQueue().offer(response);
					  break;
				  case CREATE:
					  System.out.println("Creating Session.");
					  senderClient = clients.get(message.getSenderId());
					  if(senderClient.getSession() != null) {
						  senderClient.getSession().removeClient(senderClient.getId());
					  }
				  	  ConcurrentMap<UUID, ClientInformation> sessionClients = new ConcurrentHashMap<UUID, ClientInformation>();
				  	  sessionClients.put(senderClient.getId(), senderClient);
				  	  session = new Session(sessionClients);
					  sessions.put(session.getId(), session);
					  resourcesMap.put(session.getId(), new Resources());
					  senderClient.setSession(session);
					  response = new Message(Command.SESSION, Note.CREATED, senderClient.getId(), null, session.getId(), null, sessions);
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
					  response = new Message(Command.SESSION, Note.JOINED, senderClient.getId(), null, session.getId(), null, sessions);
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
					  response = new Message(Command.SESSION, Note.LEFT, senderClient.getId(), null, null, null, sessions);
					  senderClient.getQueue().offer(response);
					  response = new Message(Command.SESSION, Note.COMPLETED, senderClient.getId(), null, null, null, sessions);
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
					  
					  NetworkingDemo.startServerGame(session, resourcesMap, sessions);
					  
					  List<resources.Character> characters = resourcesMap.get(session.getId()).getPlayerList();
					  List<CharacterInfo> characterInfo = new ArrayList<CharacterInfo>();
					  for(int i=0; i<characters.size(); i++) {
						  resources.Character character = characters.get(i);
						  characterInfo.add(new CharacterInfo(character.getId(), character.getX(), character.getY(), character.getPlayerNumber()));
					  }
					  
					  GameData data = new GameData(characterInfo);
					  Message startGame = new Message(Command.GAME, Note.START, message.getSenderId(), message.getReceiverId(), message.getCurrentSessionId(), message.getTargetSessionId(), data);
					  
					  // Send to all clients in the session.
					  for(int i=0; i<clientList.size(); i++) {
						  clientList.get(i).getQueue().offer(startGame);
					  }
					  
					  break;
				  case UPDATE:
					  session = sessions.get(message.getCurrentSessionId());
					  clientList = session.getAllClients();
					  
					  CharacterInfo info = ((GameData)message.getObject()).getInfo();
					  for(int i=0; i<resourcesMap.get(session.getId()).getPlayerList().size(); i++) {
						  resources.Character c = resourcesMap.get(session.getId()).getPlayerList().get(i);
						  if(info.getId().equals(c.getId())) {
							  c.setControls(info.isUp(), info.isDown(), info.isLeft(), info.isRight(), info.isJump(), info.isPunch(), info.isBlock());
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
			  System.exit(1);
		  }
		  
		  
	  }
      // No point in trying to close sockets. Just give up.
      // We end this thread (we don't do System.exit(1)).
  }
  
//  private ConcurrentMap<UUID, SerializableSession> serialize(ConcurrentMap<UUID, Session> sessionsToChange) {
//	  ConcurrentMap<UUID, SerializableSession> serialized = new ConcurrentHashMap<UUID, SerializableSession>();
//	  for (Map.Entry<UUID, Session> entry : sessionsToChange.entrySet()) {
//		    UUID key = entry.getKey();
//		    Session value = entry.getValue();
//		    serialized.put(key, value.serialize());
//	  }
//	  
//	  System.out.println("Originial Hashcode: " + sessionsToChange.hashCode());
//	  System.out.println("New Hashcode: " + serialized.hashCode());
//	  
//	  return serialized;
//  }

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
