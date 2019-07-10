package ysaak.hexgame.data;

import java.util.List;

public class Game {
	public final Board board;
	public final List<Long> nextItemList;
	public final long score;

	public Game(Board board, List<Long> nextItemList, long score) {
		this.board = board;
		this.nextItemList = nextItemList;
		this.score = score;
	}

}
