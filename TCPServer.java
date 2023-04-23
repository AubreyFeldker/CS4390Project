import java.net.*;
import java.util.ArrayList;  

class ClientInfo {
    public int      id;
    public boolean  isActive;
    public long     timeStarted;
    public long     timeAttached;
    public int      numEquations;

    public ClientInfo(int id) {
        this.id = id;
        isActive = true;
        timeStarted = System.currentTimeMillis();
        numEquations = 0;
    }

    public void endClient() {
        isActive = false;
        timeAttached = System.currentTimeMillis() - timeStarted;
    }
}

class TCPServer {

    public static void main(String argv[]) throws Exception
    {
        //Welcoming socket port
        ServerSocket welcomeSocket = new ServerSocket(6789);
        ArrayList<ClientInfo> userList = new ArrayList<ClientInfo>();

        //Keep looping
        while(true) {
            //Wait for welcome socket to accept, creating a TCP socket for the client
            Socket connectionSocket = welcomeSocket.accept();

            //Create a thread to run that socket
            Thread serverThread = new Thread(new ServerThread(connectionSocket, userList));
            serverThread.start();
        }
    }
}

 

           