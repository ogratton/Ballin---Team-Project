package networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import resources.Resources;

public class Session implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1929781468355749346L;
	private UUID id;
	private ConcurrentMap<UUID, ClientInformation> clients;
	private boolean gameInProgress;
	//private Resources resources;
	
	public Session(ConcurrentMap<UUID, ClientInformation> clients) {
		this.id = UUID.randomUUID();
		this.clients = clients;
		this.gameInProgress = false;
		//this.resources = new Resources();
	}
	
	public Session(UUID id, ConcurrentMap<UUID, ClientInformation> clients, boolean gameInProgress) {
		this.id = id;
		this.clients = clients;
		this.gameInProgress = gameInProgress;
		//this.resources = null;
	}

//	public SerializableSession serialize() {
//		return new SerializableSession(id, clients, gameInProgress);
//	}
	
//	public Resources getResources() {
//		return resources;
//	}
//
//	public void setResources(Resources resources) {
//		this.resources = resources;
//	}

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
	
	public boolean allClientsReady() {
		boolean ready = true;
		for (ClientInformation client : clients.values()) {
			ready = ready && client.isReady();
		}
		
		return ready;
	}
}
