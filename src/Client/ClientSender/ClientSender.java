package Client.ClientSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSender extends Thread {
    private String targetHost;
    private final int PORT = 6789;
    private BufferedReader inFromUser;
    private Socket clientSocket;

    public ClientSender(String targetHost) {
        this.targetHost = targetHost;
        inFromUser = new BufferedReader(new InputStreamReader(System.in));
    }

    private Socket make_connection() {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(targetHost, PORT);
            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println("Failed to makeConnectionSocket");
        }
        return clientSocket;
    }

    /**
     * Logging to system as first connection.
     */
    private void welcomeMessage() {
        org.json.JSONObject obj = new org.json.JSONObject();
        obj.put("type", 1);
        obj.put("content", new org.json.JSONObject());
        send_message(obj.toString());
    }

    private void send_message(String message) {

        if (clientSocket != null) {
            try {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(message + '\n');
            } catch (IOException e) {
                System.out.println("Can't send message");
            }
        }
    }

    public void run() {
        System.out.println("Making new connection");
        clientSocket = make_connection();
        welcomeMessage();
        while (true) {
            //GUI but now reading from console
            try {
                String send_text = inFromUser.readLine();
                send_message(send_text);
            } catch (IOException e) {
                System.out.println("Can't read from console");
            }
        }
    }
}
