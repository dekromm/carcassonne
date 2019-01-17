package net.rmi;

import java.rmi.RemoteException;
import java.util.*;

/**
 * This {@link Observable} class acts as a repeater: a (possibly remote) object
 * can call this class' method receive() give it a parameter. As soon as the
 * parameter is received it's boradcasted to every {@link Observer} that's
 * observing this {@link Observable}.
 * 
 * @author Guido Gerosa
 * 
 */
public class RMIObservableReceiver extends Observable implements RMIReceiver {

	/**
	 * Receive a parameter and broadcasts it using the {@link Observable}'s
	 * notifyObserver().
	 * 
	 * @param arg
	 *            - the object to be broadcasted
	 */
	@Override
	public void receive(Object arg) throws RemoteException {
		setChanged();
		notifyObservers(arg);
	}

}
