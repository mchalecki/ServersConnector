package Servers;


import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class ServerMain {
    private static final int PORT = 6789;
    private static String temp_host;
    @Nullable
    private static ServerSocket createServer() {
        temp_host = "127.0.0.3";
        InetAddress address = null;
        try {
            address = InetAddress.getByName(temp_host);
        } catch (UnknownHostException e) {
            System.out.println("Can't create inet address");
        }
        try {

            ServerSocket serverSocket = new ServerSocket(PORT, 50, address);
            System.out.println("Created server");
            return serverSocket;
        } catch (IOException e) {
            System.out.println("Cant create server");
            return null;
        }
    }
    @Nullable
    private static Socket makeConnectionSocket(ServerSocket serverSocket) {

        try {
            Socket connectionSocket = serverSocket.accept();
            System.out.println("New client connected");
            return connectionSocket;
        } catch (IOException e) {
            System.out.print("Can't tak user");
            return null;
        }
    }
    private static Socket makeSenderSocket(String host) {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(host, PORT);
            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println("Failed to makeConnectionSocket");
        }
        return clientSocket;
    }
    private static void sendMessage(String host, String message)
    {
        System.out.println("Making new connection");
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
    public static void main(String args[]){
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
                sendMessage("127.0.0.2",received_text);
            } else {
                System.out.print("Client has disconnected");
                break;
            }
        }
    }
}
