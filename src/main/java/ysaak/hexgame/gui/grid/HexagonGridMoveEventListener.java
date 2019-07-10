package ysaak.hexgame.gui.grid;

import ysaak.hexgame.data.Pos;

public interface HexagonGridMoveEventListener {
	void move(Pos origin, Pos destination);
}
