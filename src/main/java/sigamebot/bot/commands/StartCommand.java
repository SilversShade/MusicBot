package sigamebot.bot.commands;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.core.TelegramBotMessageApi;
import sigamebot.user.ChatInfo;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;
import sigamebot.utilities.properties.FilePaths;
import sigamebot.utilities.StreamReader;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class StartCommand extends SigameBotCommand{
    public StartCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(ChatInfo chatInfo) {
        var display = chatInfo.getGameDisplay();
        if (display.currentBotState.getState() != BotStates.DEFAULT_STATE)
            return;
        try {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            buttons.add(List.of(TelegramBotMessageApi.createInlineKeyboardButton("Меню",
                    CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME)));
            display.updateMenuMessage(StreamReader.readFromInputStream(
                            FilePaths.START_COMMAND_MESSAGE),buttons);
        } catch (IOException e) {
            this.bot.sendMessage("Произошла ошибка при исполнении команды.", chatInfo.getChatId());
        }
    }
}
