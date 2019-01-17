package model;

import java.io.Serializable;

/**
 * The Marker class represents one of the seven markers that every player owns.
 * The state of the class consists only in the the player owner.
 * 
 * The class allows to get the player owner.
 * 
 * @author Edoardo Galimberti
 * 
 */

public class Marker implements Serializable {

	private static final long serialVersionUID = -7429898136879360820L;
	private final Player playerOwner;

	/**
	 * Construct a marker whit the specified player owner.
	 * 
	 * @param playerOwner
	 *            Player owner of the marker
	 */
	public Marker(Player playerOwner) {
		this.playerOwner = playerOwner;
	}

	/**
	 * Get the player owner.
	 * 
	 * @return Player owner
	 */
	public Player getOwner() {
		return this.playerOwner;
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
		result = prime * result
				+ ((playerOwner == null) ? 0 : playerOwner.hashCode());
		return result;
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
		Marker other = (Marker) obj;
		if (playerOwner == null) {
			if (other.playerOwner != null) {
				return false;
			}
		} else if (!playerOwner.equals(other.playerOwner)) {
			return false;
		}
		return true;
	}
}