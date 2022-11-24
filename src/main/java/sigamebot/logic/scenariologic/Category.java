package sigamebot.logic.scenariologic;

import java.util.ArrayList;

public class Category {
    public final String name;
    public final ArrayList<Question> questions;

    public Category(String name, ArrayList<Question> questions) {
        this.name = name;
        this.questions = questions;
    }
}
