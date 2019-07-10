package ysaak.hexgame.gui.hexagon;

import javafx.scene.paint.Color;

public class SelectedHexagon extends Hexagon {

	public SelectedHexagon() {
		super();

		this.emptyBackgroundColor = Color.TRANSPARENT;
		this.hexagonShape.setStroke(Color.BLACK);
		this.setMouseTransparent(true);

		setShapeVisible(false);
	}

	public void setShapeVisible(boolean visible) {
		this.setManaged(visible);
		this.setVisible(visible);
	}
}
