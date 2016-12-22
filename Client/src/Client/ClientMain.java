package Client;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;

public class ClientMain {
    private static final String version = "1.01";
    private static final String redir_ip = "172.17.02";
    public static void main(String args[]) {
        System.out.println("Client v" + version);
        ClientServer srv = new ClientServer();
        srv.start();
        ClientSender sender = new ClientSender(redir_ip);
        sender.run();
    }
}
