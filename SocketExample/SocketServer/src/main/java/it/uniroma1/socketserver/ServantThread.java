package it.uniroma1.socketserver;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServantThread implements Runnable {
    
    private boolean running = false;
    private int[] result;
    
    private synchronized void atomicAction(int i) {
        Double random = Math.random();
        int j = (int) (random * 10);
        result[i] = j;
        System.out.println(Thread.currentThread() + " ... I am working and producing " + j);
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread() + " Start working");
        running = true;
        result = new int[10];
        for (int i=0; i<10; i++) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServantThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            atomicAction(i);
        }
        
        System.out.println(Thread.currentThread() + " Stop working");
        System.out.println(Thread.currentThread() + " is returning the result " + Arrays.toString(result));
        running = false;
    }
    public synchronized boolean isRunning() {
        return running;
    }
    
    public synchronized int[] getResult() {
        System.out.println(Thread.currentThread() + " is returning the result " + Arrays.toString(result));
        if (running == false) return result;
        else return null;
    }

}
