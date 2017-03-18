package networking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import gamemodes.GameModeFFA;
import resources.Resources;
import resources.Resources.Mode;

public class Session {
	
	private String id;
	private ConcurrentMap<String, ClientInformation> clients;
	private boolean gameInProgress;
	private String mapName;
	private resources.Map.World tileset;
	private String sessionName;
	private Mode gameMode;
	private String hostName;
	private int numberOfAI;

	public Session() {
		
	}

	public Session(ConcurrentMap<String, ClientInformation> clients) {
		this.id = UUID.randomUUID().toString();
		this.clients = clients;
		this.gameInProgress = false;
	}
	
	public Session(String id, ConcurrentMap<String, ClientInformation> clients, boolean gameInProgress) {
		this.id = id;
		this.clients = clients;
		this.gameInProgress = gameInProgress;
	}
	
	public Session(String sessionName, ClientInformation client, String mapName, resources.Map.World tileset, Mode modeName, String hostName, int numberOfAI) {
		this.id = UUID.randomUUID().toString();
		this.sessionName = sessionName;
		this.clients = new ConcurrentHashMap<String, ClientInformation>();
		this.clients.put(client.getId(), client);
		this.mapName = mapName;
		this.tileset = tileset;
		this.gameMode = modeName;
		this.hostName = hostName;
		this.numberOfAI = numberOfAI;
	}

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
	
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public resources.Map.World getTileset() {
		return tileset;
	}

	public void setTileset(resources.Map.World tileset) {
		this.tileset = tileset;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public Mode getGameMode() {
		return gameMode;
	}

	public void setGameMode(Mode gameMode) {
		this.gameMode = gameMode;
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public int getNumberOfAI() {
		return numberOfAI;
	}

	public void setNumberOfAI(int numberOfAI) {
		this.numberOfAI = numberOfAI;
	}
}


