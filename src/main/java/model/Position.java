package model;

import model.Coordinate;

/**
 * The Position class represents positions in a 2D grid in which each cell is
 * divided in sectors. The Position class univocally represents the position of
 * a sector in a grid by using a {@link Coordinate} attribute coupled with a
 * {@link Direction} attribute.
 * 
 * 
 * @author Guido Gerosa
 * 
 */

public final class Position {
	private final Coordinate coordinate;
	private final Direction direction;

	/**
	 * The constructor creates the new {@link Position} by giving its two
	 * components.
	 * 
	 * @param coordinate
	 *            - Coordinate object to be part of the new Position
	 * @param direction
	 *            - Coordinate enum to be part of the new Position
	 */
	public Position(Coordinate coordinate, Direction direction) {
		this.coordinate = coordinate;
		this.direction = direction;
	}

	/**
	 * Returns the Coordinate object passed upon construction.
	 * 
	 * @return the Coordinate object passed upon construction
	 */
	public Coordinate getCoordinate() {
		return this.coordinate;
	}

	/**
	 * Returns the Direction enum passed upon construction.
	 * 
	 * @return the Direction enum passed upon construction
	 */
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		if (coordinate == null) {
			if (other.coordinate != null) {
				return false;
			}
		} else if (!coordinate.equals(other.coordinate)) {
			return false;
		}
		if (direction != other.direction) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return coordinate + "," + direction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinate == null) ? 0 : coordinate.hashCode());
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		return result;
	}
}
