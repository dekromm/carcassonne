package model;

import java.io.Serializable;

/**
 * The Player class represents one player of the game. The state of the class
 * consists in the color identifier, whose value is a Color, and the score,
 * whose value is a int.
 * 
 * The class allows to get player's color and score, to add points to the score,
 * to clone the player and to get the color throw a string.
 * 
 * @author Edoardo Galimberti
 * 
 */

public class Player implements Cloneable, Serializable {

	private static final long serialVersionUID = -8918866125874629136L;
	private final PlayerColor color;
	private int score;

	/**
	 * Construct a player with the specified color and name, score is set to 0.
	 * 
	 * @param color
	 *            Player's color
	 * @param name
	 *            Player's name
	 */
	public Player(PlayerColor color) {
		this.color = color;
		this.score = 0;
	}

	/**
	 * Get player's color.
	 * 
	 * @return Player's color
	 */
	public PlayerColor getColor() {
		return this.color;
	}

	/**
	 * Get player's score.
	 * 
	 * @return Player's score
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Add points to the score.
	 * 
	 * @param add
	 *            Number of points to add
	 */
	public void addScore(int add) {
		this.score += add;
	}

	/**
	 * Creates and returns a copy of this object.
	 */
	public Player clone() {
		try {
			return (Player) super.clone();
		} catch (CloneNotSupportedException e) {
			System.err.println("Player clone() failed");
		}
		return null;
	}

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		return result;
	}

	/**
	 * Returns the {@link String} of this player's {@link PlayerColor}.
	 */
	@Override
	public String toString() {
		return color.toString();
	}

	/**
	 * Get the player color represented throw a string.
	 * 
	 * @return player color string
	 */
	public String shortName() {
		return color.letter();
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @return Equality
	 */
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
		Player other = (Player) obj;
		if (color != other.color) {
			return false;
		}
		return true;
	}
}