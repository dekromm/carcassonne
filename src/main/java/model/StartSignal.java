package model;

import java.io.Serializable;

/**
 * This class is used to tell to the views in which match they are playing,
 * which player they represent, how many players are in the match, and which is
 * the {@link Card} they have to show in the starting coordinates (0,0).
 * 
 * This class allows to get the player, to get the first card, to get the match
 * name and to get the players number.
 * 
 * @author Edoardo Galimberti, Guido Gerosa
 */
public class StartSignal implements Serializable {

	private static final long serialVersionUID = -4521739900514859003L;
	private final Card starterCard;
	private final String gameName;
	private final Player player;
	private final int playersNumber;

	/**
	 * 
	 * @param starterCard
	 *            - the {@link Card} to store
	 * @param gameName
	 *            - the {@link String} representing the game name
	 * @param player
	 *            - the {@link Player} to store
	 * @param playersNumber
	 *            - the number of {@link Player}s in the match
	 */
	public StartSignal(Card starterCard, String gameName, Player player,
			int playersNumber) {
		this.starterCard = starterCard;
		this.gameName = gameName;
		this.player = player;
		this.playersNumber = playersNumber;
	}

	/**
	 * Get the player.
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Get the first card of the game.
	 * 
	 * @return the starterCard
	 */
	public Card getStarterCard() {
		return starterCard;
	}

	/**
	 * Get the match name.
	 * 
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * Get the players number.
	 * 
	 * @return the playersNumber
	 */
	public int getPlayersNumber() {
		return playersNumber;
	}

}
