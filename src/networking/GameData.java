package networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import resources.NetworkMove;

public class GameData implements Serializable {
	
	public enum Tag {
		START, POSITION
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2012501718012423753L;
	private Tag tag;
	private List<CharacterInfo> characters;
	private CharacterInfo info;
	private Queue<NetworkMove> clientMoves;
	
	public GameData(List<CharacterInfo> characters) {
		this.characters = characters;
	}
	
	public GameData(CharacterInfo info) {
		this.info = info;
	}
	
	public GameData(Queue<NetworkMove> moves) {
		clientMoves = moves;
	}
	
	public List<CharacterInfo> getCharactersList() {
		return characters;
	}
	
	public CharacterInfo getInfo() {
		return this.info;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public Queue<NetworkMove> getMoves() {
		return clientMoves;
	}
	
	public void setMoves(Queue<NetworkMove> moves) {
		clientMoves = moves;
	}
	
}
