package Client;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;
import Client.GUI.ChatFrame;
import Client.GUI.FirstFrame;

public class ClientMain {
    private static final String version = "1.01";
    public static void main(String args[]) {
        System.out.println("Client v" + version);
        FirstFrame firstframe = new FirstFrame();
        ChatFrame chatframe = new ChatFrame();
        firstframe.chatframe = chatframe;

        ClientServer srv = new ClientServer();
        ClientSender sender = new ClientSender();

        firstframe.srv = srv;
        firstframe.sender = sender;
        chatframe.sender = sender;
        chatframe.server = srv;
        srv.gui = chatframe;
        sender.gui = chatframe;
        srv.start();
    }
}
