package controllerTests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Observable;

import model.PlayerColor;
import modelTests.CarcassonneModelTest.UpdateBuffer;

import org.junit.Before;
import org.junit.Test;

import view.AbstractCarcassonneView;

import controller.Match;

public class MatchTest {
	private Match match;
	private BufferView view;

	public class BufferView extends AbstractCarcassonneView {
		private LinkedList<Object> updateBuffer = new LinkedList<Object>();

		@Override
		public void update(Observable o, Object arg) {
			updateBuffer.offer(arg);
		}

		public int size() {
			return updateBuffer.size();
		}
	}

	@Before
	public void setUp() {
		match = new Match("Test Match");
		view = new BufferView();
	}

	@Test
	public void matchStartTest() {
		//aggiungo il massimo dei giocatori
		for (PlayerColor pc : PlayerColor.values()) {
			assertTrue(match.addPlayer(view));
		}
		//provo ad aggiungere un giocatore di troppo
		assertFalse(match.addPlayer(view));
		//faccio partire il match e verifico che sia arrivato il giusto numero di update dal model
		match.start();
		assertTrue(view.size() == 8); 
	}
	
	@Test
	public void matchStartLocalTest() {
		//aggiungo il massimo dei giocatori
		for (PlayerColor pc : PlayerColor.values()) {
			assertTrue(match.addPlayer(view));
		}
		//provo ad aggiungere un giocatore di troppo
		assertFalse(match.addPlayer(view));
		//faccio partire il match e verifico che sia arrivato il giusto numero di update dal model
		match.startLocal();
		assertTrue(view.size() == 4); 
	}
}
