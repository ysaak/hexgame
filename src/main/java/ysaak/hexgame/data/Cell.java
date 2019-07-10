package ysaak.hexgame.data;

import java.util.Objects;
import java.util.StringJoiner;

public class Cell {
	public final long value;
	public final boolean empty;

	public static Cell empty() {
		return new Cell(0, true);
	}

	public static Cell of(long value) {
		return new Cell(value, false);
	}

	private Cell(long value, boolean empty) {
		this.value = value;
		this.empty = empty;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cell cell = (Cell) o;
		return value == cell.value &&
				empty == cell.empty;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, empty);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Cell.class.getSimpleName() + "[", "]")
				.add("value=" + value)
				.add("empty=" + empty)
				.toString();
	}
}
