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


        //Used to read from and write to the server
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //Initial connection message with server
        equation = inFromUser.readLine(); //write initial message
        outToServer.writeBytes(equation + '\n'); //send initial message
        answer = inFromServer.readLine(); //retrieve initial server response
        System.out.println(answer + " has connected to the server");
        while(true) {

            //Get line written from user
            equation = inFromUser.readLine();

            // Signifies time to disconnect
            if (equation.compareTo("exit") == 0) {
                
                while (true) { // Make sure the connection is closed on the server side
                    outToServer.writeBytes("DISCONNECT" + '\n');

                    answer = inFromServer.readLine();
                    if(answer.compareTo("DISCONNECT") == 0)
                        break; 
                }
                break;
            }

            outToServer.writeBytes(equation + '\n');
            answer = inFromServer.readLine();

            String[] split = answer.split(" ");
            System.out.println("ANSWER FROM SERVER: " + split[split.length-1]);
        }
        
        System.out.println("CONNECTION WITH SERVER TERMINATED. GOODBYE.");
        clientSocket.close();
    }
}
        