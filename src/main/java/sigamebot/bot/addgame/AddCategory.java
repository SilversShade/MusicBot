package sigamebot.bot.addgame;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.addgame.containers.Category;
import sigamebot.bot.addgame.containers.Game;
import sigamebot.bot.addgame.containers.Round;
import sigamebot.bot.core.ITelegramBot;

public class AddCategory implements IState {
    Round currentRound;
    Category category;

    public AddCategory(Round round) {
        currentRound = round;
    }

    @Override
    public void action(ITelegramBot bot, Message msg, Logic logic, Game game) {
        category = new Category();
        category.name = msg.getText();
        currentRound.categories.add(category);
        bot.sendMessage("Название категории " + category.name + ". Введите название вопроса", msg.getChatId());
        logic.currentState = new AddQuestion(category, currentRound);
    }


}
