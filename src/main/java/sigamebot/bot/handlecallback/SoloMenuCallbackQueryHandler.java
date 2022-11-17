package sigamebot.bot.handlecallback;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.commands.MenuCommand;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.settings.Settings;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.FileParser;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;
import sigamebot.utilities.properties.FilePaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SoloMenuCallbackQueryHandler implements ICallbackQueryHandler {
    private final ITelegramBot bot;

    public SoloMenuCallbackQueryHandler(ITelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId) {
        var display = SigameBot.displays.get(chatId);
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
                        CallbackPrefix.MENU + " " + CommandNames.CANCEL_COMMAND_NAME)));
                display.updateMenuMessage("Отправьте Ваш пак", buttons);
                SigameBot.displays.get(chatId).currentBotState.next(BotStates.PACK_REQUESTED);
            }
            case "base" -> sendPacks(splitData, chatId, messageId, "packs");
            case "user_pack" -> sendPacks(splitData, chatId, messageId, "userpacks");
            case "settings" -> Settings.displaySettingsOptions(display);
            default -> {
                String path = FilePaths.RESOURCES_DIRECTORY + splitData[1];
                SoloGame.startNewSoloGame(chatId, Integer.parseInt(splitData[2]),
                        path,
                        new TelegramGameDisplay(bot, chatId, messageId));
            }
        }
    }

    private void createUserpacksDirectoryIfNotPresent() throws IOException {
        Path pathToUserpacksDirectory = Path.of(FilePaths.USERPACKS_DIRECTORY);
        if (Files.notExists(pathToUserpacksDirectory))
            Files.createDirectory(pathToUserpacksDirectory);
    }

    private void sendErrorMessage(Long chatId, int messageId) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(ITelegramBot.createInlineKeyboardButton("Вернуться в меню",
                CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME)));
        bot.editMessage("Ошибка, игры не найдены. Выберите опцию \"Добавить свою игру\".",
                chatId, messageId, buttons);
    }

    private List<InlineKeyboardButton> createNavigationInterface(int pageNumber, int maxPageNumber) {
        List<InlineKeyboardButton> raw = new ArrayList<>();
        int previousPage = pageNumber - 1 < 0 ? maxPageNumber - 1 : pageNumber - 1;
        int nextPage = pageNumber + 1 > maxPageNumber - 1 ? 0 : pageNumber + 1;
        raw.add(ITelegramBot.createInlineKeyboardButton("<", CallbackPrefix.SOLO_MENU + " base " + previousPage));
        raw.add(ITelegramBot.createInlineKeyboardButton((pageNumber + 1) + "/" + maxPageNumber,
                CallbackPrefix.SOLO_MENU + " base " + pageNumber));
        raw.add(ITelegramBot.createInlineKeyboardButton(">", CallbackPrefix.SOLO_MENU + " base " + nextPage));
        return raw;
    }

    private void sendPacks(String[] splitData, Long chatId, int messageId, String type) {
        var display = SigameBot.displays.get(chatId);
        var packs = FileParser.getAllFilesFromDir(FilePaths.RESOURCES_DIRECTORY + type);
        var page = Integer.parseInt(splitData[2]);
        var maxPage = packs.size() / 5 + (packs.size() % 5 > 0 ? 1 : 0);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        if (packs.size() == 0) {
            sendErrorMessage(chatId, messageId);
            return;
        }

        for (var i = 5 * page; i < Math.min(5 * (page + 1), packs.size()); i++) {
            buttons.add(List.of(ITelegramBot.createInlineKeyboardButton(FilenameUtils.removeExtension(packs.get(i).getName()),
                    CallbackPrefix.SOLO_MENU + " " + type + " " + i)));
        }

        if (packs.size() > 5)
            buttons.add(createNavigationInterface(page, maxPage));

        buttons.add(MenuCommand.BACK_BUTTON);
        display.updateMenuMessage("Выберите текст", buttons);
    }
}
