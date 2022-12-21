package sigamebot.bot.testbuilder;

import sigamebot.bot.botstate.AnswerBuilderStates;
import sigamebot.bot.botstate.QuestionBuilderStates;
import sigamebot.logic.scenariologic.Question;

import java.util.ArrayList;
import java.util.Objects;

public class QuestionBuilder {
    public QuestionBuilderStates state;
    private ArrayList<Question> questions;
    private String questionTitle;
    private String questionDesc;
    private int questionCost;
    private ArrayList<String> answerOption;
    private String correctAnswer;
    private AnswerBuilder answerBuilder;
    public QuestionBuilder(){
        state = QuestionBuilderStates.NONE;
        answerOption = new ArrayList<>();
        questions = new ArrayList<>();
    }
    public String getText(String text){
        switch(state){
            case NONE ->{
                state = QuestionBuilderStates.TITLE;
                return "Введите название вопроса";
            }
            case TITLE -> {
                questionTitle = text;
                state = QuestionBuilderStates.DESC;
                return "Введите описание вопроса";
            }
            case DESC -> {
                questionDesc = text;
                state = QuestionBuilderStates.COST;
                return "Введите цену вопроса";
            }
            case COST -> {
                questionCost = tryToParse(text);
                state = QuestionBuilderStates.ANSWER;
                answerBuilder = new AnswerBuilder();
                return answerBuilder.getText(text);
            }
            case ANSWER -> {
                String re = answerBuilder.getText(text);
                if(answerBuilder.state == AnswerBuilderStates.NONE){
                    answerOption = answerBuilder.getAnswerOption();
                    correctAnswer = answerBuilder.getCorrectAnswer();
                    state = QuestionBuilderStates.END;
                    return re;
                }
                return re;
            }
            case END ->{
                if (Objects.equals(text.toLowerCase(), "нет")) {
                    state = QuestionBuilderStates.NONE;
                    questions.add(new Question(questionCost, questionTitle, questionDesc, answerOption, correctAnswer));
                    return "Тест создан";
                }
                state = QuestionBuilderStates.TITLE;
                return "Введите название вопроса";
            }
        }
        return "";
    }
    public ArrayList<String> getButtons(){
        ArrayList<String> re = new ArrayList<>();
        if(state == QuestionBuilderStates.END)
        {
            re.add("нет");
            re.add("да");
        } else if (state == QuestionBuilderStates.ANSWER) {
            return answerBuilder.getButtons();
        }
        return re;
    }
    public ArrayList<Question> getQuestions(){
        return questions;
    }
    private int tryToParse(String text){
        try{
            return Integer.parseInt(text);
        }
        catch (Exception e){
            return 100;
        }
    }
}
