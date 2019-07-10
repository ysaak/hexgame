package ysaak.hexgame.data;

import ysaak.hexgame.data.cellaction.CellAction;

import java.util.List;

public class PlayResult {
    public final Game game;
    public final List<CellAction> actionList;

    public PlayResult(Game game, List<CellAction> actionList) {
        this.game = game;
        this.actionList = actionList;
    }
}
