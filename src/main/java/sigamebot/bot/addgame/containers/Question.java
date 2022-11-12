package sigamebot.bot.addgame.containers;

import java.io.Serializable;

public class Question implements Serializable {
    public String correctAnswer;
    public Integer cost;
    public String questionTitle;
    public Integer type;
    public String questionDescription;
    public String[] variants;

}
