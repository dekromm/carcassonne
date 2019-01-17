package net;

import java.util.HashMap;
import java.util.Map;

import controller.Match;

import view.AbstractCarcassonneView;

import model.PlayerColor;

/**
 * This class manages the arrival of new player (
 * {@link AbstractCarcassonneView} ) by adding them to a {@link Match}. The
 * Match will start if the number of players hits the maximum or if no player is
 * added after a timeout specified upon construction.
 * 
 * @author Guido Gerosa
 * 
 */
public abstract class Server implements Runnable {

	private Match currentMatch;
	private Map<String, Match> matches;
	private int connectedPlayers;
	private int matchIndex;
	private Thread timer;
	private long timerTick;
	static final String MATCHTITLE = "Room";
	static final int DEFAULT_PORT = 1099;

	/**
	 * Given a timout in milliseconds, each match with more than 2 people will
	 * automatically start if no player is added within that time.
	 * 
	 * @param timerTick
	 *            - the time in milliseconds to wait before starting the current
	 *            match
	 */
	public Server(long timerTick) {
		this.timerTick = timerTick;
		matches = new HashMap<String, Match>();
		matchIndex = 0;
		newMatch();
	}

	private synchronized void newMatch() {
		if (currentMatch == null || currentMatch.start()) {
			String matchName = MATCHTITLE + matchIndex;
			currentMatch = new Match(matchName);
			matches.put(matchName, currentMatch);
			connectedPlayers = 0;
			matchIndex++;
		}
	}

	/**
	 * This method takes an {@link AbstractCarcassonneView} and puts it as a
	 * player in the current Match. If the current match is full after this
	 * operation it is started and a new Match is created.
	 * 
	 * @param newView
	 *            - the view to add to the current match
	 */
	public synchronized void newPlayer(AbstractCarcassonneView newView) {
		if (connectedPlayers >= 2) {
			timer.interrupt();
		}
		connectedPlayers++;
		currentMatch.addPlayer(newView);
		if (connectedPlayers >= PlayerColor.values().length) {
			newMatch();
		} else if (connectedPlayers >= 2) {
			timer = new Thread(this);
			timer.start();
		}
	}

	/**
	 * This method sleeps for the timout specified upon construction and creates
	 * a new match if it's not interrupted when sleeping.
	 */
	public void run() {
		try {
			Thread.sleep(timerTick);
		} catch (InterruptedException e) {
			return;
		}
		newMatch();
	}

	/**
	 * This method must be implemented by any concrete server with the
	 * operations needed to get the server up and running on the specified port.
	 * 
	 * @param port
	 *            - the port the server will use to listen for connections
	 */
	public abstract void start(int port);

	/**
	 * Calls the start(int port) method passing the default port 1099
	 */
	public void start() {
		start(DEFAULT_PORT);
	}
}