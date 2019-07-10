package ysaak.hexgame.rules;

import com.google.common.base.Preconditions;
import ysaak.hexgame.data.Pos;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BoardRules {
	public static final int GRID_WIDTH = 5;
	public static final int GRID_HEIGHT = 8;

	private static final int[][][] CELL_NEIGHBORHOOD_DIRECTION = new int[][][] {
			// Even column
			new int[][] {
					new int[] { -1,  0 },
					new int[] {  0, -1 },
					new int[] { +1,  0 },
					new int[] { -1, +1 },
					new int[] {  0, +1 },
					new int[] { +1, +1 }
			},
			// Odd column
			new int[][] {
					new int[] { -1, -1 },
					new int[] {  0, -1 },
					new int[] { +1, -1 },
					new int[] { -1,  0 },
					new int[] {  0, +1 },
					new int[] { +1,  0 }
			}
	};

	private BoardRules() { /**/ }

	public static boolean isPositionValid(Pos position) {
		return position != null
				&& position.x >= 0 && position.y >= 0
				&& position.x < GRID_WIDTH && position.y < GRID_HEIGHT;
	}

	public static List<Pos> getNeighborhood(Pos position) {
		Preconditions.checkNotNull(position);

		int parity = position.x & 1;

		return Stream.of(CELL_NEIGHBORHOOD_DIRECTION[parity])
				.map(dir -> Pos.of(position.x + dir[0], position.y + dir[1]))
				.filter(BoardRules::isPositionValid)
				.collect(Collectors.toList());
	}

	/**
	 * Calculates the grid distance between two positions
	 *
	 * @param origin the start position
	 * @param destination the destination position
	 * @return the distance (number of hexagons)
	 */
	public static int getDistance(Pos origin, Pos destination) {
		return ((Math.abs(origin.x - destination.x) + Math.abs(origin.y - destination.y) + Math.abs(origin.x + origin.y - destination.x - destination.y)) / 2);
	}
}
