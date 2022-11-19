package sigamebot.bot.userinteraction.filehandlers;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.userinteraction.UpdateProcessor;
import sigamebot.logic.SoloGame;
import sigamebot.logic.scenariologic.Category;
import sigamebot.utilities.FileParser;
import sigamebot.utilities.properties.FilePaths;
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
        SigameBot.displays.get(chatId).currentBotState.next(BotStates.DEFAULT_STATE);
    }

    public static int getNumberOfFilesInDirectory(String directoryPath) throws IOException{
        var packsInDirectory = new File(directoryPath).list();
        if (packsInDirectory == null)
            throw new IOException("No such directory");
        return packsInDirectory.length;
    }

    private static void downloadUserPack(SigameBot bot, Document pack, long chatId, int numberOfPacksInDirectory) {
        var getFile = new GetFile();
        getFile.setFileId(pack.getFileId());
        try {
            var filePath = bot.execute(getFile).getFilePath();
            bot.downloadFile(filePath, new File(FilePaths.USERPACKS_DIRECTORY + numberOfPacksInDirectory + ".json"));
        } catch (TelegramApiException e) {
            bot.sendMessage("Произошла ошибка во время обработки Вашего пака.", chatId);
            e.printStackTrace();
        }
    }

    /**
     *
     * @param bot
     * @param message
     */

    @Deprecated
    public static void handleNewUserPack(SigameBot bot, Message message) {
        UpdateProcessor.handleUserFile(() -> {
            var chatId = message.getChatId();

            if (SigameBot.displays.get(chatId).currentBotState.getState() != BotStates.PACK_REQUESTED)
                return;

            var pack = message.getDocument();
            if (documentHasWrongExtension(pack, "json")) {
                respondToWrongExtension(bot, chatId);
                return;
            }

            int numberOfPacksInDirectory;
            try {
                numberOfPacksInDirectory = getNumberOfFilesInDirectory(FilePaths.USERPACKS_DIRECTORY);
            } catch (IOException e) {
                bot.sendMessage("Не найдена директория с пользовательскими паками.", chatId);
                return;
            }

            downloadUserPack(bot, pack, chatId, numberOfPacksInDirectory);

            Category parsedPack;
            try {
                parsedPack = JsonParser.getGameFromJson(numberOfPacksInDirectory, FilePaths.USERPACKS_DIRECTORY);
            } catch (IOException | IndexOutOfBoundsException e) {
                bot.sendMessage("Не удалось получить игру из json.", chatId);
                return;
            }

            try {
                FileParser.renameFile(FilePaths.USERPACKS_DIRECTORY + numberOfPacksInDirectory + ".json",
                        numberOfPacksInDirectory+1 + "." + parsedPack.name + ".json");
            } catch (IOException e) {
                bot.sendMessage("Произошла ошибка во время обработка Вашего пака.", chatId);
                e.printStackTrace();
                return;
            }
            bot.deleteMessage(chatId, message.getMessageId());
            SoloGame.startNewSoloGame(chatId, parsedPack, SigameBot.displays.get(chatId));
            SigameBot.displays.get(chatId).currentBotState.next(BotStates.DEFAULT_STATE);
        });
    }
}
