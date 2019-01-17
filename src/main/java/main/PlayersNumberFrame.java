package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controller.Match;

import view.*;

/**
 * The PlayersNumberFrame allows to chose the players number throw a JFrame. It
 * implements ActionListener interface and has two attributes: a ButtonGroup and
 * boolean, which informs if the the starting match has a SWING view or not.
 * 
 * This class has only the actionPerformed method.
 * 
 * @author Edoardo Galimberti
 * 
 */

public class PlayersNumberFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	public static final int NUMBER_X = 360;
	public static final int NUMBER_Y = 100;

	private ButtonGroup group;
	private boolean swing;

	/**
	 * Construct the PlayersNumberFrame.
	 */
	public PlayersNumberFrame(boolean swing) {

		this.swing = swing;

		// Set frame options
		Toolkit kit = Toolkit.getDefaultToolkit();

		Dimension screenSize = kit.getScreenSize();
		int screenX = screenSize.width;
		int screenY = screenSize.height;
		setLocation((screenX - NUMBER_X) / 2, (screenY - NUMBER_Y) / 2);
		setSize(NUMBER_X, NUMBER_Y);

		Image icon = kit.getImage("src/main/resources/images/icon.jpg");
		setIconImage(icon);
		setUndecorated(false);
		setTitle("Players number");
		setLayout(new BorderLayout());

		// Add number buttons
		JPanel numbers = new JPanel();
		numbers.setLayout(new BoxLayout(numbers, BoxLayout.X_AXIS));
		this.group = new ButtonGroup();

		for (Integer i = 2; i < 6; i++) {
			JRadioButton button = new JRadioButton(i.toString() + " players");
			button.setActionCommand(i.toString());
			numbers.add(button);
			group.add(button);
			button.setSelected(false);
		}

		add(numbers, BorderLayout.EAST);

		// Add start button
		JButton start = new JButton("Start!");
		start.addActionListener(this);
		add(start, BorderLayout.SOUTH);

	}

	private int getPlayersNumber() {
		String s = this.group.getSelection().getActionCommand();

		if (s.equals("2")) {
			return 2;
		}
		if (s.equals("3")) {
			return 3;
		}
		if (s.equals("4")) {
			return 4;
		}
		if (s.equals("5")) {
			return 5;
		}
		return -1;
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * It allows to start a local game whit the selected players number.
	 */
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		if (this.swing) {
			Match match = new Match("Local Match");
			AbstractCarcassonneView view = new SwingCarcassonneView();
			for (int i = 0; i < this.getPlayersNumber(); i++) {
				match.addPlayer(view);
			}

			match.startLocal();
		} else {
			Match match = new Match("Local Match");
			AbstractCarcassonneView view = new TextualCarcassonneView();
			for (int i = 0; i < this.getPlayersNumber(); i++) {
				match.addPlayer(view);
			}

			match.startLocal();
		}

	}

}
