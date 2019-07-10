package ysaak.hexgame.data.cellaction;

import ysaak.hexgame.data.Pos;

import java.util.List;

public class CellMoveAction extends CellAction {
    public final Pos origin;
    public final Pos destination;
    public final List<Pos> pathBetweenCell;

    public CellMoveAction(Pos origin, Pos destination, List<Pos> pathBetweenCell) {
        super(ActionType.MOVE);

        this.origin = origin;
        this.destination = destination;
        this.pathBetweenCell = pathBetweenCell;
    }
}
