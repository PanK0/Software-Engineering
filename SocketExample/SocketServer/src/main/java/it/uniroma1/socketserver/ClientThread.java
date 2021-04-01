package it.uniroma1.socketserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientThread implements Runnable {

    private Socket sock;
    private ServantThread st = null;
    private boolean running = false;

    public ClientThread(Socket s) {
        sock = s;
    }

    @Override
    public void run() {

        boolean running = true;
        Scanner in = null;
        PrintWriter pw = null;
        try {
            in = new Scanner(sock.getInputStream());
            pw = new PrintWriter(sock.getOutputStream());
        } catch (IOException e) {
            System.out.println("Client thread error" + Thread.currentThread());
        }
        try {
            while (running) {
                String cmd = in.nextLine();
                System.out.println("Received: " + cmd);
                if (cmd.equals("start")) {
                    st = new ServantThread();
                    Thread t = new Thread(st);
                    t.start();
                } else if (cmd.equals("getStatus")) {
                    String out;
                    if (st.isRunning()) {
                        out = "running";
                    } else {
                        out = "finished";
                    }
                    pw.println(out);
                    pw.flush();
                    System.out.println("Sending: " + out);
                } else if (cmd.equals("getResult")) {
                    for (int n : st.getResult()) {
                        pw.println(String.valueOf(n));
                        pw.flush();
                    }
                    running = false;
                    pw.println("###");
                    pw.flush();
                }
            }
            sock.close();
            pw.close();
            in.close();

        } catch (Exception ex) {
            System.out.println("Client thread error" + Thread.currentThread());
            ex.printStackTrace();
        }
    }
}
