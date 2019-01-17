package model;

/**
 * The Place enum represents the possible place on the card. The places are:
 * city, street or nothing.
 * 
 * This class allows to get the relatives value, string and first letter.
 * 
 * @author Edoardo Galimberti
 * 
 */

public enum Place {
	CITY, STREET, NOTHING;

	/**
	 * Returns the score of a tile of this type.
	 * 
	 * @return the score value of a tile of this type.
	 */
	public int value() {
		switch (this) {
		case CITY:
			return 2;
		case STREET:
			return 1;
		default:
			return 0;
		}
	}

	/**
	 * Returns the Place corresponding to the given char. the mapping is: 'C'
	 * for CITY 'S' for STREET 'N' for NOTHING
	 * 
	 * @param c
	 *            - the char representation
	 * @return the {@link Place} corresponding to the given char
	 */
	public static Place read(char c) {
		switch (c) {
		case 'N':
			return Place.NOTHING;
		case 'C':
			return Place.CITY;
		case 'S':
			return Place.STREET;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a {@link String} containing the first letter.
	 */
	public String firstLetter() {
		switch (this) {
		case NOTHING:
			return "N";
		case CITY:
			return "C";
		case STREET:
			return "S";
		default:
			throw new IllegalArgumentException();
		}
	}

}
