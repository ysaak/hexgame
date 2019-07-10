package ysaak.hexgame.service;

import org.junit.Assert;
import org.junit.Test;
import ysaak.hexgame.data.Cell;
import ysaak.hexgame.data.Pos;
import ysaak.hexgame.rules.BoardRules;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestBoardService {

	private final BoardService boardService;

	public TestBoardService() {
		this.boardService = new BoardService();
	}

	@Test
	public void testInitBoard() {
		// Given
		int expectedGridSize = BoardRules.GRID_WIDTH * BoardRules.GRID_HEIGHT;

		// When
		Board board = boardService.initBoard();

		// Then
		Assert.assertNotNull(board);
		Assert.assertNotNull(board.grid);
		Assert.assertEquals(expectedGridSize, board.grid.size());

		for (Pos pos : board.grid.keySet()) {
			// Check grid position are valid
			Assert.assertTrue(pos + " is an invalid position", BoardRules.isPositionValid(pos));

			Cell cell = board.grid.get(pos);

			Assert.assertNotNull("Cell at position " + pos + " is null", cell);
			Assert.assertTrue("Cell at position " + pos + " is not empty", cell.empty);
		}
	}

	@Test
	public void testCloneBoard() {
		// Given
		Board initialBoard = boardService.initBoard();

		// When
		Board clonedBoard = boardService.cloneBoard(initialBoard);

		// Then
		Assert.assertNotNull(clonedBoard);
		Assert.assertNotSame(initialBoard, clonedBoard);
		Assert.assertEquals(initialBoard, clonedBoard);
	}

	@Test
	public void testIsMoveAllowed_samePosition() {
		// Given
		Board board = boardService.initBoard();
		Pos origin = Pos.of(0, 0);
		Pos destination = Pos.of(0, 0);
		boolean expectedResponse = false;

		// When
		boolean response = boardService.isMoveAllowed(board, origin, destination);

		// Then
		Assert.assertEquals(expectedResponse, response);
	}

	@Test
	public void testIsMoveAvailable_emptyBoard() {
		// Given
		Board board = boardService.initBoard();
		Pos origin = Pos.of(0, 0);
		Pos destination = Pos.of(0, 1);
		boolean expectedResponse = true;

		// When
		boolean response = boardService.isMoveAllowed(board, origin, destination);

		// Then
		Assert.assertEquals(expectedResponse, response);
	}

	@Test
	public void testIsMoveAvailable_withObstacles() {
		// Given
		Board board = boardService.initBoard();
		board.grid.put(Pos.of(1, 0), Cell.of(1));
		board.grid.put(Pos.of(1, 1), Cell.of(1));

		Pos origin = Pos.of(0, 0);
		Pos destination = Pos.of(2, 0);
		boolean expectedResponse = true;

		// When
		boolean response = boardService.isMoveAllowed(board, origin, destination);

		// Then
		Assert.assertEquals(expectedResponse, response);
	}

	@Test
	public void testIsMoveAvailable_noPath() {
		// Given
		Board board = boardService.initBoard();
		board.grid.put(Pos.of(1, 0), Cell.of(1));
		board.grid.put(Pos.of(1, 1), Cell.of(1));
		board.grid.put(Pos.of(0, 1), Cell.of(1));

		Pos origin = Pos.of(0, 0);
		Pos destination = Pos.of(4, 7);
		boolean expectedResponse = false;

		// When
		boolean response = boardService.isMoveAllowed(board, origin, destination);

		// Then
		Assert.assertEquals(expectedResponse, response);
	}

	@Test
	public void testFindSequenceOfCellWithValue_noSequence() {
		// Given
		Board board = boardService.initBoard();
		Pos position = Pos.of(0, 0);

		int expectedSequenceLength = 0;

		// When
		Set<Pos> sequenceSet = boardService.findSequenceOfCellWithValue(board, position, 100);

		// Then
		Assert.assertNotNull(sequenceSet);
		Assert.assertEquals(expectedSequenceLength, sequenceSet.size());
	}

	@Test
	public void testFindSequenceOfCellWithValue_withSequence() {
		// Given
		long cellValue = 100;

		Board board = boardService.initBoard();
		board.grid.put(Pos.of(0, 0), Cell.of(cellValue));
		board.grid.put(Pos.of(1, 0), Cell.of(cellValue));
		board.grid.put(Pos.of(2, 0), Cell.of(cellValue));
		board.grid.put(Pos.of(4, 0), Cell.of(cellValue));

		Pos position = Pos.of(0, 0);

		int expectedSequenceLength = 3;
		Set<Pos> expectedSequenceSet = new HashSet<>(Arrays.asList(
				Pos.of(0, 0),
				Pos.of(1, 0),
				Pos.of(2, 0)
		));

		// When
		Set<Pos> sequenceSet = boardService.findSequenceOfCellWithValue(board, position, cellValue);

		// Then
		Assert.assertNotNull(sequenceSet);
		Assert.assertEquals(expectedSequenceLength, sequenceSet.size());
		Assert.assertEquals(expectedSequenceSet, sequenceSet);
	}
}
