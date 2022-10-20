package SiGameBot.Utilities;

import SiGameBot.Logic.ScenarioLogic.Category;
import SiGameBot.Logic.ScenarioLogic.Question;

import java.util.ArrayList;

public class JsonParser {

    public static Category getGameFromJson(int gameNumberInFolder) {
        return new Category("", new ArrayList<>());
    }
}
