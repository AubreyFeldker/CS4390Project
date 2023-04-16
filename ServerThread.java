import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket clientSocket;

    public ServerThread(Socket socket){
        clientSocket = socket;
        System.out.println("Client socket has been created: " + socket);
    }

    @Override
    public void run(){
        String clientSentence;
        try{

            //Allows reading and writing to the server
            BufferedReader inFromClient =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));


            DataOutputStream outToClient =
                    new DataOutputStream(clientSocket.getOutputStream());

            //Read a line from client until they stop sending in line inputs
            while((clientSentence = inFromClient.readLine()) != null) {
                //Script Engine to evaluate math equations
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                String answer = "";
                try {
                    System.out.println("Answer is: " + engine.eval(clientSentence));
                    answer = (engine.eval(clientSentence)).toString();
                } catch (ScriptException error) {
                    System.out.println("Invalid equation: " + error);
                }

                outToClient.writeBytes(answer + '\n');
            }
        }
        catch(Exception e){
            System.out.println("Error when running thread: " + e);
        }
    }
}
