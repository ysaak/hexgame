package ysaak.hexgame.gui.grid;

import com.google.common.base.Preconditions;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import ysaak.hexgame.data.Pos;
import ysaak.hexgame.data.cellaction.ActionType;
import ysaak.hexgame.data.cellaction.CellAction;
import ysaak.hexgame.data.cellaction.CellDropAction;
import ysaak.hexgame.data.cellaction.CellMergeAction;
import ysaak.hexgame.data.cellaction.CellMoveAction;
import ysaak.hexgame.gui.hexagon.Hexagon;
import ysaak.hexgame.gui.hexagon.SelectedHexagon;
import ysaak.hexgame.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class HexagonGrid extends Pane {

	private final int gridWidth;
	private final int gridHeight;

	private final Map<Pos, Hexagon> map;

	private final SelectedHexagon selectedHexagon;
	private Pos selectedPosition = null;

	private HexagonGridMoveEventListener moveRequestListener = null;

	public HexagonGrid(int gridWidth, int gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;

		this.map = new HashMap<>(gridWidth * gridHeight);
		initHexagonGrid();

		selectedHexagon = new SelectedHexagon();
		this.getChildren().add(selectedHexagon);


		double width = selectedHexagon.getWidth() * gridWidth;
		double height = selectedHexagon.getHeight() * (gridHeight + 1) + selectedHexagon.getHeight() / 2.;
		this.setMinSize(width, height);
		this.setMaxSize(width, height);
	}

	// https://www.redblobgames.com/grids/hexagons/
	// https://github.com/linustornkrantz/hexagons/blob/master/src/main/java/com/prettybyte/hexagons/GridDrawer.java
	private void initHexagonGrid() {
		IntStream.range(0, gridWidth).forEachOrdered(x -> IntStream.range(0, gridHeight).forEachOrdered(y -> buildHexagon(x, y)));
	}

	private void buildHexagon(int x, int y) {
		Pos position = Pos.of(x, y);
		Hexagon hexagon = new Hexagon();

		placeHexagonOnGrid(hexagon, position);

		getChildren().add(hexagon);

		map.put(position, hexagon);

		// selection
		hexagon.setOnMouseReleased(event -> handleHexagonSelection(position));
	}

	private void placeHexagonOnGrid(Hexagon hexagon, Pos position) {
		// Calculate hexagon position
		final double x;
		final double y;
		if (position.x % 2 == 0) {
			x = hexagon.getWidth() / 2 + ((hexagon.getWidth() * 3. / 4.) * position.x);
			y = hexagon.getHeight() + (hexagon.getHeight() * position.y);
		}
		else {
			x = hexagon.getWidth()  * 5. / 4. + ((hexagon.getWidth() * 3. / 4.) * (position.x - 1));
			y = hexagon.getHeight() / 2.  + (hexagon.getHeight() * position.y);
		}

		hexagon.setLayoutX(x);
		hexagon.setLayoutY(y);
	}

	private void handleHexagonSelection(Pos newPosition) {
		Hexagon selectedHexagon = map.get(newPosition);

		if (selectedPosition != null) {
			// Previous selection was made
			if (selectedPosition.equals(newPosition)) {
				// Same node was selected, disable selection
				this.selectedHexagon.setShapeVisible(false);

				selectedPosition = null;
			}
			else {
				if (selectedHexagon.getValue() > 0) {
					// Not empty hexagon, switch selection
					setSelection(newPosition, selectedHexagon.getValue());
				}
				else if (moveRequestListener != null) {
					// Do move
					moveRequestListener.move(selectedPosition, newPosition);
				}
			}
		}
		else {
			if (selectedHexagon.getValue() > 0) {
				setSelection(newPosition, selectedHexagon.getValue());
			}
		}
	}

	private void setSelection(Pos newPosition, long value) {
		placeHexagonOnGrid(selectedHexagon, newPosition);
		this.selectedHexagon.setTranslateX(0);
		this.selectedHexagon.setTranslateY(0);
		this.selectedHexagon.setShapeVisible(true);
		this.selectedHexagon.setValue(value);

		selectedPosition = newPosition;
	}

	private void clearSelection() {
		this.selectedHexagon.setShapeVisible(false);
		selectedPosition = null;
	}

	public void setOnMoveRequestEvent(HexagonGridMoveEventListener listener) {
		this.moveRequestListener = listener;
	}

	public void setBoard(Board board) {
		Preconditions.checkNotNull(board);
		Preconditions.checkNotNull(board.grid);

		board.grid.forEach((pos, cell) -> map.get(pos).setValue(cell.value));
	}

	public void playCellAction(final List<CellAction> actionList, final Runnable callback) {
		actionList.sort(Comparator.naturalOrder());

		Transition moveTransition = null;
		List<Transition> dropTransitionList = new ArrayList<>();
		List<Transition> mergeTransitionList = new ArrayList<>();

		for (CellAction action : actionList) {
			if (action.type == ActionType.MOVE && selectedHexagon != null) {
				moveTransition = createCellMoveTransition((CellMoveAction) action);
			}
			else if (action.type == ActionType.DROP) {
				dropTransitionList.add(createDropCellTransition((CellDropAction) action));
			}
			else if (action.type == ActionType.MERGE) {
				mergeTransitionList.add(createCellMergeTransition((CellMergeAction) action));
			}
		}


		final SequentialTransition sequentialTransition = new SequentialTransition();
		if (moveTransition != null) {
			sequentialTransition.getChildren().add(moveTransition);
		}

		if (CollectionUtils.isNotEmpty(dropTransitionList)) {
			ParallelTransition dropTransition = new ParallelTransition();
			dropTransition.getChildren().addAll(dropTransitionList);
			sequentialTransition.getChildren().add(dropTransition);
		}

		if (CollectionUtils.isNotEmpty(mergeTransitionList)) {
			sequentialTransition.getChildren().addAll(mergeTransitionList);
		}

		sequentialTransition.setOnFinished( event -> callback.run());
		sequentialTransition.play();
	}

	private Transition createCellMoveTransition(CellMoveAction moveAction) {
		Path path = new Path();
		path.getElements().add(new MoveToAbs(this.selectedHexagon));

		for (int p=1; p<moveAction.pathBetweenCell.size(); p++) {
			Hexagon destinationHexagon = map.get(moveAction.pathBetweenCell.get(p));

			path.getElements().add(new LineToAbs(this.selectedHexagon, destinationHexagon.getLayoutX(), destinationHexagon.getLayoutY()));
		}

		PathTransition transition = new PathTransition();
		transition.setDuration(TransitionDurations.MOVE_ACTION);
		transition.setPath(path);
		transition.setNode(this.selectedHexagon);
		transition.setCycleCount(1);
		transition.setAutoReverse(false);
		transition.setOnFinished(event -> {
			map.get(moveAction.destination).setValue(selectedHexagon.getValue());
			clearSelection();
		});

		map.get(moveAction.origin).setEmpty();

		return transition;
	}

	private Transition createDropCellTransition(CellDropAction dropAction) {
		final Hexagon tmpHexagon = new Hexagon(dropAction.value);
		tmpHexagon.setOpacity(0.0);
		placeHexagonOnGrid(tmpHexagon, dropAction.position);
		getChildren().add(tmpHexagon);

		final FadeTransition transition = new FadeTransition();
		transition.setFromValue(0.0);
		transition.setToValue(1.0);
		transition.setNode(tmpHexagon);
		transition.setDuration(TransitionDurations.DROP_ACTION);

		transition.setOnFinished(event -> {
			map.get(dropAction.position).setValue(dropAction.value);
			getChildren().remove(tmpHexagon);
		});

		return transition;
	}

	private Transition createCellMergeTransition(CellMergeAction mergeAction) {
		final ParallelTransition mergeTransition = new ParallelTransition();

		final Hexagon fusionHexagon = map.get(mergeAction.fusionPosition);

		for (Pos origin : mergeAction.cellToMergeList) {
			if (origin.equals(mergeAction.fusionPosition)) {
				continue;
			}

			final Hexagon tmpHexagon = new Hexagon(mergeAction.initialValue);
			placeHexagonOnGrid(tmpHexagon, origin);
			getChildren().add(tmpHexagon);

			Path path = new Path();
			path.getElements().addAll(
					new MoveToAbs(tmpHexagon),
					new LineToAbs(tmpHexagon, fusionHexagon.getLayoutX(), fusionHexagon.getLayoutY())
			);

			PathTransition transition = new PathTransition();
			transition.setDuration(TransitionDurations.MOVE_ACTION);
			transition.setPath(path);
			transition.setNode(tmpHexagon);
			transition.setOnFinished(event -> getChildren().remove(tmpHexagon));

			map.get(origin).setEmpty();

			mergeTransition.getChildren().add(transition);
		}

		mergeTransition.setOnFinished(event -> fusionHexagon.setValue(mergeAction.fusionValue));

		return mergeTransition;
	}

	static class MoveToAbs extends MoveTo {
		MoveToAbs( Node node) {
			super( node.getLayoutBounds().getWidth() / 2, node.getLayoutBounds().getHeight() / 2);
		}
	}

	static class LineToAbs extends LineTo {
		LineToAbs( Node node, double x, double y) {
			super( x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2, y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
		}
	}
}
