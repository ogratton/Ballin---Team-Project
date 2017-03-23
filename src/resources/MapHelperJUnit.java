package resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import org.junit.Test;

import resources.Map.Tile;
import resources.Map.World;
import resources.Resources.Mode;

/**
 * Test MapCosts, MapMetaData, MapReader
 * 
 * @author Oliver Gratton
 *
 */
public class MapHelperJUnit
{

	/**
	 * Test map reader by reading a known map and testing a number of tiles
	 * @throws IOException 
	 */
	@Test
	public void testMapReader() throws IOException
	{
		MapReader mr = new MapReader();
		Tile[][] map1Tiles = mr.readMap(FilePaths.maps + "map1.csv");
		// NOTE: first index is row, second column
		assertEquals(Tile.ABYSS, map1Tiles[0][0]);
		assertEquals(Tile.EDGE_NW, map1Tiles[2][3]);
		assertEquals(Tile.EDGE_S, map1Tiles[10][16]);
		assertEquals(Tile.EDGE_SE, map1Tiles[22][44]);
		assertEquals(Tile.FLAT, map1Tiles[7][35]);
	}
	
	/**
	 * Test map costs by reading a known map and testing a number of tiles
	 */
	@Test
	public void testMapCosts()
	{
		Resources resources = new Resources();
		resources.setMap(new Map(1200, 650, World.CAKE, "map1"));
		MapCosts mc = new MapCosts(resources);
		
		Map map = resources.getMap();
		int[][] proxmask = map.getProxMask();
		double[][] costmask = map.getCostMask();
		
		assertEquals(0, proxmask[0][0]);
		assertEquals(mc.costEquation(0), costmask[0][0], 0);
		
		assertEquals(1, proxmask[2][3]);
		assertEquals(mc.costEquation(1), costmask[2][3], 0);
		
		assertEquals(1, proxmask[10][16]);
		assertEquals(mc.costEquation(1), costmask[10][16], 0);
		
		assertEquals(1, proxmask[22][44]);
		assertEquals(mc.costEquation(1), costmask[22][44], 0);
		
		assertEquals(6, proxmask[7][35]);
		assertEquals(mc.costEquation(6), costmask[7][35], 0);
		
	}
	
	/**
	 * Test map metadata tables by manually making the sets of all the compatible maps
	 * @throws IOException 
	 */
	@Test
	public void testMapMetaDataTables() throws IOException
	{
		// This is a set of the maps that are compatible with Deathmatch:
		HashSet<String> deathmatchMaps = new HashSet<String>();
		deathmatchMaps.add("ballin");
		deathmatchMaps.add("template");
		deathmatchMaps.add("trendy");
		deathmatchMaps.add("map1");
		deathmatchMaps.add("map0");
		deathmatchMaps.add("infinity");
		deathmatchMaps.add("pit");
		deathmatchMaps.add("asteroid");
		deathmatchMaps.add("plus");
		Hashtable<Mode, HashSet<String>> deathmatchTable = MapMetaData.getTable();
		assertTrue(deathmatchTable.get(Mode.Deathmatch).containsAll(deathmatchMaps));
		
		// All maps compatible with Deathmatch are also compatible with LastManStanding
		assertTrue(deathmatchTable.get(Mode.LastManStanding).containsAll(deathmatchMaps));
		
		// This is a set of the maps that are compatible with Hot Potato:
		HashSet<String> potatoMaps = new HashSet<String>();
		deathmatchMaps.add("newpotato");
		deathmatchMaps.add("potato");
		deathmatchMaps.add("jacketpotato");
		deathmatchMaps.add("potato2");
		Hashtable<Mode, HashSet<String>> potatoTable = MapMetaData.getTable();
		assertTrue(potatoTable.get(Mode.Deathmatch).containsAll(potatoMaps));
		
	}
	
	@Test
	public void testMapMetaDataPerMap() throws IOException
	{
		MapMetaData mmd = new MapMetaData();
		mmd.readMetaData(FilePaths.maps + "asteroid.csv");
		ArrayList<Mode> asteroidModes = new ArrayList<Mode>();
		asteroidModes.add(Mode.Deathmatch);
		asteroidModes.add(Mode.LastManStanding);
		asteroidModes.add(Mode.Debug);
		
		assertEquals("Asteroid", mmd.getName());
		assertEquals(asteroidModes, mmd.getCompatibleModes());
	}
	
}
