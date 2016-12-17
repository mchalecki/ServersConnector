package Client;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;

public class ClientMain {
    public static void main(String args[]) {
        ClientServer srv = new ClientServer();
        srv.start();
        ClientSender sender = new ClientSender("192.168.1.100");
        sender.run();
    }
}
