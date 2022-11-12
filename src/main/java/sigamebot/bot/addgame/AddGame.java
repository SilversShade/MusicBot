package sigamebot.bot.addgame;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.addgame.containers.Game;
import sigamebot.bot.core.ITelegramBot;

public class AddGame implements IState {

    private boolean msgSent = false;

    @Override
    public void action(ITelegramBot bot, Message msg, Logic logic, Game game) {
        if (!msgSent) {
            bot.sendMessage("Введите название игры", msg.getChatId());
            msgSent = true;
        } else {
            game.name = msg.getText();
            bot.sendMessage("Название вашей игры " + game.name + ". Введите название раунда", msg.getChatId());
            logic.currentState = new AddRound();
        }
    }
}