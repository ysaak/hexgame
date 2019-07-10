package ysaak.hexgame.data.cellaction;

public abstract class CellAction implements Comparable<CellAction> {
    public final ActionType type;

    CellAction(ActionType type) {
        this.type = type;
    }

    @Override
    public int compareTo(CellAction o) {
        return Integer.compare(type.order, o.type.order);
    }
}
