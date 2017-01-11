package Client.ClientServer;

import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import Client.GUI.ChatFrame;
import Client.GUI.ChatFrame.MyButton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JButton;

public class ClientServer extends Thread {
    private final int PORT = 6789;
    private String temp_my_inet = "127.0.0.11";
    public ChatFrame gui;
    public String target;

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

    @Nullable
    private Socket makeConnectionSocket(ServerSocket serverSocket) {

        try {
            Socket connectionSocket = serverSocket.accept();
            System.out.println("New client connected");
            return connectionSocket;
        } catch (IOException e) {
            System.out.print("Can't tak user");
            return null;
        }
    }

    public void run() {
        String received_text;
        ServerSocket serverSocket = createServer();
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            Socket connectionSocket = makeConnectionSocket(serverSocket);
            received_text = null;
            try {
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                received_text = inFromClient.readLine();
            } catch (IOException e) {
                System.out.println("IO_exception");
            }
            if (received_text != null) {
                System.out.println("Received: " + received_text);
                MyButton button = IterateList(received_text);
                if(button != null)
                {
                     if(button.user.equals(gui.target))
                     {
                        gui.ChatBox.append("\n" + button.user + " > " + processReceivedMessage(received_text));
                     }
                    button.content += "\n" + button.user + " > " + processReceivedMessage(received_text);  
                }
                 } else {
                     System.out.print("Client has disconnected");
                     break;
                 }
    }
    }
    
    private MyButton IterateList(String message) {
        MyButton but;
        for(int i = 0; i < gui.buttonList.size(); i++)
        {
            if(processUserFrom(message).equals(gui.buttonList.get(i).user))
            {
                but = gui.buttonList.get(i);

                return but;
            }
        }
        return null;
    }

    private String processUserFrom(String message) {
        String a = null;
        String[] parts = message.split("\"");
        int l = parts.length;
        for(int i =0;i<l;i++) {
            if (parts[i].equals("from_user")) a = parts[i + 2];
        }
        return a;
    }

    private String processReceivedMessage(String message) {
        String a = null;
        String[] parts = message.split("\"");
        int l = parts.length;
        for(int i =0;i<l;i++) {
            if (parts[i].equals("text")) a = parts[i + 2];
        }
        return a;
    }
}
