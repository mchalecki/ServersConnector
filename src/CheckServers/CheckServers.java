/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CheckServers;

import Client.ClientSender.ClientSender;
import Client.ClientServer.ClientServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author user
 */
public class CheckServers {
    private ClientServer[] servers;
    
    private void checkAllDeadServers()

    {
        for (int i=0; i<servers.length; i++) {

           if (!servers[i].getAlive())

               if (alive(servers[i].getHost(), servers[i].getPort()))     {
                    servers[i].setAlive(true);

               }

        }

    }
    
        private boolean alive(String host, int port)
 {
        boolean result = false;

        try {
           Socket s = new Socket(host, port);
           result = true;
           s.close();
        } catch (IOException ioe) {
           ioe.printStackTrace();
        }
         return result;

 }
}
