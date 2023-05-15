package sigamebot.bot.testbuilder;

import sigamebot.bot.botstate.AnswerBuilderStates;

import java.util.ArrayList;

public class AnswerBuilder {
    private final ArrayList<String> answerOption;
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
                if ("нет".equalsIgnoreCase(text)) {
                    state = AnswerBuilderStates.NONE;
                    return "Вы хотите добавить еще один вопрос?";
                }
                state = AnswerBuilderStates.ANSWER;
                return "Введите неверный ответ";
            }
            default -> {
                return "Необработанное состояние";
            }
        }
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
        return answerOption;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
