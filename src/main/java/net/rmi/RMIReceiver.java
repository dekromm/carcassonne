package net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This Interface defines the only method that needs to be called remotely:
 * receive.
 * 
 * @author Guido Gerosa
 * 
 */
public interface RMIReceiver extends Remote {

	/**
	 * This method receives an argument and can be invoked remotely.
	 * 
	 * @param arg
	 * @throws RemoteException
	 *             - when an RMI error occurs
	 */
	void receive(Object arg) throws RemoteException;
}
