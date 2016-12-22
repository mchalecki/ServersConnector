package Servers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import tools.Tools;


public class ServerMain {
    private final int PORT = 6789;
    private final String redir_ip = "172.17.02";
    private String nextHost = null;
    private BiMap<String, String> users = HashBiMap.create();
    private static String version = "1.03";

    public static void main(String args[]) {
        System.out.println("Server main " + version);
        ServerMain srv = new ServerMain();
        srv.run();
    }

    private void run() {
        String received_text;
        sendConnectionMessageToRedir();
        ServerSocket serverSocket = Tools.createServer(PORT);
        while (true) {
            Socket connectionSocket = Tools.makeConnectionSocket(serverSocket);
            received_text = null;
            try {
                //This will return probably -1 if disconnection
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

    private void sendConnectionMessageToRedir() {
        org.json.JSONObject mes = new org.json.JSONObject();
        mes.put("type", 4);
        sendToRedir(mes.toString());
    }

    private void sendToRedir(String message) {
        Socket clientSocket = Tools.connectTo(redir_ip, PORT);
        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(message + '\n');
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Can't send message");
        }
    }

    private Socket makeSenderSocket(String host, int PORT_next) {
        System.out.println("Making new connection");
        Socket clientSocket = null;
        System.out.println(host + "|" + PORT_next);
        try {
            clientSocket = new Socket(host, PORT_next);
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
            case 4:
                System.out.println("New srv wants to connect");
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
        if (entry != null) {
            users.inverse().remove(IP);
            System.out.println("Deleted user");
        }
    }

    private void sendForwardMessage(String message) {
        String host = null;
        if (nextHost == null) {
            org.json.JSONObject obj = new org.json.JSONObject(message);
            org.json.JSONObject content = new org.json.JSONObject(obj.get("content").toString());
            System.out.println(content.toString());
            String to = content.getString("to");
            System.out.println("User socket address" + users.get(to));
            String IP = Tools.getIp(users.get(to));
            if (IP != null) {
                System.out.println("IP: " + IP);
                host = IP;
            } else System.out.println("No match");
        } else {
            host = nextHost;
        }
        Socket clientSocket = makeSenderSocket(host, PORT);
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
