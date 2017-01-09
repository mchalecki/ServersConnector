package Client;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;
import Client.GUI.ChatFrame;
import Client.GUI.FirstFrame;

public class ClientMain {
    public static void main(String args[]) {

        FirstFrame firstframe = new FirstFrame();

        ChatFrame chatframe = new ChatFrame();
        firstframe.chatframe = chatframe;

        ClientServer srv = new ClientServer();
        ClientSender sender = new ClientSender();

        firstframe.srv = srv;
        firstframe.sender = sender;
        chatframe.sender = sender;
        srv.gui = chatframe;
        sender.gui = chatframe;
        srv.start();
        //sender.run();
    }
}
