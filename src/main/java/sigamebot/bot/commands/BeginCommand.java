package sigamebot.bot.commands;

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
    @Override
    public void executeCommand(long chatId) {
        var tests = FileParser.getAllFilesFromDir("src/main/resources/tests");
        if (tests.isEmpty()) {
            this.bot.sendMessage("Не найдено ни одного теста.", chatId);
            return;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (var i = 0; i < tests.size(); i++) {
            var button = new InlineKeyboardButton();
            button.setText(String.format("%d. %s", i + 1, FilenameUtils.removeExtension(tests.get(i).getName())));
            button.setCallbackData(BEGIN_COMMAND_CALLBACK_PREFIX + " " + i);
            buttons.add(List.of(button));
        }

        this.bot.sendMessage("Выберите тест из списка:", chatId, buttons);
    }

    public static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId) {
        bot.deleteMessage(chatId, messageId);
        SoloGame.getOngoingSoloGames().put(chatId, new SoloGame(chatId,
                JsonParser.getGameFromJson(Integer.parseInt(callData.split(" ")[1])),
                new TelegramGameDisplay(bot, chatId)));
        SoloGame.getOngoingSoloGames().get(chatId).start();
    }

}
