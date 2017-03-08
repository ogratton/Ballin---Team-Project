package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import resources.Resources;

public class ClientUpdater extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UUID sessionId;
	private ConcurrentMap<UUID, Resources> resourcesMap;
	private ConcurrentMap<UUID, Session> sessions;
	
/**
 * This creates a panel of buttons controlling the client GUI. It includes 4 buttons: Exit, Online Clients, Score Card, Request.
 * When a button is clicked it sends a message to the Server Receiver where the message is interpreted.	
 * @param cModel The Client Data Model object. This is where all the information about the client is stored.
 * @param toServer The output stream to the Server Receiver.
 */
	
	public ClientUpdater(UUID sessionId, ConcurrentMap<UUID, Resources> resourcesMap, ConcurrentMap<UUID, Session> sessions) {
		super();
		this.sessionId = sessionId;
		this.resourcesMap = resourcesMap;
		this.sessions = sessions;
	}

/**
 * Updates the button panel when changes are made to the Client Data Model
 * If the client is connected to another client (i.e: playing a game) the buttons in this panel are disabled.
 * This means that the client is forced to finish or quit the game before trying to press any other commands.
 * @param o
 * @param arg
 */
	@Override
	public void update(Observable o, Object arg) {
		//System.out.println("Updated");
//		List<resources.Character> characters = resourcesMap.get(sessionId).getPlayerList();
//		List<CharacterInfo> charactersList = new ArrayList<CharacterInfo>();
//		resources.Character c;
//		for(int i=0; i<characters.size(); i++) {
//			c = characters.get(i);
//			//System.out.println("X: " + c.getX());
//			CharacterInfo info = new CharacterInfo(c.getId(), c.getX(), c.getY(), c.getPlayerNumber(), c.isFalling(), c.isDead(), c.isDashing(), c.isBlocking(), c.getRequestId());
//			charactersList.add(info);
//		}
		
		GameData data = new GameData(resourcesMap.get(sessionId).getClientMoves());
		Message message = new Message(Command.GAME, Note.UPDATE, null, null, sessionId, sessionId, data);
		List<ClientInformation> clients = sessions.get(sessionId).getAllClients();
		ClientInformation client;
		for(int i = 0; i<clients.size(); i++) {
			client = clients.get(i);
			client.getQueue().offer(message);
		}
	}
	
	public UUID getSessionId() {
		return sessionId;
	}

	public void setSessionId(UUID sessionId) {
		this.sessionId = sessionId;
	}
}




