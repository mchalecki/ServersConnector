package Client.GUI;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FirstFrame extends JFrame {

    public ChatFrame chatframe;
    private static JFrame FirstFrame = new JFrame("Chat");
   // private static JLabel WelcomeLabel1;
    private static JLabel WelcomeLabel2;
    private static JLabel NickLabel;
    private static JLabel AddressLabel;
    private static JTextField NickBox;
    private static JTextField AddressBox;
    private static JButton EnterButton;

    public String Nick;
    public String Address;

    public FirstFrame() {

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        /*WelcomeLabel1 = new JLabel("ELO");
        WelcomeLabel1.setFont(new Font("Serif", Font.BOLD, 28));
        WelcomeLabel1.setHorizontalAlignment(JLabel.CENTER);*/

        WelcomeLabel2 = new JLabel("Enter nick and address");
        WelcomeLabel2.setFont(new Font("Serif", Font.BOLD, 18));

        NickLabel = new JLabel("Nick: ");
        NickLabel.setFont(new Font("Serif", Font.BOLD, 15));
        AddressLabel = new JLabel("Address: ");
        AddressLabel.setFont(new Font("Serif", Font.BOLD, 15));
        NickBox = new JTextField();
        AddressBox = new JTextField();

        NickBox.addKeyListener(new EnterListener());
        AddressBox.addKeyListener(new EnterListener());

        EnterButton = new JButton("Enter");
        EnterButton.addActionListener(new EnterListener());

        GridBagConstraints welcome = new GridBagConstraints();
        welcome.fill = GridBagConstraints.HORIZONTAL;
        welcome.gridx =0;
        welcome.gridy =0;
        welcome.insets = new Insets(0,0,10,0);
        welcome.gridwidth = 2;

        GridBagConstraints nick = new GridBagConstraints();
        nick.fill = GridBagConstraints.HORIZONTAL;
        nick.gridx =0;
        nick.gridy =1;

        GridBagConstraints adrs = new GridBagConstraints();
        adrs.fill = GridBagConstraints.HORIZONTAL;
        adrs.gridx =0;
        adrs.gridy =2;

        GridBagConstraints nickbox = new GridBagConstraints();
        nickbox.fill = GridBagConstraints.HORIZONTAL;
        nickbox.gridx =1;
        nickbox.gridy =1;
        nickbox.insets = new Insets(10,0,10,0);

        GridBagConstraints adrsbox = new GridBagConstraints();
        adrsbox.fill = GridBagConstraints.HORIZONTAL;
        adrsbox.gridx =1;
        adrsbox.gridy =2;
        welcome.insets = new Insets(10,0,10,0);

        GridBagConstraints button = new GridBagConstraints();
        button.fill = GridBagConstraints.HORIZONTAL;
        button.gridx =0;
        button.gridy =3;
        button.insets = new Insets(30,0,0,0);
        button.gridwidth = 2;

        centerPanel.add(WelcomeLabel2, welcome);
        centerPanel.add(NickLabel, nick);
        centerPanel.add(AddressLabel, adrs);
        centerPanel.add(NickBox, nickbox);
        centerPanel.add(AddressBox, adrsbox);
        centerPanel.add(EnterButton, button);

        FirstFrame.add(centerPanel);
        FirstFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FirstFrame.setSize(300, 250);
        FirstFrame.setLocation(300, 100);
        FirstFrame.setVisible(true);
    }

    public class EnterListener implements ActionListener, KeyListener
    {
        public void actionPerformed(ActionEvent event)
        {
            SetInformation();
        }

        public void keyPressed(KeyEvent event)
        {
            if(event.getKeyCode() == KeyEvent.VK_ENTER)
            {
                SetInformation();
            }
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    private void SetInformation()
    {
        if (NickBox.getText().length() >= 1) {
            if(AddressBox.getText().length() >= 1) {
                Nick = NickBox.getText();
                Address = AddressBox.getText();

                chatframe.getInformation(Nick, Address);
                FirstFrame.setVisible(false);
            }
        }
    }

    private org.json.JSONObject AddNewUser(String Nick, String Address) {

        org.json.JSONObject mes = new org.json.JSONObject();
        org.json.JSONObject content = new org.json.JSONObject();
        content.put("nick", Nick);
        mes.put("type", 1);
        mes.put("content", content);
        mes.put("IP_from", Address);
        return mes;
    }
}

