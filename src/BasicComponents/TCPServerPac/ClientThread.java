package BasicComponents.TCPServerPac;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader brinp = null;
    private DataOutputStream out = null;

    ClientThread(Socket clientSocket) {
        System.out.println("New client");
        socket = clientSocket;
        try {
            InputStream inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Cannot initialize socket");
            e.printStackTrace();
        }
    }

    private int sendBack(String message) {
        try {
            out.writeBytes(message + "\n\r");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    private int ping() throws IOException {
        sendBack("PING");
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String received_text = inFromServer.readLine();
        if (received_text.equals("PONG"))
            return 1;
        else return 0;
    }

    private void interpretMessage(String message) {
        if (message.equals("PING")) {
            sendBack("PONG");
        } else {
            sendBack(message);
        }
    }

    public void run() {
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    System.out.println("Client has disconnected");
                    socket.close();
                    return;
                } else {
                    System.out.println(line);
                    interpretMessage(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
