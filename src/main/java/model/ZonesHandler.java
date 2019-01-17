package model;

import java.util.*;

/**
 * The ZonesMonitor Class is meant to be used in a {@link Board} to keep track
 * of the {@link Zone} that are formed by adding new {@link Card}s to the
 * {@link Board}.
 * 
 * @author Guido Gerosa
 * 
 */
public class ZonesHandler {

	private Collection<Zone> completedZones = new ArrayList<Zone>();
	private Collection<Zone> uncompletedZones = new ArrayList<Zone>();

	/**
	 * This method returns the {@link Zone} containing the {@link Position}
	 * given, if any. If there's no {@link Zone} in this {@link ZonesHandler}
	 * containing the specified {@link Position} an InvalidPositionException is
	 * raised.
	 * 
	 * @param position
	 *            - the {@link Position} to look for
	 * @return the {@link Zone} containing the {@link Position}
	 * @throws InvalidPositionException
	 *             - if the {@link Position} could not be found in any
	 *             {@link Zone}
	 */
	public Zone getZoneHaving(Position position)
			throws InvalidPositionException {
		for (Zone zone : uncompletedZones) {
			if (zone.covers(position)) {
				return zone;
			}
		}
		for (Zone zone : completedZones) {
			if (zone.covers(position)) {
				return zone;
			}
		}
		throw new InvalidPositionException();
	}

	/**
	 * This method updates the state of the zones inside the zoneMonitor by
	 * adding new elements to touched zones and by adjusting their perimeters.
	 * This method returns a {@link Map} containing zones that got modified
	 * indexed by the {@link Direction}.
	 * 
	 * @param coordinate
	 *            Coordinate on which the zoneMonitor has to operate
	 * @param tile
	 *            Card that the zoneMonitor is going to use to update its state
	 * @return A Map containing all the {@link Zone} that got modified
	 */
	public Map<Direction, Zone> update(Coordinate coordinate, Card tile) {
		Map<Direction, Zone> touchedZones = new HashMap<Direction, Zone>();
		for (Position toBeRemoved : coordinate.getContiguousPositions()) {
			Zone toBeModified;
			try {
				toBeModified = getZoneHaving(toBeRemoved);
				toBeModified.perimeterRemove(toBeRemoved);
				toBeModified.add(new Position(coordinate, toBeRemoved
						.getDirection().opposite()));
				touchedZones.put(toBeRemoved.getDirection().opposite(),
						toBeModified);
			} catch (InvalidPositionException e) {
				Place newZonePlace = tile.getPlace(toBeRemoved.getDirection()
						.opposite());
				if (newZonePlace != Place.NOTHING) {
					Set<Position> newZoneElements = new HashSet<Position>();
					newZoneElements.add(new Position(coordinate, toBeRemoved
							.getDirection().opposite()));
					Zone newZone = new Zone(newZoneElements, newZonePlace);
					uncompletedZones.add(newZone);
					touchedZones.put(toBeRemoved.getDirection().opposite(),
							newZone);
				}

			}

		}

		// If the zone is over the coordinates it's mapped as NORTH
		// At this point every touched zone has had his touched "edge" (a
		// Position) removed from the perimeter.
		// Now the zones merging and perimeter adjusting takes place.
		LinkedList<Direction> touchedZonesDirections = new LinkedList<Direction>(
				touchedZones.keySet());
		Map<Direction, Zone> modifiedZones = new HashMap<Direction, Zone>();
		while (!touchedZonesDirections.isEmpty()) {
			Direction currentDirection = touchedZonesDirections.remove();
			Zone mergingZone = touchedZones.remove(currentDirection);
			modifiedZones.put(currentDirection, mergingZone);
			for (Direction direction : tile.cross(currentDirection)) {
				Zone toBeMerged = touchedZones.remove(direction);
				if (toBeMerged != null) {
					mergingZone.merge(toBeMerged);// zones merging
					modifiedZones.put(direction, mergingZone);
					uncompletedZones.remove(toBeMerged);
					touchedZonesDirections.remove(direction);
				} else {
					mergingZone.addToPerimeter(new Position(coordinate,
							direction));// perimeter adjusting
				}
			}
			if (mergingZone.isCompleted()) {
				completedZones.add(mergingZone);
			}
		}
		return modifiedZones;
	}

	public Collection<Zone> getUncompletedZones() {
		return uncompletedZones;
	}

}
