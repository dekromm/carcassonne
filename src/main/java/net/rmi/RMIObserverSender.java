package net.rmi;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

/**
 * This Observer class acts as a repeater. Upon construction a
 * {@link RMIReceiver} is passed and every time an update() is called on this
 * object it sends the received parameter to it
 * 
 * @author Guido Gerosa
 * 
 */
public class RMIObserverSender implements Observer {
	private RMIReceiver receiver;

	/**
	 * The constructor asks for a {@link RMIReceiver} to which the messages will
	 * be repeated
	 * 
	 * @param receiver
	 */
	public RMIObserverSender(RMIReceiver receiver) {
		this.receiver = receiver;
	}

	/**
	 * This update receives the object and repeat it to the {@link RMIReceiver}
	 * given on construction.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			receiver.receive(arg1);
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Ahi caramba! Call to RemoteMessageReceiver failed!");
		}
	}

}
