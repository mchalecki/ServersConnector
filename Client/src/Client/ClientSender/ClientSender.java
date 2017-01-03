package Client.ClientSender;

import Client.GUI.ChatFrame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSender extends Thread {
    private String targetHost;
    private final int PORT_server = 6789;
    private BufferedReader inFromUser;
    private Socket clientSocket;
    private Boolean quit;

    public ChatFrame gui;

    public ClientSender(String targetHost) {
        this.targetHost = targetHost;
        inFromUser = new BufferedReader(new InputStreamReader(System.in));
        quit = false;
    }

    private Socket make_connection() {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(targetHost, PORT_server);
            System.out.println("Connected");
            gui.ChatBox.append("Connected \n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to makeConnectionSocket");
        }
        return clientSocket;
    }

    /**
     * Logging to system as first connection.
     */
    private void welcomeMessage(String nick) {
        org.json.JSONObject mes = new org.json.JSONObject();
        org.json.JSONObject content = new org.json.JSONObject();
        content.put("nick", nick);
        mes.put("type", 1);
        mes.put("content", content);
        sendForward(mes.toString());
    }

    private void disconnectMessage() {
        org.json.JSONObject mes = new org.json.JSONObject();
        mes.put("type", 2);
        mes.put("content", new org.json.JSONObject());
        sendForward(mes.toString());
    }

    public void sendMessage(String message, String target) {
        org.json.JSONObject mes = new org.json.JSONObject();
        org.json.JSONObject content = new org.json.JSONObject();
        content.put("text", message);
        content.put("to", target);
        mes.put("type", 3);
        mes.put("content", content);
        sendForward(mes.toString());
    }

    private void sendForward(String message) {
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
        gui.ChatBox.append("Making new connection \n");

        clientSocket = make_connection();
        welcomeMessage("adam");
        while (!quit) {
            try {
                String send_text = inFromUser.readLine();
                if (send_text.equals("q")) {
                    quit = true;
                    disconnectMessage();
                    clientSocket.close();
                    System.exit(0);
                } else
                    sendMessage(send_text, "adam");
            } catch (IOException e) {
                System.out.println("Can't read from console");
            }
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
