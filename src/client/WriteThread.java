package client;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    private boolean forceName;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
        forceName = true;
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("ChatClientWriter: Cannot get output stream");
            e.printStackTrace();
        }
    }

    public void setForceName (boolean b) {
        //System.out.println("setting forcename");
        forceName = b;
    }

    public void sendMessage(String message) {
        if (forceName) message = "/name " + message;
        try {
            // Convert the string to UTF-8 encoded bytes
            byte[] utf8Bytes = message.getBytes("UTF-16");
            
            // Display the bytes as a UTF-8 string
            String decodedMessage = new String(utf8Bytes, "UTF-16");
            writer.println(decodedMessage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        if (message.equalsIgnoreCase("bye")) {
            try {
                socket.close(); // Close the socket
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.exit(0); // Terminate the ChatClient
        } 
    }

    public void run() {
        // Keep listening for user input and send messages to the server
        while (true) {}
    }
}
