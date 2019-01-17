package net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the only method that a remote RMIServer lets see to
 * the clients.
 * 
 * @author Guido
 * 
 */
public interface RMIServer extends Remote {

	/**
	 * This method takes a {@link RMIReceiver} to which messages will be sent
	 * and returns the {@link RMIReceiver} on which the server will listen
	 * 
	 * @param receiver
	 *            - the {@link RMIReceiver} that the server will use to send
	 *            message
	 * @return the {@link RMIReceiver} on which the server will listen
	 * @throws RemoteException
	 *             when an RMI error occurs
	 */
	RMIReceiver connect(RMIReceiver receiver) throws RemoteException;
}
