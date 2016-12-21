package Redirect;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RedirectMain {
    private final int PORT = 6789;
    private static String version = "1.03";

    @Nullable
    private ServerSocket createServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT, 50);
            System.out.println("Created server");
            return serverSocket;
        } catch (IOException e) {
            System.out.println("Cant create server");
            return null;
        }
    }

    public static void main(String argv[]) throws IOException, InterruptedException {
        System.out.println("Redirect main " + version);
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
