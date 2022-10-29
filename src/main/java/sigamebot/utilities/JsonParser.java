package sigamebot.utilities;

import com.google.gson.Gson;
import sigamebot.logic.scenariologic.Category;

import java.io.IOException;
import java.util.ArrayList;

import static sigamebot.utilities.StreamReader.readFromInputStream;

public class JsonParser {

    public static final Gson GSON = new Gson();

    public static Category getGameFromJson(int gameNumberInFolder, String pathToFolder){
        var tests = FileParser.getAllFilesFromDir(pathToFolder);
        String path = tests.get(gameNumberInFolder).getPath();
        String jsonString;
        try {
            jsonString = readFromInputStream(path);
            return GSON.fromJson(jsonString, Category.class);
        } catch (IOException e) {
            return new Category("", new ArrayList<>());
        }
    }
}
