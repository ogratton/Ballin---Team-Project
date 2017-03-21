package resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import resources.Resources.Mode;

/**
 * Extract map metadata from csv files
 * and add it to the given resources
 * 
 * @author Oliver Gratton
 *
 */
public class MapMetaData
{
	private BufferedReader br;
	private Resources resources;
	private Mode[] gamemodes = Mode.values();
	
	private String line = "";
	private final String cvsSplitBy = ",";
	private final String metadata = "@";
	
	public MapMetaData(Resources resources)
	{
		this.resources = resources;
		
		ArrayList<String> gamemodeNames = new ArrayList<String>();
		for (int i = 0; i < gamemodes.length; i++)
		{
			gamemodeNames.add(gamemodes[i].toString());
		}
	}
	
	/**
	 * Read a map and write the metadata to the corresponding map object in resources
	 * @param filename
	 * @throws IOException
	 */
	public void readMetaData(String filename) throws IOException
	{
		br = new BufferedReader(new FileReader(filename));

		while ((line = br.readLine()) != null)
		{
			if (line.startsWith(metadata))
			{
				// remove the leading symbol
				line = line.substring(1);
				// parse and write the metadata into resources
				String[] items = line.split(cvsSplitBy);
				for (int i = 0; i < items.length; i++)
				{
					// parse items[i] into resources.Mode enum and add to list in Map
					// TODO if items[i] is in gamemodeNames, add corresponding mode to a list in the map object
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		Resources res = new Resources();
		MapMetaData mmd = new MapMetaData(res);
	}
	
}
