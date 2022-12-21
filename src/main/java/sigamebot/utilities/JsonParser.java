package sigamebot.utilities;

import com.google.gson.Gson;
import sigamebot.logic.scenariologic.Category;

import java.io.IOException;

import static sigamebot.utilities.StreamReader.readFromInputStream;

public class JsonParser {

    private static final Gson GSON = new Gson();

    public static Category getGameFromJson(int gameNumberInFolder, String pathToFolder) throws IOException, IndexOutOfBoundsException{
        var tests = FileParser.getAllFilesFromDir(pathToFolder);
        return GSON.fromJson(readFromInputStream(tests.get(gameNumberInFolder).getPath()), Category.class);
    }
    public static String getJsonFromGame(Category game){
        return GSON.toJson(game);
    }
}
