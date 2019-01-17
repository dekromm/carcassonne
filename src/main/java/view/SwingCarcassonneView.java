package view;

import java.awt.event.*;
import java.io.Serializable;
import java.util.Observable;

import view.swing.TopFrame;
import model.*;

/**
 * The SwingCarcassonneView is the SWING view of this game. It extends
 * AbstractCarcassonneView and implements ActionLister and has the following
 * attributes: a TopFrame reference, the current card, the current player and
 * the last string received from the model.
 * 
 * 
 * This class has only the update and actionPerfomed method.
 * 
 * 
 * @author Edoardo Galimberti
 * 
 */

public class SwingCarcassonneView extends AbstractCarcassonneView implements
		ActionListener, Serializable {

	private static final long serialVersionUID = 1L;
	private TopFrame topFrame;
	private Card currentCard;
	private Player currentPlayer;
	private String currentString;

	/**
	 * Construct a SwingCarcassonneView with its attributes to null.
	 */
	public SwingCarcassonneView() {
		this.currentCard = null;
		this.currentPlayer = null;
		this.currentString = null;
		this.topFrame = null;
	}

	/**
	 * Recognizes the update object received and call the right private method
	 * or construct the TopFrame.
	 */
	@Override
	public void update(Observable model, Object update) {
		if (update instanceof String) {
			this.stringUpdate((String) update);
		}
		if (update instanceof Card) {
			this.cardUpdate((Card) update);
		}
		if (update instanceof Player) {
			this.playerUpdate((Player) update);
		}
		if (update instanceof StartSignal) {
			this.topFrame = new TopFrame(this, this.currentCard,
					this.currentPlayer, this.currentString);
			topFrame.setVisible(true);
		}
	}

	private void stringUpdate(String string) {
		if (this.currentString == null) {
			this.currentString = string;
		} else {
			this.currentString = string;
			this.topFrame.setString(string);
			this.topFrame.repaint();
		}
	}

	private void playerUpdate(Player player) {
		if (this.currentPlayer == null) {
			this.currentPlayer = player;
		} else {
			this.currentPlayer = player;
			this.topFrame.setPlayer(player);
			this.topFrame.repaint();
		}

	}

	private void cardUpdate(Card card) {
		if (this.currentCard == null) {
			this.currentCard = card;
		} else {
			if (this.currentCard.getStringID().equals(card.getStringID())) {
				this.topFrame.rotateCard();
				this.topFrame.repaint();
			} else {
				this.currentCard = card;
				this.topFrame.setCard(card);
				this.topFrame.repaint();
			}
		}
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * It allows to send notifyObservers to the control.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Rotate")) {
			setChanged();
			notifyObservers("rotate");
		}

		if (command.equals("Pass")) {
			setChanged();
			notifyObservers("pass");
		}

		if (command.equals("North")) {
			setChanged();
			notifyObservers(Direction.NORTH);
		}
		if (command.equals("South")) {
			setChanged();
			notifyObservers(Direction.SOUTH);
		}
		if (command.equals("West")) {
			setChanged();
			notifyObservers(Direction.WEST);
		}
		if (command.equals("East")) {
			setChanged();
			notifyObservers(Direction.EAST);
		}
	}

}
