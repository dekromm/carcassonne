package model;

import java.util.*;

/**
 * The CarcassonneModel class represents the state of a single Carcassonne
 * match. The state of the class consists in the references to the board, to the
 * deck, to the current card, to the current player, to the last placed card
 * coordinate and to the last touched zones, in a List of the players of the
 * match and in a list of the markers.
 * 
 * The class allows to get the match state, to add a player to the match, to
 * change the current player with the next one, to get a maker, to rotate the
 * current card, to place the current card on the board, to place a marker on
 * the last placed card, to change the current card with the next one and to get
 * the first card of the game.
 * 
 * @author Edoardo Galimberti, Guido Gerosa
 * 
 */

public class CarcassonneModel extends Observable {

	private Deck deck;
	private Board board;
	private Coordinate lastPlacedCardCoordinate = new Coordinate(0, 0);
	private Card currentCard;
	private Queue<Player> turns = new LinkedList<Player>();
	private Player currentPlayer;
	private List<Marker> markers = new ArrayList<Marker>();
	private boolean cardIsPlaced;
	private Collection<Zone> lastTouchedZones = new ArrayList<Zone>();;
	static final int MARKER_PER_PLAYER = 7;

	/**
	 * Construct a Carcassonne model containing all the objects used to play a
	 * match.
	 * 
	 */
	public CarcassonneModel(Deck deck) {
		this.deck = deck;
		this.board = new Board();
		this.cardIsPlaced = true;
	}

	/**
	 * Returns a {@link String} containing a brief report on player's score and
	 * markers.
	 * 
	 * @return a brief report on player's score and markers
	 */
	private String getMatchState() {
		String report = "";
		for (Player player : turns) {
			int markerCount = 0;
			for (Marker marker : markers) {
				if (marker.getOwner().equals(player)) {
					markerCount++;
				}
			}
			report = report.concat("Player " + player + ": "
					+ player.getScore() + " (" + markerCount + ")" + "; ");
		}
		report = report.concat("Tiles left: " + deck.size());
		return report;
	}

	/**
	 * Adds a new player to the match and returns it.
	 * 
	 * @return - the Player that has been added
	 * @throws FullMatchException
	 *             if this model can't take any other player
	 */
	public Player addPlayer() throws FullMatchException {
		Player newPlayer;
		if (turns.size() < PlayerColor.values().length) {
			newPlayer = new Player(PlayerColor.values()[turns.size()]);
		} else {
			throw new FullMatchException();
		}
		if (!turns.offer(newPlayer)) {
			throw new FullMatchException();
		}
		for (int j = 0; j < MARKER_PER_PLAYER; j++) {
			this.markers.add(new Marker(newPlayer));
		}
		return newPlayer;
	}

	/**
	 * Changes the current player. This method allows to change the current
	 * player with the next one in the list, and add the changed player at the
	 * end of the same list.
	 */
	public Player nextTurn() {
		if (cardIsPlaced) {
			assignLastTurnScores();
			// reset the touched zones
			lastTouchedZones = new ArrayList<Zone>();
			// pick the next card
			try {
				nextCard();
				// if the deck was not empty:
				// change the current player
				this.currentPlayer = this.turns.remove();
				this.turns.add(currentPlayer);
				// send scores and markers.
				setChanged();
				notifyObservers(getMatchState());
				// notify the new turn by sending the next player.
				setChanged();
				notifyObservers(this.currentPlayer.clone());
				// send the currentCard.
				setChanged();
				notifyObservers(currentCard.clone());
			} catch (IndexOutOfBoundsException e) {
				// The game is over
				assignScoresUncompletedZones();
				setChanged();
				notifyObservers("Game Over!");
				// send scores and markers.
				setChanged();
				notifyObservers(getMatchState());
				return this.currentPlayer;
			}
			this.cardIsPlaced = false;
		} else {
			setChanged();
			notifyObservers(currentPlayer + ": you must place the tile first.");
		}
		return this.currentPlayer;
	}

	private void assignLastTurnScores() {
		for (Zone zone : lastTouchedZones) {
			if (zone.isCompleted() && zone.isRuled()) {
				for (Player ruler : zone.getRulers()) {
					ruler.addScore(zone.getScore());
				}
				Map<Coordinate, Card> changedTiles = board.removeMarkers(zone,
						markers);
				for (Map.Entry<Coordinate, Card> entry : changedTiles
						.entrySet()) {
					BoardUpdate update = new BoardUpdate(entry.getValue()
							.clone(), entry.getKey());
					setChanged();
					notifyObservers(update);
				}
			}
		}
	}

