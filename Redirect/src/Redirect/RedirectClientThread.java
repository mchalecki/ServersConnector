package Redirect;


import org.json.JSONObject;
import tools.Tools;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class RedirectClientThread extends Thread {
    private static String nextHost = null;
    private static ArrayList<String> servers = new ArrayList<>();
    private final int PORT = 6789;
    private Socket socket;
    private BufferedReader brinp = null;

    RedirectClientThread(Socket clientSocket) {
        System.out.println("New client ip=" + clientSocket.getRemoteSocketAddress());
        socket = clientSocket;
        try {
            InputStream inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
        } catch (IOException e) {
            System.out.println("Cannot initialize socket");
            e.printStackTrace();
        }
    }

    public void run() {
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    System.out.println("Client has disconnected");
                    socket.close();
                    return;
                } else {
                    line = addIpToMessage(line);
                    processMessage(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private Socket make_connection() {
        System.out.println("Making new connection");
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(nextHost, PORT);
            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println(e.toString());
            System.out.println("Failed to makeConnectionSocket");
        }
        return clientSocket;
    }

    private String addIpToMessage(String message) {
        org.json.JSONObject obj = new org.json.JSONObject(message);
        obj.put("IP_from", socket.getRemoteSocketAddress().toString());
        return obj.toString();
    }

    private void processMessage(String message) {
        org.json.JSONObject obj = new org.json.JSONObject(message);
        int type = obj.getInt("type");
        switch (type) {
            case 4:
                addNewSrv(obj);
                break;
            case 6:
                handleBrokenConnection();
                break;
            default:
                sendForward(message);
                break;
        }
    }

    private void handleBrokenConnection() {
        String ipOfBrokenConnection = Tools.getIp(socket.getRemoteSocketAddress().toString());
        System.out.println("Received info of broken connection from " + ipOfBrokenConnection);
        for (int i = 0; i < servers.size(); i++)
            if (Objects.equals(servers.get(i), ipOfBrokenConnection)) {
                for (int j = i + 2; j < servers.size(); j++)
                    sendConnectAgainOrder(servers.get(j));
                break;
            }
    }

    private void sendConnectAgainOrder(String target) {
        System.out.println("Sending order to connect again to " + target);
    }

    private void addNewSrv(JSONObject obj) {
        String from = obj.getString("IP_from");
        String ipOfNewSrv = Tools.getIp(from);
        servers.add(ipOfNewSrv);
        if (nextHost == null) {
            nextHost = ipOfNewSrv;
            System.out.println("New direct srv connected. System can work");
        } else {
            sendForward(obj.toString());
            System.out.println("Forwarder request to connect srv");
        }
    }

    private void sendForward(String message) {
        System.out.println("Forwarding message " + message);
        Socket clientSocket = make_connection();
        if (clientSocket != null) {
            try {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(message + '\n');
            } catch (IOException e) {
                System.out.println("Can't send message");
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
