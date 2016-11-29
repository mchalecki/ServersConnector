package java_project_programming3.TCPServerPac;

import com.sun.istack.internal.Nullable;

import java.io.*;
import java.net.*;

class TCPServer {
    private static final int PORT = 6789;

    @Nullable
    private static ServerSocket createServer() {
        InetAddress address = null;
        try {
            address = InetAddress.getByName("127.0.0.2");
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

    public static void main(String argv[]) throws IOException, InterruptedException {
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
