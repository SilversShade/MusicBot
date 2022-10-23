package sigamebot.commands;

import sigamebot.SigameBot;
import sigamebot.utilities.FileParser;
import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BeginCommand extends SigameBotCommand {
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
}
