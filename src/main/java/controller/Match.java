package controller;

import java.util.*;

import view.AbstractCarcassonneView;

import model.*;

/**
 * This class represent a Carcassonne Match. It provides method to add players
 * and to start a match.
 * 
 * @author Guido Gerosa
 * 
 */
public class Match implements Observer {

	private String gameName;
	private CarcassonneModel model;
	private CarcassonneController controller;
	private Map<Player, AbstractCarcassonneView> players;

	/**
	 * Each {@link Match} has a gameName that is passed upon construction
	 * 
	 * @param gameName
	 *            - the game name to register
	 */
	public Match(String gameName) {
		this.gameName = gameName;
		model = new CarcassonneModel(new Deck());
		model.addObserver(this);
		controller = new CarcassonneController(model);
		players = new HashMap<Player, AbstractCarcassonneView>();
	}

	/**
	 * If there are enough players have been added, lets the model and the views
	 * know that the game should start.
	 * 
	 * @return true if the game has been started
	 */
	public boolean start() {
		if (players.size() < 2) {
			return false;
		}
		Card startingTile = model.start();
		Player firstPlayer = model.nextTurn();
		players.get(firstPlayer).addObserver(controller);
		for (Map.Entry<Player, AbstractCarcassonneView> entry : players
				.entrySet()) {
			StartSignal signal = new StartSignal(startingTile, gameName,
					entry.getKey(), players.size());
			entry.getValue().update(null, signal);
		}
		return true;
	}

	/**
	 * Same as start, but sends the same {@link StartSignal} to each registered
	 * View with the {@link Player} field set to null
	 */
	public void startLocal() {
		Card startingTile = model.start();
		Player firstPlayer = model.nextTurn();
		players.get(firstPlayer).addObserver(controller);
		StartSignal signal = new StartSignal(startingTile, gameName, null,
				players.size());
		players.get(firstPlayer).update(null, signal);
	}

	/**
	 * Given a view the controller adds a new player to the model. The view will
	 * be associated to the added {@link Player}.
	 * 
	 * @param view
	 *            - the view to associate to the new player
	 * @return true if the player has been added
	 */
	public boolean addPlayer(AbstractCarcassonneView view) {
		Player newPlayer;
		try {
			newPlayer = model.addPlayer();
		} catch (FullMatchException e) {
			return false;
		}
		this.players.put(newPlayer, view);
		model.addObserver(view);
		return true;
	}

	/**
	 * When this method receives a instance of {@link Player} it makes the
	 * controller observe only the view of the given player
	 */
	public void update(Observable o, Object arg) {
		if (arg instanceof Player) {
			// When a new turn is signaled the control is passed to the next
			// view
			switchView((Player) arg);
		}
	}

	private void switchView(Player playerToObserve) {
		for (Map.Entry<Player, AbstractCarcassonneView> entry : players
				.entrySet()) {
			entry.getValue().deleteObserver(controller);
		}
		players.get(playerToObserve).addObserver(controller);
	}

}
