package java_project_programming3.TCPClientPac;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

class TCPClient {
    private static boolean exit;
    private static BufferedReader inFromUser;

    private TCPClient() {
        exit = false;
        inFromUser = new BufferedReader(new InputStreamReader(System.in));
    }

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

    public static void main(String argv[]) throws IOException, InterruptedException {
        TCPClient abc = new TCPClient();
        abc.run();
    }

    private void run() throws InterruptedException {
        String received_text;
        wholeServer:
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Making new connection");
            Socket clientSocket = make_connection();
            while (clientSocket != null) {
                try {
                    sendMessageFromSystemInput(clientSocket);
                } catch (IOException e) {
                    System.out.println("Cant't send message");
                    break;
                }
                try {
                    received_text = getMessageFromServer(clientSocket);
                } catch (IOException e) {
                    System.out.println("Cant receive a message");
                    break;
                }
                System.out.println("FROM SERVER: " + received_text);
                if (exit) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                    break wholeServer;
                }
            }
            System.out.println("Connection broken");
        }

    }

    private static String getMessageFromServer(Socket clientSocket) throws IOException {
        String received_text;
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        received_text = inFromServer.readLine();
        return received_text;

    }

    private static void sendMessageFromSystemInput(Socket clientSocket) throws IOException {
        String send_text;
        send_text = inFromUser.readLine();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes(send_text + '\n');
    }
}