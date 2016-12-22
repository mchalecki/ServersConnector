package Redirect;

import com.sun.security.ntlm.Client;
import tools.Tools;

import javax.tools.Tool;
import java.io.*;
import java.net.Socket;

public class RedirectClientThread extends Thread {
    private Socket socket;
    private final int PORT = 6789;
    private static String nextHost = null;
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
                if (nextHost == null) {
                    String from = obj.getString("IP_from");
                    nextHost = Tools.getIp(from);
                    System.out.println("New direct srv connected. System can work");
                }
                System.out.println("New Server connected");
                break;
            default:
                System.out.println("NEXTHOST" + nextHost);
                sendForward(message);
                break;
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
