package sigamebot.bot.handlecallback;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.CallbackPrefix;
import sigamebot.utilities.FileParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SoloMenuCallbackQueryHandler implements ICallbackQueryHandler {
    private final ITelegramBot bot;
    public SoloMenuCallbackQueryHandler(ITelegramBot bot){ this.bot = bot; }

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId){
        var splitData = callData.split(" ");
        switch (splitData[1]) {
            case "add_game" -> {
                try {
                    createUserpacksDirectoryIfNotPresent();
                } catch (IOException e) {
                    bot.editMessage("Не удалось создать каталог для хранения пользовательских паков", chatId, messageId);
                    e.printStackTrace();
                    return;
                }
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                buttons.add(List.of(ITelegramBot.createInlineKeyboardButton("Вернуться в меню",
                        CallbackPrefix.MENU + " /cancel")));
                bot.editMessage("Отправьте Ваш пак", chatId, messageId, buttons);
                SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
                SigameBot.idMessageWithFileRequest.put(chatId, messageId);
            }
            case "base" -> sendPacks(splitData, chatId, messageId, "packs");
            case "user_pack" -> sendPacks(splitData, chatId, messageId, "userpacks");
            default -> {
                String path = "src/main/resources/" + splitData[1];
                SoloGame.startNewSoloGame(chatId, Integer.parseInt(splitData[2]),
                        path,
                        new TelegramGameDisplay(bot, chatId, messageId));
            }
        }
    }

    private void createUserpacksDirectoryIfNotPresent() throws IOException {
        Path pathToUserpacksDirectory = Path.of("src/main/resources/userpacks/");
        if (Files.notExists(pathToUserpacksDirectory))
            Files.createDirectory(pathToUserpacksDirectory);
    }

    private void sendErrorMessage(Long chatId, int messageId){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(ITelegramBot.createInlineKeyboardButton("Вернуться в меню", CallbackPrefix.MENU + " /menu")));
        bot.editMessage("Ошибка, игры не найдены. Выберите опцию \"Добавить свою игру\".",
                chatId, messageId, buttons);
    }
    private void sendPacks(String[] splitData, Long chatId, int messageId, String type){
        var packs = FileParser.getAllFilesFromDir("src/main/resources/" + type);
        var page = Integer.parseInt(splitData[2]);
        var maxPage = packs.size() / 5 + (packs.size() % 5 > 0 ? 1 : 0);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        if(packs.size() == 0){
            sendErrorMessage(chatId, messageId);
            return;
        }

        for(var i = 5 * page; i < Math.min(5 * (page + 1), packs.size()); i++){
            buttons.add(List.of(ITelegramBot.createInlineKeyboardButton(FilenameUtils.removeExtension(packs.get(i).getName()),
                    CallbackPrefix.SOLO_MENU + " " + type + " " + i)));
        }

        if(packs.size() > 5){
            List<InlineKeyboardButton> raw = new ArrayList<>();
            int previousPage = page - 1 < 0 ? maxPage - 1 : page - 1;
            int nextPage = page + 1 > maxPage - 1 ? 0 : page + 1;
            raw.add(ITelegramBot.createInlineKeyboardButton("<", CallbackPrefix.SOLO_MENU + " base " + previousPage));
            raw.add(ITelegramBot.createInlineKeyboardButton((page + 1) + "/" + maxPage, CallbackPrefix.SOLO_MENU + " base " + page));
            raw.add(ITelegramBot.createInlineKeyboardButton(">", CallbackPrefix.SOLO_MENU + " base " + nextPage));
            buttons.add(raw);
        }
        buttons.add(List.of(ITelegramBot.createInlineKeyboardButton("Назад", CallbackPrefix.MENU + " /solo")));
        bot.editMessage("Выберите текст", chatId, messageId, buttons);
    }
}
