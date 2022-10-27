package sigamebot.bot.commands;

import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.userinteraction.ICallbackQueryHandler;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.FileParser;
import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.JsonParser;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BeginCommand extends SigameBotCommand implements ICallbackQueryHandler {
    public BeginCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    public static final String BEGIN_COMMAND_CALLBACK_PREFIX = "begin";

    public static final String BEGIN_COMMAND_ADD_NEW_PACK_CALLBACK_PREFIX = "newpack";

    private InlineKeyboardButton createAddNewPackButton() {
        var addNewPackButton = new InlineKeyboardButton();
        addNewPackButton.setText("Выбрать свой пак");
        addNewPackButton.setCallbackData(BEGIN_COMMAND_CALLBACK_PREFIX + " " + BEGIN_COMMAND_ADD_NEW_PACK_CALLBACK_PREFIX);
        return addNewPackButton;
    }

    @Override
    public void executeCommand(long chatId) {
        if (SigameBot.chatToBotState.get(chatId) != SigameBotState.DEFAULT_STATE)
            return;
        var tests = FileParser.getAllFilesFromDir("src/main/resources/packs");
        if (tests.isEmpty()) {
            this.bot.sendMessage("Не найдено ни одного пака.", chatId);
            return;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (var i = 0; i < tests.size(); i++) {
            var button = new InlineKeyboardButton();
            button.setText(String.format("%d. %s", i + 1, FilenameUtils.removeExtension(tests.get(i).getName())));
            button.setCallbackData(BEGIN_COMMAND_CALLBACK_PREFIX + " " + i);
            buttons.add(List.of(button));
        }

        buttons.add(List.of(createAddNewPackButton()));
        this.bot.sendMessage("Выберите пак из списка:", chatId, buttons);
    }

    public static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId) {
        bot.deleteMessage(chatId, messageId);
        if (callData.split(" ")[1].equals(BEGIN_COMMAND_ADD_NEW_PACK_CALLBACK_PREFIX)) {
            bot.sendMessage("Отправьте Ваш пак. Если Вы передумали, введите команду /cancel", chatId);
            SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
            return;
        }

        SoloGame.startNewSoloGame(bot, chatId, Integer.parseInt(callData.split(" ")[1]), "src/main/resources/packs/");
    }

}
