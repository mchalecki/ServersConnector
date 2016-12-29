package Redirect;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RedirectMain {
    private final int PORT = 6789;
    private String temp_my_inet = "127.0.0.10";

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
            return serverSocket;
        } catch (IOException e) {
            System.out.println("Cant create server");
            return null;
        }
    }

    public static void main(String argv[]) throws IOException, InterruptedException {
        RedirectMain redir = new RedirectMain();
        redir.run();
    }

    private void run() throws InterruptedException, IOException {
        Socket socket = null;
        ServerSocket serverSocket = createServer();

        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new RedirectClientThread(socket).start();
        }

    }
}
