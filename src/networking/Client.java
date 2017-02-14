package networking;

// Usage:
//        java Client user-nickname hostname
//
// After initializing and opening appropriate sockets, we start two
// client threads, one to send messages, and another one to get
// messages.
//
// A limitation of our implementation is that there is no provision
// for a client to end after we start it. However, we implemented
// things so that pressing ctrl-c will cause the client to end
// gracefully without causing the server to fail.
//
// Another limitation is that there is no provision to terminate when
// the server dies.


import java.io.*;
import java.net.*;

class Client {

  public static void main(String[] args) {

    // Check correct usage:
    if (args.length != 3) {
      System.err.println("Usage: java Client user-nickname hostname");
      System.exit(1); // Give up.
    }

    // Initialize information:
    String nickname = args[0];
    String hostname = args[2];
    String port = args[1];

    // Open sockets:
    ObjectOutputStream toServer = null;
    ObjectInputStream fromServer = null;
    Socket server = null;
    
    // Create an object to store the data of a Tic Tac Toe game when it is created.
    // This allows us to share the data between the Client Sender and Reciever.
    GameSharing gs = new GameSharing();
    
    // Create the Client Data and Model. This stores all the information about the client.
    // i.e: If it is connected to another client, if it has sent a request etc.
    // It is created here to allow the Model to be accessed in the GUI, Client Sender and Receiver.
    ConnectionData conn = new ConnectionData();
    ConnectionDataModel cModel = new ConnectionDataModel(conn);
    
    System.out.println("Trying to connect...");

    try {
      //System.out.println("connected3");
      server = new Socket(hostname, Integer.parseInt(port));
      //System.out.println("connected4");
      toServer = new ObjectOutputStream(server.getOutputStream());
      Message m = new Message();
      m.setMessage(nickname);
      m.setCommand(Command.MESSAGE);
      toServer.writeObject(m);
      //System.out.println("connected5");
      fromServer = new ObjectInputStream(server.getInputStream());
      System.out.println("Connected to Server.");
    } 
    catch (UnknownHostException e) {
      System.err.println("Unknown host: " + hostname);
      System.exit(1); // Give up.
    } 
    catch (IOException e) {
      System.err.println("The server doesn't seem to be running " + e.getMessage());
      System.exit(1); // Give up.
    }

    // Create two client threads:
    ClientSender sender = new ClientSender(nickname,toServer, gs);
    ClientReceiver receiver = new ClientReceiver(fromServer, gs, cModel, toServer);
    
    // Create a thread for the GUI:
    ClientGUI gui = new ClientGUI(cModel, nickname, toServer);

    // Run them in parallel:
    sender.start();
    receiver.start();
    gui.start();
    
    System.out.println("GUI Initialised.");
    
    // Wait for them to end and close sockets.
    try {
      sender.join();
      toServer.close();
      receiver.join();
      fromServer.close();
      gui.join();
      server.close();
    }
    catch (IOException e) {
      System.err.println("Something wrong " + e.getMessage());
      System.exit(1); // Give up.
    }
    catch (InterruptedException e) {
      System.err.println("Unexpected interruption " + e.getMessage());
      System.exit(1); // Give up.
    }
  }
}
