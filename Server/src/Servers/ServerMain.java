package Servers;


import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ServerMain {
    private final int PORT = 6789;
    private String nextHost = "192.168.1.193";
    private BiMap<String, String> users = HashBiMap.create();
    private static String version = "1.01";

    public static void main(String args[]) {
        System.out.println("Server main " + version);
        ServerMain srv = new ServerMain();
        srv.run();
    }

    private void run() {
        String received_text;
        ServerSocket serverSocket = createServer();
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            Socket connectionSocket = makeConnectionSocket(serverSocket);
            received_text = null;
            try {
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                received_text = inFromClient.readLine();
            } catch (IOException e) {
                System.out.println("IO_exception");
            }
            if (received_text != null) {
                System.out.println("Received: " + received_text);
                processMessage(received_text);
            } else {
                System.out.print("Client has disconnected");
                break;
            }
        }
    }

    @Nullable
    private ServerSocket createServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT, 50);
            System.out.println("Created server");
            return serverSocket;
        } catch (IOException e) {
            System.out.println("Cant create server");
            return null;
        }
    }

    @Nullable
    private Socket makeConnectionSocket(ServerSocket serverSocket) {

        try {
            Socket connectionSocket = serverSocket.accept();
            System.out.println("New client connected");
            return connectionSocket;
        } catch (IOException e) {
            System.out.print("Can't tak user");
            return null;
        }
    }

    private Socket makeSenderSocket(String host) {
        System.out.println("Making new connection");
        Socket clientSocket = null;
        System.out.println(host + "|" + PORT);
        try {
            clientSocket = new Socket(host, PORT);
            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println("Failed to makeConnectionSocket");
        }
        return clientSocket;
    }

    private void processMessage(String message) {
        org.json.JSONObject obj = new org.json.JSONObject(message);
        int type = obj.getInt("type");
        switch (type) {
            case 1:
                addUser(obj);
                break;
            case 2:
                deleteUser(obj);
                break;
            case 3:
                sendForwardMessage(message);
                break;
        }
    }

    private void addUser(org.json.JSONObject message) {
        org.json.JSONObject content = new org.json.JSONObject(message.get("content").toString());
        String nick = content.getString("nick");
        String IP = message.getString("IP_from");
        String entry = users.get(nick);
        if (entry == null) users.put(nick, IP);
        System.out.println("Added new user " + nick);
    }

    private void deleteUser(org.json.JSONObject message) {
        String IP = message.getString("IP_from");
        String entry = users.inverse().get(IP);
        if (entry != null) users.inverse().remove(IP);
    }

    private void sendForwardMessage(String message) {
        String host;

        if (nextHost == null) {
            org.json.JSONObject obj = new org.json.JSONObject(message);
            org.json.JSONObject content = new org.json.JSONObject(obj.get("content").toString());
            System.out.println(content.toString());
            String to = content.getString("to");
            host = users.get(to);
        } else
            host = nextHost;
        Socket clientSocket = makeSenderSocket(host);
        if (clientSocket != null) {
            try {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(message + '\n');
            } catch (IOException e) {
                System.out.println("Can't send message");
            }
        }
    }
}
