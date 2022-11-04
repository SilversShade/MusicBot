package sigamebot.bot.userinteraction.filehandlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.core.SigameBot;

public class UserFileHandler {

    public static void handleUserFiles(SigameBot bot, Message message) {
        NewUserPackHandler.handleNewUserPack(bot, message);
    }
}
