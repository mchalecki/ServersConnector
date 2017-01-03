package BasicComponents.TCPClientPac;

import Client.GUI.ChatFrame;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TCPClient extends JFrame{
    private static boolean exit;
    private static BufferedReader inFromUser;
    static JFrame ChatFrame = new JFrame("Chat");
    static JTextArea ChatBox;
    static JTextField WriteMessageBox;
    static JButton SendMessageButton;

    private TCPClient() {
        exit = false;
        inFromUser = new BufferedReader(new InputStreamReader(System.in));
      JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel downPanel = new JPanel();
        downPanel.setLayout(new GridBagLayout());

        WriteMessageBox = new JTextField();


        SendMessageButton = new JButton("Send Message!");


        ChatBox = new JTextArea();
        ChatBox.setEditable(false);
        ChatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(ChatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 150;

        GridBagConstraints right = new GridBagConstraints();

        downPanel.add(WriteMessageBox, left);
        downPanel.add(SendMessageButton, right);

        mainPanel.add(BorderLayout.SOUTH, downPanel);

        ChatFrame.add(mainPanel);
        ChatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChatFrame.setSize(500, 300);
        ChatFrame.setLocation(300, 100);
        ChatFrame.setVisible(true);
    }

    private static Socket make_connection(String host) {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(host, 6789);
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
            Socket clientSocket = make_connection("127.0.0.2");
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
