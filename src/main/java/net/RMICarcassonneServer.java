package net;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

import net.rmi.*;

/**
 * This Class extends {@link Server}. It provides a {@link Server}
 * implementation using RMI technology. The Remote Interface implemented offers
 * one method only: connect() which is used to establish comunication between
 * the server and the calling {@link RMIClient}.
 * 
 * @author Guido Gerosa
 * 
 */
public class RMICarcassonneServer extends Server implements net.rmi.RMIServer {

	static final String EXPORT_NAME = "RMIServer";

	/**
	 * This constructor creates a {@link Server} that lets a match start after
	 * the given number of seconds.
	 * 
	 * @param timeout
	 *            - the timeout in seconds
	 */
	public RMICarcassonneServer(int timeout) {
		super(TimeUnit.MILLISECONDS.convert(timeout, TimeUnit.SECONDS));
	}

	/**
	 * Registers the given {@link RMIReceiver} so that the model updates are
	 * sent to it too and returns a {@link RMIReceiver} that will listen for
	 * commands.
	 * 
	 * @param receiver
	 *            - the {@link RMIReceiver} to send model's updates to
	 * @return the RMIReceiver to send controls to
	 */
	@Override
	public RMIReceiver connect(RMIReceiver receiver) throws RemoteException {
		RMIRemoteView newView = new RMIRemoteView(receiver);
		this.newPlayer(newView);
		RMIReceiver viewStub = (RMIReceiver) UnicastRemoteObject.exportObject(
				newView, 0);
		return viewStub;
	}

	/**
	 * Creates a RMI Registry at the specified port and export the remote
	 * connect method
	 * 
	 * @param port
	 *            - the port on which the connection will be opened
	 */
	public void start(int port) {
		try {
			RMIServer serverStub = (RMIServer) UnicastRemoteObject
					.exportObject(this, 0);
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind(EXPORT_NAME, serverStub);
			System.out.println("Server registered as: " + EXPORT_NAME);
		} catch (Exception e) {
			System.err.println("Server exception:");
			e.printStackTrace();
		}
	}

	/**
	 * Creates and starts a server with timout in seconds specified in
	 * command-line
	 * 
	 * @param args
	 *            - the timeout in seconds.
	 */
	public static void main(String[] args) {
		RMICarcassonneServer server = new RMICarcassonneServer(
				Integer.valueOf(args[0]));
		server.start();
	}

}
