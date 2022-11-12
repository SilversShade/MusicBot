package sigamebot.bot.addgame;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.addgame.containers.Game;
import sigamebot.bot.addgame.containers.Round;
import sigamebot.bot.core.ITelegramBot;

public class AddRound implements IState {
    @Override
    public void action(ITelegramBot bot, Message msg, Logic logic, Game game) {

        Round round = new Round();
        round.name = msg.getText();
        game.rounds.add(round);
        bot.sendMessage("Название раунда " + round.name + ". Введите название категории", msg.getChatId());
        logic.currentState = new AddCategory(round);
    }


}
