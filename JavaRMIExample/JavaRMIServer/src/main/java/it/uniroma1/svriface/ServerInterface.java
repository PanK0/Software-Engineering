package it.uniroma1.svriface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
* In JavaRMI, in order to be considered an IDL module, an interface should extend Remote.
* This file is the Interface Definition file of this program.
*/
public interface ServerInterface extends Remote {

   int startTask() throws RemoteException;
   boolean isReady(int id) throws RemoteException;
   int[] getResults(int id) throws RemoteException;
}
