package networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class SerializableSession implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7643089963106161264L;
	private UUID id;
	private ConcurrentMap<UUID, ClientInformation> clients;
	private boolean gameInProgress;
	
	public SerializableSession(UUID id, ConcurrentMap<UUID, ClientInformation> clients, boolean gameInProgress) {
		this.id = id;
		this.clients = clients;
		this.gameInProgress = gameInProgress;
	}
	
	public SerializableSession() {
		
	}

	public boolean isGameInProgress() {
		return gameInProgress;
	}

	public void setGameInProgress(boolean gameInProgress) {
		this.gameInProgress = gameInProgress;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public ClientInformation getClient(UUID id) {
		return clients.get(id);
	}
	
	public void addClient(UUID id, ClientInformation client) {
		clients.put(id, client);
	}
	
	public void removeClient(UUID id) {
		clients.remove(id);
	}
	
	public List<ClientInformation> getAllClients() {
		List<ClientInformation> clientList = new ArrayList<ClientInformation>();
		for (ClientInformation client : clients.values()) {
			clientList.add(client);
		}
	    return clientList;
	}
	
	public ConcurrentMap<UUID, ClientInformation> getClients() {
		return this.clients;
	}
}
