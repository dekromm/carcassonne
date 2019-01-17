package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Card class represents a single card in the game. The state of the class
 * consists in the four cardinal points which memorize what is found for each
 * side of the card, whose value is a Place, the connections between each side,
 * whose value is a boolean (1 for connection, 0 for no connection), the
 * reference of the possible marker and its position, whose value is a
 * Direction, and the string representation.
 * 
 * The class allows to rotate the card of 90 degrees clockwise, to add a marker,
 * to get the marker added and its position, to delete the marker, to get the
 * place of one side, to get the connected sides whit a given side, to clone the
 * card and to get the string representation.
 * 
 * @author Edoardo Galimberti
 * 
 */

public class Card implements Cloneable, Serializable {

	private static final long serialVersionUID = -1252734291919627633L;
	private String stringID;
	private Place n, s, w, e;
	private Direction markerPosition;
	private boolean ns, ne, nw, we, se, sw;
	private Marker markerOn;

	/**
	 * Construct a card with the specified place for each cardinal point and
	 * their relative connections.
	 * 
	 * @param n
	 *            Place in North
	 * @param s
	 *            Place in South
	 * @param w
	 *            Place in West
	 * @param e
	 *            Place in East
	 * @param ns
	 *            Connection between North and South
	 * @param ne
	 *            Connection between North and East
	 * @param nw
	 *            Connection between North and West
	 * @param we
	 *            Connection between West and East
	 * @param se
	 *            Connection between South and East
	 * @param sw
	 *            Connection between South and West
	 */

	public Card(Place n, Place s, Place w, Place e, boolean ns, boolean ne,
			boolean nw, boolean we, boolean se, boolean sw) {
		this.n = n;
		this.s = s;
		this.w = w;
		this.e = e;

		this.ns = ns;
		this.ne = ne;
		this.nw = nw;
		this.we = we;
		this.se = se;
		this.sw = sw;

		this.markerPosition = null;
		this.markerOn = null;
		this.stringID = null;
	}

	/**
	 * Construct a card from a string in a specific format.
	 * 
	 * @param string
	 *            String in the specific format
	 */
	public Card(String string) {
		// String Example with positions:
		// 000000000011111111112222222222333333333344444
		// 012345678901234567890123456789012345678901234
		// N=N S=S W=S E=C NS=0 NE=1 NW=0 WE=0 SE=0 SW=0
		this.stringID = string;
		this.n = Place.read(string.charAt(2));
		this.s = Place.read(string.charAt(6));
		this.w = Place.read(string.charAt(10));
		this.e = Place.read(string.charAt(14));
		this.ns = (string.charAt(19) == '1');
		this.ne = (string.charAt(24) == '1');
		this.nw = (string.charAt(29) == '1');
		this.we = (string.charAt(34) == '1');
		this.se = (string.charAt(39) == '1');
		this.sw = (string.charAt(44) == '1');
	}

	/**
	 * Get the string representation of the card.
	 * 
	 * @return stringID
	 */
	public String getStringID() {
		return stringID;
	}

	/**
	 * Rotate the card. This method allows to rotate card's cardinal points of
	 * 90° clockwise and their related connections.
	 */
	public void rotate() {
		Place tempChar;
		boolean tempBoolean;

		tempChar = n;
		n = w;
		w = s;
		s = e;
		e = tempChar;

		tempBoolean = ns;
		ns = we;
		we = tempBoolean;

		tempBoolean = ne;
		ne = nw;
		nw = sw;
		sw = se;
		se = tempBoolean;
	}

	/**
	 * Add a marker on the card. This method allows to add marker in a specified
	 * cardinal points on the card.
	 * 
	 * @param markerPosition
	 *            Marker position on the card
	 * @param markerOn
	 *            Marker reference
	 */
	public void addMarker(Direction markerPosition, Marker markerOn) {
		this.markerPosition = markerPosition;
		this.markerOn = markerOn;
	}

	/**
	 * Remove the marker from the card.
	 */
	public void removeMarker() {
		this.markerPosition = null;
		this.markerOn = null;
	}

	/**
	 * Get the marker on the card.
	 * 
	 * @return Marker reference
	 * @throws NoMarkerOnCardException
	 */
	public Marker getMarker() throws NoMarkerOnCardException {
		if (this.markerOn != null) {
			return this.markerOn;
		} else {
			throw new NoMarkerOnCardException();
		}
	}

