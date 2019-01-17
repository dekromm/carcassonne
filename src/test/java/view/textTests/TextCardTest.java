package view.textTests;

import static org.junit.Assert.*;
import model.Card;
import model.Direction;

import org.junit.Before;
import org.junit.Test;

import view.text.TextualCard;

public class TextCardTest {

	private TextualCard textCard;

	@Before
	public void setUp() {
		textCard = new TextualCard(new Card(
				"N=S S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0"));
	}

	@Test
	public void testRep() {
		// testo che l'incrocio a 4 restituisca direzioni diverse per ogni Sx
		assertTrue(textCard.getEdge("S1") != null);
		assertTrue(textCard.getEdge("S4") != null);
		assertTrue(textCard.getEdge("S3") != null);
		assertTrue(textCard.getEdge("S2") != null);
		// verifico che tutte le direzioni restituite siano diverse a secodna
		// della Sx
		assertFalse(textCard.getEdge("S1") == textCard.getEdge("S2"));
		assertFalse(textCard.getEdge("S1") == textCard.getEdge("S3"));
		assertFalse(textCard.getEdge("S1") == textCard.getEdge("S4"));
		assertFalse(textCard.getEdge("S2") == textCard.getEdge("S3"));
		assertFalse(textCard.getEdge("S2") == textCard.getEdge("S4"));
		assertFalse(textCard.getEdge("S3") == textCard.getEdge("S4"));
		// creo una textCard uguale a quella di partenza
		textCard = new TextualCard(new Card(
				"N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0"));
		// testo la richiesta sbagliata
		assertTrue(textCard.getEdge("S32") == null);
		assertTrue(textCard.getEdge("S2") == null);
		assertTrue(textCard.getEdge("C2") == null);
		assertTrue(textCard.getEdge("") == null);
		// testo S1
		assertTrue(textCard.getEdge("S1") == Direction.EAST
				|| textCard.getEdge("S1") == Direction.WEST);
		// testo C1
		assertTrue(textCard.getEdge("C1") == Direction.SOUTH);
	}
}
