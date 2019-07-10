package ysaak.hexgame.data.cellaction;

import ysaak.hexgame.data.Pos;

public class CellDropAction extends CellAction {
    public final Pos position;
    public final long value;

    public CellDropAction(Pos position, long value) {
        super(ActionType.DROP);
        this.position = position;
        this.value = value;
    }
}
