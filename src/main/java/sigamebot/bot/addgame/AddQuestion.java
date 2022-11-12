package sigamebot.bot.addgame;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.addgame.containers.Category;
import sigamebot.bot.addgame.containers.Game;
import sigamebot.bot.addgame.containers.Question;
import sigamebot.bot.addgame.containers.Round;
import sigamebot.bot.core.ITelegramBot;

import java.util.Objects;

public class AddQuestion implements IState {
    public Category currentCategory;
    public Round currentRound;
    public Question question;
    public boolean questionDone = false;

    public AddQuestion(Category category, Round round) {
        currentCategory = category;
        currentRound = round;
        question = new Question();
        currentCategory.Questions.add(question);
    }

    @Override
    public void action(ITelegramBot bot, Message msg, Logic logic, Game game) {
        if (question.questionTitle == null) {
            question.questionTitle = msg.getText();
            bot.sendMessage("Введите цену вопроса", msg.getChatId());
        } else if (question.cost == null) {
            question.cost = Integer.valueOf(msg.getText());
            bot.sendMessage("В вопросе будут варианты ответа? Ответьте да/нет", msg.getChatId());
        } else if (question.type == null) {
            inputQuestionType(msg, bot);
        } else if (question.questionDescription == null) {
            question.questionDescription = msg.getText();
            if (question.type == 1)
                bot.sendMessage("Введите 4 варианта ответа, разделяя их ; Последним должен быть правильный вариант. Например вариант;вариант;верный ответ", msg.getChatId());
            else
                bot.sendMessage("Введите правильный ответ", msg.getChatId());
        } else if (question.correctAnswer == null) {
            if (question.type == 1)
                parseVariants(msg.getText());
            else
                question.correctAnswer = msg.getText();
            questionDone = true;
        }
        if (questionDone) {
            bot.sendMessage("Хотите добавить еще вопрос? Ответьте да/нет", msg.getChatId());
            logic.currentState = new Switcher(this);
        }
    }

    public void parseVariants(String text) {
        String[] variants = text.split(";");
        question.variants = variants;
        question.correctAnswer = variants[variants.length - 1];
    }

    public void inputQuestionType(Message msg, ITelegramBot bot) {

        if (Objects.equals(msg.getText(), "да")) {
            question.type = 1;
            bot.sendMessage("Введите вопрос", msg.getChatId());
        } else if (Objects.equals(msg.getText(), "нет")) {
            question.type = 0;
            bot.sendMessage("Введите вопрос", msg.getChatId());
        } else
            bot.sendMessage("Некорректный ввод. Ответьте да/нет", msg.getChatId());
    }

}
