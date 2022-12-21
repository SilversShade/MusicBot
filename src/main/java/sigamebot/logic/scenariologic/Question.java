package sigamebot.logic.scenariologic;

import java.util.ArrayList;

public class Question {
    public int cost;
    public String questionTitle;
    public int type;
    public String questionDescription;
    public ArrayList<String> answerOptions;
    public String correctAnswer;

    public Question(int cost, String questionTitle, String questionDescription, ArrayList<String> answerOptions, String correctAnswer) {
        this.cost = cost;
        this.questionTitle = questionTitle;
        this.questionDescription = questionDescription;
        this.answerOptions = answerOptions;
        this.correctAnswer = correctAnswer;
        this.type = 2;
    }
}
