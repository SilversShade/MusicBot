package sigamebot.bot.commands;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.CallbackPrefix;
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
    public void executeCommand(long chatId) {
        if (SigameBot.chatToBotState.get(chatId) != SigameBotState.DEFAULT_STATE)
            return;
        try {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            buttons.add(List.of(bot.createButton("Меню", CallbackPrefix.MENU + " /menu")));
            this.bot.sendMessage(StreamReader.readFromInputStream(
                            "src/main/resources/commandmessages/startcommandmessage.txt"),
                    chatId,buttons);
        } catch (IOException e) {
            this.bot.sendMessage("Произошла ошибка при исполнении команды.", chatId);
        }
    }
}
