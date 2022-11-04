package sigamebot.bot.commands;

import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.properties.CallbackPrefix;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.properties.CommandNames;
import sigamebot.utilities.properties.FilePaths;
import sigamebot.utilities.StreamReader;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SoloMenuCommand extends SigameBotCommand{
    public SoloMenuCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }
    @Override
    public void executeCommand(long chatId) {
        if (SigameBot.chatToBotState.get(chatId) != SigameBotState.DEFAULT_STATE)
            return;

        String[] menuOptions = new String[0];
        try {
            menuOptions = StreamReader.readFromInputStream(FilePaths.SOLO_MENU_COMMAND_MESSAGE)
                    .split("\r");
        } catch (IOException e) {
            this.bot.sendMessage("Произошла ошибка при исполнении команды.", chatId);
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String menuOption : menuOptions) {
            var parsed = menuOption.split(":");
            buttons.add(List.of(ITelegramBot.createInlineKeyboardButton(parsed[0], CallbackPrefix.SOLO_MENU + " " + parsed[1])));
        }
        var button = ITelegramBot.createInlineKeyboardButton("Назад",
                CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME);
        buttons.add(List.of(button));
        this.bot.sendMessage("Одиночная игра", chatId, buttons);
    }

}
