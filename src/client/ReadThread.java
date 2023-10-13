package client;
import java.io.*;
import java.net.*;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.client = client;
        this.socket = socket;
        try {
            InputStream input = socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
        }
        catch (IOException e) {
            System.err.println("Exception in ReadThread - constructor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getLine() {
        String s = null;
        try {
            s = reader.readLine();
        }
        catch (IOException e) {
            System.err.println("Exception in ReadThread - getLine(): " + e.getMessage());
        }
        return s;
    }

    public void run() {
        while(true) {
            String response = getLine();
            if (response == null || response.length() == 0) continue;
            if (response.charAt(0) == '/') {
                client.responseHandler(response);
            }
            else {
                client.writeToGUI(response);
            }
        }
    }
}
