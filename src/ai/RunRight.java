package ai;

import resources.Character;

public class RunRight extends Thread{

	private Character character;
	
	public RunRight(Character character){
		this.character = character;
	}
	
	public void run(){
		
		character.setRight(true);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		character.setDashing(true);
		
		
	}
	
}
