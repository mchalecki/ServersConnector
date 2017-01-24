package Redirect;

import tools.Tools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RedirectMain {
    private static String version = "1.05";
    private final int PORT = 6789;

    /**
     * Creation of Redirect
     * @param argv
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String argv[]) throws IOException, InterruptedException {
        System.out.println("Redirect main " + version);
        RedirectMain redir = new RedirectMain();
        redir.run();
    }
    
    /**
     * Creation of server socket
     * Staring thread
     * @throws InterruptedException
     * @throws IOException 
     */

    private void run() throws InterruptedException, IOException {
        Socket socket = null;
        ServerSocket serverSocket = Tools.createServer(PORT);

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
