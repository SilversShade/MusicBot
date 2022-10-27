package sigamebot.utilities;

import com.google.gson.Gson;
import sigamebot.logic.scenariologic.Category;

import java.io.IOException;
import java.util.ArrayList;

public class JsonParser {
    public static Category getGameFromJson(int gameNumberInFolder, String pathToFolder){
        var tests = sigamebot.utilities.FileParser.getAllFilesFromDir(pathToFolder);
        String path = pathToFolder + tests.get(gameNumberInFolder).getName();
        String jsonString;
        try {
            jsonString = sigamebot.utilities.StreamReader.readFromInputStream(path);
            var gson = new Gson();
            return gson.fromJson(jsonString, Category.class);
        } catch (IOException e) {
            return new Category("", new ArrayList<>());
        }
    }
}
