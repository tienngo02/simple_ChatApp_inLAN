package client;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class ChatClient {
    private String hostAddress;
    private int port;
    private Socket socket;

    private ReadThread reader;
    private WriteThread writer;
    private ChatClientGUI gui;

    private String username;

    public ChatClient(String hostAddress, int port) {
        this.hostAddress = hostAddress;
        this.port = port;
        try {
            this.socket = new Socket(hostAddress, port);
        }
        catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e) {
            System.err.println("I/O Exception in ChatClient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void writeToGUI (String message) {
        gui.displayMessage(message);
    }

    public void sendMessage (String message) {
        writer.sendMessage(message);
    }

    public void responseHandler(String response) {
        System.out.println("Received response: " + response);
        String[] tokens = response.split(" ");
        switch (tokens[0]) {
            case "/invalid":
                writeToGUI("Invalid command.");
                break;
            /* /name OK <username> */
            case "/name":
                if (tokens[1].equals("OK")) {
                    String newUsername = tokens[2];
                    this.username = newUsername;
                    writeToGUI("Your username has been set to " + newUsername);
                    writer.setForceName(false);
                    //Update GUI
                    SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        gui.setTitle("Chat client [" + newUsername + "]");
                    }
                    });
                }   
                if (tokens[1].equals("EX")) {
                    writeToGUI("The username you chose already exists.");
                }
                break;
            case "/all":
                writeToGUI("You are now chatting in public chatroom.");
                break;
            case "/pm":
                if (tokens[1].equals("OK")) {
                    writeToGUI("You are now chatting privately with " + tokens[2] + ".");
                }
                if (tokens[1].equals("DNE")) {
                    writeToGUI("This username does not exist.");
                }
                break;
            default:
                System.out.println("Received invalid response from server (idk how)");
                break;
        }
    }
    
    String getUsername () {
        return this.username;
    }

    public void exec() {
        System.out.println("Welcome to the chat server!");

        writer = new WriteThread(socket, this);
        reader = new ReadThread(socket, this);
        writer.start();
        reader.start();

        gui = new ChatClientGUI(this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.setVisible(true);
                // Example usage to display a message in the GUI
            }
        });
        writeToGUI("Hello, welcome to the chat!");
        writer.setForceName(false);
        writer.sendMessage("/users");
        writer.setForceName(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Error in wait: " + e.getMessage());
            e.printStackTrace();
        }
        writeToGUI("Please enter your username: ");
    }
    public static void main(String[] args) {
        // try {
        //     DatagramSocket socket = new DatagramSocket();
        //     socket.setBroadcast(true);
            
        //     int serverPort = 5000;  // Server's listening port
            
        //     String requestMessage = "Requesting server IP address";
        //     byte[] sendData = requestMessage.getBytes();
            
        //     InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
        //     DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddress, serverPort);
        //     socket.send(sendPacket);
            
        //     byte[] receiveData = new byte[1024];
        //     DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        //     socket.receive(receivePacket);
            
        //     String serverInfo = new String(receivePacket.getData(), 0, receivePacket.getLength());
        //     System.out.println("Received response from server: " + serverInfo);
            
        //     ChatClient client = new ChatClient(serverInfo, 5000);
        //     client.exec();
            
        //     socket.close();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        //Add support for hostAddress - port input from terminal
        
        ChatClient client = new ChatClient("10.128.18.196", 5000);
        client.exec();

    }
}
