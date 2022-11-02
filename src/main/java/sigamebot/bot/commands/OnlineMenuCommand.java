package sigamebot.bot.commands;

import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.CallbackPrefix;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.StreamReader;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class OnlineMenuCommand extends SigameBotCommand{
    public OnlineMenuCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }
    @Override
    public void executeCommand(long chatId) {

    }

}
