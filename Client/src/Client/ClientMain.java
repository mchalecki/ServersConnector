package Client;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;
import Client.GUI.ChatFrame;

public class ClientMain {
    public static void main(String args[]) {
        ChatFrame _gui = new ChatFrame();
        ClientServer srv = new ClientServer();
        ClientSender sender = new ClientSender("127.0.0.10");
        _gui.client = sender;
        srv.gui = _gui;
        sender.gui = _gui;
        srv.start();
        sender.run();
    }
}
