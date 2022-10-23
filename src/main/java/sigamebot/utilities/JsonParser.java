package sigamebot.utilities;

import com.google.gson.Gson;
import sigamebot.logic.scenariologic.Category;
import sigamebot.logic.scenariologic.Question;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JsonParser {
    public static Category getGameFromJson(int gameNumberInFolder){
        var tests = sigamebot.utilities.FileParser.getAllFilesFromDir("src/main/resources/tests");
        String path = "src/main/resources/tests/" + tests.get(gameNumberInFolder).getName();
        String jsonString;
        try {
            jsonString = sigamebot.utilities.StreamReader.readFromInputStream(
                    path);
            Gson gson = new Gson();
            var scenario = gson.fromJson(jsonString, Category.class);
            return scenario;
        } catch (IOException e) {
            return new Category("", new ArrayList<Question>());
        }
    }
}
