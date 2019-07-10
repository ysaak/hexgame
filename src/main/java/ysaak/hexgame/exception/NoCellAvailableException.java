package ysaak.hexgame.exception;

public class NoCellAvailableException extends Exception {
    public NoCellAvailableException() {
        super("Not enough cell available to place new items");
    }
}
