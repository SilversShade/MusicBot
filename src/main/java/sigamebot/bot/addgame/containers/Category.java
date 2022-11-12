package sigamebot.bot.addgame.containers;

import java.io.Serializable;
import java.util.HashSet;

public class Category implements Serializable {
    public String name;
    public HashSet<Question> Questions;

    public Category() {
        Questions = new HashSet<Question>();
    }
}

