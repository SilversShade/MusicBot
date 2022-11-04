package sigamebot.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import sigamebot.logic.scenariologic.Category;

import java.io.IOException;
import java.util.ArrayList;

import static sigamebot.utilities.StreamReader.readFromInputStream;

public class JsonParser {

    public static final Gson GSON = new Gson();

    public static Category getGameFromJson(int gameNumberInFolder, String pathToFolder) throws IOException{
        var tests = FileParser.getAllFilesFromDir(pathToFolder);
        return GSON.fromJson(readFromInputStream(tests.get(gameNumberInFolder).getPath()), Category.class);
    }
}
