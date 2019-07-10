package ysaak.hexgame.gui;

import com.google.common.base.Preconditions;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import ysaak.hexgame.gui.hexagon.Hexagon;
import ysaak.hexgame.rules.GameRules;

import java.util.List;
import java.util.stream.IntStream;

public class NextValueListPane extends HBox {

    public NextValueListPane() {
        super(2);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5));

        IntStream.range(0, GameRules.MAX_SPAWNABLE_ITEM).forEach(e -> getChildren().add(new Hexagon()));
    }

    public void setData(final List<Long> nextItemList) {
        Preconditions.checkNotNull(nextItemList);

        getChildren().clear();

        nextItemList.stream()
                .map(Hexagon::new)
                .forEachOrdered(getChildren()::add);
    }
}
