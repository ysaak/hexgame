package ysaak.hexgame;

import ysaak.hexgame.data.Game;
import ysaak.hexgame.exception.SaveException;
import ysaak.hexgame.service.SaveService;

import java.util.Optional;

public class MockSaveService extends SaveService {
    @Override
    public Optional<Game> load() {
        return Optional.empty();
    }

    @Override
    public void save(Game game) throws SaveException {
        return;
    }
}
