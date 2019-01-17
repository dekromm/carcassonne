package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.RMIClient;
import view.*;

/**
 * The FirstFrame class represents the first frame of the game throw a JFrame.
 * It implements ActionListener interface.
 * 
 * This class has only the actionPerformed method.
 * 
 * @author Edoardo Galimberti
 * 
 */

public class FirstFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	public static final int FIRST_X = 580;
	public static final int FIRST_Y = 400;

	/**
	 * Construct the FirstFrame.
	 */
	public FirstFrame() {

		// Set frame options
		Toolkit kit = Toolkit.getDefaultToolkit();

		Dimension screenSize = kit.getScreenSize();
		int screenX = screenSize.width;
		int screenY = screenSize.height;
		setLocation((screenX - FIRST_X) / 2, (screenY - FIRST_Y) / 2);
		setSize(FIRST_X, FIRST_Y);

		Image icon = kit.getImage("src/main/resources/images/icon.jpg");
		setIconImage(icon);
		setUndecorated(true);
		setTitle("Carcassonne");

		setLayout(new BorderLayout());

		// Add buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2, 2));
		addButton(buttons, "Local SWING Game");
		addButton(buttons, "Local Textual Game");
		addButton(buttons, "RMI SWING Game");
		addButton(buttons, "RMI Textual Game");
		add(buttons, BorderLayout.SOUTH);

		// Add image
		WelcomeImage image = new WelcomeImage();
		add(image, BorderLayout.CENTER);

	}

	private void addButton(JPanel panel, String label) {
		JButton button = new JButton(label);
		button.addActionListener(this);
		panel.add(button);
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * It allows to start an RMI match or chose the players number for a local
	 * match.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Local SWING Game")) {
			PlayersNumberFrame frame = new PlayersNumberFrame(true);
			frame.setVisible(true);
		}
		if (command.equals("Local Textual Game")) {
			PlayersNumberFrame frame = new PlayersNumberFrame(false);
			frame.setVisible(true);
		}
		if (command.equals("RMI SWING Game")) {
			AbstractCarcassonneView view = new SwingCarcassonneView();
			RMIClient client = new RMIClient(view, "127.0.0.1");
			client.start();
		}
		if (command.equals("RMI Textual Game")) {
			AbstractCarcassonneView view = new TextualCarcassonneView();
			RMIClient client = new RMIClient(view, "127.0.0.1");
			client.start();
		}

	}

}