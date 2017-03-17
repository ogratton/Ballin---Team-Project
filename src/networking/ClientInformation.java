package networking;

import java.util.UUID;

import graphics.sprites.Sprite;

public class ClientInformation {
	
	private String id;
	private String name;
	private String sessionId;
	private boolean ready;
	private resources.Character.Class characterClass;
	private int playerNumber;

	public ClientInformation() {
		
	}
	
	public ClientInformation(String name) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.ready = false;
	}
	
	public ClientInformation(String id, String name) {
		this.id = id;
		this.name = name;
		this.ready = false;
	}
	
	public ClientInformation(String id, String name, resources.Character.Class characterClass) {
		this.id = id;
		this.name = name;
		this.ready = false;
		this.characterClass = characterClass;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean b) {
		this.ready = b;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	

	public resources.Character.Class getCharacterClass() {
		return characterClass;
	}

	public void setCharacterClass(resources.Character.Class characterClass) {
		this.characterClass = characterClass;
	}
	

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
}
