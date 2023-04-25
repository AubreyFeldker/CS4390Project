import java.io.*;
import java.net.*;

class TCPClient {

    public static void main(String argv[]) throws Exception
    {
        String equation;
        String answer;
        int id;

        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("127.0.0.1", 6789);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //Initial connection message with server
        outToServer.writeBytes("CONNECT" + '\n');
        answer = inFromServer.readLine();

        id = Integer.parseInt(answer.split(" ")[1]);

        while(true) {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            equation = inFromUser.readLine();

            // Signifies time to disconnect
            if (equation.compareTo("exit") == 0) {
                
                while (true) { // Make sure the connection is closed on the server side
                    outToServer.writeBytes("DISCONNECT " + id + '\n');

                    answer = inFromServer.readLine();
                    if(answer.compareTo("DISCONNECT " + id) == 0)
                        break; 
                }
                break;
            }

            outToServer.writeBytes("MATH " + id + " " + equation + '\n');
            answer = inFromServer.readLine();

            String[] split = answer.split(" ");
            System.out.println("ANSWER FROM SERVER: " + split[2]);
        }
        
        System.out.println("CONNECTION WITH SERVER TERMINATED. GOODBYE.");
        clientSocket.close();
    }
}
        