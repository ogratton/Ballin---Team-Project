package resources;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Reads a Comma Separated Value file and returns it as an ArrayList of lines
 * Could be done statically but for the dictionary
 * 
 * @author Oliver
 */
public class MapReader
{
	private BufferedReader br;
	private String line = "";
	private String cvsSplitBy = ",";
	private String comment = "#";
	private  Hashtable<String, Map.Tile> tileDict;

	/**
	 * Make a new object and initialise the dictionary of string to enum
	 */
	public MapReader()
	{
		tileDict = new Hashtable<String, Map.Tile>();
		tileDict.put("0", Map.Tile.ABYSS);
		tileDict.put("1", Map.Tile.FLAT);
		tileDict.put("2", Map.Tile.EDGE_N);
		tileDict.put("3", Map.Tile.EDGE_NE);
		tileDict.put("4", Map.Tile.EDGE_E);
		tileDict.put("5", Map.Tile.EDGE_SE);
		tileDict.put("6", Map.Tile.EDGE_S);
		tileDict.put("7", Map.Tile.EDGE_SW);
		tileDict.put("8", Map.Tile.EDGE_W);
		tileDict.put("9", Map.Tile.EDGE_NW);
		tileDict.put("a", Map.Tile.EDGE_ABYSS);
		tileDict.put("b", Map.Tile.TEST);
	}
	
	/**
	 * Read the contents of a csv into an ArrayList
	 * Ignore lines that start with a #
	 * 
	 * NOTE: ensure file has no empty whitespace lines
	 * 
	 * @param filename the filepath of the file relative to the src folder
	 * @return array list of string arrays
	 */
	private ArrayList<String[]> readContents(String filename) throws IOException
	{
		ArrayList<String[]> lines = new ArrayList<String[]>();

		br = new BufferedReader(new FileReader(filename));

		while ((line = br.readLine()) != null)
		{
			if (!line.startsWith(comment))
			{
				// use comma as separator
				String[] items = line.split(cvsSplitBy);
				lines.add(items);
			}
		}
		return lines;

	}
	
	/**
	 * Read and convert a file to Tile enum
	 * @param filename the filepath of the file relative to the src folder
	 * @return 2D Tile array
	 */
	public Map.Tile[][] readMap(String filename) throws IOException
	{
		ArrayList<String[]> mapString = readContents(filename);
		int height = mapString.size();
		int width = mapString.get(0).length;
		
		Map.Tile[][] map = new Map.Tile[height][width];
		
		for (int i = 0; i < map.length; i++)
		{
			String[] row = mapString.get(i);
			for (int j = 0; j < map[i].length; j++)
			{
				map[i][j] = tileDict.get(row[j]);
			}
		}
		
		return map;
	}
	
	/**
	 * For testing
	 */
	public static void main(String[] args)
	{
		MapReader mr = new MapReader();	
		try
		{
			Map.Tile[][] map = mr.readMap("./resources/maps/map1.csv");
			System.out.println("I guess it worked then");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
		}
	}
}
