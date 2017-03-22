package graphics.tests;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import graphics.Graphics;
import resources.Resources;
import resources.Character;

public class TopBarTests {

	@Before
	public void setUp(){
		
		Resources resources = new Resources();
		Graphics g = new Graphics(resources);
		
		Character alice = new Character(Character.Class.WIZARD);
		
		
		
		
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
