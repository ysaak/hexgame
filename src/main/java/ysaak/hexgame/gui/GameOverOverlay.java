package ysaak.hexgame.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GameOverOverlay extends VBox {

    public GameOverOverlay() {
        this.setBackground(new Background(new BackgroundFill(Colors.GAMEOVER_OVERLAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Font font = new Font(48);

        Text text = new Text("GAME OVER");
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(font);
        text.setStyle("-fx-font-weight: bold");
        text.setFill(Color.WHITE);
        getChildren().add(text);

        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
    }

    public void show() {
        setVisible(true);
        setManaged(true);
    }

    public void hide() {
        setVisible(false);
        setManaged(false);
    }
}
