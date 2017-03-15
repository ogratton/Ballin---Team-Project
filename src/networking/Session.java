package networking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import resources.Resources;

public class Session {
	
	private String id;
	private ConcurrentMap<String, ClientInformation> clients;
	private ConcurrentMap<String, ClientInformation> serializedClients;
	private boolean gameInProgress;
	//private Resources resources;
	
	public Session() {
		
	}
	
	public Session(ConcurrentMap<String, ClientInformation> clients) {
		this.id = UUID.randomUUID().toString();
		this.clients = clients;
		this.gameInProgress = false;
		//this.resources = new Resources();
	}
	
	public Session(String id, ConcurrentMap<String, ClientInformation> clients, boolean gameInProgress) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public ClientInformation getClient(String id) {
		return clients.get(id);
	}
	
	public void addClient(String id, ClientInformation client) {
		clients.put(id, client);
	}
	
	public void removeClient(String id) {
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
