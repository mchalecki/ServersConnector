package java_project_programming3.TCPServerPac;

import com.sun.istack.internal.Nullable;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

class TCPServer {
    @Nullable
    private static ServerSocket createServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            System.out.println("Created server");
            return serverSocket;
        }
        catch (IOException e){
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

    public static void main(String argv[]) throws IOException,InterruptedException {
        run();
    }

    private static void run() throws InterruptedException, IOException {
        String received_text;
        boolean exit = false;
        ServerSocket serverSocket = createServer();
        wholeServer:
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            Socket connectionSocket = makeConnectionSocket(serverSocket);
            while (connectionSocket != null) {
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                received_text = inFromClient.readLine();
                if (received_text != null) {
                    System.out.println("Received: " + received_text);
                    outToClient.writeBytes(received_text+'\n');
                } else {
                    System.out.print("Client has disconnected");
                    break;
                }
                if (exit) {
                    System.out.print("Exit");
                    break wholeServer;
                }
            }
            if (exit) {
                System.out.print("Exit");
                break wholeServer;
            }
        }
    }
}
