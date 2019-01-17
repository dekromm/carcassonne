package model;

import java.util.*;

/**
 * The Direction enum provide representations for the 4 cardinal points. It also
 * provides methods to get the opposite Direction and the next Direction in a
 * clockwise rotation.
 * 
 * @author Guido Gerosa
 * @version 2.0
 * 
 */
public enum Direction {
	NORTH, SOUTH, EAST, WEST;
	/**
	 * Get the enum representing the opposite Direction of the current instance.
	 * 
	 * @return the enum representing the opposite of the current Direction
	 */
	public Direction opposite() {
		switch (this) {
		case NORTH:
			return Direction.SOUTH;
		case SOUTH:
			return Direction.NORTH;
		case WEST:
			return Direction.EAST;
		case EAST:
			return Direction.WEST;
		default:
			return null;
		}
	}

	/**
	 * Get the enum representing the next Direction of the current instance in a
	 * clockwise rotation
	 * 
	 * @return the enum representing the next Direction in a clockwise rotation
	 */
	public Direction clockWise() {
		switch (this) {
		case NORTH:
			return Direction.EAST;
		case SOUTH:
			return Direction.WEST;
		case WEST:
			return Direction.NORTH;
		case EAST:
			return Direction.SOUTH;
		default:
			return null;
		}
	}

	/**
	 * This method checks whether the current {@link Direction} is NORTH or
	 * SOUTH
	 * 
	 * @return true if the current {@link Direction} is vertical
	 */
	public boolean isVertical() {
		return (this == Direction.NORTH || this == Direction.SOUTH);
	}

	/**
	 * This method checks whether the current {@link Direction} is WEST or EAST
	 * 
	 * @return true if the current {@link Direction} is horizontal
	 */
	public boolean isHorizontal() {
		return (this == Direction.WEST || this == Direction.EAST);
	}

	/**
	 * Returns a String containing the uppercase initial letter of the current
	 * {@link Direction}.
	 * 
	 * @return a String containing the uppercase initial letter of the current
	 *         {@link Direction}
	 */
	public String toString() {
		switch (this) {
		case NORTH:
			return "N";
		case SOUTH:
			return "S";
		case WEST:
			return "W";
		case EAST:
			return "E";
		default:
			return "";
		}
	}

	/**
	 * This method returns a {@link Queue} containing all possible values for
	 * {@link Direction}
	 * 
	 * @return a {@link Queue} containing all possible values for
	 *         {@link Direction}
	 */
	public static Queue<Direction> valuesQueue() {
		LinkedList<Direction> valuesQueue = new LinkedList<Direction>();
		for (Direction direction : Direction.values()) {
			valuesQueue.add(direction);
		}
		return valuesQueue;
	}

}
