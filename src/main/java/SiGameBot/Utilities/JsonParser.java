package SiGameBot.Utilities;

import SiGameBot.Logic.ScenarioLogic.Category;
import SiGameBot.Logic.ScenarioLogic.Question;
import org.json.simple.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JsonParser {

    public static Category getGameFromJson(int gameNumberInFolder){
        var tests = FileParser.getAllFilesFromDir("src/main/resources/Tests");
        String path = "src/main/resources/Tests/" + tests.get(gameNumberInFolder).getName();
        String jsonString;
        JSONObject json;
        try {
            jsonString = StreamReader.readFromInputStream(
                    new FileInputStream(path));
            Object obj = JSONValue.parse(jsonString);
            json = (JSONObject) obj;
            json = (JSONObject) json.get("test");
            String name = (String) json.get("desc");
            JSONArray jsonQuestions = (JSONArray) json.get("questions");
            var questions = new ArrayList<Question>();
            for(var i = 0; i < jsonQuestions.size(); i++){
                var optionAnswers = new ArrayList<String>();
                JSONArray jsonAnswers = (JSONArray)((JSONObject)jsonQuestions.get(i)).get("options");
                for(var j = 0; j < jsonAnswers.size(); j++){
                    optionAnswers.add((String)jsonAnswers.get(j));
                }
                Question question = new Question(i,
                        ((Long)((JSONObject)jsonQuestions.get(i)).get("points")).intValue(),
                        (String)((JSONObject)jsonQuestions.get(i)).get("title"),
                        (String)((JSONObject)jsonQuestions.get(i)).get("desc"),
                        optionAnswers,
                        (String)((JSONObject)jsonQuestions.get(i)).get("correct")
                        );
                questions.add(question);
            }
            return new Category(name, questions);
        } catch (IOException e) {
            return new Category("", new ArrayList<>());
        }
    }
}
