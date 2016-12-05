package Client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//Jak mówiłam to działało jak było w Client Senderze. Jeszcze nie odkryłam co zrobić, gdzie wstawić, żeby to połączyć z resztą,
//bo różnie próbowałam, ale ciągle wyskakują jakieś błędy.

public class GUI extends JFrame{

    static JFrame Frame = new JFrame("Chat");
    static JTextArea ChatBox;
    static JTextField WriteMessageBox;
    static JButton SendMessageButton;

    public GUI()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel downPanel = new JPanel();
        downPanel.setLayout(new GridBagLayout());

        WriteMessageBox = new JTextField(50);
        WriteMessageBox.requestFocusInWindow();

        SendMessageButton = new JButton("Send Message!");
        SendMessageButton.addActionListener(new SendMessageButtonListener());

        ChatBox = new JTextArea();
        ChatBox.setEditable(false);
        ChatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(ChatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        downPanel.add(WriteMessageBox, left);
        downPanel.add(SendMessageButton, right);

        mainPanel.add(BorderLayout.SOUTH, downPanel);

        Frame.add(mainPanel);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(470, 300);
        Frame.setVisible(true);
    }


    class SendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (WriteMessageBox.getText().length() >= 1) {

                String send_text = WriteMessageBox.getText();

                //to działało jak było w ClientSenderze
                //send_message(send_text);

                ChatBox.append("<username>:  " + WriteMessageBox.getText() + "\n");
                WriteMessageBox.setText("");
            }
            WriteMessageBox.requestFocusInWindow();
        }
    }
}
