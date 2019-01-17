package model;

import java.io.Serializable;
import java.util.*;

import model.Direction;

/**
 * The immutable Coordinate class represents positions in a 2D grid through 2
 * integer values. It allows to request 1 of the 4 contiguous coordinates by
 * giving a {@link Direction} or a {@link Collection} containing all of them.
 * 
 * @author Guido Gerosa
 * 
 */

public final class Coordinate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4027510495727884476L;
	private final int x;
	private final int y;

	/**
	 * This is too obvious to be explained.
	 * 
	 * @param x
	 * @param y
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * This method returns the closest {@link Coordinate} in the
	 * {@link Direction} given.
	 * 
	 * @param direction
	 *            - {@link Direction} to go towards
	 * @return the next {@link Coordinate} when going in the given
	 *         {@link Direction}
	 */
	public Coordinate go(Direction direction) {
		switch (direction) {
		case NORTH:
			return new Coordinate(x, y + 1);
		case SOUTH:
			return new Coordinate(x, y - 1);
		case WEST:
			return new Coordinate(x - 1, y);
		case EAST:
			return new Coordinate(x + 1, y);
		default:
			return null;
		}
	}

	/**
	 * This method returns a {@link Collection} containing all of the 4
	 * {@link Position} elements touching this {@link Coordinate}
	 * 
	 * @return a {@link Collection} containing all of the 4 {@link Position}
	 *         elements touching this {@link Coordinate}
	 */
	public Collection<Position> getContiguousPositions() {
		Collection<Position> contiguous = new ArrayList<Position>();
		for (Direction direction : Direction.values()) {
			contiguous.add(new Position(this.go(direction), direction
					.opposite()));
		}
		return contiguous;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
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
		Coordinate other = (Coordinate) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	/**
	 * Returns a {@link Collection} containing the {@link Coordinate}s that are
	 * contiguous to this one.
	 * 
	 * @return a {@link Collection} containing the {@link Coordinate}s that are
	 *         contiguous to this one
	 */
	public Collection<Coordinate> getNeighbours() {
		Collection<Coordinate> result = new ArrayList<Coordinate>();
		for (Direction direction : Direction.values()) {
			result.add(this.go(direction));
		}
		return result;
	}
}
