package main;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * The WelcomeImage class allows to print the first image of the game.
 * 
 * This class has only the paintComponent method.
 * 
 * @author Edoardo Galimberti
 * 
 */

public class WelcomeImage extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final int WEL_I_X = 0;
	public static final int WEL_I_Y = 0;

	/**
	 * Print the first image of the game.
	 */
	public void paintComponent(Graphics grap) {
		super.paintComponent(grap);
		Image image;
		try {
			image = ImageIO.read(new File(
					"src/main/resources/images/welcome.jpg"));
			grap.drawImage(image, WEL_I_X, WEL_I_Y, null);
		} catch (IOException e) {
			System.err.println("IOException: welcome.jpg can't be red.");
		}

	}

}
