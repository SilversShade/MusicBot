package SiGameBot.Logic.ScenarioLogic;

import java.util.ArrayList;

public class Question {
    public int questionNumber;
    public int cost;
    public String questionTitle;

    public String questionDescription;
    public ArrayList<String> answerOptions;
    public String correctAnswer;

    public Question(int questionNumber, int cost, String questionTitle, String questionDescription, ArrayList<String> answerOptions, String correctAnswer) {
        this.questionNumber = questionNumber;
        this.cost = cost;
        this.questionTitle = questionTitle;
        this.questionDescription = questionDescription;
        this.answerOptions = answerOptions;
        this.correctAnswer = correctAnswer;
    }
}
