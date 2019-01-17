package model;

import java.io.Serializable;

/**
 * This class contains a {@link Card} and a {@link Coordinate} and represents an
 * element of a {@link Board}
 * 
 * @author Guido Gerosa
 * 
 */
public class BoardUpdate implements Serializable {

	private static final long serialVersionUID = 5318332397461518337L;
	private final Card updatedCard;
	private final Coordinate where;

	/**
	 * Since this class has a {@link Card} and a {@link Coordinate} those
	 * attributes are set by the contructor
	 * 
	 * @param updatedCard
	 *            - the {@link Card} to register
	 * @param where
	 *            - the {@link Coordinate} to register
	 */
	public BoardUpdate(Card updatedCard, Coordinate where) {
		this.updatedCard = updatedCard;
		this.where = where;
	}

	/**
	 * Returns the registered {@link Card}
	 * 
	 * @return the registered {@link Card}
	 */
	public Card getUpdatedCard() {
		return updatedCard;
	}

	/**
	 * Returns the registered {@link Coordinate}
	 * 
	 * @return the registered {@link Coordinate}
	 */
	public Coordinate getWhere() {
		return where;
	}
}
