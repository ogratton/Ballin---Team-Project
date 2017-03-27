package junit;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import ai.AITests;
import ai.pathfinding.PathFindingJUnit;
import audio.AudioJUnit;
import gamemodes.GamemodesTests;
import networking.NetworkingTests;
import physics.PhysicsTests;
import resources.MapHelperJUnit;

/**
 * This will run all the JUnit tests in the project
 * 
 * @author Oliver Gratton
 *
 */
public class RunAllJUnitTests
{
	
	public static void main(String[] args)
	{
//		System.out.println(AITests.class.getCanonicalName());
		
		JUnitCore junit = new JUnitCore();
		Result result;
	
		// Audio
		result = junit.run(AudioJUnit.class );
		System.out.println("AUDIO RESULTS: " + result.getFailureCount());
		
		// Game Logic
		result = junit.run( GamemodesTests.class );
		System.out.println("GAME LOGIC RESULTS: " + result.getFailureCount());
		
		// Networking
		result = junit.run(NetworkingTests.class );
		System.out.println("NETWORKING RESULTS: " + result.getFailureCount());
	
		
		// General
		result = junit.run(MapHelperJUnit.class );
		System.out.println("MISC RESULTS: " + result.getFailureCount());
		
		// AI
		result = junit.run(PathFindingJUnit.class, AITests.class );
		System.out.println("AI RESULTS: " + result.getFailureCount() + " errors");
		
		// Physics
		result = junit.run(PhysicsTests.class );
		System.out.println("PHYSICS RESULTS: " + result.getFailureCount());
		
		System.exit(1);
		
	}

}
