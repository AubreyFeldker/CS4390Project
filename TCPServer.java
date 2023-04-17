import java.io.*; 
import java.net.*; 

class ClientInfo {
  public int      id;
  public boolean  isActive;
  public int   timeStarted;
  public int   timeAttached;
}

class TCPServer {

    public static void main(String argv[]) throws Exception
    {
        //Welcoming socket port
        ServerSocket welcomeSocket = new ServerSocket(6789);

        //Keep looping
        while(true){
            //Wait for welcome socket to accept, creating a TCP socket for the client
            Socket connectionSocket = welcomeSocket.accept();

            //Create a thread to run that socket
            Thread serverThread = new Thread(new ServerThread(connectionSocket));
            serverThread.start();
        }
    }
}
 

           