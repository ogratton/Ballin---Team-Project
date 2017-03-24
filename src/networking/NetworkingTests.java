package networking;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gamemodes.Deathmatch;
import resources.Map;
import resources.Powerup;
import resources.Powerup.Power;
import resources.Resources;
import resources.Map.World;

public class NetworkingTests {
	private boolean verbose = false;
	private ArrayList<Powerup> powerUps;
	private Session session;
	private ClientInformation client;
	private ConcurrentMap<String, Session> sessions;
	private ConcurrentMap<String, ClientInformation> clients;
	private Resources resources;
	
	@Before
	public void setUp() {
		powerUps = new ArrayList<Powerup>(Arrays.asList(new Powerup(), new Powerup(), new Powerup()));
		resources = new Resources();
		
		for(int i=0; i<5; i++) {
			client = new ClientInformation();
			session = new Session("A Random Session", client, "map0", Map.World.DESERT, (Mode)(new Deathmatch(resources, false, false)), client.getName(), 0);
		}
	}

	@After
	public void tearDown() {
		powerUps = null;
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
	
	
}
