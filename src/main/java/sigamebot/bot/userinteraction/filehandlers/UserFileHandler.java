package sigamebot.bot.userinteraction.filehandlers;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.core.SigameBot;
import sigamebot.exceptions.UserPackHandlerException;
import sigamebot.logic.SoloGame;

public class UserFileHandler {

    public static void handleUserFiles(TelegramLongPollingBot bot, Message message) throws UserPackHandlerException{
        handleNewUserPack(bot, message);
    }

    private static void handleNewUserPack(TelegramLongPollingBot bot, Message message) throws UserPackHandlerException {
        var chatId = message.getChatId();
        if (SigameBot.displays.get(chatId).currentBotState.getState() != BotStates.PACK_REQUESTED)
            return;

        var packFromMessage = NewUserPackHandler.getPackFromMessage(message);
        int numberOfPacksInDirectory = NewUserPackHandler.getNumberOfPacksInDirectory();

        NewUserPackHandler.downloadUserPack(bot, packFromMessage, numberOfPacksInDirectory);

        var deserializedUserPack = NewUserPackHandler.parseUserPack(numberOfPacksInDirectory);
        NewUserPackHandler.giveCorrectNameToUserPack(deserializedUserPack, numberOfPacksInDirectory);

        SoloGame.startNewSoloGame(chatId, deserializedUserPack, SigameBot.displays.get(chatId));
        SigameBot.displays.get(chatId).currentBotState.next(BotStates.DEFAULT_STATE);
    }
}
