package model;

import java.util.*;

/**
 * The Zone Class represents a city or a road through two {@link HashSet} of
 * {@link Position} objects: one representing all the contained {@link Position}
 * s and another representing every {@link Position} through which the
 * {@link Zone} can be expanded. It provides methods to allow merging of Zones
 * and calculate the score of the zone.
 * 
 * @author Guido Gerosa
 * 
 */

public class Zone {

	private Set<Position> elements = new HashSet<Position>();
	private Set<Position> perimeter = new HashSet<Position>();
	private Collection<Marker> markers = new ArrayList<Marker>();
	private Place landType;

	/**
	 * Creates a new {@link Zone} by passing a {@link Set} of {@link Position}
	 * and a {@link Place} that represents the kind of {@link Zone}. Note that
	 * {@link Position}s contained in the {@link Set} will be added to both the
	 * perimeter and the contained elements of the {@link Zone} .
	 * 
	 * @param perimeter
	 *            - a {@link Set} of {@link Position} to be considered as the
	 *            perimeter
	 * @param landType
	 *            - a {@link Place} representing the landType
	 */
	public Zone(Set<Position> perimeter, Place landType) {
		this.elements.addAll(perimeter);
		this.perimeter.addAll(perimeter);
		this.landType = landType;
	}

	/**
	 * Returns true if the Zone is closed.
	 * 
	 * @return true if the Zone is closed
	 */
	public boolean isCompleted() {
		return perimeter.isEmpty();
	}

	/**
	 * Returns true if it's possible to place a {@link Marker} in this
	 * {@link Zone}.
	 * 
	 * @return true if it's possible to place a {@link Marker} in this
	 *         {@link Zone}
	 */
	public boolean isRuled() {
		return !this.markers.isEmpty();
	}

	/**
	 * Returns true if the given {@link Position} belongs to the {@link Zone}.
	 * 
	 * @param position
	 *            - the {@link Position} to check
	 * @return true if the given {@link Position} belongs to the {@link Zone}
	 */
	public boolean perimeterContains(Position position) {
		return perimeter.contains(position);
	}

	/**
	 * This methods calculates the score of a {@link Zone} by counting the
	 * different {@link Coordinate} among its {@link Position} elements. This
	 * count is then multiplied by the coefficient given by the {@link Place}.
	 * 
	 * @return the actual score given by this {@link Zone}
	 */
	public Integer getScore() {
		Set<Coordinate> touchedTiles = new HashSet<Coordinate>();
		for (Position element : elements) {
			touchedTiles.add(element.getCoordinate());
		}
		return touchedTiles.size() * landType.value();
	}

	/**
	 * Returns a Set containing the Coordinates that this {@link Zone} covers
	 * (also the partially covered ones).
	 * 
	 * @return - a Set containing the Coordinates that this {@link Zone} covers
	 */
	public Set<Coordinate> getCoveredCoordinates() {
		Set<Coordinate> touched = new HashSet<Coordinate>();
		for (Position position : elements) {
			touched.add(position.getCoordinate());
		}
		return touched;
	}

	/**
	 * This method returns a {@link Collection} containing the {@link Player}s
	 * that have the maximum amount of {@link Marker}s in the Zone.
	 * 
	 * @return the {@link Player}s that rules the {@link Zone}
	 */
	public Collection<Player> getRulers() {
		Map<Player, Integer> counters = new HashMap<Player, Integer>();
		Integer maxMarkerCount = 0;
		for (Marker marker : markers) {
			Player player = marker.getOwner();
			if (counters.containsKey(player)) {
				Integer incrementedCount = counters.get(player) + 1;
				counters.put(player, incrementedCount);
				if (incrementedCount > maxMarkerCount) {
					maxMarkerCount = incrementedCount;
				}
			} else {
				counters.put(player, 0);
			}
		}
		Collection<Player> rulers = new ArrayList<Player>();
		for (Map.Entry<Player, Integer> counter : counters.entrySet()) {
			if (counter.getValue() == maxMarkerCount) {
				rulers.add(counter.getKey());
			}
		}
		return rulers;
	}

	/**
	 * Returns the {@link Place} type of this {@link Zone}
	 * 
	 * @return the {@link Place} type of this {@link Zone}
	 */
	public Place getPlace() {
		return landType;
	}

	/**
	 * This method unify the given {@link Zone} to the current that will get
	 * bigger (probably).
	 * 
	 * @param zone
	 *            - the {@link Zone} to be attached to the current.
	 */
	public void merge(Zone zone) {
		if (zone != this) {
			this.elements.addAll(zone.elements);
			this.perimeter.addAll(zone.perimeter);
			this.markers.addAll(zone.markers);
		}
	}

	/**
	 * Force the given {@link Marker} in the internal {@link Marker}
	 * {@link Collection}.
	 * 
	 * @param marker
	 *            - the {@link Marker} to be added.
	 */
	public void forceMarker(Marker marker) {
		this.markers.add(marker);
	}

	/**
	 * Gently tries to put a {@link Marker} on the {@link Zone}. If it's already
	 * ruled an {@link InvalidMarkerPositionException} is raised.
	 * 
	 * @param marker
	 *            - the {@link Marker} to put on the {@link Zone}
	 * @throws InvalidMarkerPositionException
	 *             - if the {@link Zone} is already ruled.
	 */
	public void addMarker(Marker marker) throws InvalidMarkerPositionException {
		if (!this.isRuled()) {
			this.markers.add(marker);
		} else {
			throw new InvalidMarkerPositionException();
		}
	}

	/**
	 * Removes {@link Position} from the perimeter of the {@link Zone}.
	 * 
	 * @param position
	 *            - the {@link Position} to be removed.
	 * @return true if successful
	 */
	public boolean perimeterRemove(Position position) {
		return perimeter.remove(position);
	}

	/**
	 * Adds the given {@link Position} to the {@link Zone}.
	 * 
	 * @param position
	 *            - the {@link Position} to be added
	 * @return true if this {@link Zone} did not already contain the specified
	 *         element
	 */
	public boolean add(Position position) {
		return elements.add(position);
	}

	/**
	 * Adds the given {@link Position} to the perimeter of the {@link Zone}.
	 * 
	 * @param position
	 *            - the {@link Position} to be added
	 * @return true if the perimeter of this {@link Zone} did not already
	 *         contain the specified element
	 */
	public boolean addToPerimeter(Position position) {
		return (elements.add(position) && perimeter.add(position));

	}

	/**
	 * Given a {@link Position} the method returns true if it's covered by this
	 * Zone
	 * 
	 * @param position
	 *            - the position whose coverage is checked
	 * @return true if the given {@link Position} is covered
	 */
	public boolean covers(Position position) {
		return elements.contains(position);
	}

	/**
	 * Sets the {@link Place} of this {@link Zone}
	 * 
	 * @param landType
	 *            - the {@link Place} to set
	 */
	public void setPlace(Place landType) {
		this.landType = landType;
	}

	/**
	 * This method returns a {@link Collection} containing all the markers on
	 * this {@link Zone} and then removes them.
	 * 
	 * @return a {@link Collection} containing all the markers on this
	 *         {@link Zone}
	 */
	public Collection<Marker> removeMarkers() {
		Collection<Marker> toReturn = markers;
		markers = new ArrayList<Marker>();
		return toReturn;
	}

	/**
	 * Returns a string containing all the positions covered.
	 */
	@Override
	public String toString() {
		return "Zone [elements=" + elements + "]";
	}

}
