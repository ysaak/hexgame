package ysaak.hexgame.service;

import com.google.common.base.Preconditions;
import ysaak.hexgame.data.Board;
import ysaak.hexgame.data.Cell;
import ysaak.hexgame.data.Pos;
import ysaak.hexgame.exception.NoPathFoundException;
import ysaak.hexgame.rules.BoardRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardService {

	public Board initBoard() {
		Map<Pos, Cell> grid = new HashMap<>();

		IntStream.range(0, BoardRules.GRID_WIDTH).forEachOrdered(x ->
				IntStream.range(0, BoardRules.GRID_HEIGHT).forEachOrdered(y ->
						grid.put(Pos.of(x, y), Cell.empty())));

		return new Board(grid);
	}

	public Board cloneBoard(Board board) {
		Preconditions.checkNotNull(board);

		Map<Pos, Cell> grid = new HashMap<>();
		board.grid.forEach(grid::put);

		return new Board(grid);
	}

	public boolean isMoveAllowed(Board board, Pos origin, Pos destination) {
		Preconditions.checkNotNull(board);
		Preconditions.checkArgument(BoardRules.isPositionValid(origin), "Origin is invalid");
		Preconditions.checkArgument(BoardRules.isPositionValid(destination), "Destination is invalid");

		boolean moveAllowed = false;

		if (!origin.equals(destination)) {
			Set<Pos> availablePositionSet = findAvailableNextPosition(board, origin);
			moveAllowed = availablePositionSet.contains(destination);
		}

		return moveAllowed;
	}

	public Set<Pos> findSequenceOfCellWithValue(final Board board, final Pos position, final long value) {
		final Set<Pos> positionSequenceSet = new HashSet<>();

		Predicate<Pos> validCellPredicate = p -> {
			Cell cell = board.grid.get(p);
			return !cell.empty && cell.value == value;
		};

		if (validCellPredicate.test(position)) {
			positionSequenceSet.add(position);
		}

		findPositionSequenceWithPredicate(position, positionSequenceSet, validCellPredicate);

		return positionSequenceSet;
	}

	public Set<Pos> findAvailableNextPosition(Board board, Pos position) {
		final Set<Pos> positionSequenceSet = new HashSet<>();
		positionSequenceSet.add(position);

		findPositionSequenceWithPredicate(position, positionSequenceSet, p -> board.grid.get(p).empty);

		positionSequenceSet.remove(position);

		return positionSequenceSet;
	}

	private void findPositionSequenceWithPredicate(Pos currentPosition, Set<Pos> availablePositionSet, Predicate<Pos> predicate) {
		BoardRules.getNeighborhood(currentPosition).stream()
			.filter(p -> !availablePositionSet.contains(p))
			.filter(predicate)
			.forEach(p -> {
				availablePositionSet.add(p);

				findPositionSequenceWithPredicate(p, availablePositionSet, predicate);
			});
	}

	private List<Pos> reconstructPath(final Map<Pos, Pos> cameFrom, final Pos start, final Pos goal) {
		List<Pos> totalPath = new ArrayList<>();

		Pos current = goal;

		while (current != start) {
			totalPath.add(current);
			current = cameFrom.get(current);
		}

		totalPath.add(start);

		Collections.reverse(totalPath);
		return totalPath;
	}

	public List<Pos> getPathBetween(Board board, final Pos start, final Pos goal) throws NoPathFoundException {

		List<Pos> obstacleList = board.grid.entrySet().stream()
				.filter(e -> !e.getValue().empty)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());

		LinkedList<Pos> frontier = new LinkedList<>();
		frontier.addLast(start);

		Map<Pos, Pos> came_from = new HashMap<>();
		came_from.put(start, null);

		while (!frontier.isEmpty()) {
			Pos current = frontier.pollFirst();

			if (goal.equals(current)) {
				return reconstructPath(came_from, start, goal);
			}

			for (Pos next : BoardRules.getNeighborhood(current)) {
				if (!came_from.containsKey(next) && !obstacleList.contains(next)) {
					frontier.addLast(next);
					came_from.put(next, current);
				}
			}
		}

		throw new NoPathFoundException(start, goal);
	}

}
