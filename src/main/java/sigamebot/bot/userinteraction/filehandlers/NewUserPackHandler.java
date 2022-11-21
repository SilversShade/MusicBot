package sigamebot.bot.userinteraction.filehandlers;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sigamebot.exceptions.UserPackHandlerException;
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

    public static int getNumberOfFilesInDirectory(String directoryPath) throws IOException {
        var packsInDirectory = new File(directoryPath).list();
        if (packsInDirectory == null)
            throw new IOException("No such directory");
        return packsInDirectory.length;
    }

    public static void downloadUserPack(TelegramLongPollingBot bot, Document pack, int numberOfPacksInDirectory) throws UserPackHandlerException {
        var getFile = new GetFile();
        getFile.setFileId(pack.getFileId());
        try {
            var filePath = bot.execute(getFile).getFilePath();
            bot.downloadFile(filePath, new File(FilePaths.USERPACKS_DIRECTORY + numberOfPacksInDirectory + ".json"));
        } catch (TelegramApiException e) {
            throw new UserPackHandlerException("Произошла ошибка во время обработки Вашего пака.");
        }
    }

    public static Category parseUserPack(int numberOfPacksInDirectory) {
        Category parsedPack;
        try {
            parsedPack = JsonParser.getGameFromJson(numberOfPacksInDirectory, FilePaths.USERPACKS_DIRECTORY);
        } catch (IOException | IndexOutOfBoundsException e) {
            throw new UserPackHandlerException("Не удалось получить игру из json.");
        }
        return parsedPack;
    }

    public static int getNumberOfPacksInDirectory() {
        int numberOfPacksInDirectory;
        try {
            numberOfPacksInDirectory = NewUserPackHandler.getNumberOfFilesInDirectory(FilePaths.USERPACKS_DIRECTORY);
        } catch (IOException e) {
            throw new UserPackHandlerException("Не найдена директория с пользовательскими паками.");
        }
        return numberOfPacksInDirectory;
    }
    public static void giveCorrectNameToUserPack(Category deserializedPack, int numberOfPacksInDirectory) {
        try {
            FileParser.renameFile(FilePaths.USERPACKS_DIRECTORY + numberOfPacksInDirectory + ".json",
                    numberOfPacksInDirectory + 1 + "." + deserializedPack.name + ".json");
        } catch (IOException e) {
            throw new UserPackHandlerException("Произошла ошибка во время обработка Вашего пака.");
        }
    }

    public static Document getPackFromMessage(Message messageWithUserPack) throws UserPackHandlerException {
        var pack = messageWithUserPack.getDocument();
        if (documentHasWrongExtension(pack, "json"))
            throw new UserPackHandlerException("Пак должен иметь расширение .json");
        return pack;
    }
}
