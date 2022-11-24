package sigamebot.bot.userinteraction.filehandlers;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.botstate.BotStates;
import sigamebot.exceptions.UserPackHandlerException;
import sigamebot.logic.SoloGame;
import sigamebot.user.ChatInfo;

public class UserFileHandler {

    public static void handleUserFiles(TelegramLongPollingBot bot, Message message, ChatInfo chatInfo) throws UserPackHandlerException{
        handleNewUserPack(bot, message, chatInfo);
    }

    private static void handleNewUserPack(TelegramLongPollingBot bot, Message message, ChatInfo chatInfo) throws UserPackHandlerException {
        if (chatInfo.getGameDisplay().currentBotState.getState() != BotStates.PACK_REQUESTED)
            return;

        var packFromMessage = NewUserPackHandler.getPackFromMessage(message);
        int numberOfPacksInDirectory = NewUserPackHandler.getNumberOfPacksInDirectory();

        NewUserPackHandler.downloadUserPack(bot, packFromMessage, numberOfPacksInDirectory);

        var deserializedUserPack = NewUserPackHandler.parseUserPack(numberOfPacksInDirectory);
        NewUserPackHandler.giveCorrectNameToUserPack(deserializedUserPack, numberOfPacksInDirectory);

        SoloGame.startNewSoloGame(chatInfo, deserializedUserPack, chatInfo.getGameDisplay());
        chatInfo.getGameDisplay().currentBotState.next(BotStates.DEFAULT_STATE);
    }
}
