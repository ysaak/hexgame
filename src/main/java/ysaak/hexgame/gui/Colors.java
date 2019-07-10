package ysaak.hexgame.gui;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Colors {
	public static final Color RED = Color.web("#f44336");
	public static final Color BROWN = Color.web("#795548");
	public static final Color PINK = Color.web("#e91e63");
	public static final Color DEEP_ORANGE = Color.web("#ff5722");
	public static final Color PURPLE = Color.web("#9c27b0");
	public static final Color ORANGE = Color.web("#ff9800");
	public static final Color INDIGO = Color.web("#3f51b5");
	public static final Color AMBER = Color.web("#ffc107");
	public static final Color BLUE = Color.web("#2196f3");
	public static final Color YELLOW = Color.web("#ffeb3b");
	public static final Color LIGHT_BLUE = Color.web("#03a9f4");
	public static final Color LIME = Color.web("#cddc39");
	public static final Color CYAN = Color.web("#00bcd4");
	public static final Color LIGHT_GREEN = Color.web("#8bc34a");
	public static final Color TEAL = Color.web("#009688");
	public static final Color GREEN = Color.web("#4caf50");
	public static final Color GRAY = Color.web("#9e9e9e");
	public static final Color BLUE_GREY = Color.web("#607d8b");


	public static final Color GAMEOVER_OVERLAY = Color.web("#000000", 0.7d);

	public static final Color EMPTY_HEXAGON_COLOR = GRAY;
	private static final Map<Long, Color> VALUE_TO_COLOR_MAP;
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
	}

	public static Color getColorForValue(long value) {
		return VALUE_TO_COLOR_MAP.getOrDefault(value, EMPTY_HEXAGON_COLOR);
	}
}
