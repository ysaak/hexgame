package ysaak.hexgame.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BottomPane extends HBox {

    private final Text scoreLabel;

    public BottomPane() {
        super(5);
        setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(5, 10, 5, 10));

        scoreLabel = new Text("");
        scoreLabel.setFont(new Font(20.));
        scoreLabel.setFill(Color.WHITE);

        getChildren().add(scoreLabel);


        setScore(0);
    }

    public void setScore(long score) {
        scoreLabel.setText(Long.toString(score));
    }
}
