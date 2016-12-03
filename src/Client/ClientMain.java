package Client;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;

public class ClientMain {
    public static void main(String args[]) throws InterruptedException {
        ClientServer srv = new ClientServer();
        srv.start();
        ClientSender sender = new ClientSender("127.0.0.2");
        sender.run();
    }
}
