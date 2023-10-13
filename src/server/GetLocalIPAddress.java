package server;

import java.net.*;

public class GetLocalIPAddress {
    public static void main(String[] args) {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String localIPAddress = localHost.getHostAddress();
            
            System.out.println("Local IP Address: " + localIPAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
