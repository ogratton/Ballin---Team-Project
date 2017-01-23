package networking;

import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

// Gets messages from other clients via the server (by the
// ServerSender thread).

public class ClientReceiver extends Thread {

  private BufferedReader server;
  private int i = -1;
  private int j = -1;
  private GameSharing gs;
  private ConnectionDataModel cModel;
  private PrintStream toServer;

  /**
   * Creates a Client Receiver Thread which receives messages from the server.
   * These messages are interpreted and then the Client Receiver changes the Client Data Model.
   * When a game is started, the information about the game is shared to the Client Sender by the Game Sharing class.
   * @param server The server from which the Client Receiver reads the messages.
   * @param gs The GameSharing object which contains the information about the game.
   * @param cModel The Client Data Model which contains the information about the client.
   * @param toServer The output stream to the server receiver to which messages are printed to.
   */
  ClientReceiver(BufferedReader server, GameSharing gs, ConnectionDataModel cModel, PrintStream toServer) {
    this.server = server;
    this.gs = gs;
    this.cModel = cModel;
    this.toServer = toServer;
  }

  /**
   * Runs the thread.
   */
  public void run() {
    // Print to the user whatever we get from the server:
    try {
      
    }
    catch (Exception e) {
      System.out.println("Server seems to have died " + e.getMessage());
      System.exit(1); // Give up.
    }
  }
}
