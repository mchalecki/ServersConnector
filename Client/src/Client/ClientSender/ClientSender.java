package Client.ClientSender;

import Client.GUI.ChatFrame;
import tools.Tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSender extends Thread {
    private final int PORT = 6789;
    public Socket clientSocket;
    public ChatFrame gui;
    private String targetHost;
    private BufferedReader inFromUser;
    private Boolean quit;
    private String Nick;
    private String target;

    /**
     *In constructor creating stream 
     */
    public ClientSender() {
        inFromUser = new BufferedReader(new InputStreamReader(System.in));
        quit = false;
    }

     /**
     *Creating JSON object with description of message, then sending is as a String
     * @param message
     * @param target says to whom message will be send
     */
    public void sendMessage(String message, String target) {
        org.json.JSONObject mes = new org.json.JSONObject();
        org.json.JSONObject content = new org.json.JSONObject();
        content.put("text", message);
        content.put("to", target);
        mes.put("type", 3);
        mes.put("content", content);
        sendForward(mes.toString());
    }
    /**
     * Logging to system as first connection.
     * @param nick which is new user
     */
    public void welcomeMessage(String nick) {
        org.json.JSONObject mes = new org.json.JSONObject();
        org.json.JSONObject content = new org.json.JSONObject();
        content.put("nick", nick);
        mes.put("type", 1);
        mes.put("content", content);
        sendForward(mes.toString());
    }

    public void disconnectMessage() {
        org.json.JSONObject mes = new org.json.JSONObject();
        mes.put("type", 2);
        mes.put("content", new org.json.JSONObject());
        sendForward(mes.toString());
    }

    /**
     *Making socket and stream for sending message
     */
    private void sendForward(String message) {
        if (clientSocket == null)
            clientSocket = Tools.connectTo(targetHost, PORT);
        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(message + '\n');
        } catch (IOException e) {
            System.out.println("Can't send message");
        }
    }

     /**
     * @param Nick taken from FirstFrame
     * @param Address taken from FirstFrame
     */
    public void getInformation(String Nick, String Address)
    {
        this.Nick = Nick;
        this.targetHost = Address;
    }

    /**
     *@version without GUI
     * reading message from console and sending it until user end conversation with "q"
     */
    public void run() {
        System.out.println("Making new connection");
        gui.ChatBox.append("Making new connection \n");

        while (!quit) {
            try {
                String send_text = inFromUser.readLine();
                if (send_text.equals("q")) {
                    quit = true;
                    disconnectMessage();
                    clientSocket.close();
                    System.exit(0);
                } else
                    sendMessage(send_text, target);
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

    /**
     * @return Socket
     */
    public Socket make_connection() {
        return Tools.connectTo(targetHost, PORT);
    }
}
