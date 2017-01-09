package Client.GUI;

import Client.ClientSender.ClientSender;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ChatFrame extends JFrame
{
    private JFrame ChatFrame = new JFrame("Chat");
    public JTextArea ChatBox;
    private JTextField WriteMessageBox;
    private JButton SendMessageButton;
    private JButton New;
    private JPanel friendListPanel;

    private String Nick;
    private String Address;

    public static ClientSender sender;

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

        New = new JButton("Add Friend!");
        New.setMinimumSize(new Dimension(120,50));
        New.setPreferredSize(new Dimension(120,50));
        New.setMaximumSize(new Dimension(120,50));
        eastPanel.add(New, BorderLayout.NORTH);

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


        //ONLY TO CHECK!!!
        for(int i = 1; i <= 10; i++)
        {
            AddFriend(friendListPanel, i);
        }
    }

    public void AddFriend(JPanel friendListPanel, int number)
    {
        int rows = 6;
        if(number > 6)
        {
            rows++;
            friendListPanel.setLayout(new GridLayout(number, 1));
        }
        else friendListPanel.setLayout(new GridLayout(rows, 1));

        JButton button = new JButton();
        button.setMinimumSize(new Dimension(100,30));
        button.setPreferredSize(new Dimension(100,30));
        button.setMaximumSize(new Dimension(100,30));
        button.setText("user " + number);
        friendListPanel.add(button);
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
        if (WriteMessageBox.getText().length() >= 1)
        {
            String send_text = WriteMessageBox.getText();

            sender.sendMessage(send_text, "adam");

            ChatBox.append("\n" + Nick + " > " + send_text + "\n");
            WriteMessageBox.setText("");
        }
        WriteMessageBox.requestFocusInWindow();
    }

    public void getInformation(String Nick, String Address)
    {
        this.Nick = Nick;
        this.Address = Address;
        ChatFrame.setVisible(true);
    }
}