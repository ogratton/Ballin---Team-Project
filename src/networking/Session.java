package networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

public class Session implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -859097128269222813L;
	private int id;
	private ConcurrentMap<Integer, ClientInformation> clients;
	
	public Session(int id, ConcurrentMap<Integer, ClientInformation> clients) {
		this.id = id;
		this.clients = clients;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public ClientInformation getClient(int id) {
		return clients.get(id);
	}
	
	public void addClient(int id, ClientInformation client) {
		clients.put(id, client);
	}
	
	public void removeClient(int id) {
		clients.remove(id);
	}
	
	public List<ClientInformation> getAllClients() {
		List<ClientInformation> clientList = new ArrayList<ClientInformation>();
		for (ClientInformation client : clients.values()) {
			clientList.add(client);
		}
	    return clientList;
	}
}
