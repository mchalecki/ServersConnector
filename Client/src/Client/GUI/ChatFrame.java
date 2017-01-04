package Client.GUI;

import Client.ClientSender.ClientSender;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ChatFrame
{
    private static JFrame ChatFrame = new JFrame("Chat");
    public static JTextArea ChatBox;
    private static JTextField WriteMessageBox;
    private static JButton SendMessageButton;

    private String Nick;
    private String Address;

    public static ClientSender client;

    public ChatFrame()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel downPanel = new JPanel();
        downPanel.setLayout(new GridBagLayout());

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

        GridBagConstraints left = new GridBagConstraints();
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 150;

        GridBagConstraints right = new GridBagConstraints();

        downPanel.add(WriteMessageBox, left);
        downPanel.add(SendMessageButton, right);

        mainPanel.add(BorderLayout.SOUTH, downPanel);

        ChatFrame.add(mainPanel);
        ChatFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ChatFrame.setSize(500, 300);
        ChatFrame.setLocation(300, 100);
        ChatFrame.setVisible(false);
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

            client.sendMessage(send_text, "adam");

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