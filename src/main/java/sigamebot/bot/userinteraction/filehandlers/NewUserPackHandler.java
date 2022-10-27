package sigamebot.bot.userinteraction.filehandlers;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.userinteraction.UpdateProcessor;

import java.util.Objects;

public class NewUserPackHandler {

    public static boolean documentHasWrongExtension(Document pack, String expectedExtension) {
        return !Objects.equals(FilenameUtils.getExtension(pack.getFileName()), expectedExtension);
    }

    private static void respondToWrongExtension(ITelegramBot bot, long chatId) {
        bot.sendMessage("Пак должен быть в формате json", chatId);
        SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
    }

    public static void handleNewUserPack(ITelegramBot bot, Message message) {
        UpdateProcessor.handleUserFile(() -> {
            var chatId = message.getChatId();
            if (SigameBot.chatToBotState.get(chatId) != SigameBotState.PACK_REQUESTED)
                return;
            var pack = message.getDocument();
            if (documentHasWrongExtension(pack, "json")) {
                respondToWrongExtension(bot, chatId);
                return;
            }

            var getFile = new GetFile();
            getFile.setFileId(pack.getFileId());

            SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
        });
    }
}
