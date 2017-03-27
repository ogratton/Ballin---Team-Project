package networking;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.kryonet.Connection;

import gamemodes.Deathmatch;
import gamemodes.GameModeFFA;
import gamemodes.HotPotato;
import gamemodes.LastManStanding;
import resources.Map;
import resources.Powerup;
import resources.Powerup.Power;
import resources.Resources;
import resources.Resources.Mode;
import resources.Map.World;
import resources.MapMetaData;

public class NetworkingTests {
	private boolean verbose = false;
	private ArrayList<Powerup> powerUps;
	private Session session;
	private ClientInformation client;
	private ClientInformation client1;
	private ConcurrentMap<String, Session> sessions;
	private ConcurrentMap<String, ClientInformation> clients;
	private ConcurrentMap<String, Connection> connections;
	private ConcurrentMap<String, Resources> resourcesMap;
	private Resources res;
	private Mode gameMode;
	private HashSet<String> mapNames;
	private String[] mapNamesString;
	private String[] names = {"John", "Marcus", "Susan", "Henry", "Bob", "Mary", "Arthur", "Joe"};
	private ConnectionData data;
	private ConnectionDataModel cModel;
	
	@Before
	public void setUp() {
		sessions = new ConcurrentHashMap<String, Session>();
		clients = new ConcurrentHashMap<String, ClientInformation>();
		connections = new ConcurrentHashMap<String, Connection>();
		resourcesMap = new ConcurrentHashMap<String, Resources>();
		data = new ConnectionData();
		cModel = new ConnectionDataModel(data);
		powerUps = new ArrayList<Powerup>(Arrays.asList(new Powerup(), new Powerup(), new Powerup()));
		Random rand = new Random();
		String clientName;
		
		
		// Set up sessions and clients
		for(int i=0; i<rand.nextInt(10); i++) {
			clientName = names[rand.nextInt(names.length)];
			client = new ClientInformation(clientName);
			client.setCharacterClass(resources.Character.Class.values()[rand.nextInt(resources.Character.Class.values().length)]);
			client.setPlayerNumber(rand.nextInt(7));
			clients.putIfAbsent(client.getId(), client);
			
			gameMode = Mode.Debug;
			while(!gameMode.equals(Mode.Deathmatch) && !gameMode.equals(Mode.LastManStanding) && !gameMode.equals(Mode.HotPotato)) {
				gameMode = Mode.values()[rand.nextInt(Mode.values().length)];
			}
			
			try {
				mapNames = MapMetaData.getTable().get(gameMode);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mapNamesString = mapNames.toArray(new String[mapNames.size()]);
			
			session = new Session(clientName + "'s Lobby", client, mapNamesString[rand.nextInt(mapNamesString.length)], Map.World.values()[rand.nextInt(Map.World.values().length)], gameMode, client.getName(), 0);
			for(int j=0; j<rand.nextInt(8); j++) {
				client1 = new ClientInformation(names[rand.nextInt(names.length)]);
				client1.setCharacterClass(resources.Character.Class.values()[rand.nextInt(resources.Character.Class.values().length)]);
				client1.setPlayerNumber(rand.nextInt(7));
				session.addClient(client1.getId(), client1);
				clients.put(client1.getId(), client1);
			}
			
			sessions.put(session.getId(), session);
		}
		
		cModel.setSessionsTable(sessions);
	}

	@After
	public void tearDown() {
		powerUps = null;
		session = null;
		client = null;
		client1 = null;
		sessions = null;
		clients = null;
		connections = null;
		resourcesMap = null;
		res = null;
		gameMode = null;
		mapNames = null;
		mapNamesString = null;
		data = null;
		cModel = null;
	}

	@Test
	public void testSerializePowerups() {
		ArrayList<SerializablePowerUp> serialized = ClientUpdater.serializePowerUps(powerUps);
		for(int i=0; i<powerUps.size(); i++) {
			assertTrue("Incorrect conversion!", powerUps.get(i).getX() == serialized.get(i).getX());
			assertTrue("Incorrect conversion!", powerUps.get(i).getY() == serialized.get(i).getY());
			assertTrue("Incorrect conversion!", powerUps.get(i).getPower().equals(serialized.get(i).getP()));
			assertTrue("Incorrect conversion!", powerUps.get(i).isActive() == serialized.get(i).isActive());
		}
	}
	
	@Test
	public void testDeserializePowerups() {
		ArrayList<Powerup> deserialized = ClientListener.deserialize(ClientUpdater.serializePowerUps(powerUps));
		for(int i=0; i<powerUps.size(); i++) {
			assertTrue("Incorrect conversion!", powerUps.get(i).getX() == deserialized.get(i).getX());
			assertTrue("Incorrect conversion!", powerUps.get(i).getY() == deserialized.get(i).getY());
			assertTrue("Incorrect conversion!", powerUps.get(i).getPower().equals(deserialized.get(i).getPower()));
			assertTrue("Incorrect conversion!", powerUps.get(i).isActive() == deserialized.get(i).isActive());
		}
	}
	
	@Test
	public void testStartServer() {
		
		for(Session s : sessions.values()) {
			GameModeFFA mode;
			NetworkingDemo.startServerGame(s, resourcesMap, sessions, connections);
			
			res = resourcesMap.get(s.getId());
			
			assertTrue("Incorrect game mode!", res.mode.equals(s.getGameMode()));
			assertTrue("Incorrect number of players", res.getPlayerList().size() == 8);
			int numOfHumanPlayers = 0;
			for(int i=0; i<res.getPlayerList().size(); i++) {
				if(!(res.getPlayerList().get(i).isAI())) {
					numOfHumanPlayers++;
				}
			}
			
			assertTrue("Incorrect number of Human Players", numOfHumanPlayers == s.getAllClients().size());
			
			if(s.getGameMode().equals(Mode.Deathmatch)) {
				mode = new Deathmatch(res, true, false);
				assertTrue("Incorrent game mode!", res.gamemode.equals(mode));
				for(int i=0; i<res.getPlayerList().size(); i++) {
					assertTrue("Incorrect number of lives!", res.getPlayerList().get(i).getLives() == -1);
				}
			}
			else if(s.getGameMode().equals(Mode.LastManStanding)) {
				mode = new LastManStanding(res, 5 , true, false);
				assertTrue("Incorrent game mode!", res.gamemode.equals(mode));
				for(int i=0; i<res.getPlayerList().size(); i++) {
					assertTrue("Incorrect number of lives!", res.getPlayerList().get(i).getLives() == 5);
				}
			}
			else if(s.getGameMode().equals(Mode.HotPotato)){
				mode = new HotPotato(res, true, false);
				assertTrue("Incorrent game mode!", res.gamemode.equals(mode));
				for(int i=0; i<res.getPlayerList().size(); i++) {
					assertTrue("Incorrect number of lives!", res.getPlayerList().get(i).getLives() == 1);
				}
			}
			else {
				assertTrue("", true);
			}
			
		}
	}
	
	@Test
	public void testGetAllSessionsFromConnectionData() {
		sessions = new ConcurrentHashMap<String, Session>();
		Session session1 = new Session();
		session1.setId(UUID.randomUUID().toString());
		
		Session session2 = new Session();
		session2.setId(UUID.randomUUID().toString());
		
		Session session3 = new Session();
		session3.setId(UUID.randomUUID().toString());
		
		sessions.putIfAbsent(session1.getId(), session1);
		sessions.putIfAbsent(session2.getId(), session2);
		sessions.putIfAbsent(session3.getId(), session3);
		
		data.setSessionsTable(sessions);
		Session testSession;
		
		List<Session> sessionList = data.getAllSessions();
		for(int i=0; i<sessionList.size(); i++) {
			testSession = sessionList.get(i);
			assertTrue("Method not working!", testSession.getId().equals(session1.getId()) || testSession.getId().equals(session2.getId()) || testSession.getId().equals(session3.getId()));
		}
	}
	
	@Test
	public void testGetAllClientsFromSession() {
		clients = new ConcurrentHashMap<String, ClientInformation>();
		session = new Session(clients);
		
		ClientInformation client1 = new ClientInformation("Bob");
		session.addClient(client1.getId(), client1);
		
		ClientInformation client2 = new ClientInformation("Alice");
		session.addClient(client2.getId(), client2);
		
		ClientInformation client3 = new ClientInformation("Clive");
		session.addClient(client3.getId(), client3);
		
		List<ClientInformation> clientList = session.getAllClients();
		ClientInformation testClient;
		
		for(int i=0; i<clientList.size(); i++) {
			testClient = clientList.get(i);
			assertTrue("Method not working!", testClient.getId().equals(client1.getId()) || testClient.getId().equals(client2.getId()) || testClient.getId().equals(client3.getId()));
		}
	}
	
	@Test
	public void testAllClientsReady() {
		clients = new ConcurrentHashMap<String, ClientInformation>();
		Session session1 = new Session(clients);
		
		ClientInformation client1 = new ClientInformation("Bob");
		client1.setReady(true);
		session1.addClient(client1.getId(), client1);
		
		ClientInformation client2 = new ClientInformation("Alice");
		client2.setReady(true);
		session1.addClient(client2.getId(), client2);
		
		ClientInformation client3 = new ClientInformation("Clive");
		client3.setReady(true);
		session1.addClient(client3.getId(), client3);
		
		assertTrue("Method not working", session1.allClientsReady());
		
		client1.setReady(false);
		
		assertTrue("Method not working", !session1.allClientsReady());
	}
	
}
