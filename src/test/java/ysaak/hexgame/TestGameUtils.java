package ysaak.hexgame;

import ysaak.hexgame.data.Board;
import ysaak.hexgame.data.Game;
import ysaak.hexgame.exception.NoCellAvailableException;
import ysaak.hexgame.rules.GameRules;
import ysaak.hexgame.service.BoardService;

import java.util.List;

public final class TestGameUtils {
    public static Game createEmptyGame(BoardService boardService) {
        Board board = boardService.initBoard();
        final List<Long> nextItemList = GameRules.generateItemList();
        return new Game(board, nextItemList, 0);
    }

    public static long calculateGridValueSum(final Game game) {
        return game.board.grid.values().stream().mapToLong(c -> c.value).sum();
    }
}
