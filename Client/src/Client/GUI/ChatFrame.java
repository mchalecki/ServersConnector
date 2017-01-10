package Client.GUI;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.util.HashMap;

public class ChatFrame extends JFrame
{
    private JFrame ChatFrame = new JFrame("Chat");
    public JTextArea ChatBox;
    private JTextField WriteMessageBox;
    private JButton SendMessageButton;
    private JButton New;
    private JPanel friendListPanel;
    private JTextField FriendNickBox;
    public JButton clickedButton;

    private String Nick;
    private String Address;
    private int i = 0;
    private String target;

    public ClientSender sender;
    public ClientServer server;

    public HashMap<JButton, String> buttonList = new HashMap<JButton, String>();

    public ChatFrame()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel downPanel = new JPanel();
        downPanel.setLayout(new GridBagLayout());

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());

        JPanel friendPanel = (JPanel)this.getContentPane();
        friendPanel.setLayout(new BorderLayout());

        WriteMessageBox = new JTextField();
        WriteMessageBox.addKeyListener(new SendMessageListener());

        SendMessageButton = new JButton("Send Message!");
        SendMessageButton.addActionListener(new SendMessageListener());

        ChatBox = new JTextArea();
        ChatBox.setEditable(false);
        ChatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(ChatBox), BorderLayout.CENTER);
        DefaultCaret caret = (DefaultCaret)ChatBox.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JPanel addFriendPanel = new JPanel();
        addFriendPanel.setLayout(new BorderLayout());
        New = new JButton("Add Friend!");
        New.setMinimumSize(new Dimension(120,40));
        New.setPreferredSize(new Dimension(120,40));
        New.setMaximumSize(new Dimension(120,40));
        New.addActionListener(new AddFriendListener());

        FriendNickBox = new JTextField();
        FriendNickBox.setMinimumSize(new Dimension(120,25));
        FriendNickBox.setPreferredSize(new Dimension(120,25));
        FriendNickBox.setMaximumSize(new Dimension(120,25));
        FriendNickBox.addKeyListener(new AddFriendListener());

        addFriendPanel.add(FriendNickBox, BorderLayout.NORTH);
        addFriendPanel.add(New, BorderLayout.CENTER);

        eastPanel.add(addFriendPanel, BorderLayout.NORTH);

        this.friendListPanel = new JPanel();
        int rows = 6;
        friendListPanel.setLayout(new GridLayout(rows, 1));
        JScrollPane scrollPane = new JScrollPane(friendListPanel);
        friendPanel.add(scrollPane, BorderLayout.CENTER);
        eastPanel.add(friendPanel, BorderLayout.CENTER);

        mainPanel.add(new JScrollPane(ChatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 150;

        GridBagConstraints right = new GridBagConstraints();

        downPanel.add(WriteMessageBox, left);
        downPanel.add(SendMessageButton, right);

        mainPanel.add(downPanel, BorderLayout.SOUTH);
        mainPanel.add(eastPanel, BorderLayout.EAST);

        ChatFrame.add(mainPanel);
        ChatFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ChatFrame.setSize(500, 300);
        ChatFrame.setLocation(300, 100);
        ChatFrame.setVisible(false);

        ChatFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                sender.disconnectMessage();
                try {
                    sender.clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public class AddFriendListener implements ActionListener, KeyListener
    {
        public void actionPerformed(ActionEvent e)
        {
            i++;
            AddFriend(friendListPanel, i);
        }

        public void keyPressed(KeyEvent event)
        {
            if(event.getKeyCode() == KeyEvent.VK_ENTER)
            {
                i++;
                AddFriend(friendListPanel, i);
            }
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    private void AddFriend(JPanel friendListPanel, int number)
    {
        String friend;
        if(FriendNickBox.getText().length() >= 1) {
            friend = FriendNickBox.getText();

            int rows = 5;
            if (number > 5) {
                friendListPanel.setLayout(new GridLayout(number, 1));
            } else friendListPanel.setLayout(new GridLayout(rows, 1));

            JButton button = new JButton();
            button.setMinimumSize(new Dimension(100, 30));
            button.setPreferredSize(new Dimension(100, 30));
            button.setMaximumSize(new Dimension(100, 30));
            button.setText(friend);
            friendListPanel.add(button);
            button.addActionListener(new ButtonListener());
            buttonList.put(button, "");
            friendListPanel.revalidate();
            FriendNickBox.setText("");
            FriendNickBox.requestFocusInWindow();
        }
    }

    public class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae) {
            clickedButton = (JButton) ae.getSource();
            String nameOfButton = ((JButton) ae.getSource()).getActionCommand();
            target = nameOfButton;
            server.target = nameOfButton;
            ChatBox.setText("Now you can talk with " + target + "\n");
            ChatBox.append(buttonList.get(clickedButton));
        }
    }

    public class SendMessageListener implements ActionListener, KeyListener
    {
        public void actionPerformed(ActionEvent event)
        {
            sendMessageGUI();
        }

        public void keyPressed(KeyEvent event)
        {
            if(event.getKeyCode() == KeyEvent.VK_ENTER)
            {
                sendMessageGUI();
            }
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    private void sendMessageGUI()
    {
        if(target != null) {
            if (WriteMessageBox.getText().length() >= 1) {
                String send_text = WriteMessageBox.getText();

                sender.sendMessage(send_text, target);

                ChatBox.append("\n" + Nick + " > " + send_text);
                String new_message = buttonList.get(clickedButton);
                new_message += "\n" + Nick + " > " + send_text;
                buttonList.put(clickedButton, new_message);

                WriteMessageBox.setText("");
            }
            WriteMessageBox.requestFocusInWindow();
        }
        else
        {
            errorMessage();
            WriteMessageBox.setText("");
        }
    }

    private void errorMessage() {
        JOptionPane.showMessageDialog(null,
                "Error: You have no friends :(", "Error Massage",
                JOptionPane.ERROR_MESSAGE);
    }

    public void getInformation(String Nick, String Address)
    {
        this.Nick = Nick;
        this.Address = Address;
        ChatFrame.setVisible(true);
    }
}