package ysaak.hexgame.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ysaak.hexgame.data.Game;
import ysaak.hexgame.data.Pos;
import ysaak.hexgame.data.SaveState;
import ysaak.hexgame.exception.SaveException;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class SaveService {

    public Optional<Game> load() throws SaveException {
        Path savePath = getSavePath();

        Game savedGame = null;

        if (Files.exists(savePath)) {

            try (BufferedReader reader = Files.newBufferedReader(savePath)) {

                SaveState state = getGson().fromJson(reader, SaveState.class);

                savedGame = state.game;
            }
            catch (IOException e) {
                throw new SaveException("Error while reading save", e);
            }
        }

        return Optional.ofNullable(savedGame);
    }

    public void save(Game game) throws SaveException {

        SaveState save = new SaveState(game);
        String jsonSave = getGson().toJson(save);

        try {
            Files.write(getSavePath(), jsonSave.getBytes());
        }
        catch (IOException e) {
            throw new SaveException("Error while writing save", e);
        }
    }

    private Path getSavePath() {
        return Paths.get(".", "save.game");
    }

    private Gson getGson() {
        return new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(Pos.class, new PosTypeAdapter())
                .create();
    }

    private class PosTypeAdapter implements JsonSerializer<Pos>, JsonDeserializer<Pos> {
        @Override
        public Pos deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final String[] elements = jsonElement.getAsString().split(",");

            return Pos.of(
                    Integer.parseInt(elements[0]),
                    Integer.parseInt(elements[1])
            );
        }

        @Override
        public JsonElement serialize(Pos pos, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(pos.x + "," + pos.y);
        }
    }
}
