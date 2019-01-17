package main;

/**
 * The main class is the class from which the game starts.
 * 
 * This class has only the main method.
 * 
 * @author Edoardo Galimberti
 * 
 */

public final class Main {

	private Main() {
		super();
	}

	/**
	 * Starts the game.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FirstFrame frame = new FirstFrame();
		frame.setVisible(true);

	}

}
