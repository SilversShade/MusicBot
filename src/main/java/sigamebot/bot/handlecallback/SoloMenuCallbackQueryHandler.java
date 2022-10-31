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
        bot.deleteMessage(chatId, messageId);
        var splittedData = callData.split(" ");
        switch (splittedData[1]) {
            case "add_game" -> {
                try {
                    createUserpacksDirectoryIfNotPresent();
                } catch (IOException e) {
                    bot.sendMessage("Не удалось создать каталог для хранения полтзовательских паков", chatId);
                    e.printStackTrace();
                    return;
                }
                bot.sendMessage("Отправьте Ваш пак. Если Вы передумали, введите команду /cancel", chatId);
                SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
            }
            case "base" -> sendPacks(splittedData, chatId, "packs");
            case "user_pack" -> sendPacks(splittedData, chatId, "userpacks");
            default -> {
                String path = "src/main/resources/" + splittedData[1];
                SoloGame.startNewSoloGame(chatId, Integer.parseInt(splittedData[2]), path, new TelegramGameDisplay(bot, chatId));
            }
        }
    }

    private void createUserpacksDirectoryIfNotPresent() throws IOException {
        Path pathToUserpacksDirectory = Path.of("src/main/resources/userpacks/");
        if (Files.notExists(pathToUserpacksDirectory))
            Files.createDirectory(pathToUserpacksDirectory);
    }

    private void sendErrorMessage(Long chatId){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText("Вернуться в меню");
        button.setCallbackData(CallbackPrefix.MENU + " /menu");
        buttons.add(List.of(button));
        bot.sendMessage("Ошибка, игры не найдены. Выберите опцию \"Добавить свою игру\".", chatId, buttons);
    }
    private void sendPacks(String[] splittedData, Long chatId, String type){
        var packs = FileParser.getAllFilesFromDir("src/main/resources/" + type);
        var page = Integer.parseInt(splittedData[2]);
        var maxPage = packs.size() / 5 + (packs.size() % 5 > 0 ? 1 : 0);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        if(packs.size() == 0){
            sendErrorMessage(chatId);
            return;
        }

        for(var i = 5 * page; i < Math.min(5 * (page + 1), packs.size()); i++){
            var button = new InlineKeyboardButton();
            button.setText(FilenameUtils.removeExtension(packs.get(i).getName()));
            button.setCallbackData(CallbackPrefix.SOLO_MENU + " " + type + " " + i);
            buttons.add(List.of(button));
        }

        if(packs.size() > 5){
            List<InlineKeyboardButton> raw = new ArrayList<>();
            int previousPage = page - 1 < 0 ? maxPage - 1 : page - 1;
            int nextPage = page + 1 > maxPage - 1 ? 0 : page + 1;
            var buttonLeft = new InlineKeyboardButton();
            buttonLeft.setText("<");
            buttonLeft.setCallbackData(CallbackPrefix.SOLO_MENU + " base " + previousPage);
            raw.add(buttonLeft);
            var buttonCenter = new InlineKeyboardButton();
            buttonCenter.setText((page + 1) + "/" + maxPage);
            buttonCenter.setCallbackData(CallbackPrefix.SOLO_MENU + " base " + page);
            raw.add(buttonCenter);
            var buttonRight = new InlineKeyboardButton();
            buttonRight.setText(">");
            buttonRight.setCallbackData(CallbackPrefix.SOLO_MENU + " base " + nextPage);
            raw.add(buttonRight);
            buttons.add(raw);
        }
        var button = new InlineKeyboardButton();
        button.setText("Назад");
        button.setCallbackData(CallbackPrefix.MENU + " /solo");
        buttons.add(List.of(button));
        bot.sendMessage("Выберите текст", chatId, buttons);
    }
}
