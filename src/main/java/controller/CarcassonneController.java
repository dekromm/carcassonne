package controller;

import java.util.*;
import model.*;

/**
 * This Class contains and defines all the common methods that should be
 * implemented and used to manage user's input (through {@link Observable}) and
 * the model (through {@link CarcassonneModel}).
 * 
 * @author Galimberti Edoardo, Guido Gerosa
 * 
 */
public class CarcassonneController implements Observer {

	private CarcassonneModel model;

	/**
	 * The controller created will call the methods of the given
	 * {@link CarcassonneModel}.
	 * 
	 * @param model
	 *            - the {@link CarcassonneModel} to control
	 */
	public CarcassonneController(CarcassonneModel model) {
		this.model = model;
	}

	/**
	 * Receives updates and interprets them in order to modify the model
	 * accordingly.
	 */
	public void update(Observable o, Object arg) {
		if (arg instanceof String && ((String) arg).equalsIgnoreCase("rotate")) {
			model.rotateCard();
		} else if (arg instanceof Coordinate) {
			model.placeCurrentCardOnBoard((Coordinate) arg);
		} else if (arg instanceof Direction) {
			if (model.placeMarkerOnLastPlacedCard((Direction) arg)) {
				model.nextTurn();
			}
		} else if (arg instanceof String
				&& ((String) arg).compareTo("pass") == 0) {
			model.nextTurn();
		}
	}

}