	/**
	 * Get the marker position on the card.
	 * 
	 * @return Marker position
	 * @throws NoMarkerOnCardException
	 */
	public Direction getMarkerPosition() throws NoMarkerOnCardException {
		if (this.markerPosition != null) {
			return this.markerPosition;
		} else {
			throw new NoMarkerOnCardException();
		}
	}

	/**
	 * Get the place in a direction. This method allows to get the place in a
	 * specific direction of the card.
	 * 
	 * @param direction
	 *            Direction
	 * @return Place
	 */
	public Place getPlace(Direction direction) {
		if (direction == Direction.NORTH) {
			return n;
		}
		if (direction == Direction.SOUTH) {
			return s;
		}
		if (direction == Direction.WEST) {
			return w;
		}
		if (direction == Direction.EAST) {
			return e;
		}
		return null;
	}

	/**
	 * Get the connection between a given direction and the others. This method
	 * allows to get the possible connections between a direction given and all
	 * the others.
	 * 
	 * @param in
	 *            Given direction
	 * @return Connected directions
	 * @throws NoCrossInCardException
	 */
	public List<Direction> cross(Direction in) {
		// ArrayList initialization
		ArrayList<Direction> outs = new ArrayList<Direction>();
		// case NORTH
		if (in == Direction.NORTH) {
			// case N
			if (this.n == Place.NOTHING) {
				return outs;
			}
			// case C or S
			else {
				if (this.ns) {
					outs.add(Direction.SOUTH);
				}
				if (this.nw) {
					outs.add(Direction.WEST);
				}
				if (this.ne) {
					outs.add(Direction.EAST);
				}
			}
			return outs;
		}
		// case SOUTH
		if (in == Direction.SOUTH) {
			// case N
			if (this.s == Place.NOTHING) {
				return outs;
			}
			// case C or S
			else {
				if (this.ns) {
					outs.add(Direction.NORTH);
				}
				if (this.sw) {
					outs.add(Direction.WEST);
				}
				if (this.se) {
					outs.add(Direction.EAST);
				}
			}
			return outs;
		}
		// case WEST
		if (in == Direction.WEST) {
			// case N
			if (this.w == Place.NOTHING) {
				return outs;
			}
			// case C or S
			else {
				if (this.nw) {
					outs.add(Direction.NORTH);
				}
				if (this.sw) {
					outs.add(Direction.SOUTH);
				}
				if (this.we) {
					outs.add(Direction.EAST);
				}
			}
			return outs;
		}
		// case EAST
		if (in == Direction.EAST) {
			// case N
			if (this.e == Place.NOTHING) {
				return outs;
			}
			// case C or S
			else {
				if (this.ne) {
					outs.add(Direction.NORTH);
				}
				if (this.we) {
					outs.add(Direction.WEST);
				}
				if (this.se) {
					outs.add(Direction.SOUTH);
				}
			}
			return outs;
		}
		// case error
		return outs;
	}

	/**
	 * Creates and returns a copy of this object.
	 */
	public Card clone() {
		try {
			return (Card) super.clone();
		} catch (CloneNotSupportedException e) {
			System.err.println("Card clone() failed.");
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
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((n == null) ? 0 : n.hashCode());
		result = prime * result + (ne ? 1231 : 1237);
		result = prime * result + (ns ? 1231 : 1237);
		result = prime * result + (nw ? 1231 : 1237);
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		result = prime * result + (se ? 1231 : 1237);
		result = prime * result + (sw ? 1231 : 1237);
		result = prime * result + ((w == null) ? 0 : w.hashCode());
		result = prime * result + (we ? 1231 : 1237);
		result = prime * result
				+ ((markerOn == null) ? 0 : markerOn.hashCode());
		result = prime * result
				+ ((markerPosition == null) ? 0 : markerPosition.hashCode());
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
		Card other = (Card) obj;
		if (e != other.e) {
			return false;
		}
		if (n != other.n) {
			return false;
		}
		if (ne != other.ne) {
			return false;
		}
		if (ns != other.ns) {
			return false;
		}
		if (nw != other.nw) {
			return false;
		}
		if (s != other.s) {
			return false;
		}
		if (se != other.se) {
			return false;
		}
		if (sw != other.sw) {
			return false;
		}
		if (w != other.w) {
			return false;
		}
		if (we != other.we) {
			return false;
		}
		if (markerOn == null) {
			if (other.markerOn != null) {
				return false;
			}
		} else if (!markerOn.equals(other.markerOn)) {
			return false;
		}
		if (markerPosition != other.markerPosition) {
			return false;
		}
		return true;
	}
}