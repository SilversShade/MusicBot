package sigamebot.bot.commands;

import sigamebot.bot.userinteraction.ICallbackQueryHandler;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.StreamReader;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.JsonParser;
import sigamebot.utilities.callbackPrefix;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SelectOnlineGame extends SigameBotCommand implements ICallbackQueryHandler {
    public SelectOnlineGame(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }
    @Override
    public void executeCommand(long chatId){

    }

    public static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId) {

    }

}
