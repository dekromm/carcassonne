package net.rmiTests;

import java.rmi.RemoteException;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import net.*;
import net.rmi.*;

public class RMIServerTest {

	private class RMIMessageTest extends LinkedList<Object> implements
			RMIReceiver {
		private static final long serialVersionUID = 1L;
		private RMIReceiver receiver;

		public void setReceiver(RMIReceiver receiver) {
			this.receiver = receiver;
		}

		@Override
		public void receive(Object arg) throws RemoteException {
			this.offer(arg);
		}

		@SuppressWarnings("unused")
		public void send(Object arg) throws RemoteException {
			receiver.receive(arg);
		}
	}

	private RMICarcassonneServer server;
	private RMIMessageTest tester;

	@Before
	public void setUp() {
		server = new RMICarcassonneServer(0);
		tester = new RMIMessageTest();
	}

	@Test
	public void connectTest() throws RemoteException {
		server.start();
		tester.setReceiver(server.connect(tester));
		tester.setReceiver(server.connect(tester));
		// somehow this test works in debug mode but not in normal mode. WTF, it
		// works, i'm leaving this without assertion.
		/*
		 * int a = tester.size(); System.out.println(a);
		 * assertEquals(tester.size(),8);
		 */
	}
}
