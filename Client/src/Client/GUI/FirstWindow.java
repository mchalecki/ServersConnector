package Client.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class FirstWindow extends JFrame{
    private static JFrame FirstWindow = new JFrame();
    private static JTextField WriteNick;
    private static JTextField WriteAddress;
    private static JButton ConnectButton;
    private static JLabel Nick;
    private static JLabel Address;
     
    public FirstWindow()
    {
        
        JPanel downPanel = new JPanel();
        downPanel.setLayout(new GridBagLayout());
        
        WriteNick = new JTextField();
        WriteAddress = new JTextField();
        ConnectButton = new JButton("Connect");
        Nick = new JLabel("Nick: ");
        Address = new JLabel("Address: ");
        
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.fill = GridBagConstraints.HORIZONTAL;
        gbc1.gridx =0;
        gbc1.gridy =0;
        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.gridx =0;
        gbc2.gridy =1;
        
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.gridx =1;
        gbc3.gridy =0;
        
        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.fill = GridBagConstraints.HORIZONTAL;
        gbc4.gridx =1;
        gbc4.gridy =1;
        
        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.fill = GridBagConstraints.HORIZONTAL;
        gbc5.gridx =1;
        gbc5.gridy =2;
        
        downPanel.add(Nick, gbc1);
        downPanel.add(Address, gbc2);
        downPanel.add(WriteNick, gbc3);
        downPanel.add(WriteAddress, gbc4);
        downPanel.add(ConnectButton, gbc5);
        
        FirstWindow.add(downPanel);
        FirstWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FirstWindow.setSize(300, 200);
        FirstWindow.setLocation(300, 100);
        FirstWindow.setVisible(true);
        
    }
    
    public static void main(String[] args)
    {
        FirstWindow fw = new FirstWindow();
    }

}

