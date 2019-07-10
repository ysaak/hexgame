package ysaak.hexgame.rules;

import org.junit.Assert;
import org.junit.Test;
import ysaak.hexgame.data.Pos;

import java.util.Arrays;
import java.util.List;

public class TestBoardRules {


	@Test
	public void testPositionValid_List() {
		// Out of grid
		testPositionValid(Pos.of(-1,-1), false);
		testPositionValid(Pos.of(100,0), false);
		testPositionValid(Pos.of(100,0), false);
		testPositionValid(Pos.of(0,100), false);
		testPositionValid(Pos.of(100,100), false);


		// Border
		testPositionValid(Pos.of(0,0), true);
		testPositionValid(Pos.of(0,7), true);
		testPositionValid(Pos.of(4,0), true);
		testPositionValid(Pos.of(4,7), true);
	}

	private void testPositionValid(Pos position, boolean valid) {
		// Given
		// See parameters

		//When
		boolean isValid = BoardRules.isPositionValid(position);

		// Then
		Assert.assertEquals("Position " + position.x + "," + position.y + " is invalid", valid, isValid);
	}

	@Test
	public void testGetNeighborList_topLeft() {
		// Given
		Pos initialPosition = Pos.of(0, 0);
		List<Pos> expectedNeighborList = Arrays.asList(
				Pos.of(1, 0),
				Pos.of(0, 1),
				Pos.of(1, 1)
		);

		// When
		List<Pos> neighborList = BoardRules.getNeighborhood(initialPosition);

		// Then
		Assert.assertEquals(expectedNeighborList, neighborList);
	}

	@Test
	public void testGetNeighborList_bottomRight() {
		// Given
		Pos initialPosition = Pos.of(4, 7);
		List<Pos> expectedNeighborList = Arrays.asList(
				Pos.of(3, 7),
				Pos.of(4, 6)
		);

		// When
		List<Pos> neighborList = BoardRules.getNeighborhood(initialPosition);

		// Then
		Assert.assertEquals(expectedNeighborList, neighborList);
	}

	@Test
	public void testGetNeighborList_bottom() {
		// Given
		Pos initialPosition = Pos.of(2, 7);
		List<Pos> expectedNeighborList = Arrays.asList(
				Pos.of(1, 7),
				Pos.of(2, 6),
				Pos.of(3, 7)
		);

		// When
		List<Pos> neighborList = BoardRules.getNeighborhood(initialPosition);

		// Then
		Assert.assertEquals(expectedNeighborList, neighborList);
	}

	@Test
	public void testGetNeighborList_middle() {
		// Given
		Pos initialPosition = Pos.of(1, 1);
		List<Pos> expectedNeighborList = Arrays.asList(
				Pos.of(0, 0),
				Pos.of(1, 0),
				Pos.of(2, 0),
				Pos.of(0, 1),
				Pos.of(1, 2),
				Pos.of(2, 1)
		);

		// When
		List<Pos> neighborList = BoardRules.getNeighborhood(initialPosition);

		// Then
		Assert.assertEquals(expectedNeighborList, neighborList);
	}
}
