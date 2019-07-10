package ysaak.hexgame.service;

import com.google.common.base.Preconditions;
import ysaak.hexgame.data.Cell;
import ysaak.hexgame.data.Game;
import ysaak.hexgame.data.PlayResult;
import ysaak.hexgame.data.Pos;
import ysaak.hexgame.data.cellaction.CellAction;
import ysaak.hexgame.data.cellaction.CellDropAction;
import ysaak.hexgame.data.cellaction.CellMergeAction;
import ysaak.hexgame.data.cellaction.CellMoveAction;
import ysaak.hexgame.exception.NoPathFoundException;
import ysaak.hexgame.exception.NoCellAvailableException;
import ysaak.hexgame.exception.SaveException;
import ysaak.hexgame.rules.GameRules;
import ysaak.hexgame.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class GameService {
	private final SaveService saveService;
	private final BoardService boardService;

	public GameService(SaveService saveService, BoardService boardService) {
		this.saveService = saveService;
		this.boardService = boardService;
	}

	public Game start() throws SaveException {
		final Optional<Game> loadedGame = saveService.load();

		final Game game;

		if (loadedGame.isPresent()) {
			game = loadedGame.get();
		}
		else {
			Board board = boardService.initBoard();

			// Place initial items
			final List<Long> initItemList = GameRules.generateItemList();

			try {
				dropItemListInBoard(board, initItemList);
			} catch (NoCellAvailableException e) {
				// Should not be thrown at the beginning
				System.out.println(e.getMessage());
			}

			final List<Long> nextItemList = GameRules.generateItemList();

			game = new Game(board, nextItemList, 0);

			saveService.save(game);
		}

		return game;
	}

	private DropActionResults dropItemListInBoard(Board board, List<Long> itemList) throws NoCellAvailableException {
		final List<Pos> emptyPosList = board.grid.entrySet()
				.stream()
				.filter(e -> e.getValue().empty)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());

		if (emptyPosList.size() < itemList.size()) {
			throw new NoCellAvailableException();
		}

		final List<Pos> newItemPosList = CollectionUtils.randomItems(emptyPosList, itemList.size());

		final List<CellDropAction> dropActionList = new ArrayList<>();

		for (int i = 0; i < itemList.size(); i++) {
			board.grid.put(newItemPosList.get(i), Cell.of(itemList.get(i)));
			dropActionList.add(new CellDropAction(newItemPosList.get(i), itemList.get(i)));
		}

		final List<CellMergeAction> mergeActionList = findAndMergeSequence(board, newItemPosList);

		return new DropActionResults(dropActionList, mergeActionList);
	}

	private List<CellMergeAction> findAndMergeSequence(final Board board, final List<Pos> originList) {
		final Queue<Pos> givenOriginList = new LinkedList<>(originList);
		final List<CellMergeAction> actionList = new ArrayList<>();

		Pos sequenceOrigin = givenOriginList.poll();

		do {
			Optional<CellMergeAction> cellSequenceSet = findAndMergeSequence(board, sequenceOrigin);

			if (cellSequenceSet.isPresent()) {
				givenOriginList.removeAll(cellSequenceSet.get().cellToMergeList);
				actionList.add(cellSequenceSet.get());
			}
			else {
				sequenceOrigin = givenOriginList.poll();
			}
		}
		while (sequenceOrigin != null);

		return actionList;
	}

	private Optional<CellMergeAction> findAndMergeSequence(final Board board, final Pos origin) {
		final long value = board.grid.get(origin).value;

		final Set<Pos> cellSequenceList = boardService.findSequenceOfCellWithValue(board, origin, value);

		if (cellSequenceList.size() >= 4) {
			cellSequenceList.forEach(p -> board.grid.put(p, Cell.empty()));
			board.grid.put(origin, Cell.of(value * 4));
		}
		else {
			cellSequenceList.clear();
		}


		return cellSequenceList.isEmpty()
				? Optional.empty()
		        : Optional.of(new CellMergeAction(cellSequenceList, origin, value, value * 4));
	}

	public PlayResult play(Game game, Pos origin, Pos destination) throws NoPathFoundException, NoCellAvailableException {
		Preconditions.checkNotNull(game);
		Preconditions.checkNotNull(origin);
		Preconditions.checkNotNull(destination);

		List<CellAction> actionList = new ArrayList<>();

		long score = game.score;

		// Create move action
		List<Pos> pathBetweenCell = boardService.getPathBetween(game.board, origin, destination);
		actionList.add(new CellMoveAction(origin, destination, pathBetweenCell));


		Board newBoard = boardService.cloneBoard(game.board);

		// move cell
		Cell cell = game.board.grid.get(origin);
		newBoard.grid.put(origin, Cell.empty());
		newBoard.grid.put(destination, cell);

		// Merge items
		final List<CellMergeAction> mergeActionList = findAndMergeSequence(newBoard, Collections.singletonList(destination));
		actionList.addAll(mergeActionList);

		score += calculateScoreEvolution(mergeActionList);

		List<Long> nextItemList = game.nextItemList;
		if (CollectionUtils.isEmpty(mergeActionList)) {
			// No merge done, drop item into board
			DropActionResults dropResults = dropItemListInBoard(newBoard, game.nextItemList);

			if (CollectionUtils.isNotEmpty(dropResults.dropActionList)) {
				actionList.addAll(dropResults.dropActionList);
			}

			if (CollectionUtils.isNotEmpty(dropResults.mergeActionList)) {
				actionList.addAll(dropResults.mergeActionList);
				score += calculateScoreEvolution(dropResults.mergeActionList);
			}

			nextItemList = GameRules.generateItemList();
		}


		return new PlayResult(
				new Game(newBoard, nextItemList, score),
				actionList
		);
	}

	private long calculateScoreEvolution(List<CellMergeAction> mergeActionList) {
		return mergeActionList.stream()
				.mapToLong(action -> action.initialValue * action.cellToMergeList.size())
				.sum();
	}

	private class DropActionResults {
		final List<CellDropAction> dropActionList;
		final List<CellMergeAction> mergeActionList;

		DropActionResults(List<CellDropAction> dropActionList, List<CellMergeAction> mergeActionList) {
			this.dropActionList = dropActionList;
			this.mergeActionList = mergeActionList;
		}
	}
}
