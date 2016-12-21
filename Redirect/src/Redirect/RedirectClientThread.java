package Redirect;

import java.io.*;
import java.net.Socket;

public class RedirectClientThread extends Thread {
    private Socket socket;
    private final int PORT = 6789;
    private String temp_target = "172.17.0.3";
    private BufferedReader brinp = null;

    RedirectClientThread(Socket clientSocket) {
        System.out.println("New client");
        System.out.println("IP's");
        socket = clientSocket;
        try {
            InputStream inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
        } catch (IOException e) {
            System.out.println("Cannot initialize socket");
            e.printStackTrace();
        }
    }

    private Socket make_connection() {
        System.out.println("Making new connection");
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(temp_target, PORT);
            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println(e.toString());
            System.out.println("Failed to makeConnectionSocket");
        }
        return clientSocket;
    }

    private void processMessage(String message) {
        org.json.JSONObject obj = new org.json.JSONObject(message);
        obj.put("IP_from", socket.getRemoteSocketAddress().toString());
        sendForward(obj.toString());
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
                    processMessage(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
