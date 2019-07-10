package ysaak.hexgame.data.cellaction;

public enum ActionType {
    MOVE(1),
    DROP(10),
    MERGE(20)
    ;

    public final int order;

    ActionType(int order) {
        this.order = order;
    }
}
