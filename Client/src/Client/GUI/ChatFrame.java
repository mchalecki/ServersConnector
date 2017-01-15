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
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

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
    public JPanel friendListPanel;
    private JTextField FriendNickBox;
    public MyButton clickedButton;
    /**
     * Nick and Address are specified by user
     * Address points to Redirect
     */
    private String Nick;
    private String Address;

    /**
     * Target is name of friend to whom message will be send
     */
    public String target;
    public ClientSender sender;
    public ClientServer server;
    public ArrayList<MyButton> buttonList = new ArrayList<>();

    /**
     *Constructor of ChatFrame sets all components and their location
     */
    public ChatFrame()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel downPanel = new JPanel();
        downPanel.setLayout(new GridBagLayout());

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());

        JPanel friendPanel =(JPanel) this.getContentPane();
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
        
         /**
         *With closing of window we need to do cleanup
         */
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

    /**
     * Extended version of JButton to hold description of each one
     */
    public static class MyButton extends JButton{

        /**
         *Variable content holds whole conversation of specified user
         */
        public String content;
        public String user;
        public MyButton() {
           
            content = null;
            user = null;
        }
    }
    public class AddFriendListener implements ActionListener, KeyListener
    {

       /**
         *New friend is added to Panel
         * @param e event which is click of button
         */
        public void actionPerformed(ActionEvent e)
        {
            AddFriend(friendListPanel);
        }

        /**
         *
         * @param event which is press of enter
         */
        public void keyPressed(KeyEvent event)
        {
            if(event.getKeyCode() == KeyEvent.VK_ENTER)
            {
                AddFriend(friendListPanel);
            }
        }

        /**
         *
         * @param e
         */
        public void keyReleased(KeyEvent e) {
        }

        /**
         *
         * @param e
         */
        public void keyTyped(KeyEvent e) {
        }
    }
        /**
         *Method is taking from JTextField name of friend
         * and adding new button with all specifications
         */
    private void AddFriend(JPanel friendListPanel)
    {
        String friend;
        if(FriendNickBox.getText().length() >= 1) {
            friend = FriendNickBox.getText();

            if(notAdded(friend)) {
                if (buttonList.size() > 5) {
                    friendListPanel.setLayout(new GridLayout(buttonList.size() + 1, 1));
                } else friendListPanel.setLayout(new GridLayout(5, 1));

                MyButton button = new MyButton();
                button.setMinimumSize(new Dimension(100, 30));
                button.setPreferredSize(new Dimension(100, 30));
                button.setMaximumSize(new Dimension(100, 30));
                button.setText(friend);
                button.user = friend;
                button.content = "Now you can talk with " + friend + "\n";
                buttonList.add(button);
                friendListPanel.add(button);
                button.addActionListener(new ButtonListener());
                friendListPanel.revalidate();
                FriendNickBox.setText("");
                FriendNickBox.requestFocusInWindow();

                if (clickedButton != null) {
                    clickedButton.setBackground(defaultColor);
                }
                clickedButton = button;
                clickedButton.setBackground(clickedColor);
                target = friend;
                server.target = friend;
                ChatBox.setText("");
                ChatBox.append(clickedButton.content);
            }
            else {
                FriendNickBox.setText("");
                errorMessage("You have " + friend + " on the list");
            }
        }
    }
    /**
     * Checking if friend is on the list
     */
    private boolean notAdded(String name) {
        for(int i = 0; i < buttonList.size(); i++) {
            if(name.equals(buttonList.get(i).user)) return false;
        }
        return true;
    }

   
    public Color defaultColor = new Color(204,204,255);
    public Color clickedColor = new Color(153,153,255);
    public class ButtonListener implements ActionListener
    {
        /**
         *Getting target from clicked button
         * show conversation assigned to button
         * @param ae which is click of button AddFriend
         */
        public void actionPerformed(ActionEvent ae) {
            clickedButton.setBackground(defaultColor);
            clickedButton = (MyButton) ae.getSource();
            clickedButton.setBackground(clickedColor);
            String nameOfButton = ((JButton) ae.getSource()).getActionCommand();
            target = nameOfButton;
            server.target = nameOfButton;
            ChatBox.setText("");
            ChatBox.append(clickedButton.content);
        }
    }

    public class SendMessageListener implements ActionListener, KeyListener
    {

        /**
         *on button click send message
         * @param event
         */
        public void actionPerformed(ActionEvent event)
        {
            sendMessageGUI();
        }

        /**
         *on enter pressed send message
         * @param event
         */
        public void keyPressed(KeyEvent event)
        {
            if(event.getKeyCode() == KeyEvent.VK_ENTER)
            {
                sendMessageGUI();
            }
        }

        /**
         *
         * @param e
         */
        public void keyReleased(KeyEvent e) {
        }

        /**
         *
         * @param e
         */
        public void keyTyped(KeyEvent e) {
        }
    }
         /**
         *get message from JTextField
         * send this message by calling method from ClientSender
         * show this message on JTextArea
         * if friend is not chosen show error
         */
    private void sendMessageGUI()
    {
        if(target != null) {
            if (WriteMessageBox.getText().length() >= 1) {
                String send_text = WriteMessageBox.getText();

                sender.sendMessage(send_text, target);

                ChatBox.append("\n" + Nick + " > " + send_text);

                clickedButton.content += "\n" + Nick + " > " + send_text;

                WriteMessageBox.setText("");
            }
            WriteMessageBox.requestFocusInWindow();
        }
        else
        {
            errorMessage("You have no friends :(");
            WriteMessageBox.setText("");
        }
    }

    private void errorMessage(String s) {
        JOptionPane.showMessageDialog(null,
                "Error: " + s, "Error Massage",
                JOptionPane.ERROR_MESSAGE);
    }

     /**
     * @param Nick specified by user
     * @param Address specified by user
     */
    public void getInformation(String Nick, String Address) {
        this.Nick = Nick;
        this.Address = Address;
        ChatFrame.setVisible(true);
    }
}