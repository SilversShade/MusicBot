package sigamebot.bot.testbuilder;

import sigamebot.bot.botstate.AnswerBuilderStates;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class AnswerBuilder {
    private ArrayList<String> answerOption;
    public AnswerBuilderStates state;
    private String correctAnswer;

    public AnswerBuilder() {
        answerOption = new ArrayList<>();
        state = AnswerBuilderStates.NONE;
    }

    public String getText(String text) {
        switch (state) {
            case NONE -> {
                state = AnswerBuilderStates.CORRECT;
                return "Введите верный ответ";
            }
            case CORRECT -> {
                correctAnswer = text;
                answerOption.add(text);
                state = AnswerBuilderStates.ANSWER;
                return "Введите неверный ответ";
            }
            case ANSWER -> {
                answerOption.add(text);
                state = AnswerBuilderStates.END;
                return "Вы хотите добавить еще один ответ?";
            }
            case END -> {
                if (Objects.equals(text.toLowerCase(), "нет")) {
                    state = AnswerBuilderStates.NONE;
                    return "Вы хотите добавить еще один вопрос?";
                }
                state = AnswerBuilderStates.ANSWER;
                return "Введите неверный ответ";
            }
        }
        return "";
    }
    public ArrayList<String> getButtons(){
        if(state == AnswerBuilderStates.END){
            var re = new ArrayList<String>();
            re.add("нет");
            re.add("да");
            return re;
        }
        return new ArrayList<>();
    }
    public ArrayList<String> getAnswerOption() {
        ArrayList<String> newList = new ArrayList<>();
        var indices = range(answerOption.size());
        Random random = new Random();
        for (int i = 0; i < answerOption.size(); i++) {
            int index = random.nextInt(indices.size());
            newList.add(answerOption.get(index));
            indices.remove(index);
        }
        return newList;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    private ArrayList<Integer> range(int max) {
        ArrayList<Integer> re = new ArrayList<>();
        for (int i = 0; i < max; i++)
            re.add(i);
        return re;
    }
}
