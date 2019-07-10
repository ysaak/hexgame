package ysaak.hexgame.data;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class Board {
	public final Map<Pos, Cell> grid;

	public Board(Map<Pos, Cell> grid) {
		this.grid = grid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Board board = (Board) o;
		return Objects.equals(grid, board.grid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(grid);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Board.class.getSimpleName() + "[", "]")
				.add("grid=" + grid)
				.toString();
	}
}
