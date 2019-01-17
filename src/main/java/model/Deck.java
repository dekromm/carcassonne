package model;

import java.io.*;
import java.util.*;

/**
 * The Deck class represents the deck of cards used during the game. The state
 * of this class consists in an ArrayList of cards.
 * 
 * The class allows to pick the specific first card of the game, to get the size
 * of the deck, to pick a card and to put a card in the deck.
 * 
 * @author Edoardo Galimberti
 * 
 */

public class Deck {

	private List<Card> cards;

	/**
	 * Construct the deck reading the file "carcassone.txt" and constructing
	 * each card.
	 * 
	 * @throws IOException
	 */
	public Deck() {
		// variables and input file initialization
		this.cards = new LinkedList<Card>();
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(
					"src/main/resources/carcassonne.txt"));
			String newLine;
			newLine = input.readLine();
			while (newLine != null) {
				this.cards.add(new Card(newLine));
				newLine = input.readLine();
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("File can't be red.");
		} catch (IOException e) {
			System.err.println("File error.");
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				System.err.println("File can't be closed.");
			}
		}
		this.shuffle();
	}

	/**
	 * Pick the specific first card of the game. This method allows to pick form
	 * the deck the first card that is needed to start the game.
	 * 
	 * @return First card of the game
	 */
	public Card pickStarter() {
		return new Card("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
	}

	/**
	 * Shuffle the deck.
	 */
	private void shuffle() {
		Collections.shuffle(this.cards);
	}

	/**
	 * Pick a card from the deck. Pick the first card of the deck and delete
	 * that card from the deck.
	 * 
	 * @return Card picked
	 */
	public Card pick() {
		Card temp = this.cards.get(0);
		this.cards.remove(0);
		return temp;
	}

	/**
	 * Add a card to the deck. Add a card to the deck at the last place.
	 * 
	 * @param discarded
	 *            Card added to the deck
	 */
	public void add(Card discarded) {
		this.cards.add(discarded);
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
		result = prime * result + ((cards == null) ? 0 : cards.hashCode());
		return result;
	}

	/**
	 * Returns the number of cards left in this deck.
	 * 
	 * @return the number of cards left in this deck
	 */
	public int size() {
		return cards.size();
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
		Deck other = (Deck) obj;
		if (cards == null) {
			if (other.cards != null) {
				return false;
			}
		} else if (!cards.equals(other.cards)) {
			return false;
		}
		return true;
	}
}