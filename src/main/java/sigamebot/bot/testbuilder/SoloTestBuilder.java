package sigamebot.bot.testbuilder;

import sigamebot.bot.botstate.BuilderStates;
import sigamebot.bot.botstate.QuestionBuilderStates;
import sigamebot.logic.scenariologic.Category;
import sigamebot.logic.scenariologic.Question;

import java.util.ArrayList;

public class SoloTestBuilder {
    public BuilderStates state;
    private ArrayList<Question> questions;
    private String name;
    private Category soloGame;
    private QuestionBuilder questionBuilder;
    public SoloTestBuilder(){
        questions = new ArrayList<>();
        questionBuilder = new QuestionBuilder();
        state = BuilderStates.DEFAULT_STATE;
    }
    public String nextStep(String text){
        switch (state){
            case DEFAULT_STATE -> {
                state = BuilderStates.CATEGORY;
                return "Введите название игры";
            }
            case CATEGORY -> {
                name = text;
                state = BuilderStates.QUESTION;
                questionBuilder = new QuestionBuilder();
                return questionBuilder.getText(text);
            }
            case QUESTION -> {
                var re = questionBuilder.getText(text);
                if(questionBuilder.state == QuestionBuilderStates.NONE){
                    questions = questionBuilder.getQuestions();
                    soloGame = new Category(name, questions);
                    state = BuilderStates.FINISH;
                }
                return re;
            }
            case FINISH -> {
                questions = new ArrayList<>();
                state = BuilderStates.DEFAULT_STATE;
            }
        }
        return "";
    }
    public ArrayList<String> getButtons(){
        return questionBuilder.getButtons();
    }
    public Category getSoloGame(){
        return soloGame;
    }
}
