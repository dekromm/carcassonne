package model;

import java.util.*;

import model.Coordinate;

/**
 * This class represents the game table in a Carcassonne Match. It provides
 * methods to place new {@link Card} objects in given {@link Coordinate} and
 * updates the players score.
 * 
 * @author Guido Gerosa
 * 
 */
public class Board {

	private Map<Coordinate, Card> grid;
	private ZonesHandler zonesHandler;
	private Map<Direction, Zone> touchedZones = null;
	private Set<Coordinate> openings;
	static final int START_X = 0;
	static final int START_Y = 0;

	/**
	 * The Constructor places the tile given in the (0,0) coordinate.
	 * 
	 * @param firstTile
	 *            - the {@link Card} to start with
	 * @throws NoCrossInCardException
	 */
	public Board() {
		zonesHandler = new ZonesHandler();
		grid = new HashMap<Coordinate, Card>();
		openings = new HashSet<Coordinate>();
		openings.add(new Coordinate(0, 0));
	}

	/**
	 * This method returns true if it is possible to place the given
	 * {@link Card} at the given {@link Coordinate}
	 * 
	 * @param coordinate
	 *            - the {@link Coordinate} to check
	 * @param tile
	 *            - the {@link Card} to check
	 * @return true if the tile is placeable
	 */
	private boolean isPlaceable(Coordinate coordinate, Card tile) {
		if (openings.contains(coordinate)) {
			for (Direction direction : Direction.values()) {
				if (grid.get(coordinate.go(direction)) != null
						&& !tile.getPlace(direction).equals(
								grid.get(coordinate.go(direction)).getPlace(
										direction.opposite()))) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method returns true if the given {@link Card} can be put on the
	 * {@link Board}.
	 * 
	 * @param tile
	 *            - the {@link Card} to check for "putability"
	 * @return true if the given {@link Card} can be put on the {@link Board}
	 */
	public boolean canAccept(Card tile) {
		for (Coordinate coordinate : openings) {
			for (int i = 0; i < Direction.values().length; i++) {
				if (isPlaceable(coordinate, tile)) {
					return true;
				}
				tile.rotate();
			}
		}
		return false;
	}

	/**
	 * Returns a {@link Collection} containing the last modified {@link Zone}s.
	 * 
	 * @return a {@link Collection} containing the last modified {@link Zone}s
	 */
	public Collection<Zone> getModifiedZones() {
		Collection<Zone> result = new ArrayList<Zone>();
		// returns the touched zones without duplicates.
		for (Zone zone : touchedZones.values()) {
			if (!result.contains(zone)) {
				result.add(zone);
			}
		}
		return result;
	}

	/**
	 * This method tries to put the given {@link Card} at the given
	 * {@link Coordinate} using this class' isPlaceable() method. If the put is
	 * illegal an {@link InvalidPutException} is raised. Whenever a {@link Card}
	 * is successfully put the given coordinates are stored in a cache
	 * attribute.
	 * 
	 * @param coordinate
	 *            - the {@link Coordinate} at which put the give {@link Card}
	 * @param tile
	 *            - the {@link Card} to put in the grid
	 * @return the {@link Card} that was previously positioned at the given
	 *         {@link Coordinate}
	 * @throws InvalidPutException
	 *             - if the put is illegal
	 */
	private Card put(Coordinate coordinate, Card tile)
			throws InvalidPutException {
		if (this.isPlaceable(coordinate, tile)) {
			openings.remove(coordinate);
			for (Coordinate toAdd : coordinate.getNeighbours()) {
				if (!grid.keySet().contains(toAdd)) {
					openings.add(toAdd);
				}
			}
			return grid.put(coordinate, tile);
		} else {
			throw new InvalidPutException();
		}
	}

	/**
	 * This method tries to place the given {@link Marker} in the
	 * {@link Position} formed by the {@link Coordinate} used for the last
	 * successful put() and the given {@link Direction}. If the place fails an
	 * {@link InvalidMarkerPositionException} is raised.
	 * 
	 * @param marker
	 *            - the {@link Marker} to be added to the
	 * @param direction
	 *            - the {@link Direction} on which the {@link Marker} will be
	 *            put
	 * @param direction
	 *            - the {@link Coordinate} at which the {@link Marker} will be
	 *            put
	 * @throws InvalidMarkerPositionException
	 *             - if the place is illegal
	 */
	public Collection<Zone> place(Marker marker, Coordinate coordinate,
			Direction direction) throws InvalidMarkerPositionException,
			InvalidPutException {
		Zone toRule = touchedZones.get(direction);
		if (toRule == null) {
			throw new InvalidMarkerPositionException();
		} else {
			toRule.addMarker(marker);
		}
		grid.get(coordinate).addMarker(direction, marker);
		return getModifiedZones();
	}

	/**
	 * This method allow to store a given {@link Card} in the given
	 * {@link Coordinate}. An {@link InvalidPutException} is raised if the edges
	 * of the given {@link Card} don't match with its neighbours.
	 * 
	 * @param coordinate
	 *            - the {@link Coordinate} in which put the given{@link Card}
	 * @param tile
	 *            - the {@link Card} to put in the grid
	 * @return a {@link Collection} containing all the {@link Zone} that got
	 *         extended by placing the tile.
	 * @throws InvalidPutException
	 *             : if the put is illegal.
	 * @throws NoCrossInCardException
	 */
	public Collection<Zone> place(Coordinate coordinate, Card tile)
			throws InvalidPutException {
		this.put(coordinate, tile);
		touchedZones = zonesHandler.update(coordinate, tile);
		return getModifiedZones();
	}

	/**
	 * Given a {@link Zone} this methods removes the markers from it and from
	 * the {@link Card}s covered by it and put them in the specified
	 * {@link Marker} {@link Collection}.
	 * 
	 * @param zone
	 *            - the {@link Zone}
	 * @param markers
	 *            - the {@link Collection} to which the removed markers will be
	 *            added
	 * @return - a {@link Map} containing, for each Coordinate corresponding to
	 *         a ruled {@link Card}, the {@link Card} without marker.
	 */
	public Map<Coordinate, Card> removeMarkers(Zone zone,
			Collection<Marker> markers) {
		Map<Coordinate, Card> changedTiles = new HashMap<Coordinate, Card>();
		markers.addAll(zone.removeMarkers());
		for (Coordinate coordinate : zone.getCoveredCoordinates()) {
			Card tileToStrip = grid.get(coordinate);
			try {
				if (zone.covers(new Position(coordinate, tileToStrip
						.getMarkerPosition()))) {
					tileToStrip.removeMarker();
					changedTiles.put(coordinate, tileToStrip);
				}
			} catch (NoMarkerOnCardException e) {
				// the tile has no marker and hence is not modified.
			}
		}
		return changedTiles;
	}

	/**
	 * Returns a Collection containing the zones that haven't been closed yet
	 * 
	 * @return a Collection containing the zones that haven't been closed yet
	 */
	public Collection<Zone> getUncompletedZones() {
		return zonesHandler.getUncompletedZones();
	}
}
