package BasicComponents.TCPServerPac;

import Client.GUI.ChatFrame;
import com.sun.istack.internal.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.net.*;

class TCPServer extends JFrame{
    private static final int PORT = 6789;
    
    static JFrame ChatFrame = new JFrame("Chat");
    static JTextArea ChatBox;
    static JTextField WriteMessageBox;
    static JButton SendMessageButton;
    
    public TCPServer()
    {
         JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel downPanel = new JPanel();
        downPanel.setLayout(new GridBagLayout());

        WriteMessageBox = new JTextField();
        WriteMessageBox.addKeyListener(new ChatFrame.SendMessageListener());

        SendMessageButton = new JButton("Send Message!");
        SendMessageButton.addActionListener(new ChatFrame.SendMessageListener());

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

