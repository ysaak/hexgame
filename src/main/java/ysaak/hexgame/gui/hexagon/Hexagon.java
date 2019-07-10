package ysaak.hexgame.gui.hexagon;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ysaak.hexgame.gui.Colors;

import java.util.HashMap;
import java.util.Map;

public class Hexagon extends Group {
	private static final double HEXAGON_SIZE = 30;

	private static final Map<Long, Color> VALUE_TO_COLOR_MAP;
	private static final Map<Integer, Font> TEXT_LENGTH_TO_FONT_MAP;

	private final double width;
	private final double height;

	private double centerX;
	private double centerY;

	Color emptyBackgroundColor = Colors.EMPTY_HEXAGON_COLOR;

	final Polygon hexagonShape;
	private final Text valueLabel;

	private long value;

	public Hexagon() {
		width = HEXAGON_SIZE * 2;
		height = Math.sqrt(3) * HEXAGON_SIZE;

		centerX = width / 2.;
		centerY = height / 2.;

		// Create hexagon shape
		hexagonShape = new Polygon();
		initHexagonShape();

		// Create valueLabel
		valueLabel = new Text("");
		valueLabel.setMouseTransparent(true);
		this.getChildren().add(valueLabel);

		setEmpty();
	}

	public Hexagon(long initialValue) {
		this();
		setValue(initialValue);
	}

	private void initHexagonShape() {
		hexagonShape.setStroke(Color.WHITE);

		for (double p : calculatePolygonPoints()) {
			hexagonShape.getPoints().add(p);
		}

		hexagonShape.setLayoutX(0.);
		hexagonShape.setLayoutY(0.);

		this.getChildren().add(hexagonShape);
	}

	public void setValue(long value) {
		this.value = value;

		final Color fillColor;
		final String text;
		if (value == 0) {
			fillColor = emptyBackgroundColor;
			text = "";
		}
		else {
			fillColor = Colors.getColorForValue(value);
			text = String.valueOf(value);
		}

		this.hexagonShape.setFill(fillColor);

		this.valueLabel.setText(text);
		this.valueLabel.setFont(TEXT_LENGTH_TO_FONT_MAP.get(text.length()));

		computeLabelPosition();
	}

	public void setEmpty() {
		setValue(0);
	}

	public long getValue() {
		return value;
	}

	private void computeLabelPosition() {
		double textWidth = valueLabel.getBoundsInLocal().getWidth();
		double textHeight = valueLabel.getBoundsInLocal().getHeight();

		valueLabel.setLayoutX(centerX - textWidth / 2);
		valueLabel.setLayoutY(centerY + textHeight / 4);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}


	private double[] calculatePolygonPoints() {
		double[] polyPoints = new double[12];
		double angle;

		for (int i = 0; i < 6; i++) {
			angle = Math.toRadians(60 * i);
			polyPoints[(i * 2)] = (centerX + HEXAGON_SIZE * Math.cos(angle));
			polyPoints[(i * 2 + 1)] = (centerY + HEXAGON_SIZE * Math.sin(angle));
		}

		return polyPoints;
	}

	static {
		// Value to color grid
		VALUE_TO_COLOR_MAP = new HashMap<>();
		VALUE_TO_COLOR_MAP.put(1L, Colors.RED);
		VALUE_TO_COLOR_MAP.put(2L, Colors.BROWN);
		VALUE_TO_COLOR_MAP.put(4L, Colors.PINK);
		VALUE_TO_COLOR_MAP.put(8L, Colors.DEEP_ORANGE);
		VALUE_TO_COLOR_MAP.put(16L, Colors.PURPLE);
		VALUE_TO_COLOR_MAP.put(32L, Colors.ORANGE);
		VALUE_TO_COLOR_MAP.put(64L, Colors.INDIGO);
		VALUE_TO_COLOR_MAP.put(128L, Colors.AMBER);
		VALUE_TO_COLOR_MAP.put(256L, Colors.BLUE);
		VALUE_TO_COLOR_MAP.put(512L, Colors.YELLOW);
		VALUE_TO_COLOR_MAP.put(1024L, Colors.LIGHT_BLUE);
		VALUE_TO_COLOR_MAP.put(2048L, Colors.LIME);
		VALUE_TO_COLOR_MAP.put(4096L, Colors.CYAN);
		VALUE_TO_COLOR_MAP.put(8192L, Colors.LIGHT_GREEN);
		VALUE_TO_COLOR_MAP.put(16384L, Colors.TEAL);
		VALUE_TO_COLOR_MAP.put(32768L, Colors.GREEN);

		// Text length to font grid
		TEXT_LENGTH_TO_FONT_MAP = new HashMap<>();
		TEXT_LENGTH_TO_FONT_MAP.put(0, new Font(16));
		TEXT_LENGTH_TO_FONT_MAP.put(1, new Font(16));
		TEXT_LENGTH_TO_FONT_MAP.put(2, new Font(16));
		TEXT_LENGTH_TO_FONT_MAP.put(3, new Font(15));
		TEXT_LENGTH_TO_FONT_MAP.put(4, new Font(14));
		TEXT_LENGTH_TO_FONT_MAP.put(5, new Font(13));
	}
}
