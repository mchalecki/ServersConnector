package java_project_programming3.TCPServerPac;

import com.sun.istack.internal.Nullable;

import java.io.*;
import java.net.*;

class TCPServer {
    private static final int PORT = 6789;
    @Nullable
    private static ServerSocket createServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
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
        Socket socket = null;
        ServerSocket serverSocket = createServer();

            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }
                new ClientThread(socket).start();
            }

    }
}
