package controllerTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import controller.CarcassonneController;

import model.*;

public class CarcassonneControllerTest {

	public class FakeModel extends CarcassonneModel {
		private Map<String, Boolean> commands = new HashMap<String, Boolean>();

		public FakeModel(Deck deck) {
			super(deck);
			commands.put("rotate", false);
			commands.put("placeCard", false);
			commands.put("placeMarker", false);
			commands.put("nextTurn", false);
		}

		@Override
		public void rotateCard() {
			commands.put("rotate", true);
		}

		@Override
		public Player nextTurn() {
			commands.put("nextTurn", true);
			return null;
		}

		@Override
		public void placeCurrentCardOnBoard(Coordinate coordinate) {
			commands.put("placeCard", true);
		}

		@Override
		public boolean placeMarkerOnLastPlacedCard(Direction direction) {
			if (commands.put("placeMarker", true)) {
				return false;
			} else {
				return true;
			}
		}

		public Boolean get(String command) {
			return commands.put(command, false);
		}
	}

	private CarcassonneController controller;
	private FakeModel model;

	@Before
	public void setUp() {
		model = new FakeModel(new Deck());
		controller = new CarcassonneController(model);
	}

	@Test
	public void controllerTest() {
		controller.update(null, "rotate");
		assertTrue(model.get("rotate"));
		controller.update(null, new Coordinate(0, 0));
		assertTrue(model.get("placeCard"));
		controller.update(null, Direction.NORTH);
		assertTrue(model.get("placeMarker"));
		assertTrue(model.get("nextTurn"));
		controller.update(null, "pass");
		assertTrue(model.get("nextTurn"));
		controller.update(null, Direction.NORTH);
		assertTrue(model.get("nextTurn"));
		controller.update(null, Direction.NORTH);
		assertFalse(model.get("nextTurn"));
	}
}
