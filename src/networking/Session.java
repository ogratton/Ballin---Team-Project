package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import resources.Resources.Mode;

/**
 * Sessions store all the different aspects of a game lobby. The number of clients etc.
 * @author aaquibnaved
 *
 */
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

	/**
	 * Constructs the Session object and automatically sets the Game In Progress to false.
	 * @param clients The Hash Map of clients which are in the session.
	 */
	public Session(ConcurrentMap<String, ClientInformation> clients) {
		this.id = UUID.randomUUID().toString();
		this.clients = clients;
		this.gameInProgress = false;
	}
	
	/**
	 * Constructs the Session object taking in a specified ID and gameInProgress boolean.
	 * @param id The ID of the Session
	 * @param clients HashMap of all clients in the session
	 * @param gameInProgress Whether the game is in progress or not
	 */
	public Session(String id, ConcurrentMap<String, ClientInformation> clients, boolean gameInProgress) {
		this.id = id;
		this.clients = clients;
		this.gameInProgress = gameInProgress;
	}
	
	/**
	 * Constructs the session taking in all the variables associated with a game in a session.
	 * @param sessionName The Name of the session specified by the session creator.
	 * @param client The Client starting the session.
	 * @param mapName The name of the map which which the game on the session will be played on.
	 * @param tileset The tileset of the map
	 * @param modeName The name of the Game Mode.
	 * @param hostName The name of the host
	 * @param numberOfAI The number of AI in the session
	 */
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

	/**
	 * Is the game in progress or not?
	 * @return true if the game is in progress, false otherwise.
	 */
	public boolean isGameInProgress() {
		return gameInProgress;
	}
	
	/**
	 * Set whether the game is in progress.
	 * @param gameInProgress 
	 */
	public void setGameInProgress(boolean gameInProgress) {
		this.gameInProgress = gameInProgress;
	}

	/**
	 * Get the ID of the Session
	 * @return The ID of the session as a String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the ID of the Session
	 * @param id The ID of the session as a string.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get a client from the session using their ID.
	 * @param id The ID of the client
	 * @return Information regarding the specified Client
	 */
	public ClientInformation getClient(String id) {
		return clients.get(id);
	}
	
	/**
	 * Add a client to the session
	 * @param id The ID of the Client
	 * @param client The information about the client.
	 */
	public void addClient(String id, ClientInformation client) {
		clients.put(id, client);
	}
	
	/**
	 * Remove a client from the session
	 * @param id The ID of the client
	 */
	public void removeClient(String id) {
		clients.remove(id);
	}
	
	/**
	 * Get a list of all the clients in the session as a list.
	 * @return List of all the Clients.
	 */
	public List<ClientInformation> getAllClients() {
		List<ClientInformation> clientList = new ArrayList<ClientInformation>();
		for (ClientInformation client : clients.values()) {
			clientList.add(client);
		}
	    return clientList;
	}
	
	/**
	 * Check if all the clients in a session are ready.
	 * @return true if all clients are ready, false otherwise.
	 */
	public boolean allClientsReady() {
		boolean ready = true;
		for (ClientInformation client : clients.values()) {
			ready = ready && client.isReady();
		}
		
		return ready;
	}
	
	/**
	 * Get the name of the map
	 * @return The name of the map
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * Set the name of the map
	 * @param mapName The name of the map.
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	/**
	 * Get the tileset of the map in the session
	 * @return The tileset of the map.
	 */
	public resources.Map.World getTileset() {
		return tileset;
	}

	/**
	 * Set the tileset of the map in the session
	 * @param tileset The tileset of the map.
	 */
	public void setTileset(resources.Map.World tileset) {
		this.tileset = tileset;
	}

	/**
	 * Get the session name.
	 * @return The session name.
	 */
	public String getSessionName() {
		return sessionName;
	}

	/**
	 * Set the session name
	 * @param sessionName The session Name
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	/**
	 * Get the game mode.
	 * @return The game mode
	 */
	public Mode getGameMode() {
		return gameMode;
	}

	/**
	 * Set the game mode
	 * @param gameMode The game mode
	 */
	public void setGameMode(Mode gameMode) {
		this.gameMode = gameMode;
	}
	
	/**
	 * Get the name of the host of the session
	 * @return The name of the host
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Set the name of the host of the session
	 * @param hostName The name of the host
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	/**
	 * Get the number of AI in the session.
	 * @return The number of AI
	 */
	public int getNumberOfAI() {
		return numberOfAI;
	}

	/**
	 * Set the number of AI in the session.
	 * @param numberOfAI The Number of AI.
	 */
	public void setNumberOfAI(int numberOfAI) {
		this.numberOfAI = numberOfAI;
	}
}


