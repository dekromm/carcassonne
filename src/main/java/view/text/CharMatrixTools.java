package view.text;

import java.io.PrintWriter;

/**
 * This class contains some useful static methods that might be useful when
 * handling char arrays.
 * 
 * @author Guido Gerosa
 * 
 */
public final class CharMatrixTools {

	private CharMatrixTools() {
		super();
	}

	/**
	 * Copies the source's characters horizontally onto the destination matrix
	 * starting from the given coordinates. (The given destination char array
	 * don't get copied and is modified too).
	 * 
	 * @param source
	 *            - the matrix to blit
	 * @param destination
	 *            - the matrix to blit onto
	 * @param y
	 *            - the vertical starting position
	 * @param x
	 *            - the horizontal starting position
	 */
	public static char[][] blit(char[] source, char[][] destination, int y,
			int x) {
		System.arraycopy(source, 0, destination[y], x, source.length);
		return destination;
		/*
		 * This is the previous implementation of this method. Sonar claimed it
		 * was more efficient with System.arraycopy. for (int pos = 0; pos <
		 * source.length; pos++) { destination[y][x + pos] = source[pos]; }
		 * return destination;
		 */
	}

	/**
	 * Copies the source's characters onto the destination matrix starting from
	 * the given coordinates. (The given destination char array don't get copied
	 * and is modified too).
	 * 
	 * @param source
	 *            - the matrix to blit
	 * @param destination
	 *            - the matrix to blit onto
	 * @param y
	 *            - the vertical starting position
	 * @param x
	 *            - the horizontal starting position
	 */
	public static char[][] blit(char[][] source, char[][] destination, int y,
			int x) {
		for (int row = 0; row < source.length; row++) {
			System.arraycopy(source[row], 0, destination[y + row], x,
					source[row].length);
			/*
			 * Sonar claimed it's more efficient this way... meh for (int col =
			 * 0; col < source[row].length; col++) { destination[y + row][x +
			 * col] = source[row][col]; }
			 */
		}
		return destination;
	}

	/**
	 * Fills the given matrix with the given char and returns the modified
	 * matrix. (The given char array don't get copied and is modified too).
	 * 
	 * @param result
	 *            - the matrix to fill
	 * @param c
	 *            - the char to fill with
	 * @return modified matrix
	 */
	public static char[][] fill(char[][] result, char c) {
		for (int y = 0; y < result.length; y++) {
			for (int x = 0; x < result[y].length; x++) {
				result[y][x] = c;
			}
		}
		return result;
	}

	/**
	 * This dumb method takes a char array and puts it, char by char, on the
	 * screen.
	 * 
	 * @param toPrint
	 *            - the char array to print
	 */
	public static void print(char[][] toPrint) {
		PrintWriter printer = new PrintWriter(System.out);
		for (char[] row : toPrint) {
			printer.println(row);
		}
		printer.flush();
	}
}
