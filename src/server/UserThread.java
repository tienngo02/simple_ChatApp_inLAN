package server;
import java.io.*;
import java.net.*;
import java.util.*;

/*
 * need to add:
 * 
 * some useful commands: /help (/?), /pm, /all, /users, /quit
 * /help : display the help page about commands
 * 
 * /quit: can be a replacement for the "bye" terminate method
 * 
 * 
 * 
 * extra stuff if there's still time: file sharing system
 */


//handles each client separately
public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private String username;
    
    private boolean pmMode;
    private String pmName;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        pmMode = false;
        pmName = null;
        username = null;
    }

    //write the user list to the client
    public void printUsers() { 
        ArrayList<String> usernameList = server.getUserList();
        writer.println("Server currently has " + usernameList.size() + "users");
        for (int i=0; i<usernameList.size(); i++) {
            writer.println((i+1) + ". " +usernameList.get(i));
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public void command_name (String name) {
        boolean hasUser = server.isUserInList(name);
        if (!hasUser) {
            String oldUsername = this.username;
            if (oldUsername != null) server.removeUser(oldUsername);
            this.username = name;
            sendMessage("/name OK " + name);
            server.addUser(name, this);
            
            if (oldUsername == null) server.broadcast(this.username + " has joined the chat");
            else server.broadcast(oldUsername + " has changed username to " + this.username);
        }
        else {
            sendMessage("/name EX"); //existed
        }
    }

    public void command_pm (String pmName) {
        boolean valid = server.isUserInList(pmName);
        if (valid) {
            pmMode = true;
            this.pmName = pmName;
            sendMessage("/pm OK " + pmName);
        }
        else {
            sendMessage("/pm DNE");
        }
    }

    public void command_all() {
        pmMode = false;
        this.pmName = null;
        sendMessage("/all OK");
    }

    public void command_users() {
        ArrayList<String> usernames = server.getUserList();
        String response = "There are " + usernames.size() + " users in the server\n";
        for (int i=0; i<usernames.size(); i++) {
            response = response + (i+1) + ". " + usernames.get(i) + "\n";
        }
        sendMessage(response);
    }

    public void command_invalid() {
        sendMessage("/invalid");
    }

    public void commandHandler(String command) {
        //tokenize client command and handle it
        System.out.println("Received command: " + command);
        String[] tokens = command.split(" ");

        int tokenCount = tokens.length;
        switch (tokens[0]) {
            case "/name" :
                if (tokenCount != 2) command_invalid();
                else command_name(tokens[1]);
                break;

            case "/pm" :
                if (tokenCount != 2) command_invalid();
                else command_pm(tokens[1]);
                break;

            case "/all" :
                if (tokenCount != 1) command_invalid();
                else command_all();
                break;
            
            case "/users" :
                if (tokenCount != 1) command_invalid();
                else command_users();
                break;

            default :
                command_invalid();
                break;
        }
    }
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            
            //username = reader.readLine();
            //server.addUser(username, this);

            //printUsers();

            String clientMessage = "";
            String serverMessage = "";
            do {
                clientMessage = reader.readLine();
                if (clientMessage == null || clientMessage.length() == 0) continue;
                if (clientMessage.charAt(0) == '/') {
                    commandHandler(clientMessage);
                }
                else {
                    if (pmMode) {
                        serverMessage = "[To " + pmName + "]: " + clientMessage;
                        server.individual(serverMessage, username);

                        serverMessage = "[From " + username + "]: " + clientMessage;
                        server.individual(serverMessage, pmName);
                    }
                    else {
                        serverMessage = username + "> " + clientMessage;
                        server.broadcast(serverMessage);
                    }
                }
                
            } while (!clientMessage.equals("bye"));

            server.removeUser(username);
            serverMessage = username + " has left the chat.";
            server.broadcast(serverMessage);
        }
        catch (IOException e) {
            System.err.println("Exception in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
