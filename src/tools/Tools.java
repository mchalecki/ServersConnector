package tools;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    /**
     * @param message
     * @return IP which is string.
     */
    public static String getIp(String message) {
        String pattern = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(message);
        if (m.find())
            return m.group(1);
        else return null;
    }

    /**
     * Creates server 
     * @param PORT
     * @return returns server which listen at the given PORT
     */
    @Nullable
    public static ServerSocket createServer(int PORT) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT, 50);
            System.out.println("Created server");
        } catch (IOException e) {
            System.out.println("Cant create server");
            System.out.println(e.getMessage());
        }
        return serverSocket;
    }

    /**
     * 
     * @param serverSocket
     * @return connection socket from a given serverScoket
     */
    @Nullable
    public static Socket makeConnectionSocket(ServerSocket serverSocket) {
        Socket connectionSocket = null;
        try {
            connectionSocket = serverSocket.accept();
            System.out.println("New client connected");
        } catch (IOException e) {
            System.out.print("Can't tak user");
        }
        return connectionSocket;
    }

    /**
     * Connects to server 
     * @param targetHost
     * @param PORT
     * @return socket
     */
    @Nullable
    public static Socket connectTo(String targetHost, int PORT) {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(targetHost, PORT);
            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to makeConnectionSocket");
        }
        return clientSocket;
    }
}
