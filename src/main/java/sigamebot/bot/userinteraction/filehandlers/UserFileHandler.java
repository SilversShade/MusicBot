package sigamebot.bot.userinteraction.filehandlers;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.userinteraction.filehandlers.NewUserPackHandler;

public class UserFileHandler {

    public static void handleUserFiles(SigameBot bot, Message message) {
        if (message == null || !message.hasDocument())
            return;

        NewUserPackHandler.handleNewUserPack(bot, message);
    }
}
