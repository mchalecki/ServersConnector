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
                JButton button = IterateMap(gui.buttonList, received_text);
                if(button.getText().equals(target))
                {
                    if(processReceivedMessage(received_text) != null)
                        gui.ChatBox.append("\n" + target + " > " + processReceivedMessage(received_text));
                }
                String new_message = gui.buttonList.get(button);
                new_message += "\n" + button.getText() + " > " + processReceivedMessage(received_text);
                gui.buttonList.put(button, new_message);
            } else {
                System.out.print("Client has disconnected");
                break;
            }
            
        }
    }
    
    public JButton IterateMap(Map mp, String message) {
    Iterator it = mp.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        JButton but = (JButton)pair.getKey();
        String nameOfBut = but.getText();
        if(nameOfBut.equals(processUserFrom(message)))
        {
            it.remove();
            return but;
        }
    }
    it.remove();
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