	private void assignScoresUncompletedZones() {
		for (Zone zone : board.getUncompletedZones()) {
			if (zone.isRuled()) {
				for (Player ruler : zone.getRulers()) {
					if (zone.getPlace() == Place.STREET) {
						ruler.addScore(zone.getScore());
					} else {
						ruler.addScore(zone.getScore() / 2);
					}
				}
			}
		}
	}

	/**
	 * Get a marker of the specified {@link Player} from the list. This method
	 * allows to get a marker that belongs to the current player from the list
	 * of markers.
	 * 
	 * @param playerOwner
	 *            Player owner of the marker
	 * @return Marker
	 * @throws NoMoreMarkersException
	 */
	private Marker getMarker(Player playerOwner) throws NoMoreMarkersException {
		Marker toReturn = new Marker(playerOwner);
		if (markers.remove(toReturn)) {
			// If the requested marker exists
			return toReturn;
		} else {
			throw new NoMoreMarkersException();
		}
	}

	/**
	 * Rotate the current card. This method allows to rotate the current card by
	 * 90 degrees clockwise.
	 */
	public void rotateCard() {
		setChanged();
		if (!cardIsPlaced) {
			this.currentCard.rotate();
			notifyObservers(this.currentCard.clone());
		} else {
			notifyObservers(currentPlayer + ": you already placed your tile.");
		}
	}

	/**
	 * Place the current card on the board. This method allows to place the
	 * current card in a specific position on the board.
	 * 
	 * @param coordinate
	 *            Position where the current card is placed
	 */
	public void placeCurrentCardOnBoard(Coordinate coordinate) {
		setChanged();
		if (!this.cardIsPlaced) {
			try {
				lastTouchedZones = this.board.place(coordinate,
						this.currentCard);
				this.cardIsPlaced = true;
				BoardUpdate update = new BoardUpdate(currentCard.clone(),
						coordinate);
				this.lastPlacedCardCoordinate = coordinate;
				notifyObservers(update);
			} catch (InvalidPutException e) {
				notifyObservers(currentPlayer + ": Not valid coordinate.");
			}
		} else {
			notifyObservers(currentPlayer + ": you cant re-place the card.");
		}
	}

	/**
	 * Place a marker on the last card placed on the board. This method allows
	 * to place a current player's marker on the last card placed on the board
	 * in a specific direction.
	 * 
	 * @param direction
	 *            Card direction where the marker is placed
	 */
	public boolean placeMarkerOnLastPlacedCard(Direction direction) {
		setChanged();
		if (cardIsPlaced) {
			try {
				Marker toPlace = this.getMarker(currentPlayer);
				lastTouchedZones = this.board.place(toPlace,
						this.lastPlacedCardCoordinate, direction);
				currentCard.addMarker(direction, toPlace);
				BoardUpdate update = new BoardUpdate(currentCard,
						lastPlacedCardCoordinate);
				notifyObservers(update);
				return true;
			} catch (InvalidMarkerPositionException e) {
				markers.add(new Marker(currentPlayer));
				notifyObservers(currentPlayer + ": Invalid marker postion!");
				return false;
			} catch (InvalidPutException e) {
				// e.printStackTrace();
				// Very bad stuff happened
				return false;
			} catch (NoMoreMarkersException e) {
				notifyObservers(currentPlayer
						+ ": it looks like you have no more markers to place.");
				return false;
			}
		} else {
			notifyObservers(currentPlayer
					+ ": you have to place the card first.");
			return false;
		}
	}

	/**
	 * Get the next placeable card. This method allows to pick the first
	 * placeable card from the deck and set it as the current card.
	 */
	private void nextCard() {
		do {
			currentCard = deck.pick();
			if (!board.canAccept(currentCard)) {
				deck.add(currentCard);
			}
		} while (!board.canAccept(currentCard));
	}

	/**
	 * Places the first card after removing it from the deck and then returns
	 * it.
	 * 
	 * @return the first {@link Card} that is automatically placed on the board
	 */
	public Card start() {
		Card firstCard = deck.pickStarter();
		try {
			board.place(new Coordinate(0, 0), firstCard);
		} catch (InvalidPutException e) {
			// e.printStackTrace();
			// Very bad stuff happened
		}
		return firstCard;
	}

}