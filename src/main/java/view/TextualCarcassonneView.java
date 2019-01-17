package view;

import java.io.PrintWriter;
import java.util.*;

import view.text.CharMatrixTools;
import view.text.TextualCard;
import model.*;

/**
 * This class receives reads updates coming from a {@link CarcassonneModel} and
 * prints its state in a character-based canvas. When a StartSignal is received
 * a thread asking for user input is run
 * 
 * @author Guido Gerosa
 * 
 */
public class TextualCarcassonneView extends AbstractCarcassonneView implements
		Runnable {

	private boolean isActive = false;
	private Map<Coordinate, TextualCard> grid = new HashMap<Coordinate, TextualCard>();
	private Set<Coordinate> openings = new HashSet<Coordinate>();
	private Player owner;
	private String gameName;
	private Player currentPlayer;
	private TextualCard currentTile;
	private int minX, minY, maxX, maxY, gridWidth, gridHeigth;
	static final int TILE_WIDTH = 13; // 13 is the optimal
	static final int TILE_HEIGHT = 5;// 5 is the optimal
	static final int TILE_BORDER = 1;// 1 is VERY optimal... really, there's no
										// telling what might happen changing
										// this.
	static final int PROMPT_DELAY = 200;
	public static final char SOLID = '#';
	public static final char LIGHT = '.';
	public static final char CROSS = '+';
	public static final char WHITESPACE = ' ';

	/**
	 * Construct a TextualCarcassonneView.
	 */
	public TextualCarcassonneView() {
		super();
		this.currentPlayer = null;
		this.currentTile = null;
		openings.add(new Coordinate(0, 0));
		setMaxX(0);
		setMaxY(0);
		setMinX(0);
		setMinY(0);
		gridHeigth = 1;
		gridWidth = 1;
	}

	/**
	 * Asks the user for inputs until "quit" is typed. Each line typed is
	 * notified to attached {@link Observer}s ("quit" too).
	 */
	public void run() {
		PrintWriter console = new PrintWriter(System.out);
		Scanner input = new Scanner(System.in);
		String justRead;
		do {
			prompt(console);
			justRead = input.nextLine();
			if (owner == null || owner.equals(currentPlayer)) {
				console.println(eval(justRead));
			} else {
				console.println("Wait for your turn, dude.");
				console.flush();
			}
		} while (!justRead.equals("quit"));
	}

	private String eval(String command) {
		setChanged();
		if (command.contains(",")) {
			String[] values = command.split(",");
			if (values.length < 2) {
				return help();
			}
			try {
				Coordinate toNotify = new Coordinate(Integer.valueOf((values[0]
						.trim())), Integer.valueOf((values[1].trim())));
				notifyObservers(toNotify);
			} catch (NumberFormatException e) {
				return help();
			}
		} else if (currentTile.getEdge(command) != null) {
			notifyObservers(currentTile.getEdge(command));
		} else if (command.equals("pass") || command.equals("rotate")) {
			notifyObservers(command.toLowerCase(Locale.ENGLISH));
		} else {
			return help();
		}
		return "";
	}

	private String help() {
		String helpString = "";
		helpString = helpString.concat("Possible commands are:\n");
		helpString = helpString
				.concat("x,y :to place the tile in the specified coordinates\n");
		helpString = helpString
				.concat("Sx (or Cx) : to place a marker on the specified place\n");
		helpString = helpString.concat("pass : to end your turn\n");
		return helpString;
	}

	/**
	 * Generates a char matrix containing the representation of the board.
	 * 
	 * @return a char matrix containing the representation of the board
	 */
	private char[][] boardMatrix() {
		char[][] board = new char[(TILE_HEIGHT + TILE_BORDER)
				* (gridHeigth + 2) + TILE_BORDER][(TILE_WIDTH + TILE_BORDER)
				* (gridWidth + 2) + TILE_BORDER];
		board = CharMatrixTools.fill(board, WHITESPACE);
		Coordinate pivot = new Coordinate(minX - 1, maxY + 1);
		int yTracker = 0;// initializing trackers for current position on matrix
		int xTracker;
		do {
			xTracker = 0;
			board = CharMatrixTools.blit(topBorder(pivot.getY()), board,
					yTracker, 0);
			yTracker += TILE_BORDER;
			do {
				board = CharMatrixTools.blit(leftBorder(pivot), board,
						yTracker, xTracker);
				xTracker += TILE_BORDER;
				board = CharMatrixTools.blit(cellMatrix(pivot), board,
						yTracker, xTracker);
				xTracker += TILE_WIDTH;
				pivot = pivot.go(Direction.EAST);
			} while (pivot.getX() <= maxX + 1);
			board = CharMatrixTools.blit(leftBorder(pivot), board, yTracker,
					xTracker);
			yTracker += TILE_HEIGHT;
			pivot = new Coordinate(minX - 1, pivot.go(Direction.SOUTH).getY());
		} while (pivot.getY() >= minY - 1);
		board = CharMatrixTools.blit(topBorder(pivot.getY()), board, yTracker,
				0);
		return board;
	}

	/**
	 * Returns a char matrix representation of the grid's cell that is in given
	 * position.
	 * 
	 * @param position
	 * @return char matrix representation of the single cell
	 */
	private char[][] cellMatrix(Coordinate position) {
		if (!openings.contains(position) && !grid.keySet().contains(position)) {
			return CharMatrixTools.fill(new char[TILE_HEIGHT][TILE_WIDTH],
					WHITESPACE);
		} else if (grid.keySet().contains(position)) {
			return grid.get(position).getMatrix(TILE_WIDTH, TILE_HEIGHT);
		} else {
			return coordinateMatrix(position);
		}
	}

	/**
	 * This method returns an "empty" char array labeled with the given
	 * coordinate's values.
	 * 
	 * @param coordinate
	 *            - the {@link Coordinate} used to generate the label
	 * @return the labeled char array.
	 */
	private char[][] coordinateMatrix(Coordinate coordinate) {
		char[][] result = new char[TILE_HEIGHT][TILE_WIDTH];
		result = CharMatrixTools.fill(result, WHITESPACE);
		String string = "(" + coordinate.getX() + "," + coordinate.getY() + ")";
		result = CharMatrixTools.blit(string.toCharArray(), result,
				TILE_HEIGHT / 2, TILE_WIDTH / 2 - string.length() / 2);
		return result;
	}

	/**
	 * Given an int representing a row, the method returns a char matrix
	 * containing the border to be put over that given row.
	 * 
	 * @param y
	 *            - the int pointing to the row
	 * @return a char matrix containing the border
	 */
	private char[][] topBorder(int y) {
		Coordinate here = new Coordinate(minX - 1, y);
		char[][] cross = new char[TILE_BORDER][TILE_BORDER];
		char[][] segment = new char[TILE_BORDER][TILE_WIDTH];
		cross = CharMatrixTools.fill(cross, CROSS);
		char[][] border = new char[TILE_BORDER][(TILE_WIDTH + TILE_BORDER)
				* (gridWidth + 2) + TILE_BORDER];
		int tracker = 0;// keeps track of the position I've already "drawn"
		do {
			border = CharMatrixTools.blit(cross, border, 0, tracker);
			tracker += TILE_BORDER;
			if ((grid.keySet().contains(here) && !grid.keySet().contains(
					here.go(Direction.NORTH)))
					|| (!grid.keySet().contains(here) && grid.keySet()
							.contains(here.go(Direction.NORTH)))) {
				segment = CharMatrixTools.fill(segment, SOLID);
			} else if (grid.keySet().contains(here)
					&& grid.keySet().contains(here.go(Direction.NORTH))) {
				segment = CharMatrixTools.fill(segment, LIGHT);
			} else if (!openings.contains(here)
					&& !openings.contains(here.go(Direction.NORTH))) {
				segment = CharMatrixTools.fill(segment, WHITESPACE);
			} else {
				segment = CharMatrixTools.fill(segment, LIGHT);
			}
			border = CharMatrixTools.blit(segment, border, 0, tracker);
			tracker += TILE_WIDTH;
			here = here.go(Direction.EAST);
		} while (here.getX() <= maxX + 1);
		border = CharMatrixTools.blit(cross, border, 0, tracker);
		return border;
	}

	/**
	 * Generates a char matrix containing the border between the given
	 * {@link Coordinate} and it's left neighbour.
	 * 
	 * @param position
	 *            - the {@link Coordinate} to use
	 * @return the char matrix containing the border
	 */
	private char[][] leftBorder(Coordinate position) {
		char[][] border = new char[TILE_HEIGHT][TILE_BORDER];
		if ((grid.keySet().contains(position) && !grid.keySet().contains(
				position.go(Direction.WEST)))
				|| (!grid.keySet().contains(position) && grid.keySet()
						.contains(position.go(Direction.WEST)))) {
			return CharMatrixTools.fill(border, SOLID);
		} else if (grid.keySet().contains(position)
				&& grid.keySet().contains(position.go(Direction.WEST))) {
			return CharMatrixTools.fill(border, LIGHT);
		} else if (!openings.contains(position)
				&& !openings.contains(position.go(Direction.WEST))) {
			return CharMatrixTools.fill(border, WHITESPACE);
		} else {
			return CharMatrixTools.fill(border, LIGHT);
		}
	}

	/**
	 * Puts the given {@link Card} in the given {@link Coordinate} updating the
	 * ends of the grid.
	 * 
	 * @param coordinate
	 *            - the {@link Coordinate} to put the {@link Card} in
	 * @param newTile
	 *            - the {@link Card} to put
	 * @return the previous {@link Card}, if any, or null.
	 */
	private void putInGrid(Coordinate coordinate, Card newTile) {
		setMaxX(coordinate.getX());
		setMaxY(coordinate.getY());
		setMinX(coordinate.getX());
		setMinY(coordinate.getY());
		openings.remove(coordinate);
		for (Coordinate neighbour : coordinate.getNeighbours()) {
			if (!grid.keySet().contains(neighbour)) {
				openings.add(neighbour);
			}
		}
		grid.put(coordinate, new TextualCard(newTile));
	}

	private void setMaxX(int newMaxX) {
		if (newMaxX > maxX) {
			maxX = newMaxX;
			gridWidth = maxX - minX + 1;
		}
	}

	private void setMinX(int newMinX) {
		if (newMinX < minX) {
			minX = newMinX;
			gridWidth = maxX - minX + 1;
		}
	}

	private void setMaxY(int newMaxY) {
		if (newMaxY > maxY) {
			maxY = newMaxY;
			gridHeigth = maxY - minY + 1;
		}
	}

	private void setMinY(int newMinY) {
		if (newMinY < minY) {
			minY = newMinY;
			gridHeigth = maxY - minY + 1;
		}
	}

	/**
	 * Overrides the {@link Observer}'s update. Recognizes the update Object by
	 * its class and operates accordingly.
	 */
	@Override
	public void update(Observable model, Object update) {
		PrintWriter console = new PrintWriter(System.out);
		console.println();
		if (update instanceof String) {
			console.println((String) update);
			if (((String) update).contains("Tiles left")) {
				console.println("___________________________________________________________________");
			}
			console.flush();
		}

		if (update instanceof Card) {
			currentTile = new TextualCard((Card) update);
			console.println("The card to play is:");
			console.println("#############");
			console.flush();
			CharMatrixTools.print(currentTile
					.getMatrix(TILE_WIDTH, TILE_HEIGHT));
			console.println("#############");
			console.flush();
		} else if (update instanceof BoardUpdate) {
			Card toPlace = ((BoardUpdate) update).getUpdatedCard();
			Coordinate whereToPlace = ((BoardUpdate) update).getWhere();
			putInGrid(whereToPlace, toPlace);
			console.println("Updated board:");
			console.flush();
			CharMatrixTools.print(boardMatrix());
		} else if (update instanceof Player) {
			currentPlayer = (Player) update;
			console.println("Player " + currentPlayer + ", it's your turn!");
			console.flush();
		} else if (update instanceof StartSignal && !isActive) {
			StartSignal signal = (StartSignal) update;
			owner = signal.getPlayer();
			if (owner != null) {
				console.println("You are the " + owner + " player.");
				console.flush();
			}
			gameName = signal.getGameName();
			putInGrid(new Coordinate(0, 0), signal.getStarterCard());
			CharMatrixTools.print(boardMatrix());
			isActive = true; // avoid running multiple input thread
			// creates a thread that asks for user input
			(new Thread(this)).start();
		}
	}

	private void prompt(PrintWriter output) {
		try {
			Thread.sleep(PROMPT_DELAY);
		} catch (InterruptedException e) {
		}
		String ownerName;
		if (owner != null) {
			ownerName = owner.getColor().toString() + "@";
		} else {
			ownerName = "";
		}
		if (isActive) {
			output.print(ownerName + gameName + ">");
			output.flush();
		}

	}
}
