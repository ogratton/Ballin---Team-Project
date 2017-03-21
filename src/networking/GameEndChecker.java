package networking;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;

import resources.Resources;

public class GameEndChecker extends Thread {
	
	private String sessionId;
	private ConcurrentMap<String, Resources> resourcesMap;
	private ConcurrentMap<String, Session> sessions;
	private ConcurrentMap<String, Connection> connections;
	private Resources resources;
	
	public GameEndChecker(String sessionId, ConcurrentMap<String, Resources> resourcesMap, ConcurrentMap<String, Session> sessions, ConcurrentMap<String, Connection> connections, Resources resources) {
		super();
		this.sessionId = sessionId;
		this.resourcesMap = resourcesMap;
		this.sessions = sessions;
		this.connections = connections;
		this.resources = resources;
	}
	
	public void run() {
//		while(!resources.gamemode.isGameOver()) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		Session session = sessions.get(sessionId);
//		
//		session.setGameInProgress(false);
//		List<ClientInformation> clients = session.getAllClients();
//		for(int i=0; i<clients.size(); i++) {
//			clients.get(i).setReady(false);
//		}
//		
//		for(Connection c : connections.values()) {
//			Message message = new Message(Command.GAME, Note.FINISHED, null, null, null, null);
//			c.sendTCP(message);
//			Message message1 = new Message(Command.SESSION, Note.COMPLETED, null, null, null, null, sessions);
//			c.sendTCP(message1);
//		}
	}
}
