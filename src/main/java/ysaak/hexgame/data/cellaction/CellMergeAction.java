package ysaak.hexgame.data.cellaction;

import ysaak.hexgame.data.Pos;

import java.util.Collection;

public class CellMergeAction extends CellAction {
    public final Collection<Pos> cellToMergeList;
    public final Pos fusionPosition;
    public final long initialValue;
    public final long fusionValue;

    public CellMergeAction(Collection<Pos> cellToMergeList, Pos fusionPosition, long initialValue, long fusionValue) {
        super(ActionType.MERGE);
        this.cellToMergeList = cellToMergeList;
        this.fusionPosition = fusionPosition;
        this.initialValue = initialValue;
        this.fusionValue = fusionValue;
    }
}
