package sigamebot.bot.addgame;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.addgame.containers.Game;
import sigamebot.bot.core.ITelegramBot;

import java.io.IOException;

interface IState {
    void action(ITelegramBot bot, Message msg, Logic logic, Game game) throws IOException;

}
