package resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import resources.Resources.Mode;

/**
 * Extract what gamemodes a map is compatible with
 * 
 * @author Oliver Gratton
 *
 */
public class MapMetaData
{
	private BufferedReader br;
	private Mode[] gamemodes = Mode.values();
	private ArrayList<String> gamemodeNames;

	private String line = "";
	private final String cvsSplitBy = ",";
	private final String metadata = "@";

	public MapMetaData()
	{

		gamemodeNames = new ArrayList<String>();
		for (int i = 0; i < gamemodes.length; i++)
		{
			gamemodeNames.add(gamemodes[i].toString());
		}

	}

	/**
	 * Read a map and return the list of gamemodes it can be played on
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public ArrayList<Mode> readMetaData(String filename) throws IOException
	{
		br = new BufferedReader(new FileReader(filename));
		ArrayList<Mode> compatibleModes = new ArrayList<Mode>();

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
					String supposedMode = items[i].trim();
					if (gamemodeNames.contains(supposedMode))
					{
						compatibleModes.add(correspondingMode(supposedMode));
					}
					else
					{
						System.err.println("WARNING: " + supposedMode + " is not a recognised GameMode. Ignoring");
					}
				}
			}
		}

		return compatibleModes;
	}

	/**
	 * Essentially convert from String to Mode enum
	 * Should only be called when sure translation exists
	 * 
	 * @param supposedMode string to convert to enum value
	 * @return enum value
	 */
	private Mode correspondingMode(String supposedMode)
	{
		for (int i = 0; i < gamemodes.length; i++)
		{
			if (gamemodes[i].toString().toLowerCase().equals(supposedMode.toLowerCase()))
			{
				return gamemodes[i];
			}
		}

		throw new NullPointerException("correspondingMode in MapMetaData called when it shouldn't have been");
	}

	public static void main(String[] args) throws IOException
	{
		MapMetaData mmd = new MapMetaData();
		System.out.println(mmd.readMetaData(FilePaths.maps + "asteroid.csv"));
	}
}
