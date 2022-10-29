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
        return packsInDirectory == null ? 0 : packsInDirectory.length - 1;
    }


    private static boolean createFileForUserPack(int numberOfPacksInDirectory) throws IOException {
        var newUserPack = new File("src/main/resources/userpacks/" + numberOfPacksInDirectory + ".json");
        return newUserPack.createNewFile();
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
            try {
                var result = createFileForUserPack(numberOfPacksInDirectory);
                if (!result) {
                    bot.sendMessage("Не удалось создать файл для хранения Вашего пака.", chatId);
                    return;
                }
            } catch (IOException e) {
                bot.sendMessage("Не удалось создать файл для хранения Вашего пака.", chatId);
                e.printStackTrace();
            }
            downloadUserPack(bot, pack, chatId, numberOfPacksInDirectory);
            SoloGame.startNewSoloGame(bot, chatId, numberOfPacksInDirectory + 1, "src/main/resources/userpacks/");
            SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
        });
    }
}
