package net;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;

import net.rmi.*;
import view.AbstractCarcassonneView;
import view.TextualCarcassonneView;

/**
 * This class lets the user play a Carcassonne Online Match with an RMI server.
 * 
 * @author Guido Gerosa
 * 
 */
public class RMIClient implements Observer {
	private boolean terminated;
	static final int DEFAULT_PORT = 1099;
	static final int TERMINATION_POLL_TIMER = 5000;

	/**
	 * Creates a RMI client that will play with the specified view on the server
	 * having the specified IP listening on the specified port.
	 * 
	 * @param view
	 *            - the {@link AbstractCarcassonneView} to play with
	 * @param serverAddress
	 *            - the {@link String} of the server's IP
	 * @param port
	 *            - the port on which the server's listenig
	 */
	public RMIClient(AbstractCarcassonneView view, String serverAddress,
			int port) {
		RMIObservableReceiver model;
		RMIObserverSender controller;
		terminated = false;
		view.addObserver(this);
		model = new RMIObservableReceiver();
		model.addObserver(view);
		try {
			Registry registry = LocateRegistry.getRegistry(serverAddress, port);
			RMIServer server = (RMIServer) registry.lookup("RMIServer");
			RMIReceiver modelStub = (RMIReceiver) UnicastRemoteObject
					.exportObject(model, 0);
			RMIReceiver rmiobssender = server.connect(modelStub);
			controller = new RMIObserverSender(rmiobssender);
			view.addObserver(controller);
			view.addObserver(this);
		} catch (Exception e) {
			System.err.println("Client exception:");
			e.printStackTrace();
		}
	}

	/**
	 * Creates a RMI client that will play with the specified view on the server
	 * having the specified IP listening on the default 1099 port.
	 * 
	 * @param view
	 *            - {@link AbstractCarcassonneView} to play with
	 * @param serverAddress
	 *            - the IP of the server to contact
	 */
	public RMIClient(AbstractCarcassonneView view, String serverAddress) {
		this(view, serverAddress, DEFAULT_PORT);
	}

	/**
	 * Waits for the user to use the "quit" command, then return.
	 */
	public void start() {
		while (!terminated) {
			try {
				Thread.sleep(TERMINATION_POLL_TIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * When a "quit" String is received the client will be marked as
	 * "terminated"
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String && ((String) arg1).equalsIgnoreCase("quit")) {
			terminated = true;

		}
	}

	/**
	 * Creates an RMIClient that will connect on default port to the localhost.
	 * 
	 * @param args
	 *            - not relevant
	 */
	public static void main(String[] args) {
		RMIClient client = new RMIClient(new TextualCarcassonneView(), args[0]);
		client.start();
	}
}
