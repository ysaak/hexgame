package ysaak.hexgame.data;

import java.util.Objects;
import java.util.StringJoiner;

public final class Pos {
	public final int x;
	public final int y;

	public static Pos of(int x, int y) {
		return new Pos(x, y);
	}

	private Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Pos.class.getSimpleName() + "[", "]")
				.add("x=" + x)
				.add("y=" + y)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pos position = (Pos) o;
		return x == position.x &&
				y == position.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
