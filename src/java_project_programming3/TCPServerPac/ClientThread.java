package java_project_programming3.TCPServerPac;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;

    ClientThread(Socket clientSocket) {
        System.out.println("New client");
        this.socket = clientSocket;
    }

    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                System.out.println(line);
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    System.out.println("Client has disconnected");
                    socket.close();
                    return;
                } else {
                    out.writeBytes(line + "\n\r");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

}
