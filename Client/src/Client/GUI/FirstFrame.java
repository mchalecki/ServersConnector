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
    public ClientServer srv;
    public ClientSender sender;

    private static JFrame FirstFrame = new JFrame("Chat");
    private static JLabel WelcomeLabel;
    private static JLabel NickLabel;
    private static JLabel AddressLabel;
    private static JTextField NickBox;
    private static JTextField AddressBox;
    private static JButton EnterButton;

    private String Nick;
    private String Address;
    private boolean verified;

    public FirstFrame() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        WelcomeLabel = new JLabel("Enter nick and address");
        WelcomeLabel.setFont(new Font("Serif", Font.BOLD, 18));

        NickLabel = new JLabel("Nick: ");
        NickLabel.setFont(new Font("Serif", Font.BOLD, 15));
        AddressLabel = new JLabel("Address: ");
        AddressLabel.setFont(new Font("Serif", Font.BOLD, 15));
        NickBox = new JTextField();
        AddressBox = new JTextField();

        AddressBox.addKeyListener(new InputListener());

        EnterButton = new JButton("Enter");
        EnterButton.addActionListener(new InputListener());

        GridBagConstraints welcome = new GridBagConstraints();
        welcome.fill = GridBagConstraints.HORIZONTAL;
        welcome.gridx = 0;
        welcome.gridy = 0;
        welcome.insets = new Insets(0, 0, 10, 0);
        welcome.gridwidth = 2;

        GridBagConstraints nick = new GridBagConstraints();
        nick.fill = GridBagConstraints.HORIZONTAL;
        nick.gridx = 0;
        nick.gridy = 1;

        GridBagConstraints adrs = new GridBagConstraints();
        adrs.fill = GridBagConstraints.HORIZONTAL;
        adrs.gridx = 0;
        adrs.gridy = 2;

        GridBagConstraints nickbox = new GridBagConstraints();
        nickbox.fill = GridBagConstraints.HORIZONTAL;
        nickbox.gridx = 1;
        nickbox.gridy = 1;
        nickbox.insets = new Insets(10, 0, 10, 0);

        GridBagConstraints adrsbox = new GridBagConstraints();
        adrsbox.fill = GridBagConstraints.HORIZONTAL;
        adrsbox.gridx = 1;
        adrsbox.gridy = 2;
        welcome.insets = new Insets(10, 0, 10, 0);

        GridBagConstraints button = new GridBagConstraints();
        button.fill = GridBagConstraints.HORIZONTAL;
        button.gridx = 0;
        button.gridy = 3;
        button.insets = new Insets(30, 0, 0, 0);
        button.gridwidth = 2;

        centerPanel.add(WelcomeLabel, welcome);
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

    private void SetInformation() {
        if (NickBox.getText().length() >= 1) {
            if (AddressBox.getText().length() >= 1) {
                Nick = NickBox.getText();
                Address = AddressBox.getText();
                chatframe.getInformation(Nick, Address);
                sender.getInformation(Nick, Address);
                FirstFrame.setVisible(false);
            }
        }
    }

    public class MyInputVerifier {
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            String[] parts = text.split("\\.");
            if (parts.length != 4) {
                errorMessage();
                return false;
            }

            for (int i = 0; i < 4; i++) {
                if ((parts[i].length() > 3) || (parts[i].length() < 1))
                {
                    errorMessage();
                    return false;
                }
            }
            verified = true;
            return true;
        }
    }

    private void errorMessage() {
        JOptionPane.showMessageDialog(null,
                "Error: Wrong format of address", "Error Massage",
                JOptionPane.ERROR_MESSAGE);
    }

    public class InputListener implements KeyListener, ActionListener {

        public void keyTyped(KeyEvent ke) {
            char ch = ke.getKeyChar();
            if (!(Character.isDigit(ch)) && !(ch == '.')) ke.consume();
        }

        public void keyPressed(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                MyInputVerifier miv = new MyInputVerifier();
                miv.verify(AddressBox);
                if (verified) {
                    SetInformation();
                    sender.clientSocket = sender.make_connection();
                    sender.welcomeMessage(Nick);
                    chatframe.ChatBox.append("\n Add friends! \n");
                }

            }
        }

        public void keyReleased(KeyEvent ke) {
        }

        public void actionPerformed(ActionEvent ke) {
            MyInputVerifier miv = new MyInputVerifier();
            miv.verify(AddressBox);
            if (verified) {
                SetInformation();
                sender.clientSocket = sender.make_connection();
                sender.welcomeMessage(Nick);
            }
        }
    }
}