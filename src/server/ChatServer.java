package server;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private ServerSocket serverSocket = null;
    private HashMap<String, UserThread> userList = new HashMap<String, UserThread>();

    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started!");
        }
        catch (IOException e) {
            System.err.println("Server error: Cannot start server");
            e.printStackTrace();
        }
    }

    //add a user and the corresponding thread handler to the user list
    public void addUser (String username, UserThread userThread) {
        userList.put(username, userThread);
    }

    public void removeUser(String username) {
        userList.remove(username);
    }

    //return the list of current users
    public ArrayList<String> getUserList() {
        ArrayList<String> ans = new ArrayList<String>();
        for (String i : userList.keySet()) {
            ans.add(i);
        }
        return ans;
    }

    public boolean isUserInList (String username) {
        if (userList.get(username) != null) return true;

        else return false;
    }

    //Broadcasts a message to all active clients
    public void broadcast(String message) {
        for (UserThread user_t : userList.values()) {
            user_t.sendMessage(message);
        }
    }

    //Send a message to a specific username
    public void individual(String message, String username) {
        UserThread user_t = userList.get(username);
        user_t.sendMessage(message);
    }

    public void exec() {
        try {
            System.out.println("Server is listening on port " + serverSocket.getLocalPort() + ".");
            while (true) {

                Socket socket = serverSocket.accept();
                System.out.println("New user connected!");
                
                // serverRun serverRun1 = new serverRun();
                // serverRun1.start();

                UserThread newUser = new UserThread(socket, this);
                newUser.start();

            }
        }
        catch (IOException e) {
            System.err.println("Exception in ChatServer: " + e.getMessage());
            e.printStackTrace();
        }
    }

// public class serverRun extends Thread{

//     public void respondIPadd(){
//         try {
//             System.out.println("respond");
//             int serverPort = 5000;  // Server's listening port
            
//             DatagramSocket socket = new DatagramSocket(serverPort);
            
//             byte[] receiveData = new byte[1024];
//             DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
//             while (true) {
//                 socket.receive(receivePacket);
//                 InetAddress clientAddress = receivePacket.getAddress();
//                 int clientPort = receivePacket.getPort();

//                 InetAddress localHost = InetAddress.getLocalHost();
//                 String localIPAddress = localHost.getHostAddress();
                
//                 String responseMessage = localIPAddress;
//                 byte[] sendData = responseMessage.getBytes();
                
//                 DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
//                 socket.send(sendPacket);
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
//     public void run() {
//         while(true){
//             this.respondIPadd();
//         }
        
//     }

//     public void runner(){
//         System.out.println("runner");
//         ChatServer server = new ChatServer(5000);
//         server.exec();
//     }
// }
    

    public static void main(String[] args) {
        ChatServer server = new ChatServer(5000);
        server.exec();
    }
    
}
