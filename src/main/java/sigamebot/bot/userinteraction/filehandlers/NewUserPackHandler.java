package sigamebot.bot.userinteraction.filehandlers;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.userinteraction.UpdateProcessor;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.FileParser;
import sigamebot.utilities.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class NewUserPackHandler {

    public static boolean documentHasWrongExtension(Document pack, String expectedExtension) {
        return !Objects.equals(FilenameUtils.getExtension(pack.getFileName()), expectedExtension);
    }

    private static void respondToWrongExtension(SigameBot bot, long chatId) {
        bot.sendMessage("Пак должен быть в формате json", chatId);
        SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
    }

    public static int getNumberOfPacksInDirectory(String directoryPath) {
        var packsInDirectory = new File(directoryPath).list();
        return packsInDirectory == null ? 0 : packsInDirectory.length;
    }

    private static void downloadUserPack(SigameBot bot, Document pack, long chatId, int numberOfPacksInDirectory) {
        var getFile = new GetFile();
        getFile.setFileId(pack.getFileId());
        try {
            var filePath = bot.execute(getFile).getFilePath();
            bot.downloadFile(filePath, new File("src/main/resources/userpacks/" + numberOfPacksInDirectory + ".json"));
        } catch (TelegramApiException e) {
            bot.sendMessage("Произошла ошибка во время обработки Вашего пака.", chatId);
            e.printStackTrace();
        }
    }

    public static void handleNewUserPack(SigameBot bot, Message message) {
        UpdateProcessor.handleUserFile(() -> {
            var chatId = message.getChatId();

            if (SigameBot.chatToBotState.get(chatId) != SigameBotState.PACK_REQUESTED)
                return;

            var pack = message.getDocument();
            if (documentHasWrongExtension(pack, "json")) {
                respondToWrongExtension(bot, chatId);
                return;
            }

            var numberOfPacksInDirectory = getNumberOfPacksInDirectory("src/main/resources/userpacks/");

            downloadUserPack(bot, pack, chatId, numberOfPacksInDirectory);

            var parsedPack = JsonParser.getGameFromJson(numberOfPacksInDirectory, "src/main/resources/userpacks/");

            try {
                FileParser.renameFile("src/main/resources/userpacks/" + numberOfPacksInDirectory + ".json",
                        numberOfPacksInDirectory+1 + "." + parsedPack.name + ".json");
            } catch (IOException e) {
                bot.sendMessage("Произошла ошибка во время обработка Вашего пака.", chatId);
                e.printStackTrace();
                return;
            }

            SoloGame.startNewSoloGame(chatId, parsedPack, new TelegramGameDisplay(bot, chatId));

            SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
        });
    }
}
