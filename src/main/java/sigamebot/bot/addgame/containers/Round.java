package sigamebot.bot.addgame.containers;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;

public class Round implements Serializable {
    public String name;
    public HashSet<Category> categories;

    public Round() {
        categories = new HashSet<Category>();
    }
}
