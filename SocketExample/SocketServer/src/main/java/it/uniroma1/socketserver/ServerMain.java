package it.uniroma1.socketserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain {

    public static void main(String[] args) {

        List<ClientThread> ctList = new ArrayList<ClientThread>();
        ServerSocket lis = null;

        try {
            lis = new ServerSocket(Integer.parseInt(args[0]));
        } catch (IOException e1) {
            System.out.println("Error in ServerSocket creation, application killed");
            System.exit(1);
        }
        System.out.println("Server is running");
        Socket sock = null;

        while (true) {
            try {
                sock = lis.accept();
            } catch (IOException e) {
                break;
            }
            System.out.println("Socket created, connection accepted");
            ClientThread cl = new ClientThread(sock);
            Thread tr = new Thread(cl);
            tr.start();
            ctList.add(cl);
        }
    }

}
