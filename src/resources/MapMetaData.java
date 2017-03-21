package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import resources.Resources.Mode;

/**
 * Extract what gamemodes a map is compatible with
 * Example usage in main method
 * 
 * @author Oliver Gratton
 *
 */
public class MapMetaData
{
	private BufferedReader br;
	private Mode[] gamemodes = Mode.values();
	private ArrayList<String> gamemodeNames;

	private ArrayList<Mode> compatibleModes;
	private String name;

	private String line = "";
	private final String cvsSplitBy = ",";
	private final String gamemodeTag = "@GMs:";
	private final String nameTag = "@Name:";

	public MapMetaData()
	{

		gamemodeNames = new ArrayList<String>();
		for (int i = 0; i < gamemodes.length; i++)
		{
			gamemodeNames.add(gamemodes[i].toString());
		}

	}

	/**
	 * @return the list of modes that were compatible with the last map
	 * we ran readMetaData on
	 */
	public ArrayList<Mode> getCompatibleModes()
	{
		return compatibleModes;
	}

	/**
	 * @return the name of the map we last ran readMetaData on
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Read a map and return the list of gamemodes it can be played on
	 * If tags occur twice, take the latter occurence (it overwrites)
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public void readMetaData(String filename) throws IOException
	{
		br = new BufferedReader(new FileReader(filename));
		ArrayList<Mode> compatibleModes = new ArrayList<Mode>();

		while ((line = br.readLine()) != null)
		{
			if (line.startsWith(gamemodeTag))
			{
				// remove the leading symbol
				line = line.substring(gamemodeTag.length());
				// parse and write the metadata into resources
				String[] items = line.split(cvsSplitBy);
				for (int i = 0; i < items.length; i++)
				{
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
				this.compatibleModes = compatibleModes;
			}
			else if (line.startsWith(nameTag))
			{
				line = line.substring(nameTag.length());
				this.name = line.trim();
			}
		}	
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
		File folder = new File(FilePaths.maps);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
		{
			mmd.readMetaData(listOfFiles[i].toString());
			System.out.println( mmd.getName() + ": " + mmd.getCompatibleModes());
		}

	}
}
