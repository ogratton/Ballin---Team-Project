package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

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
	public ArrayList<String> gamemodeNames;

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
	 * @param filepath path to file
	 * @throws IOException
	 */
	public void readMetaData(String filepath) throws IOException
	{	
		br = new BufferedReader(new FileReader(filepath));
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
	public Mode correspondingMode(String supposedMode)
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

	/**
	 * Go through every map and adds each to a table
	 * to build a table of Mode to map name
	 * e.g. table.get(Mode.HotPotato) will return a set
	 * of all the maps that can be played on HotPotato
	 * mode
	 * 
	 * @return table of gamemode enum to set of map names (filename - .csv)
	 * @throws IOException
	 */
	public static Hashtable<Mode, HashSet<String>> getTable() throws IOException
	{
		MapMetaData mmd = new MapMetaData();
		File folder = new File(FilePaths.maps);
		File[] listOfFiles = folder.listFiles();	
		Hashtable<Mode, HashSet<String>> modeDict = new Hashtable<Mode, HashSet<String>>();

		for (int i = 0; i < listOfFiles.length; i++)
		{
			mmd.readMetaData(listOfFiles[i].toString());
//			System.out.println( mmd.getName() + ": " + mmd.getCompatibleModes());
			
			ArrayList<Mode> compats = mmd.getCompatibleModes();
			
			for (int j = 0; j < compats.size(); j++)
			{
				Mode tempMode = compats.get(j);
				HashSet<String> tempSet = modeDict.get(tempMode);
				if (tempSet == null) tempSet = new HashSet<String>();
//				String tempFilename = mmd.getFilename();
				tempSet.add(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));				
				modeDict.put(tempMode, tempSet);
			}
		}

		return modeDict;
	}
	
	public static void main(String[] args) throws IOException
	{
		System.out.println(MapMetaData.getTable().get(Mode.Deathmatch));
	}
}
