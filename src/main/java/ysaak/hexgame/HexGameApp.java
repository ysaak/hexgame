package ysaak.hexgame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ysaak.hexgame.data.Game;
import ysaak.hexgame.data.PlayResult;
import ysaak.hexgame.data.Pos;
import ysaak.hexgame.exception.NoCellAvailableException;
import ysaak.hexgame.exception.NoPathFoundException;
import ysaak.hexgame.gui.BottomPane;
import ysaak.hexgame.gui.Colors;
import ysaak.hexgame.gui.GameOverOverlay;
import ysaak.hexgame.gui.NextValueListPane;
import ysaak.hexgame.gui.grid.HexagonGrid;
import ysaak.hexgame.service.BoardService;
import ysaak.hexgame.service.GameService;
import ysaak.hexgame.service.SaveService;
import ysaak.hexgame.util.CollectionUtils;

public class HexGameApp extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private GameService gameService;

	private Game currentGame;

	private HexagonGrid grid;
	private NextValueListPane nextValueListPane;
	private GameOverOverlay gameOverOverlay;
	private BottomPane bottomPane;

	@Override
	public void init() throws Exception {
		SaveService saveService = new SaveService();
		BoardService boardService = new BoardService();
		gameService = new GameService(saveService, boardService);

		currentGame = gameService.start();
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Hex Game");

		nextValueListPane = new NextValueListPane();

		grid = new HexagonGrid(5, 8);
		grid.setOnMoveRequestEvent(this::moveRequest);

		bottomPane = new BottomPane();

		BorderPane gamePane = new BorderPane(grid);
		gamePane.setTop(nextValueListPane);
		gamePane.setBottom(bottomPane);
		gamePane.setBackground(new Background(new BackgroundFill(Colors.BLUE_GREY, CornerRadii.EMPTY, Insets.EMPTY)));

		gameOverOverlay = new GameOverOverlay();
		gameOverOverlay.hide();

		StackPane mainPane = new StackPane(gamePane, gameOverOverlay);

		Scene scene = new Scene(mainPane, gamePane.getPrefWidth(), gamePane.getPrefHeight());
		primaryStage.setScene(scene);
		primaryStage.show();

		updateGame();
	}

	private void updateGame() {
		grid.setBoard(currentGame.board);
		nextValueListPane.setData(currentGame.nextItemList);
		bottomPane.setScore(currentGame.score);
	}

	private void moveRequest(Pos origin, Pos destination) {

		try {
			PlayResult playResult = gameService.play(currentGame, origin, destination);

			currentGame = playResult.game;

			if (CollectionUtils.isNotEmpty(playResult.actionList)) {
				grid.playCellAction(playResult.actionList, this::updateGame);
			}
			else {
				updateGame();
			}
		}
		catch (NoCellAvailableException e) {
			gameOverOverlay.show();
		}
		catch (NoPathFoundException e) {
			// FIXME logs
			System.out.println(e.getMessage());
		}
	}
}
