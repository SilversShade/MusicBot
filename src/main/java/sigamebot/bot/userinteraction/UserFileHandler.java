package sigamebot.bot.userinteraction;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.SigameBot;

public class UserFileHandler {

    private static void handleNewUserPack(Message message) {
        UpdateProcessor.handleUserFile(() -> {
            if (SigameBot.chatToBotState.get(message.getChatId()) != SigameBotState.PACK_REQUESTED)
                return;

        });
    }

    public static void handleUserFiles(Update update) {
        if (update.hasMessage())
            handleNewUserPack(update.getMessage());
    }
}
