package sigamebot.bot.addgame.containers;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    public String name;
    public List<Round> rounds;

    public Game() {
        rounds = new ArrayList<>();
    }
}
