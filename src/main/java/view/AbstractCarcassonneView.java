package view;

import java.util.Observable;
import java.util.Observer;

/**
 * This abstract class represents a generic View that's able to observe a
 * carcassonne model and can be observed by a carcassonne controller.
 * 
 * @author Guido Gerosa
 * 
 */
public abstract class AbstractCarcassonneView extends Observable implements
		Observer {

	/**
	 * This method is meant to be called every time an observable carcassonne
	 * model sends updates through notifyObservers()
	 */
	public abstract void update(Observable o, Object arg);

}
