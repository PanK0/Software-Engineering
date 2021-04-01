package it.uniroma1.svriface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

   int startTask() throws RemoteException;
   boolean isReady(int id) throws RemoteException;
   int[] getResults(int id) throws RemoteException;
}
