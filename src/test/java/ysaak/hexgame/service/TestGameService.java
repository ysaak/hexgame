package ysaak.hexgame.service;

import org.junit.Assert;
import org.junit.Test;
import ysaak.hexgame.MockSaveService;
import ysaak.hexgame.data.Cell;
import ysaak.hexgame.data.Game;
import ysaak.hexgame.data.PlayResult;
import ysaak.hexgame.data.Pos;
import ysaak.hexgame.exception.NoCellAvailableException;
import ysaak.hexgame.exception.NoPathFoundException;
import ysaak.hexgame.exception.SaveException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class TestGameService {
	private final GameService gameService;

	public TestGameService() {
		gameService = new GameService(new MockSaveService(), new BoardService());
	}

	@Test
	public void testStart() throws SaveException {
		// Given
		int expectedScore = 0;

		// When
		Game game = gameService.start();

		// Then
		Assert.assertNotNull(game);

		// Score check
		Assert.assertEquals(expectedScore, game.score);

		// Next item check
		Assert.assertNotNull(game.nextItemList);
		Assert.assertNotEquals(0, game.nextItemList.size());

		// Board check
		Assert.assertNotNull(game.board);

		Assert.assertNotEquals(0, calculateGridValueSum(game));
	}

	@Test
	public void testPlay_withMergeAction() throws NoPathFoundException, NoCellAvailableException, SaveException {
		// Given
		Game game = gameService.start();
		game.board.grid.put(Pos.of(0, 0), Cell.of(1));
		game.board.grid.put(Pos.of(1, 0), Cell.of(1));
		game.board.grid.put(Pos.of(2, 0), Cell.of(1));
		game.board.grid.put(Pos.of(4, 0), Cell.of(1));

		Pos moveOrigin = Pos.of(4, 0);
		Pos moveDestination = Pos.of(3, 0);

		long expectedCellSum = calculateGridValueSum(game);

		// When
		PlayResult result = gameService.play(game, moveOrigin, moveDestination);

		// Then
		Assert.assertNotNull(result);
		Game newGame = result.game;

		Assert.assertNotNull(newGame);
		Assert.assertNotNull(newGame.board);

		Assert.assertTrue(newGame.board.grid.get(Pos.of(0, 0)).empty);
		Assert.assertTrue(newGame.board.grid.get(Pos.of(1, 0)).empty);
		Assert.assertTrue(newGame.board.grid.get(Pos.of(2, 0)).empty);
		Assert.assertEquals(4, newGame.board.grid.get(Pos.of(3, 0)).value);

		Assert.assertEquals(expectedCellSum, calculateGridValueSum(newGame));
	}

	@Test
	public void testPlay_withoutMergeAction() throws NoPathFoundException, NoCellAvailableException, SaveException {
		// Given
		Game game = gameService.start();
		game.board.grid.put(Pos.of(0, 0), Cell.of(999));

		Pos moveOrigin = Pos.of(0, 0);
		Pos moveDestination = Pos.of(3, 0);

		long gridValue = calculateGridValueSum(game);

		// When
		PlayResult result = gameService.play(game, moveOrigin, moveDestination);

		// Then
		Assert.assertNotNull(result);
		Game newGame = result.game;

		Assert.assertNotNull(newGame);
		// Next item list
		Assert.assertNotNull(newGame.nextItemList);
		Assert.assertNotSame(game.nextItemList, newGame.nextItemList);

		Assert.assertNotNull(newGame.board);
		Assert.assertNotEquals(gridValue, calculateGridValueSum(newGame));
	}

	@Test(expected = NoCellAvailableException.class)
	public void testPlay_gameOver() throws NoPathFoundException, NoCellAvailableException, SaveException {
		// Given
		Game game = gameService.start();

		Set<Pos> posSet = new HashSet<>(game.board.grid.keySet());
		posSet.remove(Pos.of(0,0));

		final AtomicLong val = new AtomicLong(0);
		posSet.forEach( pos -> game.board.grid.put(pos, Cell.of(val.incrementAndGet())) );

		Pos moveOrigin = Pos.of(1, 0);
		Pos moveDestination = Pos.of(0, 0);

		// When
		gameService.play(game, moveOrigin, moveDestination);

		// Then
		// Exception should be thrown
	}

	private long calculateGridValueSum(final Game game) {
		return game.board.grid.values().stream().mapToLong(c -> c.value).sum();
	}
}
