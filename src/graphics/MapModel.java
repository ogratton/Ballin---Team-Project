package graphics;

import java.util.Observable;

import resources.Map;

public class MapModel extends Observable {

	private Map map;
	
	public MapModel(Map map) {
		this.map = map;
	}

}
