package view.text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.*;
import view.TextualCarcassonneView;

;
/**
 * This class holds the representation of a Card in a textual enviroment. It
 * offers a method to retreive a {@link Direction} given the string
 * representationg of a road ora a city and a method to get the char matrix
 * repersenting the {@link Card}
 * 
 * @author Guido Gerosa
 * 
 */
public class TextualCard {

	private Map<Direction, String> representations = new HashMap<Direction, String>();

	/**
	 * Given a {@link Card} a textual representation of it is built.
	 * 
	 * @param tile
	 *            - the {@link Card} whose representation has to be built
	 */
	public TextualCard(Card tile) {
		if (tile == null) {
			for (Direction direction : Direction.values()) {
				representations.put(direction, "");
			}
		} else {
			LinkedList<Direction> directionStack = new LinkedList<Direction>(
					Arrays.asList(Direction.values()));
			Map<Place, Integer> indexes = new HashMap<Place, Integer>();
			// store representation of each edge in a Map.
			for (Place place : Place.values()) {// indexes initialization
				indexes.put(place, 1);
			}
			while (!directionStack.isEmpty()) {
				Direction current = directionStack.remove();
				Place currentPlace = tile.getPlace(current);
				if (currentPlace == Place.NOTHING) {
					representations.put(current, "");
				} else {
					representations.put(current, currentPlace.firstLetter()
							+ indexes.get(currentPlace));
					for (Direction connected : tile.cross(current)) {
						directionStack.remove(connected);
						representations.put(
								connected,
								currentPlace.firstLetter()
										+ indexes.get(currentPlace));
					}
					indexes.put(currentPlace, indexes.get(currentPlace) + 1);
				}
			}
			try {
				Direction markerPosition = tile.getMarkerPosition();
				String markerString = representations.get(markerPosition) + "("
						+ tile.getMarker().getOwner().shortName() + ")";
				representations.put(markerPosition, markerString);
			} catch (NoMarkerOnCardException e) {
			}
		}
	}

	/**
	 * Returns the char matrix representing this tile that'll be large.
	 * 
	 * @param width
	 * @param height
	 * @return the char matrix representation
	 */
	public char[][] getMatrix(int width, int height) {
		char[][] tileMatrix = new char[height][width];
		tileMatrix = CharMatrixTools.fill(tileMatrix,
				TextualCarcassonneView.WHITESPACE);
		tileMatrix = CharMatrixTools.blit(representations.get(Direction.NORTH)
				.toCharArray(), tileMatrix, 0, width / 2);
		tileMatrix = CharMatrixTools.blit(representations.get(Direction.WEST)
				.toCharArray(), tileMatrix, height / 2, 0);
		int offset = representations.get(Direction.EAST).toCharArray().length;
		tileMatrix = CharMatrixTools.blit(representations.get(Direction.EAST)
				.toCharArray(), tileMatrix, height / 2, width - offset);
		tileMatrix = CharMatrixTools.blit(representations.get(Direction.SOUTH)
				.toCharArray(), tileMatrix, height - 1, width / 2);
		return tileMatrix;
	}

	/**
	 * Returns the {@link Direction} corresponding to the given place
	 * representation (such as S1, C2, etc.) or null if no representation has
	 * been found
	 * 
	 * @param place
	 *            - the {@link String} representing the place
	 * @return the {@link Direction} whose representetion corerspond to the
	 *         given
	 */
	public Direction getEdge(String place) {
		Direction toReturn = null;
		if (!place.equals("")) {
			for (Map.Entry<Direction, String> representation : representations
					.entrySet()) {
				if (representation.getValue().equalsIgnoreCase(place)) {
					toReturn = representation.getKey();
					break;
				}
			}
		}
		return toReturn;
	}
}
