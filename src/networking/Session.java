package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class Session {
	
	private int id;
	private List<Integer> clientIds;
	private Game game;
	
	public Session(int id, List<Integer> clientIds, Game game) {
		this.id = id;
		this.clientIds = clientIds;
		this.game = game;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<Integer> getClientIds() {
		return clientIds;
	}
	
	public void setClientIds() {
		clientIds = new ArrayList<Integer>();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
