package tools;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    /**
     * Retrns ip which is in string.
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
     * Creates and returns server which listen at the given PORT
     */
    @Nullable
    public static ServerSocket createServer(int PORT) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT, 50);
            System.out.println("Created server");
            return serverSocket;
        } catch (IOException e) {
            System.out.println("Cant create server");
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Returns connection socket from a given serverScoket
     */
    @Nullable
    public static Socket makeConnectionSocket(ServerSocket serverSocket) {
        try {
            Socket connectionSocket = serverSocket.accept();
            System.out.println("New client connected");
            return connectionSocket;
        } catch (IOException e) {
            System.out.print("Can't tak user");
            return null;
        }
    }
}
