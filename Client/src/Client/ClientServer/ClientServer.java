package Client.ClientServer;

import Client.GUI.ChatFrame;
import Client.GUI.ChatFrame.MyButton;
import tools.Tools;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class ClientServer extends Thread {
    private final int PORT = 6789;
    public ChatFrame gui;
    public String target;
    private Color newMessageColor = new Color (255, 100, 108);
  /**
     *create server socket
     * create stream for receiving messages
     * if author of message is among added friends then show this message
     */
    public void run() {
        String received_text;
        ServerSocket serverSocket = Tools.createServer(PORT);
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            Socket connectionSocket = Tools.makeConnectionSocket(serverSocket);
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
                if (button != null) {
                    if (button.user.equals(gui.target)) {
                        gui.ChatBox.append("\n" + button.user + " > " + processReceivedMessage(received_text));
                    }
                    else {
                        button.setBackground(newMessageColor);
                    }
                    button.content += "\n" + button.user + " > " + processReceivedMessage(received_text);
                }
            } else {
                System.out.print("Client has disconnected");
                break;
            }
        }
    }
    /**
     *@return button from list whose user field is the same as from_user
     * if user is not in the list of friends yet, add him
     */
    private MyButton IterateList(String message) {
        MyButton but;
        for (int i = 0; i < gui.buttonList.size(); i++) {
            if (processUserFrom(message) != null) {
                if (processUserFrom(message).equals(gui.buttonList.get(i).user)) {
                    but = gui.buttonList.get(i);
                    return but;
                }
            }
        }
        but = AddUnknownFriend(gui.friendListPanel, processUserFrom(message));
        return but;
    }
     /**
      * This method splits gotten String which is in JSON format
     *@return String described as from_user
     */
    private String processUserFrom(String message) {
        String a = null;
        String[] parts = message.split("\"");
        int l = parts.length;
        for (int i = 0; i < l; i++) {
            if (parts[i].equals("from_user")) a = parts[i + 2];
        }
        return a;
    }
     /**
     *This method splits gotten String which is in JSON format
     *@return String described as text
     */
    private String processReceivedMessage(String message) {
        String a = null;
        String[] parts = message.split("\"");
        int l = parts.length;
        for (int i = 0; i < l; i++) {
            if (parts[i].equals("text")) a = parts[i + 2];
        }
        return a;
    }
        /**
         *
         * Method adds button for new friend 
         */
    private MyButton AddUnknownFriend(JPanel friendListPanel, String friend)
    {
        if (gui.buttonList.size() > 5) {
            friendListPanel.setLayout(new GridLayout(gui.buttonList.size() + 1, 1));
        }
        else friendListPanel.setLayout(new GridLayout(5, 1));

        MyButton button = new MyButton();
        button.setMinimumSize(new Dimension(100, 30));
        button.setPreferredSize(new Dimension(100, 30));
        button.setMaximumSize(new Dimension(100, 30));
        button.setText(friend);
        button.user = friend;
        button.content = "Now you can talk with " + friend + "\n";
        gui.buttonList.add(button);
        friendListPanel.add(button);
        button.addActionListener(new ButtonListener());
        friendListPanel.revalidate();
        return button;
    }

    public class ButtonListener implements ActionListener
    {

        /**
         *
         * @param ae  which is click of button
         */
        public void actionPerformed(ActionEvent ae) {
            gui.clickedButton.setBackground(gui.defaultColor);
            gui.clickedButton = (MyButton) ae.getSource();
            gui.clickedButton.setBackground(gui.clickedColor);
            String nameOfButton = ((JButton) ae.getSource()).getActionCommand();
            target = nameOfButton;
            gui.target = nameOfButton;
            gui.ChatBox.setText("");
            gui.ChatBox.append(gui.clickedButton.content);
        }
    }
}
