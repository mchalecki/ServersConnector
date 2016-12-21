package Client;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;

public class ClientMain {
    public static void main(String args[]) {
        System.out.println("Client v1.01");
        ClientServer srv = new ClientServer();
        srv.start();
        ClientSender sender = new ClientSender("172.17.02");
        sender.run();
    }
}
