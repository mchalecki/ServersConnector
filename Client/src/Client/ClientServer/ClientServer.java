package Client.ClientServer;

import tools.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientServer extends Thread {
    private final int PORT = 6789;

    public void run() {
        String received_text;
        ServerSocket serverSocket = Tools.createServer(PORT);
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            Socket connectionSocket = Tools.makeConnectionSocket(serverSocket);
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
            } else {
                System.out.print("Client has disconnected");
                break;
            }
        }
    }
}
