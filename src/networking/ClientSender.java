package networking;

import java.io.*;
import java.util.Observable;
import java.util.Observer;



// Repeatedly reads recipient's nickname and text from the user in two
// separate lines, sending them to the server (read by ServerReceiver
// thread).

public class ClientSender extends Thread implements Observer {

  private String nickname;
  private PrintStream server;
  private GameSharing gs;
  private String commandType = "";
  private String command = "";

  /**
   * Creates a Client Sender thread. This sends messages to the Server.
   * This also observes the Noughts And Crosses game with another client if the game is initialized.
   * When the user makes a move in the game, it sends a message to other client through the server with the information about the users move.
   * @param nickname The name of the client
   * @param server The server to which the client is sending the messages to.
   * @param gs The game sharing object which contains the model of the Noughts And Crosses game
   */
  ClientSender(String nickname, PrintStream server, GameSharing gs) {
    this.nickname = nickname;
    this.server = server;
    this.gs = gs;
  }

  public void run() {
    // So that we can use the method readLine:
    BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

    // Tell the server what my nickname is:
      server.println(nickname);
      boolean notObserver = true;

      // Then loop forever sending messages to recipients via the server:
      while (!commandType.equals("quit")) {
    	  
      }
  }
  

	@Override
	/**
	 * Detects when the Noughts And Crosses GUI for this client has changed.
	 * It then sends a message to the other client so that the other client's GUI matches this client's.
	 * It only sends messages when this client presses a button.
	 * If the game ends in a victory or loss, it sends a message to the server to update the scorecard in the Server.
	 */
	public void update(Observable o, Object arg) {
		
			
	}

}

