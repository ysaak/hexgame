package ysaak.hexgame.exception;

import ysaak.hexgame.data.Pos;

public class NoPathFoundException extends Exception {
    public NoPathFoundException(Pos origin, Pos destination) {
        super("No getPathBetween found between " + origin + " and " + destination);
    }
}
