package net.rmi;

import java.rmi.RemoteException;
import java.util.Observable;

import view.AbstractCarcassonneView;

/**
 * This class is used to represents "a hologram" of the real Carcassonne view
 * that resides on a different JVM. Actually it's an AbstractCarcassonneView
 * implementation that provides functionalities of both RMIReceiver and
 * RMIObserverSender.
 * 
 * @author Guido Gerosa
 * 
 */
public class RMIRemoteView extends AbstractCarcassonneView implements
		RMIReceiver {

	private RMIReceiver receiver;

	/**
	 * Upon construction the RMIReceiver to which the Observed messaged will be
	 * send is specified.
	 * 
	 * @param destination
	 *            - the {@link RMIReceiver} to send the messages to.
	 */
	public RMIRemoteView(RMIReceiver destination) {
		this.receiver = destination;
	}

	/**
	 * This method will use the {@link Observable}'s notifyObserver() when
	 * invoked. The passed argument will be forwarded via notifyObservers().
	 * 
	 * @param arg
	 *            - the Object to notify
	 */
	@Override
	public void receive(Object arg) throws RemoteException {
		setChanged();
		notifyObservers(arg);
	}

	/**
	 * This method will repeat the update message received using the RMIReceiver
	 * registered during construction.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			receiver.receive(arg1);
		} catch (RemoteException e) {
			e.printStackTrace();
			// throw new RuntimeException(
			// "Ahi caramba! Call to RemoteMessageReceiver failed!");
			// Very bad stuff happened
		}
	}

}
