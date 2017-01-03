package Client.ClientServer;

import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import Client.GUI.ChatFrame;

public class ClientServer extends Thread {
    private final int PORT = 6789;
    private String temp_my_inet = "127.0.0.11";
    public ChatFrame gui;
    @Nullable
    private ServerSocket createServer() {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(temp_my_inet);
        } catch (UnknownHostException e) {
            System.out.println("Can't create inet address");
        }
        try {

            ServerSocket serverSocket = new ServerSocket(PORT, 50, address);
            System.out.println("Created server");
            gui.ChatBox.append("Created server \n");
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
            gui.ChatBox.append("New client connected \n");
            return connectionSocket;
        } catch (IOException e) {
            System.out.print("Can't tak user");
            return null;
        }
    }

    public void run() {
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
                gui.ChatBox.append("Received: " + received_text);
            } else {
                System.out.print("Client has disconnected");
                break;
            }
        }
    }
}
