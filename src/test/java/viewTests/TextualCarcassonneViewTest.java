package viewTests;

import org.junit.*;

import view.*;
import model.*;
import model.PlayerColor;

public class TextualCarcassonneViewTest {

	private TextualCarcassonneView view;

	@Before
	public void setUp() {
		view = new TextualCarcassonneView();
	}

	@Test
	public void tilePrintTest() {
		Card tileToPrint = new Card(
				"N=C S=C W=C E=C NS=1 NE=0 NW=0 WE=1 SE=0 SW=0");
		Player owner = new Player(PlayerColor.BLACK);
		tileToPrint.addMarker(Direction.EAST, new Marker(owner));
		view.update(null, tileToPrint);
		tileToPrint = new Card("N=C S=N W=C E=C NS=0 NE=1 NW=1 WE=1 SE=0 SW=0");
		owner = new Player(PlayerColor.BLUE);
		tileToPrint.addMarker(Direction.NORTH, new Marker(owner));
		view.update(null, tileToPrint);
		tileToPrint = new Card("N=S S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		owner = new Player(PlayerColor.GREEN);
		tileToPrint.addMarker(Direction.SOUTH, new Marker(owner));
		view.update(null, tileToPrint);
		tileToPrint = new Card("N=C S=S W=C E=S NS=0 NE=0 NW=0 WE=0 SE=1 SW=0");
		owner = new Player(PlayerColor.RED);
		tileToPrint.addMarker(Direction.WEST, new Marker(owner));
		view.update(null, tileToPrint);
		// caso di casino
		tileToPrint = new Card("N=S S=N W=S E=S NS=0 NE=0 NW=1 WE=0 SE=0 SW=1");
		owner = new Player(PlayerColor.RED);
		tileToPrint.addMarker(Direction.WEST, new Marker(owner));
		view.update(null, tileToPrint);
		tileToPrint.rotate();
		view.update(null, tileToPrint);
		tileToPrint.rotate();
		view.update(null, tileToPrint);
		tileToPrint.rotate();
		view.update(null, tileToPrint);
		tileToPrint.rotate();
		view.update(null, tileToPrint);
	}

	@Test
	public void boardPrintTest() {
		Card tile = new Card("N=C S=C W=C E=C NS=1 NE=0 NW=0 WE=1 SE=0 SW=0");
		Coordinate where = new Coordinate(0, 0);
		BoardUpdate update = new BoardUpdate(tile, where);
		view.update(null, update);
		Direction direction = Direction.NORTH;
		for (int j = 0; j < Direction.values().length; j++) {
			for (int i = 0; i < 4; i++) {
				where = where.go(direction);
				update = new BoardUpdate(tile, where);
				view.update(null, update);
			}
			direction = direction.clockWise();
		}
	}

	@Test
	public void playerUpdateTest() {
		for (PlayerColor color : PlayerColor.values()) {
			Player player = new Player(color);
			view.update(null, player);
		}
	}

	public static void main(String[] args) {
		TextualCarcassonneViewTest viewtest = new TextualCarcassonneViewTest();
		viewtest.setUp();

		Card tileToPrint = new Card(
				"N=S S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		Player owner = new Player(PlayerColor.BLACK);
		tileToPrint.addMarker(Direction.EAST, new Marker(owner));
		viewtest.view.update(null, tileToPrint);
		tileToPrint.rotate();
		viewtest.view.update(null, tileToPrint);
		tileToPrint.rotate();
		viewtest.view.update(null, tileToPrint);
		tileToPrint.rotate();
		viewtest.view.update(null, tileToPrint);
		viewtest.setUp();
		viewtest.tilePrintTest();/*
								 * viewtest.setUp(); viewtest.boardPrintTest();
								 * viewtest.setUp();
								 * viewtest.playerUpdateTest();
								 */

	}
}
