package sigamebot.logic.scenariologic;

import java.util.ArrayList;

public class Question {
    public final int cost;
    public final String questionTitle;
    public final int type;
    public final String questionDescription;
    public final ArrayList<String> answerOptions;
    public final String correctAnswer;

    public Question(int cost, String questionTitle, String questionDescription, ArrayList<String> answerOptions, String correctAnswer) {
        this.cost = cost;
        this.questionTitle = questionTitle;
        this.questionDescription = questionDescription;
        this.answerOptions = answerOptions;
        this.correctAnswer = correctAnswer;
        this.type = 2;
    }
}
