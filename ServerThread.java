import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class ServerThread implements Runnable {

    private Socket clientSocket;
    private ClientInfo client;
    private ArrayList<ClientInfo> clientList;

    public ServerThread(Socket socket, ArrayList<ClientInfo> users){
        clientSocket = socket;
        clientList = users;
        System.out.println("Client socket has been created: " + socket);
    }

    @Override
    public void run(){
        String clientSentence;
        String clientName;
        String answer;

        try{
            //Allows reading and writing to the server
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            //Creating new client in clientList from first message
            clientName = inFromClient.readLine();
            int id = clientList.size();
            client = new ClientInfo(id);
            clientList.add(client);

            System.out.println(clientName + " has connected to the server");
            outToClient.writeBytes("CONNECT " + clientName + '\n');
            
            //Script Engine to evaluate math equations
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");

            //Read a line from client until they stop sending in line inputs
            while((clientSentence = inFromClient.readLine()) != null) {
                //DISCONNECT MESSAGE
                if(clientSentence.compareTo("DISCONNECT") == 0)
                    break;

                client.numEquations++;
//                String[] l = clientSentence.split(" ");
//                clientSentence = String.join(" ", Arrays.copyOfRange(l,2,l.length));
                
                try {
                    System.out.println("Answer for " + clientName + " is: " + engine.eval(clientSentence));
                    answer = (engine.eval(clientSentence)).toString();
                } catch (ScriptException error) {
                    System.out.println("Invalid equation: " + error);
                    answer = "Invalid equation";
                }

                outToClient.writeBytes(clientName + " " + answer + '\n');
            }
            client.endClient();
            outToClient.writeBytes("DISCONNECT" + '\n');
            System.out.println("Connection with " + clientName + " terminated. Connection lasted " + client.timeAttached / 1000 + " seconds.");

            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new DataOutputStream(clientSocket.getOutputStream());
        }
        catch(Exception e){
            System.out.println("Error when running thread: " + e);
        }
    }
}
