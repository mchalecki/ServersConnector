package java_project_programming3.TCPClientPac;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

class TCPClient {
    private static Socket make_connection() {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("localhost", 6789);
            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println("Failed to makeConnectionSocket");
        }
        return clientSocket;
    }

    private static boolean ping(Socket clientSocket) {
        //Doesn't work due to TODO.2
        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes("Ping\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String argv[]) throws IOException, InterruptedException {
        run();
    }

    private static void run() throws IOException, InterruptedException {
        String send_text;
        String received_text;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Making new connection");
            Socket clientSocket = make_connection();

            while (clientSocket != null) {
                send_text = inFromUser.readLine();
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                outToServer.writeBytes("ABC" + '\n');
                outToServer.writeBytes(send_text + '\n');
                received_text = inFromServer.readLine();

                System.out.println("FROM SERVER: " + received_text);

                if (exit) {
                    clientSocket.close();
                    break;
                }
            }
            System.out.println("Connection broken");
        }

    }
}